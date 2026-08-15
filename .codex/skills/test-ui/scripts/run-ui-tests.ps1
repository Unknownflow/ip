[CmdletBinding()]
param(
    [string] $PlanPath = "test\ui-test-plan.md",
    [string] $SourcePath = "src\main\java",
    [string] $MainClass = "Zen",
    [string] $OutputDirectory = "out\ui-test",
    [ValidateRange(1, 600)]
    [int] $TimeoutSeconds = 30
)

$ErrorActionPreference = "Stop"

function Normalize-Output([string] $Text) {
    if ($null -eq $Text) {
        return ""
    }

    # Compare platform-independent text while preserving meaningful spaces.
    $normalized = $Text -replace "`r`n", "`n" -replace "`r", "`n"
    return $normalized.TrimEnd("`n")
}

function Get-PlanField([string] $CaseBody, [string] $FieldName) {
    $pattern = "(?ms)^\*\*" + [regex]::Escape($FieldName) + ":\*\*\s*(.*?)(?=^\*\*(?:Aim|Inputs|Expected output|Notes):\*\*|\z)"
    $match = [regex]::Match($CaseBody, $pattern)
    if (-not $match.Success) {
        throw "Missing **${FieldName}:** in test case."
    }

    return $match.Groups[1].Value.Trim()
}

function Get-FencedContent([string] $FieldText, [string] $FieldName) {
    $match = [regex]::Match($FieldText, '(?ms)^```(?:text|console)?\s*\r?\n(.*?)\r?\n```')
    if (-not $match.Success) {
        throw "The **${FieldName}:** field must contain one fenced code block."
    }

    return $match.Groups[1].Value
}

function Read-TestCases([string] $Path) {
    $plan = Get-Content -Raw -LiteralPath $Path
    $matches = [regex]::Matches($plan, '(?ms)^###\s+(.+?)\s*\r?\n(.*?)(?=^###\s+|\z)')
    if ($matches.Count -eq 0) {
        throw "No test cases found in $Path. Add cases using ### headings."
    }

    $cases = @()
    foreach ($match in $matches) {
        $cases += [pscustomobject]@{
            Name = $match.Groups[1].Value.Trim()
            Aim = Get-PlanField $match.Groups[2].Value "Aim"
            Inputs = Get-FencedContent (Get-PlanField $match.Groups[2].Value "Inputs") "Inputs"
            Expected = Get-FencedContent (Get-PlanField $match.Groups[2].Value "Expected output") "Expected output"
        }
    }

    return $cases
}

function Get-JavaMajorVersion([string] $Executable) {
    $versionText = (& $Executable -version 2>&1 | Out-String)
    $match = [regex]::Match($versionText, '(?:"|\s)(\d+)(?:\.\d+)?')
    if (-not $match.Success) {
        throw "Unable to determine the version of $Executable. Output was: $versionText"
    }

    return [int] $match.Groups[1].Value
}

function Invoke-Program([string] $ClassPath, [string] $ClassName, [string] $InputText, [int] $Timeout) {
    $startInfo = [System.Diagnostics.ProcessStartInfo]::new()
    $startInfo.FileName = "java"
    $startInfo.Arguments = "-cp `"$ClassPath`" $ClassName"
    $startInfo.WorkingDirectory = (Get-Location).Path
    $startInfo.UseShellExecute = $false
    $startInfo.RedirectStandardInput = $true
    $startInfo.RedirectStandardOutput = $true
    $startInfo.RedirectStandardError = $true
    $process = [System.Diagnostics.Process]::new()
    $process.StartInfo = $startInfo

    if (-not $process.Start()) {
        throw "Unable to start java for $ClassName."
    }

    $inputWithTerminator = $InputText
    if (-not $inputWithTerminator.EndsWith("`n") -and -not $inputWithTerminator.EndsWith("`r")) {
        $inputWithTerminator += "`n"
    }
    $process.StandardInput.Write($inputWithTerminator)
    $process.StandardInput.Close()

    $stdoutTask = $process.StandardOutput.ReadToEndAsync()
    $stderrTask = $process.StandardError.ReadToEndAsync()
    if (-not $process.WaitForExit($Timeout * 1000)) {
        $process.Kill($true)
        throw "The program exceeded the $Timeout-second timeout."
    }

    return [pscustomobject]@{
        ExitCode = $process.ExitCode
        Output = $stdoutTask.Result
        Error = $stderrTask.Result
    }
}

if (-not (Test-Path -LiteralPath $PlanPath -PathType Leaf)) {
    throw "Test plan not found: $PlanPath"
}

$javaMajor = Get-JavaMajorVersion "java"
$javacMajor = Get-JavaMajorVersion "javac"
if ($javaMajor -ne 25 -or $javacMajor -ne 25) {
    throw "Java 25 is required. Detected java $javaMajor and javac $javacMajor."
}

$cases = Read-TestCases (Resolve-Path -LiteralPath $PlanPath)
$sourceFiles = @(Get-ChildItem -LiteralPath $SourcePath -Filter "*.java" -File | Sort-Object FullName)
if ($sourceFiles.Count -eq 0) {
    throw "No Java source files found in $SourcePath."
}

New-Item -ItemType Directory -Force -Path $OutputDirectory | Out-Null
$compileArguments = @("--release", "25", "-d", $OutputDirectory) + @($sourceFiles.FullName)
& javac @compileArguments
if ($LASTEXITCODE -ne 0) {
    throw "Compilation failed with exit code $LASTEXITCODE."
}

Write-Output "Java UI test session"
Write-Output "Plan: $PlanPath"
Write-Output "Cases: $($cases.Count)"
Write-Output ""

$passed = 0
foreach ($testCase in $cases) {
    Write-Output "=== $($testCase.Name) ==="
    Write-Output "Aim: $($testCase.Aim)"
    Write-Output "--- Console input ---"
    Write-Output $testCase.Inputs

    $result = Invoke-Program $OutputDirectory $MainClass $testCase.Inputs $TimeoutSeconds
    Write-Output "--- Console output ---"
    Write-Output $result.Output
    if ($result.Error) {
        Write-Output "--- Console error ---"
        Write-Output $result.Error
    }

    $actual = Normalize-Output $result.Output
    $expected = Normalize-Output $testCase.Expected
    if ($result.ExitCode -ne 0 -or $result.Error -or $actual -cne $expected) {
        Write-Output "--- Result: FAIL ---"
        Write-Output "--- Expected output ---"
        Write-Output $testCase.Expected
        Write-Output "--- Actual output ---"
        Write-Output $result.Output
        if ($result.Error) {
            Write-Output "--- Actual error ---"
            Write-Output $result.Error
        }
        throw "Test failed: $($testCase.Name). The test session stopped immediately."
    }

    $passed++
    Write-Output "--- Result: PASS ---"
    Write-Output ""
}

Write-Output "All $passed UI test case(s) passed."
