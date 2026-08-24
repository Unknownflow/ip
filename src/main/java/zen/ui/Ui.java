package zen.ui;

import java.util.Scanner;

import zen.task.Task;
import zen.task.TaskList;

/** Handles console input and output for the Zen application. */
public class Ui {
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
    private final Scanner scanner;

    /** Creates a user interface that reads commands from standard input. */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /** Returns the next trimmed command read from standard input. */
    public String readCommand() {
        return scanner.nextLine().trim();
    }

    /**
     * Prints each supplied line with the standard console indentation.
     *
     * @param lines lines to print; embedded line breaks are also indented
     */
    private static void printIndented(String... lines) {
        for (String line : lines) {
            // Ensure each line after a line break begins with indentation.
            for (String individualLine : line.split("\\R")) {
                System.out.println(INDENTATION + individualLine);
            }
        }
    }

    /** Prints a divider between console messages. */
    public void printDivider() {
        System.out.println(INDENTED_DIVIDER);
    }

    /**
     * Prints confirmation that a task was added.
     *
     * @param task the task that was added.
     * @param size the resulting number of tasks.
     */
    public void printAddTask(Task task, int size) {
        printIndented(
                "Got it. I've added this task:",
                String.format("  %s", task),
                String.format("Now you have %d tasks in the list.", size)
        );
    }

    /**
     * Prints a message with the standard console indentation.
     *
     * @param echoString the message to print.
     */
    public void echo(String echoString) {
        printIndented(echoString);
    }

    /**
     * Prints a task list, or a message when it is empty.
     *
     * @param taskList the task list to print.
     */
    public void printTaskList(TaskList taskList) {
        if (taskList.isEmpty()) {
            printIndented("Your task list is currently empty.");
        } else {
            printIndented("Here are the tasks in your list:");
            printIndented(taskList.toString());
        }
    }

    /**
     * Prints confirmation that a task was completed.
     *
     * @param task the completed task.
     */
    public void printMarkTaskDone(Task task) {
        printIndented(
                "Nice! I've marked this task as done:",
                String.format("  %s", task)
        );
    }

    /**
     * Prints confirmation that a task was marked incomplete.
     *
     * @param task the incomplete task.
     */
    public void printMarkTaskNotDone(Task task) {
        printIndented(
                "OK, I've marked this task as not done yet:",
                String.format("  %s", task)
        );
    }

    /**
     * Prints confirmation that a task was deleted.
     *
     * @param task the deleted task.
     * @param size the resulting number of tasks.
     */
    public void printDeleteTask(Task task, int size) {
        printIndented(
                "Noted. I've removed this task:",
                String.format("  %s", task),
                String.format("Now you have %s tasks in the list.", size)
        );
    }

    /** Prints the application greeting. */
    public void printGreeting() {
        String greeting = """
                %s
                %s
                    Hello! I'm %s.
                    What can I do for you?
                %s
                """.formatted(INDENTED_DIVIDER, BANNER, NAME, INDENTED_DIVIDER);
        System.out.println(greeting);
    }

    /** Prints the farewell message and closes standard input. */
    public void printExitMessage() {
        echo("Bye. See you again soon!");
        scanner.close();
    }

    /**
     * Prints an initialization error message.
     *
     * @param errorMessage the error explanation.
     */
    public void printErrorMessage(String errorMessage) {
        String exitMessage = """
                %s
                    Error occurred: %s
                %s
                """.formatted(INDENTED_DIVIDER, errorMessage, INDENTED_DIVIDER);
        System.out.println(exitMessage);
    }
}
