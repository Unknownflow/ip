# Zen UI Test Plan

## Execution

- **Application:** `Zen`
- **Java version:** `25`
- **Source directory:** `src/main/java`
- **Compile behavior:** The `test-ui` skill compiles all Java sources with `javac --release 25` into `out/ui-test` before running cases.
- **Isolation:** Each test case starts a fresh `Zen` process.
- **Comparison:** Expected output is exact after normalizing line endings and ignoring only the final line-ending produced by the process.
- **Failure policy:** Stop immediately at the first failed case and report both expected and actual output.

## Test cases

### UI-001 — Greet and exit

**Aim:** Verify that the application starts, displays the greeting, and exits when the user enters `bye`.

**Inputs:**
```text
bye
```

**Expected output:**
```text
    ____________________________________________________________
     ______              
    |__  /___  _ __      
      / // _ \| '_ \   
     / /|  __/| | | |    
    /____\___||_| |_|
    Hello! I'm Zen.
    What can I do for you?
    ____________________________________________________________

    ____________________________________________________________
    Bye. See you again soon!
    ____________________________________________________________
```

### UI-002 — Add and list a todo

**Aim:** Verify that a todo command adds a task and `list` displays the stored task.

**Inputs:**
```text
todo buy milk
list
bye
```

**Expected output:**
```text
    ____________________________________________________________
     ______              
    |__  /___  _ __      
      / // _ \| '_ \   
     / /|  __/| | | |    
    /____\___||_| |_|
    Hello! I'm Zen.
    What can I do for you?
    ____________________________________________________________

    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] buy milk
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] buy milk
    ____________________________________________________________
    ____________________________________________________________
    Bye. See you again soon!
    ____________________________________________________________
```

### UI-003 — Reject an unknown command

**Aim:** Verify that an unrecognized command produces the invalid-command message and the application remains usable.

**Inputs:**
```text
hello
bye
```

**Expected output:**
```text
    ____________________________________________________________
     ______              
    |__  /___  _ __      
      / // _ \| '_ \   
     / /|  __/| | | |    
    /____\___||_| |_|
    Hello! I'm Zen.
    What can I do for you?
    ____________________________________________________________

    ____________________________________________________________
     This is not a valid command.
    ____________________________________________________________
    ____________________________________________________________
    Bye. See you again soon!
    ____________________________________________________________
```
