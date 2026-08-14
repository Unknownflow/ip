import java.util.Scanner;

public class Zen {
    private static final String DIVIDER = "____________________________________________________________";
    private static final String NAME = "Zen";
    private static final String BANNER = " ______              \n" +
                                         "|__  /___  _ __      \n" +
                                         "  / // _ \\| '_ \\   \n" +
                                         " / /|  __/| | | |    \n" +
                                         "/____\\___||_| |_|";

    /**
     * Returns an echo of the input string, wrapped with dividers
     *
     * @param echoString The string received to be echoed back to the user
     */
    private static void echo(String echoString) {
        System.out.println(DIVIDER);
        System.out.println(echoString);
        System.out.println(DIVIDER);
    }

    public static void main(String[] args) {
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
        String greeting = String.format(greetingTemplate, DIVIDER, BANNER, NAME, DIVIDER);
        String exitMessage = String.format(exitTemplate, DIVIDER, DIVIDER);
        Scanner scanner = new Scanner(System.in);

        System.out.println(greeting);

        while (scanner.hasNextLine()) {
            String userInput = scanner.nextLine();
            if (userInput.equals("bye")) {
                break;
            }
            echo(userInput);
        }

        scanner.close();
        System.out.println(exitMessage);
    }
}
