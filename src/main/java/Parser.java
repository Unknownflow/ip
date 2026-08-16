public class Parser {
    private static final String DEADLINE_FORMAT = "\n     Deadline Format: deadline <description> /by <due by>";
    private static final String EVENT_FORMAT = "\n     Event Format: event <description> /from <start> /to <end>";

    /**
     * Validates that no arguments were supplied for a command that expects none.
     *
     * @param args    the argument string following the command keyword.
     * @param command the name of the command, used in the error message if validation fails.
     * @throws ZenException if {@code args} is non-empty.
     */
    public void requireNoArguments(String args, String command) throws ZenException {
        if (!args.isEmpty()) {
            throw new ZenException("The " + command + " command does not take arguments.");
        }
    }

    /**
     * Parses a task index from a user-supplied argument string.
     *
     * @param arguments the raw argument string expected to contain a positive integer.
     * @param action    a description of the action being performed, used in the error message
     *                  if parsing fails (e.g. "delete", "mark", "unmark").
     * @return the parsed index as an integer.
     * @throws ZenException if {@code arguments} is not a positive integer.
     */
    public int parseIndex(String arguments, String action) throws ZenException {
        if (!arguments.matches("\\d+")) {
            throw new ZenException("Only positive integers are allowed for " + action + " task index.");
        }
        return Integer.parseInt(arguments);
    }

    /**
     * Parses a {@link Deadline} task from raw user input of the form
     * {@code <description> /by <due by>}.
     *
     * @param userInput the full user input string for the deadline command,
     *                  excluding the leading "deadline" keyword.
     * @return a new {@code Deadline} constructed from the parsed description and due date.
     * @throws ZenException if {@code /by} is missing, appears more than once,
     *                       or if the description or due-by value is empty.
     */
    public Deadline parseDeadline(String userInput) throws ZenException {
        int byIdx = userInput.indexOf("/by");

        if (byIdx == -1) {
            throw new ZenException("Please include /by in your event to separate description and due by."
                    + DEADLINE_FORMAT);
        }

        String description = userInput.substring(0, byIdx).trim();
        if (description.isEmpty()) {
            throw new ZenException("The description of a Deadline cannot be empty. Please try again!"
                    + DEADLINE_FORMAT);
        }

        int nextByIdx = userInput.indexOf("/by", byIdx + "/by".length());
        if (nextByIdx != -1) {
            throw new ZenException("Please include only one /by in your event to separate description and due by."
                    + DEADLINE_FORMAT);
        }

        String dueBy = userInput.substring(byIdx + "/by".length()).trim();
        if (dueBy.isEmpty()) {
            throw new ZenException("The due by date / time cannot be empty. Please try again!"
                    + DEADLINE_FORMAT);
        }

        return new Deadline(description, dueBy);
    }

    /**
     * Parses an {@link Event} task from raw user input of the form
     * {@code <description> /from <start> /to <end>}.
     *
     * @param userInput the full user input string for the event command,
     *                  excluding the leading "event" keyword.
     * @return a new {@code Event} constructed from the parsed description, start, and end times.
     * @throws ZenException if {@code /from} or {@code /to} is missing, either appears more than
     *                       once, {@code /from} does not precede {@code /to}, or if the
     *                       description, start, or end value is empty.
     */
    public Event parseEvent(String userInput) throws ZenException {
        int fromIdx = userInput.indexOf("/from");
        int toIdx = userInput.indexOf("/to");

        if (fromIdx == -1) {
            throw new ZenException("Please include /from in your event to separate description and timings."
                    + EVENT_FORMAT);
        }
        if (toIdx == -1) {
            throw new ZenException("Please include /to in your event." + EVENT_FORMAT);
        }
        if (fromIdx > toIdx) {
            throw new ZenException("/from must come before /to." + EVENT_FORMAT);
        }

        if (userInput.indexOf("/from", fromIdx + "/from".length()) != -1) {
            throw new ZenException("Please include only 1 /from in your event." + EVENT_FORMAT);
        }
        if (userInput.indexOf("/to", toIdx + "/to".length()) != -1) {
            throw new ZenException("Please include only 1 /to in your event." + EVENT_FORMAT);
        }

        String description = userInput.substring(0, fromIdx).trim();
        String start = userInput.substring(fromIdx + "/from".length(), toIdx).trim();
        String end = userInput.substring(toIdx + "/to".length()).trim();

        if (description.isEmpty()) {
            throw new ZenException("The description of an Event cannot be empty. Please try again." + EVENT_FORMAT);
        }
        if (start.isEmpty()) {
            throw new ZenException("The start time of an Event cannot be empty. Please try again!" + EVENT_FORMAT);
        }
        if (end.isEmpty()) {
            throw new ZenException("The end time of an Event cannot be empty. Please try again!" + EVENT_FORMAT);
        }

        return new Event(description, start, end);
    }
}
