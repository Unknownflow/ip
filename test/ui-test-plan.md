# Zen UI Test Plan

## Execution

- **Application:** `zen.Zen`
- **Java version:** `25`
- **Source directory:** `src/main/java`
- **Compile behavior:** The `test-ui` skill compiles all Java sources with `javac --release 25` into `out/ui-test` before running cases.
- **Isolation:** Each automated test case starts a fresh `zen.Zen` process with an empty, pre-existing `data/task_list.txt` file, unless its case provides storage data.
- **First-run storage check:** Before release, manually run the application from a working directory that contains neither `data/` nor `data/task_list.txt`. Enter `todo first task`, then `bye`. Verify that the application continues normally and creates `data/task_list.txt` containing `T | 0 | first task`. This manual check is required because the automated runner deliberately pre-creates the storage file for every case.
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

### UI-002 — Negative: unknown command leaves an empty list unchanged

**Aim:** Verify that an incorrect command is rejected and does not create a task in an empty list.

**Inputs:**
```text
unknown
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
     Command not found. Please try again!
    ____________________________________________________________
    ____________________________________________________________
     Your task list is currently empty.
    ____________________________________________________________
    ____________________________________________________________
     Bye. See you again soon!
    ____________________________________________________________
```

### UI-003 — Positive: add and list a todo

**Aim:** Verify that a valid todo command adds a task and `list` displays the stored task.

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

### UI-004 — Negative: empty todo does not change task state

**Aim:** Verify that a todo with no description is rejected and the list remains empty.

**Inputs:**
```text
todo
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
     The description of a Todo cannot be empty. Please try again!
    ____________________________________________________________
    ____________________________________________________________
     Your task list is currently empty.
    ____________________________________________________________
    ____________________________________________________________
     Bye. See you again soon!
    ____________________________________________________________
```

### UI-005 — Positive: add deadline and event tasks

**Aim:** Verify that valid deadline and event inputs are stored with their respective metadata and listed in insertion order.

**Inputs:**
```text
deadline submit report /by 2026-08-24 18:00:00
event team meeting /from 2026-08-24 09:00:00 /to 2026-08-24 10:00:00
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
       [D][ ] submit report (by: Aug 24 2026 18:00:00)
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [E][ ] team meeting (from: Aug 24 2026 09:00:00 to: Aug 24 2026 10:00:00)
     Now you have 2 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[D][ ] submit report (by: Aug 24 2026 18:00:00)
     2.[E][ ] team meeting (from: Aug 24 2026 09:00:00 to: Aug 24 2026 10:00:00)
    ____________________________________________________________
    ____________________________________________________________
     Bye. See you again soon!
    ____________________________________________________________
```

### UI-006 — Negative: malformed deadline and event inputs do not add tasks

**Aim:** Verify that missing deadline and event delimiters, followed by an empty todo, are rejected without changing the empty task list.

**Inputs:**
```text
deadline missing date
event missing end /from Monday
todo
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
     Please include /by in your event to separate description and due by.
     Deadline Format: deadline <description> /by yyyy-MM-dd HH:mm:ss
    ____________________________________________________________
    ____________________________________________________________
     Please include /to in your event.
     Event Format: event <description> /from yyyy-MM-dd HH:mm:ss /to yyyy-MM-dd HH:mm:ss
    ____________________________________________________________
    ____________________________________________________________
     The description of a Todo cannot be empty. Please try again!
    ____________________________________________________________
    ____________________________________________________________
     Your task list is currently empty.
    ____________________________________________________________
    ____________________________________________________________
     Bye. See you again soon!
    ____________________________________________________________
```

### UI-007 — Positive: mark and unmark a task

**Aim:** Verify that marking changes a task to done, unmarking restores it to not done, and both states are reflected by `list`.

**Inputs:**
```text
todo submit form
mark 1
list
unmark 1
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
       [T][ ] submit form
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Nice! I've marked this task as done:
       [T][X] submit form
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][X] submit form
    ____________________________________________________________
    ____________________________________________________________
     OK, I've marked this task as not done yet:
       [T][ ] submit form
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] submit form
    ____________________________________________________________
    ____________________________________________________________
     Bye. See you again soon!
    ____________________________________________________________
```

### UI-008 — Negative: unknown command preserves a completed task

**Aim:** Verify that an incorrect command after marking a task does not undo or duplicate the completed task.

**Inputs:**
```text
todo keep this
mark 1
nonsense
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
       [T][ ] keep this
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Nice! I've marked this task as done:
       [T][X] keep this
    ____________________________________________________________
    ____________________________________________________________
     Command not found. Please try again!
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][X] keep this
    ____________________________________________________________
    ____________________________________________________________
     Bye. See you again soon!
    ____________________________________________________________
```

