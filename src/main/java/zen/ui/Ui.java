package zen.ui;

import zen.task.Task;
import zen.task.TaskList;

import java.util.Scanner;

/** Handles all console input and output for the chatbot. */
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

    /** Creates a UI that reads commands from standard input. */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Reads a line from standard input and trims it.
     *
     * @return the entered command without leading or trailing whitespace
     */
    public String readCommand() {
        return scanner.nextLine().trim();
    }

    /**
     * Prints each supplied line with the standard console indentation.
     *
     * @param lines lines to print; embedded line breaks are also indented
     */
    private static void printIndented(String... lines) {
        for (String line: lines) {
            // ensure all lines with a linebreak start on a new line with indentation
            for (String individualLine: line.split("\\R")) {
                System.out.println(INDENTATION + individualLine);
            }
        }
    }

    /** Prints the divider used between console responses. */
    public void printDivider() {
        System.out.println(INDENTED_DIVIDER);
    }

    /**
     * Displays confirmation message that a task was added.
     *
     * @param task added task
     * @param size total number of tasks in task list
     */
    public void printAddTask(Task task, int size) {
        printIndented(
                "Got it. I've added this task:",
                String.format("  %s", task),
                String.format("Now you have %d tasks in the list.", size)
        );
    }

    /**
     * Prints a message using the standard indentation.
     *
     * @param echoString message to print
     */
    public void echo(String echoString) {
        printIndented(echoString);
    }

    /**
     * Displays the supplied task list or an empty-list message.
     *
     * @param taskList task list to display
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
     * Displays confirmation message that a task was marked as done.
     *
     * @param task completed task
     */
    public void printMarkTaskDone(Task task) {
        printIndented(
                "Nice! I've marked this task as done:",
                String.format("  %s", task)
        );
    }

    /**
     * Displays confirmation message that a task was marked as not done.
     *
     * @param task incomplete task
     */
    public void printMarkTaskNotDone(Task task) {
        printIndented(
                "OK, I've marked this task as not done yet:",
                String.format("  %s", task)
        );
    }

    /**
     * Displays confirmation message that a task was removed.
     *
     * @param task removed task
     * @param size total number of tasks in task list
     */
    public void printDeleteTask(Task task, int size) {
        printIndented(
                "Noted. I've removed this task:",
                String.format("  %s", task),
                String.format("Now you have %s tasks in the list.", size)
        );
    }

    /** Displays the application banner and greeting. */
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

    /** Displays the farewell message and closes the input scanner. */
    public void printExitMessage() {
        echo("Bye. See you again soon!");
        scanner.close();
    }

    /**
     * Displays an unrecoverable error message.
     *
     * @param errorMessage error text to display
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
