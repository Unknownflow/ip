package zen.ui;

import java.util.Scanner;

import zen.task.Task;
import zen.task.TaskList;

/** Handles console input and output for the Zen application. */
public class Ui {
    private static final String DIVIDER = "____________________________________________________________";
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
    private final StringBuilder response;

    /** Creates a user interface that reads commands from standard input. */
    public Ui() {
        this.scanner = new Scanner(System.in);
        this.response = new StringBuilder();
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

    /**
     * Records lines that the graphical user interface displays after a command.
     *
     * @param lines response lines to record
     */
    private void recordResponse(String... lines) {
        for (String line : lines) {
            if (!response.isEmpty()) {
                response.append('\n');
            }
            response.append(line);
        }
    }

    /** Clears the response accumulated for the previous command. */
    public void clearResponse() {
        response.setLength(0);
    }

    /**
     * Returns the response accumulated while executing the current command.
     *
     * @return the command response without console indentation
     */
    public String getResponse() {
        return response.toString();
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
        String[] lines = {
            "Got it. I've added this task:",
            String.format("  %s", task),
            String.format("Now you have %d tasks in the list.", size)
        };
        echo(lines);
    }

    /**
     * Prints the message with the standard console indentation and records it for the GUI.
     *
     * @param lines the message to print.
     */
    public void echo(String... lines) {
        printIndented(lines);
        recordResponse(lines);
    }

    /**
     * Prints a task list, or a message when it is empty.
     *
     * @param taskList the task list to print.
     */
    public void printTaskList(TaskList taskList) {
        if (taskList.isEmpty()) {
            echo("Your task list is currently empty.");
        } else {
            echo("Here are the tasks in your list:", taskList.toString());
        }
    }

    /**
     * Displays the matching tasks, or a message when no tasks match.
     *
     * @param taskList the tasks that match a find command
     */
    public void printMatchingTasks(TaskList taskList) {
        if (taskList.isEmpty()) {
            echo("No matching tasks in your list.");
        } else {
            echo("Here are the matching tasks in your list:", taskList.toString());
        }
    }

    /**
     * Prints confirmation that a task was completed.
     *
     * @param task the completed task.
     */
    public void printMarkTaskDone(Task task) {
        String[] lines = {
            "Nice! I've marked this task as done:",
            String.format("  %s", task)
        };
        echo(lines);
    }

    /**
     * Prints confirmation that a task was marked incomplete.
     *
     * @param task the incomplete task.
     */
    public void printMarkTaskNotDone(Task task) {
        String[] lines = {
            "OK, I've marked this task as not done yet:",
            String.format("  %s", task)
        };
        echo(lines);
    }

    /**
     * Prints confirmation that a task was deleted.
     *
     * @param task the deleted task.
     * @param size the resulting number of tasks.
     */
    public void printDeleteTask(Task task, int size) {
        String[] lines = {
            "Noted. I've removed this task:",
            String.format("  %s", task),
            String.format("Now you have %s tasks in the list.", size)
        };
        echo(lines);
    }

    /**
     * Prints the application greeting surrounded by console dividers.
     *
     * @param greeting greeting shared with the graphical interface
     */
    public void printGreeting(String greeting) {
        echo(DIVIDER, BANNER, greeting, DIVIDER);
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