### UI-009 — Positive: preserve order and count for multiple todos

**Aim:** Verify that two valid todos are both stored, counted, and listed in insertion order.

**Inputs:**
```text
todo alpha
todo beta
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
       [T][ ] alpha
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] beta
     Now you have 2 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] alpha
     2.[T][ ] beta
    ____________________________________________________________
    ____________________________________________________________
     Bye. See you again soon!
    ____________________________________________________________
```

### UI-010 — Negative: duplicate deadline delimiter does not add a task

**Aim:** Verify that a deadline containing two `/by` delimiters is rejected and a later valid todo is still stored as the first task.

**Inputs:**
```text
deadline report /by Friday /by Monday
todo recovered
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
     Please include only one /by in your event to separate description and due by.
     Deadline Format: deadline <description> /by yyyy-MM-dd HH:mm:ss
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] recovered
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] recovered
    ____________________________________________________________
    ____________________________________________________________
     Bye. See you again soon!
    ____________________________________________________________
```

### UI-011 — Positive: trim event fields and preserve metadata

**Aim:** Verify that a valid event with time values is stored with the correct start and end metadata.

**Inputs:**
```text
event project sync /from 2026-08-24 09:00:00 /to 2026-08-24 10:30:00
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
       [E][ ] project sync (from: Aug 24 2026 09:00:00 to: Aug 24 2026 10:30:00)
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[E][ ] project sync (from: Aug 24 2026 09:00:00 to: Aug 24 2026 10:30:00)
    ____________________________________________________________
    ____________________________________________________________
     Bye. See you again soon!
    ____________________________________________________________
```

### UI-012 — Negative: malformed events do not affect subsequent state

**Aim:** Verify that events with an empty description or start time are rejected, and a later valid todo becomes the only task.

**Inputs:**
```text
event /from 2026-08-24 09:00:00 /to 2026-08-24 10:00:00
event sync /from /to 2026-08-24 10:00:00
todo recovered event
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
     The description of an Event cannot be empty. Please try again.
     Event Format: event <description> /from yyyy-MM-dd HH:mm:ss /to yyyy-MM-dd HH:mm:ss
    ____________________________________________________________
    ____________________________________________________________
     The start time of an Event cannot be empty. Please try again!
     Event Format: event <description> /from yyyy-MM-dd HH:mm:ss /to yyyy-MM-dd HH:mm:ss
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] recovered event
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] recovered event
    ____________________________________________________________
    ____________________________________________________________
     Bye. See you again soon!
    ____________________________________________________________
```

### UI-013 — Positive: repeated mark and unmark commands are idempotent

**Aim:** Verify that repeating `mark` keeps a task done and repeating `unmark` keeps it not done.

**Inputs:**
```text
todo repeat state
mark 1
mark 1
unmark 1
unmark 1
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
       [T][ ] repeat state
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Nice! I've marked this task as done:
       [T][X] repeat state
    ____________________________________________________________
    ____________________________________________________________
     Nice! I've marked this task as done:
       [T][X] repeat state
    ____________________________________________________________
    ____________________________________________________________
     OK, I've marked this task as not done yet:
       [T][ ] repeat state
    ____________________________________________________________
    ____________________________________________________________
     OK, I've marked this task as not done yet:
       [T][ ] repeat state
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] repeat state
    ____________________________________________________________
    ____________________________________________________________
     Bye. See you again soon!
    ____________________________________________________________
```

### UI-014 — Negative: unknown command between tasks preserves order

**Aim:** Verify that an invalid command between two valid additions does not create, remove, or reorder tasks.

**Inputs:**
```text
todo first
unknown
todo second
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
       [T][ ] first
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Command not found. Please try again!
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] second
     Now you have 2 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] first
     2.[T][ ] second
    ____________________________________________________________
    ____________________________________________________________
     Bye. See you again soon!
    ____________________________________________________________
```

### UI-015 — Positive: list an empty state before adding a task

**Aim:** Verify that listing an empty task collection is safe and that a subsequent valid todo becomes task 1.

**Inputs:**
```text
list
todo after empty list
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
     Your task list is currently empty.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] after empty list
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] after empty list
    ____________________________________________________________
    ____________________________________________________________
     Bye. See you again soon!
    ____________________________________________________________
```

### UI-016 — Negative: whitespace-only todo does not create an empty task

**Aim:** Verify that a todo containing only spaces is rejected and a later valid todo remains the only task.

