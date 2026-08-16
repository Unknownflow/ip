import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Zen {
    private static final String DIVIDER = "____________________________________________________________";
    private static final String NAME = "Zen";
    private static final String INDENTATION = " ".repeat(5);
    private static final String INDENTED_DIVIDER = " ".repeat(4) + DIVIDER;
    private static final String BANNER = """
                 ______             \s
                |__  /___  _ __     \s
                  / // _ \\| '_ \\  \s
                 / /|  __/| | | |   \s
                /____\\___||_| |_|\
            """;

    private static void printIndented(String... lines) {
        for (String line: lines) {
            System.out.println(INDENTATION + line);
        }
    }

    private static void addTask(List<Task> tasks, Task task) {
        tasks.add(task);

        printIndented(
                "Got it. I've added this task:",
                String.format("  %s", task),
                String.format("Now you have %d tasks in the list.", tasks.size())
        );
    }

    private static void echo(String echoString) {
        printIndented(echoString);
    }

    /**
     * Prints all the tasks in the order they are inserted in
     * @param tasks List storing all tasks
     */
    private static void listTasks(List<Task> tasks) {
        printIndented("Here are the tasks in your list:");

        for (int i = 0; i < tasks.size(); i++) {
            String output = String.format("%d.%s", i + 1, tasks.get(i));
            printIndented(output);
        }
    }

    private static void markTaskDone(Task task) {
        task.markAsDone();

        printIndented(
                "Nice! I've marked this task as done:",
                String.format("  %s", task)
        );
    }

    private static void markTaskNotDone(Task task) {
        task.markAsNotDone();

        printIndented(
                "OK, I've marked this task as not done yet:",
                String.format("  %s", task)
        );
    }

    private static void deleteTask(List<Task> tasks, Task task, int idx) {
        tasks.remove(idx - 1);

        printIndented(
                "Noted. I've removed this task:",
                String.format("  %s", task),
                String.format("Now you have %s tasks in the list.", tasks.size())
        );
    }

    private static Task getTaskAt(List<Task> tasks, int idx, String action) throws ZenException {
        if (idx <= 0) {
            String exceptionMessage = String.format("%s index should be positive.", action);
            throw new ZenException(exceptionMessage);
        }
        if (idx > tasks.size()) {
            String exceptionMessage = String.format(
                    "Todo only has %d items, I am unable to %s task %d.", tasks.size(), action, idx
            );
            throw new ZenException(exceptionMessage);
        }
        return tasks.get(idx - 1);
    }

    private static void printGreeting() {
        String greeting = """
                %s
                %s
                    Hello! I'm %s.
                    What can I do for you?
                %s
                """.formatted(INDENTED_DIVIDER, BANNER, NAME, INDENTED_DIVIDER);
        System.out.println(greeting);
    }

    private static void printExitMessage() {
        String exitMessage = """
                %s
                    Bye. See you again soon!
                %s
                """.formatted(INDENTED_DIVIDER, INDENTED_DIVIDER);
        System.out.println(exitMessage);
    }

    public static void main(String[] args) throws ZenException {
        List<Task> tasks = new ArrayList<>();
        Parser parser = new Parser();
        Scanner scanner = new Scanner(System.in);
        printGreeting();

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                continue;
            }

            String[] parts = input.split("\\s+", 2);
            Command command = Command.fromString(parts[0]);
            String arguments = parts.length == 2 ? parts[1] : "";

            if (command == Command.BYE) {
                break;
            }

            System.out.println(INDENTED_DIVIDER);

            try {
                // AI-assisted: AI suggested using switch instead of if statements
                switch (command) {
                    case LIST -> {
                        parser.requireNoArguments(arguments, "list");
                        listTasks(tasks);
                    }
                    case MARK -> {
                        int idx = parser.parseIndex(arguments, "mark");
                        Task task = getTaskAt(tasks, idx, "mark");
                        markTaskDone(task);
                    }
                    case UNMARK -> {
                        int idx = parser.parseIndex(arguments, "unmark");
                        Task task = getTaskAt(tasks, idx, "unmark");
                        markTaskNotDone(task);
                    }
                    case DELETE -> {
                        int idx = parser.parseIndex(arguments, "delete");
                        Task task = getTaskAt(tasks, idx, "delete");
                        deleteTask(tasks, task, idx);
                    }
                    case TODO -> {
                        // input format: todo <description>
                        if (arguments.isEmpty()) {
                            throw new ZenException("The description of a Todo cannot be empty. Please try again!");
                        }

                        Task newTodo = new Todo(arguments);
                        addTask(tasks, newTodo);
                    }
                    case DEADLINE -> {
                        // input format: deadline <description> /by <dueBy>
                        Task newDeadline = parser.parseDeadline(arguments);
                        addTask(tasks, newDeadline);
                    }
                    case EVENT -> {
                        // input format: event <description> /from <start> /to <end>
                        Task newEvent = parser.parseEvent(arguments);
                        addTask(tasks, newEvent);
                    }
                    default -> {
                        // if the command is not valid, throw an exception
                        throw new ZenException("Command not found. Please try again!");
                    }
                }
            } catch (ZenException e) {
                echo(e.getMessage());
            }

            System.out.println(INDENTED_DIVIDER);
        }

        scanner.close();
        printExitMessage();
    }
}
