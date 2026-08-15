import java.util.ArrayList;
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

    /**
     * Prints an echo of the input string, wrapped with dividers
     *
     * @param echoString The string received to be echoed back to the user
     */
    private static void echo(String echoString) {
        printIndented(String.format("added: %s", echoString));
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

    public static void main(String[] args) {
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
                default -> {
                    // if there is no command given, add task to the List and echo it to the user
                    String userInput = command + scanner.nextLine();
                    Task newTask = new Task(userInput);
                    tasks.add(newTask);
                    echo(userInput);
                }
            }

            System.out.println(INDENTED_DIVIDER);
        }

        scanner.close();
        System.out.println(exitMessage);
    }
}