**Inputs:**
```text
todo   
todo valid after whitespace
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
     The description of a Todo cannot be empty. Please try again!
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] valid after whitespace
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] valid after whitespace
    ____________________________________________________________
    ____________________________________________________________
     Bye. See you again soon!
    ____________________________________________________________
```

### UI-017 — Positive: trim deadline fields

**Aim:** Verify that extra spaces around a deadline description and due date are trimmed without changing the stored values.

**Inputs:**
```text
deadline   pay bills   /by   2026-08-24 18:00:00
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
       [D][ ] pay bills (by: Aug 24 2026 18:00:00)
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[D][ ] pay bills (by: Aug 24 2026 18:00:00)
    ____________________________________________________________
    ____________________________________________________________
     Bye. See you again soon!
    ____________________________________________________________
```

### UI-018 — Negative: empty deadline description does not affect later state

**Aim:** Verify that a deadline with an empty description is rejected and a later valid todo becomes task 1.

**Inputs:**
```text
deadline /by Friday
todo after bad deadline
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
     The description of a Deadline cannot be empty. Please try again!
     Deadline Format: deadline <description> /by yyyy-MM-dd HH:mm:ss
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] after bad deadline
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] after bad deadline
    ____________________________________________________________
    ____________________________________________________________
     Bye. See you again soon!
    ____________________________________________________________
```

### UI-019 — Positive: trim event fields

**Aim:** Verify that extra spaces around an event description and timings are trimmed before storage.

**Inputs:**
```text
event   design review   /from   2026-08-24 09:00:00   /to   2026-08-24 10:00:00
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
       [E][ ] design review (from: Aug 24 2026 09:00:00 to: Aug 24 2026 10:00:00)
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[E][ ] design review (from: Aug 24 2026 09:00:00 to: Aug 24 2026 10:00:00)
    ____________________________________________________________
    ____________________________________________________________
     Bye. See you again soon!
    ____________________________________________________________
```

### UI-020 — Negative: duplicate event delimiter does not add an event

**Aim:** Verify that an event containing two `/to` delimiters is rejected and a later valid event becomes the only task.

**Inputs:**
```text
event sync /from Monday /to Tuesday /to Wednesday
event recovered /from 2026-08-24 09:00:00 /to 2026-08-24 10:00:00
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
     Please include only 1 /to in your event.
     Event Format: event <description> /from yyyy-MM-dd HH:mm:ss /to yyyy-MM-dd HH:mm:ss
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [E][ ] recovered (from: Aug 24 2026 09:00:00 to: Aug 24 2026 10:00:00)
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[E][ ] recovered (from: Aug 24 2026 09:00:00 to: Aug 24 2026 10:00:00)
    ____________________________________________________________
    ____________________________________________________________
     Bye. See you again soon!
    ____________________________________________________________
```

### UI-021 — Positive: preserve punctuation and reserved markers in todo text

**Aim:** Verify that punctuation and text resembling a deadline marker remain part of a todo description.

**Inputs:**
```text
todo call /by support? #2
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
       [T][ ] call /by support? #2
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] call /by support? #2
    ____________________________________________________________
    ____________________________________________________________
     Bye. See you again soon!
    ____________________________________________________________
```

### UI-022 — Negative: empty event start does not affect later state

**Aim:** Verify that an event with an empty start time is rejected and a later valid todo becomes the only task.

**Inputs:**
```text
event sync /from  /to Tuesday
todo after bad event
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
     The start time of an Event cannot be empty. Please try again!
     Event Format: event <description> /from yyyy-MM-dd HH:mm:ss /to yyyy-MM-dd HH:mm:ss
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] after bad event
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] after bad event
    ____________________________________________________________
    ____________________________________________________________
     Bye. See you again soon!
    ____________________________________________________________
```

### UI-023 — Positive: mark only the selected task

**Aim:** Verify that marking task 2 leaves task 1 incomplete and preserves both task positions.

**Inputs:**
```text
todo first task
todo second task
mark 2
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
       [T][ ] first task
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] second task
     Now you have 2 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Nice! I've marked this task as done:
       [T][X] second task
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] first task
     2.[T][X] second task
    ____________________________________________________________
    ____________________________________________________________
     Bye. See you again soon!
    ____________________________________________________________
```

### UI-024 — Negative: empty event end does not affect later state

**Aim:** Verify that an event without an end time is rejected and a later valid todo becomes the only task.

**Inputs:**
```text
event sync /from Monday /to
todo after empty event
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
     The end time of an Event cannot be empty. Please try again!
     Event Format: event <description> /from yyyy-MM-dd HH:mm:ss /to yyyy-MM-dd HH:mm:ss
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] after empty event
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] after empty event
    ____________________________________________________________
    ____________________________________________________________
     Bye. See you again soon!
    ____________________________________________________________
```

