# Zen User Guide

Zen is a command-line task manager. Enter one command at a time; Zen saves tasks in
`data/task_list.txt` and restores them when it starts.

## Task display and priority

Every task shows its type, completion status, and priority. Priorities are ordered
`high`, `medium`, `low`, then `none`. Tasks with the same priority retain the order in
which they were created. This is also the order used by task numbers in `mark`,
`unmark`, and `delete`.

```text
1.[T][ ] buy milk (priority: high)
2.[D][X] submit report (by: Aug 24 2026 18:00:00) (priority: none)
```

## Adding tasks

### Add a todo: `todo`

```text
todo <description> [/priority <high|medium|low>]
```

Examples:

```text
todo buy milk /priority high
todo clean the room
```

### Add a deadline: `deadline`

```text
deadline <description> /by yyyy-MM-dd HH:mm:ss [/priority <high|medium|low>]
```

Example:

```text
deadline submit report /by 2026-08-24 18:00:00 /priority medium
```

### Add an event: `event`

```text
event <description> /from yyyy-MM-dd HH:mm:ss /to yyyy-MM-dd HH:mm:ss
      [/priority <high|medium|low>]
```

Example:

```text
event project meeting /from 2026-08-24 09:00:00 /to 2026-08-24 10:00:00 /priority low
```

The optional `/priority` clause must be the final clause. Priority values are
case-insensitive, so `HIGH` and `high` are equivalent. Tasks without `/priority` have
priority `none`. Zen accepts only one priority clause; it is reserved syntax and
cannot be used as ordinary description text.

For a missing, unknown, misplaced, or duplicate priority clause, Zen creates no task
and displays:

```text
Invalid priority. Priority format: /priority <high|medium|low>
```

## Viewing tasks

### List all tasks: `list`

```text
list
```

Lists every task in priority order.

### Find by description: `find`

```text
find <keyword>
```

Displays tasks whose descriptions contain the case-sensitive keyword, in priority
order.

### View tasks on a date: `occur`

```text
occur yyyy-MM-dd
```

Displays deadlines due on that date and events spanning that date, in priority order.
Todos do not occur on a date.

## Managing tasks

```text
mark <task number>
unmark <task number>
delete <task number>
```

`mark` marks a task as done, `unmark` marks it as not done, and `delete` removes it.
Task numbers refer to the current priority-ordered task list. Priorities cannot be
changed after a task is created.

## Exiting Zen

```text
bye
```

Zen saves after every add, mark, unmark, and delete operation.

## Storage compatibility

New records append a lowercase priority field:

```text
T | 0 | buy milk | high
D | 0 | submit report | 2026-08-24T18:00 | medium
E | 0 | meeting | 2026-08-24T09:00 to 2026-08-24T10:00 | low
```

Older records without a priority field remain supported and load as priority `none`.
Malformed priority data prevents Zen from loading the saved list and triggers its
normal storage-load error.
