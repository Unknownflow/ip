public class Zen {
    private static final String DIVIDER = "____________________________________________________________";
    private static final String NAME = "Zen";
    private static final String BANNER = " ______              \n" +
                                         "|__  /___  _ __      \n" +
                                         "  / // _ \\| '_ \\   \n" +
                                         " / /|  __/| | | |    \n" +
                                         "/____\\___||_| |_|";

    public static void main(String[] args) {
        String template = """
                %s
                %s
                Hello! I'm %s.
                What can I do for you?
                
                %s
                Bye. See you again soon!
                %s""";

        String greeting = String.format(template, DIVIDER, BANNER, NAME, DIVIDER, DIVIDER);
        System.out.println(greeting);
    }
}