### UI-030 — Positive: all task-list changes retain their console behaviour

**Aim:** Verify that adding, marking, unmarking, and deleting tasks (the operations that now trigger automatic saving) retain the expected task-list state and console output.

**Inputs:**
```text
todo saved task
mark 1
unmark 1
delete 1
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
       [T][ ] saved task
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Nice! I've marked this task as done:
       [T][X] saved task
    ____________________________________________________________
    ____________________________________________________________
     OK, I've marked this task as not done yet:
       [T][ ] saved task
    ____________________________________________________________
    ____________________________________________________________
     Noted. I've removed this task:
       [T][ ] saved task
     Now you have 0 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Your task list is currently empty.
    ____________________________________________________________
    ____________________________________________________________
     Bye. See you again soon!
    ____________________________________________________________
```

### UI-025 — Interleaved: invalid mark indices preserve a valid task state

**Aim:** Verify that a valid mark operation is retained when zero, out-of-range, and non-numeric mark inputs are rejected between valid commands.

**Inputs:**
```text
todo stable task
mark 1
mark 0
mark 2
mark one
list
unmark 1
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
       [T][ ] stable task
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Nice! I've marked this task as done:
       [T][X] stable task
    ____________________________________________________________
    ____________________________________________________________
     Maximum task number is 1.
    ____________________________________________________________
    ____________________________________________________________
     Maximum task number is 1.
    ____________________________________________________________
    ____________________________________________________________
     Only positive integers are allowed for task number.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][X] stable task
    ____________________________________________________________
    ____________________________________________________________
     OK, I've marked this task as not done yet:
       [T][ ] stable task
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] stable task
    ____________________________________________________________
    ____________________________________________________________
     Bye. See you again soon!
    ____________________________________________________________
```

### UI-026 — Interleaved: invalid delete inputs preserve task ordering

**Aim:** Verify that invalid delete indices do not remove a task, and that a later valid delete removes only the selected task.

**Inputs:**
```text
todo first
todo second
delete 0
delete 3
delete two
list
delete 1
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
       [T][ ] first
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] second
     Now you have 2 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Maximum task number is 2.
    ____________________________________________________________
    ____________________________________________________________
     Maximum task number is 2.
    ____________________________________________________________
    ____________________________________________________________
     Only positive integers are allowed for task number.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] first
     2.[T][ ] second
    ____________________________________________________________
    ____________________________________________________________
     Noted. I've removed this task:
       [T][ ] first
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] second
    ____________________________________________________________
    ____________________________________________________________
     Bye. See you again soon!
    ____________________________________________________________
```

### UI-027 — Interleaved: malformed event does not corrupt existing tasks

**Aim:** Verify that an event with `/to` before `/from` is rejected after a valid deadline, and that a later valid event is appended as task 2.

**Inputs:**
```text
deadline submit form /by 2026-08-24 18:00:00
event reversed /to Tuesday /from Monday
event project meeting /from 2026-08-24 09:00:00 /to 2026-08-24 10:00:00
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
       [D][ ] submit form (by: Aug 24 2026 18:00:00)
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     /from must come before /to.
     Event Format: event <description> /from yyyy-MM-dd HH:mm:ss /to yyyy-MM-dd HH:mm:ss
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [E][ ] project meeting (from: Aug 24 2026 09:00:00 to: Aug 24 2026 10:00:00)
     Now you have 2 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[D][ ] submit form (by: Aug 24 2026 18:00:00)
     2.[E][ ] project meeting (from: Aug 24 2026 09:00:00 to: Aug 24 2026 10:00:00)
    ____________________________________________________________
    ____________________________________________________________
     Bye. See you again soon!
    ____________________________________________________________
```

### UI-028 — Interleaved: commands with unexpected arguments preserve tasks

**Aim:** Verify that `list` rejects unexpected arguments without changing an existing task, and that a subsequent valid add and list retain both tasks.

**Inputs:**
```text
todo before invalid list
list extra
todo after invalid list
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
       [T][ ] before invalid list
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     The list command does not take arguments.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] after invalid list
     Now you have 2 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] before invalid list
     2.[T][ ] after invalid list
    ____________________________________________________________
    ____________________________________________________________
     Bye. See you again soon!
    ____________________________________________________________
```

### UI-031 — Positive: load tasks saved on disk

**Aim:** Verify that tasks in the pipe-delimited storage file are loaded at startup with their types, completion status, and date/time details preserved.

