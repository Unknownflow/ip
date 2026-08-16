import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Zen {
    private static final String DIVIDER = "____________________________________________________________";
    private static final String NAME = "Zen";
    private static final String INDENTATION = " ".repeat(5);
    private static final String INDENTED_DIVIDER = " ".repeat(4) + DIVIDER;
    private static final String DEADLINE_FORMAT = "\n     Deadline Format: deadline <description> /by <due by>";
    private static final String EVENT_FORMAT = "\n     Event Format: event <description> /from <start> /to <end>";
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

    private static Task getTaskAt(List<Task> tasks, int idx, String action) throws ZenException {
        if (idx <= 0) {
            String exceptionMessage = String.format("%s index should be positive.", action);
            throw new ZenException(exceptionMessage);
        }
        if (idx > tasks.size()) {
            String exceptionMessage = String.format(
                    "Todo only has %d items, I am unable to %s task %d as done.", tasks.size(), action, idx
            );
            throw new ZenException(exceptionMessage);
        }
        return tasks.get(idx - 1);
    }

    private static Deadline parseDeadline(String userInput) throws ZenException {
        int byIdx = userInput.indexOf("/by");

        if (byIdx == -1) {
            throw new ZenException("Please include /by in your event to separate description and due by."
                    + DEADLINE_FORMAT);
        }

        String description = userInput.substring(0, byIdx).trim();
        if (description.isEmpty()) {
            throw new ZenException("The description of a Deadline cannot be empty. Please try again!"
                    + DEADLINE_FORMAT);
        }

        int nextByIdx = userInput.indexOf("/by", byIdx + "/by".length());
        if (nextByIdx != -1) {
            throw new ZenException("Please include only one /by in your event to separate description and due by."
                    + DEADLINE_FORMAT);
        }

        String dueBy = userInput.substring(byIdx + "/by".length()).trim();
        if (dueBy.isEmpty()) {
            throw new ZenException("The due by date / time cannot be empty. Please try again!"
                    + DEADLINE_FORMAT);
        }

        return new Deadline(description, dueBy);
    }

    private static Event parseEvent(String userInput) throws ZenException {
        int fromIdx = userInput.indexOf("/from");
        int toIdx = userInput.indexOf("/to");

        if (fromIdx == -1) {
            throw new ZenException("Please include /from in your event to separate description and timings."
                    + EVENT_FORMAT);
        }
        if (toIdx == -1) {
            throw new ZenException("Please include /to in your event." + EVENT_FORMAT);
        }
        if (fromIdx > toIdx) {
            throw new ZenException("/from must come before /to." + EVENT_FORMAT);
        }

        if (userInput.indexOf("/from", fromIdx + "/from".length()) != -1) {
            throw new ZenException("Please include only 1 /from in your event." + EVENT_FORMAT);
        }
        if (userInput.indexOf("/to", toIdx + "/to".length()) != -1) {
            throw new ZenException("Please include only 1 /to in your event." + EVENT_FORMAT);
        }

        String description = userInput.substring(0, fromIdx).trim();
        String start = userInput.substring(fromIdx + "/from".length(), toIdx).trim();
        String end = userInput.substring(toIdx + "/to".length()).trim();

        if (description.isEmpty()) {
            throw new ZenException("The description of an Event cannot be empty. Please try again." + EVENT_FORMAT);
        }
        if (start.isEmpty()) {
            throw new ZenException("The start time of an Event cannot be empty. Please try again!" + EVENT_FORMAT);
        }
        if (end.isEmpty()) {
            throw new ZenException("The end time of an Event cannot be empty. Please try again!" + EVENT_FORMAT);
        }

        return new Event(description, start, end);
    }

    public static void main(String[] args) throws ZenException {
        List<Task> tasks = new ArrayList<>();
        String greetingTemplate = """
                %s
                %s
                    Hello! I'm %s.
                    What can I do for you?
                %s
                """;
        String exitTemplate = """
                %s
                    Bye. See you again soon!
                %s
                """;
        String greeting = String.format(greetingTemplate, INDENTED_DIVIDER, BANNER, NAME, INDENTED_DIVIDER);
        String exitMessage = String.format(exitTemplate, INDENTED_DIVIDER, INDENTED_DIVIDER);
        Scanner scanner = new Scanner(System.in);

        System.out.println(greeting);

        while (scanner.hasNextLine()) {
            String command = scanner.next();

            if (command.equals("bye")) {
                break;
            }

            System.out.println(INDENTED_DIVIDER);

            try {
                // AI-assisted: AI suggested using switch instead of if statements
                switch (command) {
                    case "list" -> listTasks(tasks);
                    case "mark" -> {
                        if (!scanner.hasNextInt()) {
                            // skip the rest of the line since the input is not an integer
                            scanner.nextLine();
                            throw new ZenException("Only integers are allowed for marking items as done.");
                        }

                        int idx = scanner.nextInt();
                        Task task = getTaskAt(tasks, idx, "mark");
                        markTaskDone(task);
                    }
                    case "unmark" -> {
                        if (!scanner.hasNextInt()) {
                            // skip the rest of the line since the input is not an integer
                            scanner.nextLine();
                            throw new ZenException("Only integers are allowed for unmarking items as done.");
                        }

                        int idx = scanner.nextInt();
                        Task task = getTaskAt(tasks, idx, "unmark");
                        markTaskNotDone(task);
                    }
                    case "todo" -> {
                        // input format: todo <description>
                        String description = scanner.nextLine().trim();
                        if (description.isEmpty()) {
                            throw new ZenException("The description of a Todo cannot be empty. Please try again!");
                        }

                        Task newTodo = new Todo(description);
                        addTask(tasks, newTodo);
                    }
                    case "deadline" -> {
                        // input format: deadline <description> /by <dueBy>
                        String userInput = scanner.nextLine().trim();
                        Task newDeadline = parseDeadline(userInput);
                        addTask(tasks, newDeadline);
                    }
                    case "event" -> {
                        // input format: event <description> /from <start> /to <end>
                        String userInput = scanner.nextLine().trim();
                        Task newEvent = parseEvent(userInput);
                        addTask(tasks, newEvent);
                    }
                    default -> {
                        // if the command is not valid, throw an exception
                        scanner.nextLine();
                        throw new ZenException("Command not found. Please try again!");
                    }
                }
            } catch (ZenException e) {
                echo(e.getMessage());
            }

            System.out.println(INDENTED_DIVIDER);
        }

        scanner.close();
        System.out.println(exitMessage);
    }
}
