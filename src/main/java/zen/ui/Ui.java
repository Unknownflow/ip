package zen.ui;

import zen.task.Task;
import zen.task.TaskList;

import java.util.Scanner;

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

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    public String readCommand() {
        return scanner.nextLine().trim();
    }

    private static void printIndented(String... lines) {
        for (String line: lines) {
            // ensure all lines with a linebreak start on a new line with indentation
            for (String individualLine: line.split("\\R")) {
                System.out.println(INDENTATION + individualLine);
            }
        }
    }

    public void printDivider() {
        System.out.println(INDENTED_DIVIDER);
    }

    public void printAddTask(Task task, int size) {
        printIndented(
                "Got it. I've added this task:",
                String.format("  %s", task),
                String.format("Now you have %d tasks in the list.", size)
        );
    }

    public void echo(String echoString) {
        printIndented(echoString);
    }

    public void printTaskList(TaskList taskList) {
        if (taskList.isEmpty()) {
            printIndented("Your task list is currently empty.");
        } else {
            printIndented("Here are the tasks in your list:");
            printIndented(taskList.toString());
        }
    }

    public void printMarkTaskDone(Task task) {
        printIndented(
                "Nice! I've marked this task as done:",
                String.format("  %s", task)
        );
    }

    public void printMarkTaskNotDone(Task task) {
        printIndented(
                "OK, I've marked this task as not done yet:",
                String.format("  %s", task)
        );
    }

    public void printDeleteTask(Task task, int size) {
        printIndented(
                "Noted. I've removed this task:",
                String.format("  %s", task),
                String.format("Now you have %s tasks in the list.", size)
        );
    }

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

    public void printExitMessage() {
        echo("Bye. See you again soon!");
        scanner.close();
    }

    public void printErrorMessage(String errorMessage) {
        String exitMessage = """
                %s
                    Error occurred: %s
                %s
                """.formatted(INDENTED_DIVIDER, errorMessage, INDENTED_DIVIDER);
        System.out.println(exitMessage);
    }
}