**Storage data:**
```text
T | 1 | read book
D | 0 | return book | 2026-08-24T18:00
E | 0 | project meeting | 2026-08-24T14:00 to 2026-08-24T16:00
```

**Inputs:**
```text
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
     Here are the tasks in your list:
     1.[T][X] read book
     2.[D][ ] return book (by: Aug 24 2026 18:00:00)
     3.[E][ ] project meeting (from: Aug 24 2026 14:00:00 to: Aug 24 2026 16:00:00)
    ____________________________________________________________
    ____________________________________________________________
     Bye. See you again soon!
    ____________________________________________________________
```

### UI-032 — Positive and negative: find tasks occurring on a date

**Aim:** Verify that `occur` includes deadlines due on the requested date and events spanning that date, excludes todos, handles a date without matches, and rejects malformed dates.

**Inputs:**
```text
deadline submit report /by 2026-08-24 18:00:00
event overnight deployment /from 2026-08-23 23:00:00 /to 2026-08-24 01:00:00
todo unrelated
occur 2026-08-24
occur 2026-08-25
occur 2026/08/24
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
       [D][ ] submit report (by: Aug 24 2026 18:00:00)
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [E][ ] overnight deployment (from: Aug 23 2026 23:00:00 to: Aug 24 2026 01:00:00)
     Now you have 2 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] unrelated
     Now you have 3 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Tasks for 2026-08-24
     Here are the tasks in your list:
     1.[D][ ] submit report (by: Aug 24 2026 18:00:00)
     2.[E][ ] overnight deployment (from: Aug 23 2026 23:00:00 to: Aug 24 2026 01:00:00)
    ____________________________________________________________
    ____________________________________________________________
     Tasks for 2026-08-25
     Your task list is currently empty.
    ____________________________________________________________
    ____________________________________________________________
     The date is in the incorrect format.
      Correct format: yyyy-MM-dd
    ____________________________________________________________
    ____________________________________________________________
     Bye. See you again soon!
    ____________________________________________________________
```

### UI-033 — Negative: invalid date-times do not add tasks

**Aim:** Verify that malformed deadline and event date-times, including an event that ends before it starts, are rejected without changing subsequent task state.

**Inputs:**
```text
deadline invalid /by 2026-08-24
event malformed /from 2026-08-24 09:00:00 /to tomorrow
event backwards /from 2026-08-24 10:00:00 /to 2026-08-24 09:00:00
todo recovered
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
     The due by datetime is in the incorrect format.
     Deadline Format: deadline <description> /by yyyy-MM-dd HH:mm:ss
    ____________________________________________________________
    ____________________________________________________________
     The start / end datetime is in the incorrect format.
     Event Format: event <description> /from yyyy-MM-dd HH:mm:ss /to yyyy-MM-dd HH:mm:ss
    ____________________________________________________________
    ____________________________________________________________
     The start datetime is after the end datetime.
     Event Format: event <description> /from yyyy-MM-dd HH:mm:ss /to yyyy-MM-dd HH:mm:ss
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] recovered
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] recovered
    ____________________________________________________________
    ____________________________________________________________
     Bye. See you again soon!
    ____________________________________________________________
```

### UI-034 — Find: matching task descriptions

**Aim:** Verify that `find` displays matching task descriptions in their original order, excludes a differently cased description, and reports when no task matches.

**Inputs:**
```text
todo read book
deadline return book /by 2026-06-06 18:00:00
todo Book club
find book
find notes
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
       [T][ ] read book
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [D][ ] return book (by: Jun 06 2026 18:00:00)
     Now you have 2 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] Book club
     Now you have 3 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Here are the matching tasks in your list:
     1.[T][ ] read book
     2.[D][ ] return book (by: Jun 06 2026 18:00:00)
    ____________________________________________________________
    ____________________________________________________________
     No matching tasks in your list.
    ____________________________________________________________
    ____________________________________________________________
     Bye. See you again soon!
    ____________________________________________________________
```

### UI-029 — Negative: task operations on an empty list are rejected

**Aim:** Verify that mark, unmark, and delete report a clear error without changing an empty task list.

**Inputs:**
```text
mark 1
unmark 1
delete 1
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
     Task list is empty. Add new tasks into task list.
    ____________________________________________________________
    ____________________________________________________________
     Task list is empty. Add new tasks into task list.
    ____________________________________________________________
    ____________________________________________________________
     Task list is empty. Add new tasks into task list.
    ____________________________________________________________
    ____________________________________________________________
     Bye. See you again soon!
    ____________________________________________________________
```
