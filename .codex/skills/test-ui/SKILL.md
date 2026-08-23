---
name: test-ui
description: Run fail-fast, black-box console UI tests for this Java project from test/ui-test-plan.md. Use when Codex needs to execute lists of user commands, compare the program's console output with exact expected output, show input/output transcripts, or diagnose the first failed UI test.
---

# Test UI

Use `test/ui-test-plan.md` as the source of truth for console UI tests. Keep the
test plan updated before running tests; if the user provides test cases in the
prompt, record them in the plan first.

## Test-plan format

Record project execution details and one case per `###` heading. Every case
must contain an aim, a fenced list of console inputs, and a fenced expected
output block:

```markdown
### UI-001 — Add and list a todo

**Aim:** Verify that a todo is stored and displayed by `list`.

**Inputs:**
```text
todo buy milk
list
bye
```

**Expected output:**
```text
...exact console output...
```
```

Inputs are sent to one fresh program process for that case, in order. Include
the terminating command (normally `bye`). Expected output is compared exactly
after normalizing only `CRLF`/`CR` to `LF` and ignoring the process's final
line-ending. Do not use ellipses, prose, or regular expressions in the
expected-output block.

## Run the tests

1. Read the complete plan and check that each case has an aim, inputs, and
   expected output.
2. Use Java 25. If the active `java` or `javac` is not version 25, stop and
   switch the project to JDK 25 before testing.
3. From the repository root, run:

   ```powershell
   & .codex\skills\test-ui\scripts\run-ui-tests.ps1 -PlanPath test\ui-test-plan.md
   ```

   The runner compiles all `src/main/java/*.java` files into `out/ui-test`,
   starts a fresh `zen.Zen` process for each case, checks its output, and prints a
   transcript containing the console input and output.
4. Show the complete transcript in the response. If a case fails, stop
   immediately. Report its actual and expected output and do not run later
   cases. Do not change expected output merely to make a test pass.

Use `-MainClass` and `-SourcePath` when the project entry point or source
directory differs. Use `-TimeoutSeconds` for a deliberately slower program.
