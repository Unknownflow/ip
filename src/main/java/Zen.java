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

    private static Task getDeadline(String[] userInputs) throws ZenException {
        if (userInputs.length < 2) {
            throw new ZenException("Please include /by in your event to separate description and due by."
                    + DEADLINE_FORMAT);
        }

        String description = userInputs[0].trim();
        if (description.isEmpty()) {
            throw new ZenException("The description of a Deadline cannot be empty. Please try again!"
                    + DEADLINE_FORMAT);
        }

        if (userInputs.length > 2) {
            throw new ZenException("Please include only 1 /by in your event to separate description and due by."
                    + DEADLINE_FORMAT);
        }

        String dueBy = userInputs[1].trim();
        if (dueBy.isEmpty()) {
            throw new ZenException("The due by date / time cannot be empty. Please try again!"
                    + DEADLINE_FORMAT);
        }

        return new Deadline(description, dueBy);
    }

    private static Task getEvent(String[] userInputs) throws ZenException {
        if (userInputs.length < 2) {
            throw new ZenException("Please include /from in your event to separate description and timings."
                    + EVENT_FORMAT);
        }

        if (userInputs.length > 2) {
            throw new ZenException("Please include only 1 /from in your event." + EVENT_FORMAT);
        }

        String description = userInputs[0].trim();
        if (description.isEmpty()) {
            throw new ZenException("The description of an Event cannot be empty. Please try again."
                    + EVENT_FORMAT);
        }

        // start and end timings are still together, hence they need to be split
        String[] timings = userInputs[1].split(" /to ");
        if (timings.length < 2) {
            throw new ZenException("Please include /to in your event." + EVENT_FORMAT);
        }

        if (timings.length > 2) {
            throw new ZenException("Please include only 1 /to in your event." + EVENT_FORMAT);
        }

        String start = timings[0].trim();
        if (start.isEmpty()) {
            throw new ZenException("The start time of an Event cannot be empty. Please try again!" + EVENT_FORMAT);
        }

        String end = timings[1].trim();
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
                        int idx = scanner.nextInt();
                        // array is 0-indexed, hence it is idx - 1
                        Task task = tasks.get(idx - 1);
                        markTaskDone(task);
                    }
                    case "unmark" -> {
                        int idx = scanner.nextInt();
                        // array is 0-indexed, hence it is idx - 1
                        Task task = tasks.get(idx - 1);
                        markTaskNotDone(task);
                    }
                    case "todo" -> {
                        // need to trim due to leading spaces
                        // input format: todo <description>
                        String description = scanner.nextLine().trim();
                        if (description.isEmpty()) {
                            throw new ZenException("The description of a Todo cannot be empty. Please try again!");
                        }

                        Task newTodo = new Todo(description);
                        addTask(tasks, newTodo);
                    }
                    case "deadline" -> {
                        // deadline is separated by "/by", need to trim due to leading spaces
                        // input format: deadline <description> /by <dueBy>
                        String[] userInputs = scanner.nextLine().trim().split(" /by ");
                        Task newDeadline = getDeadline(userInputs);
                        addTask(tasks, newDeadline);
                    }
                    case "event" -> {
                        // event is separated by "/from" and "/to", need to trim due to leading spaces
                        // input format: event <description> /from <start> /to <end>
                        String[] userInputs = scanner.nextLine().trim().split(" /from ");
                        Task newEvent = getEvent(userInputs);
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
        System.out.println(exitMessage);
    }
}
