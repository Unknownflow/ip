package zen.command;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import zen.ZenException;
import zen.task.Deadline;
import zen.task.Event;

/** Converts user input into commands and validates command arguments. */
public class Parser {
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String DATE_TIME_FORMAT = DATE_FORMAT + " HH:mm:ss";
    private static final String DEADLINE_FORMAT = "\nDeadline Format: deadline <description> /by " + DATE_TIME_FORMAT;
    private static final String EVENT_FORMAT = String.format("\nEvent Format: event <description> /from %s /to %s",
            DATE_TIME_FORMAT, DATE_TIME_FORMAT);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);

    /**
     * Converts one line of user input into the command that will handle it.
     *
     * @param fullCommand raw input entered by the user
     * @return the matching command, or an {@link UnknownCommand} for an unsupported keyword
     */
    public static Command parse(String fullCommand) {
        if (fullCommand.isEmpty()) {
            return new UnknownCommand();
        }

        String[] parts = fullCommand.trim().split("\\s+", 2);
        String command = parts[0].toLowerCase();
        String arguments = parts.length == 2 ? parts[1] : "";

        return switch (command) {
            case "list" -> new ListCommand(arguments);
            case "mark" -> new MarkCommand(arguments);
            case "unmark" -> new UnmarkCommand(arguments);
            case "occur" -> new OccurCommand(arguments);
            case "delete" -> new DeleteCommand(arguments);
            case "todo" -> new TodoCommand(arguments);
            case "deadline" -> new DeadlineCommand(arguments);
            case "event" -> new EventCommand(arguments);
            case "bye" -> new ExitCommand();
            default -> new UnknownCommand();
        };
    }

    /**
     * Validates that no arguments were supplied for a command that expects none.
     *
     * @param args    the argument string following the command keyword.
     * @param command the name of the command, used in the error message if validation fails.
     * @throws ZenException if {@code args} is non-empty.
     */
    public static void requireNoArguments(String args, String command) throws ZenException {
        if (!args.isEmpty()) {
            throw new ZenException("The " + command + " command does not take arguments.");
        }
    }

    /**
     * Parses an ISO-8601 date from command arguments.
     *
     * @param arguments the text expected in {@code yyyy-MM-dd} format.
     * @return the parsed date.
     * @throws ZenException if the text is not a valid ISO-8601 date.
     */
    public static LocalDate parseDate(String arguments) throws ZenException {
        try {
            return LocalDate.parse(arguments);
        } catch (DateTimeParseException e) {
            throw new ZenException("The date is in the incorrect format.\n Correct format: "
                    + DATE_FORMAT);
        }
    }

    /**
     * Parses a task number from a user-supplied argument string.
     *
     * @param arguments the raw argument string expected to contain a positive integer.
     * @return the parsed index as an integer.
     * @throws ZenException if {@code arguments} is not a positive integer.
     */
    public static int parseTaskNumber(String arguments) throws ZenException {
        if (!arguments.matches("\\d+")) {
            throw new ZenException("Only positive integers are allowed for task number.");
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
    public static Deadline parseDeadline(String userInput) throws ZenException {
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

        try {
            LocalDateTime dateTime = LocalDateTime.parse(dueBy, DATE_TIME_FORMATTER);
            return new Deadline(description, dateTime);
        } catch (DateTimeParseException e) {
            throw new ZenException("The due by datetime is in the incorrect format."
                    + DEADLINE_FORMAT);
        }
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
    public static Event parseEvent(String userInput) throws ZenException {
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

        try {
            LocalDateTime startDateTime = LocalDateTime.parse(start, DATE_TIME_FORMATTER);
            LocalDateTime endDateTime = LocalDateTime.parse(end, DATE_TIME_FORMATTER);

            if (startDateTime.isAfter(endDateTime)) {
                throw new ZenException("The start datetime is after the end datetime." + EVENT_FORMAT);
            } else {
                return new Event(description, startDateTime, endDateTime);
            }
        } catch (DateTimeParseException e) {
            throw new ZenException("The start / end datetime is in the incorrect format."
                    + EVENT_FORMAT);
        }
    }
}
