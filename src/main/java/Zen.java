import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Zen {
    private static final String DIVIDER = "____________________________________________________________";
    private static final String NAME = "Zen";
    private static final String INDENTATION = "    ";
    private static final String INDENTED_DIVIDER = INDENTATION + DIVIDER;
    private static final String BANNER = "     ______              \n" +
                                         "    |__  /___  _ __      \n" +
                                         "      / // _ \\| '_ \\   \n" +
                                         "     / /|  __/| | | |    \n" +
                                         "    /____\\___||_| |_|";

    /**
     * Prints an echo of the input string, wrapped with dividers
     *
     * @param echoString The string received to be echoed back to the user
     */
    private static void echo(String echoString) {
        System.out.println(INDENTED_DIVIDER);
        System.out.println(INDENTATION + "added: " + echoString);
        System.out.println(INDENTED_DIVIDER);
    }

    /**
     * Prints all the tasks in the order they are inserted in
     * @param tasks List storing all tasks
     */
    private static void listTasks(List<String> tasks) {
        System.out.println(INDENTED_DIVIDER);

        for (int i = 0; i < tasks.size(); i++) {
            String output = String.format("    %d. %s", i + 1, tasks.get(i));
            System.out.println(output);
        }

        System.out.println(INDENTED_DIVIDER);
    }

    public static void main(String[] args) {
        List<String> tasks = new ArrayList<>();
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
            String userInput = scanner.nextLine();
            if (userInput.equals("bye")) {
                break;
            } else if (userInput.equals("list")) {
                listTasks(tasks);
            } else {
                tasks.add(userInput);
                echo(userInput);
            }
        }

        scanner.close();
        System.out.println(exitMessage);
    }
}
