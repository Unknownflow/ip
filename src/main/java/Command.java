public enum Command {
    LIST, MARK, UNMARK, OCCUR, DELETE, TODO, DEADLINE, EVENT, BYE, UNKNOWN;

    public static Command fromString(String input) {
        try {
            return Command.valueOf(input.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}
