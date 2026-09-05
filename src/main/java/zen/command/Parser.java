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
    private static final String EVENT_FROM_DELIMITER = "/from";
    private static final String EVENT_TO_DELIMITER = "/to";
    private static final String DEADLINE_FORMAT = "\nDeadline format: deadline <description> /by " + DATE_TIME_FORMAT;
    private static final String EVENT_FORMAT = String.format("\nEvent format: event <description> %s %s %s %s",
            EVENT_FROM_DELIMITER, DATE_TIME_FORMAT, EVENT_TO_DELIMITER, DATE_TIME_FORMAT);
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
            case "find" -> new FindCommand(arguments);
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
            throw new ZenException("The date is not in the correct format.\nCorrect format: "
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
            throw new ZenException("The task number must be a positive integer.");
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
            throw new ZenException("Please include /by in your deadline to separate the description "
                    + "from the due date and time."
                    + DEADLINE_FORMAT);
        }

        String description = userInput.substring(0, byIdx).trim();
        if (description.isEmpty()) {
            throw new ZenException("The deadline description cannot be empty. Please try again."
                    + DEADLINE_FORMAT);
        }

        int nextByIdx = userInput.indexOf("/by", byIdx + "/by".length());
        if (nextByIdx != -1) {
            throw new ZenException("Please include only one /by in your deadline to separate the description "
                    + "from the due date and time."
                    + DEADLINE_FORMAT);
        }

        String dueBy = userInput.substring(byIdx + "/by".length()).trim();
        if (dueBy.isEmpty()) {
            throw new ZenException("The due date and time cannot be empty. Please try again."
                    + DEADLINE_FORMAT);
        }

        try {
            LocalDateTime dateTime = LocalDateTime.parse(dueBy, DATE_TIME_FORMATTER);
            return new Deadline(description, dateTime);
        } catch (DateTimeParseException e) {
            throw new ZenException("The due date and time must follow the required format."
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
        int fromIdx = userInput.indexOf(EVENT_FROM_DELIMITER);
        int toIdx = userInput.indexOf(EVENT_TO_DELIMITER);

        validateEventDelimiters(userInput, fromIdx, toIdx);

        String description = userInput.substring(0, fromIdx).trim();
        String start = userInput.substring(fromIdx + EVENT_FROM_DELIMITER.length(), toIdx).trim();
        String end = userInput.substring(toIdx + EVENT_TO_DELIMITER.length()).trim();

        validateEventParts(description, start, end);
        return createEvent(description, start, end);
    }

    /**
     * Validates that an event command contains one correctly ordered pair of delimiters.
     *
     * @param userInput raw event command arguments
     * @param fromIdx position of the {@code /from} delimiter
     * @param toIdx position of the {@code /to} delimiter
     * @throws ZenException if either delimiter is missing, duplicated, or out of order
     */
    private static void validateEventDelimiters(String userInput, int fromIdx, int toIdx) throws ZenException {
        if (fromIdx == -1) {
            throw new ZenException("Please include " + EVENT_FROM_DELIMITER
                    + " in your event to separate the description from the timings." + EVENT_FORMAT);
        }
        if (toIdx == -1) {
            throw new ZenException("Please include " + EVENT_TO_DELIMITER + " in your event." + EVENT_FORMAT);
        }
        if (fromIdx > toIdx) {
            throw new ZenException(EVENT_FROM_DELIMITER + " must come before " + EVENT_TO_DELIMITER + "."
                    + EVENT_FORMAT);
        }

        if (userInput.indexOf(EVENT_FROM_DELIMITER, fromIdx + EVENT_FROM_DELIMITER.length()) != -1) {
            throw new ZenException("Please include only one " + EVENT_FROM_DELIMITER + " in your event."
                    + EVENT_FORMAT);
        }
        if (userInput.indexOf(EVENT_TO_DELIMITER, toIdx + EVENT_TO_DELIMITER.length()) != -1) {
            throw new ZenException("Please include only one " + EVENT_TO_DELIMITER + " in your event."
                    + EVENT_FORMAT);
        }
    }

    /**
     * Validates the description and timing text extracted from an event command.
     *
     * @param description event description
     * @param start event start date and time text
     * @param end event end date and time text
     * @throws ZenException if a required event value is empty
     */
    private static void validateEventParts(String description, String start, String end) throws ZenException {
        if (description.isEmpty()) {
            throw new ZenException("The event description cannot be empty. Please try again." + EVENT_FORMAT);
        }
        if (start.isEmpty()) {
            throw new ZenException("The event start time cannot be empty. Please try again." + EVENT_FORMAT);
        }
        if (end.isEmpty()) {
            throw new ZenException("The event end time cannot be empty. Please try again." + EVENT_FORMAT);
        }
    }

    /**
     * Parses validated event values and creates the corresponding event.
     *
     * @param description event description
     * @param start event start date and time text
     * @param end event end date and time text
     * @return an event with the supplied description and timing
     * @throws ZenException if a time is malformed or the event ends before it starts
     */
    private static Event createEvent(String description, String start, String end) throws ZenException {
        try {
            LocalDateTime startDateTime = LocalDateTime.parse(start, DATE_TIME_FORMATTER);
            LocalDateTime endDateTime = LocalDateTime.parse(end, DATE_TIME_FORMATTER);

            if (startDateTime.isAfter(endDateTime)) {
                throw new ZenException("The start date and time is after the end date and time." + EVENT_FORMAT);
            }

            assert !startDateTime.isAfter(endDateTime) : "An event's start must not be after its end";

            return new Event(description, startDateTime, endDateTime);
        } catch (DateTimeParseException e) {
            throw new ZenException("The start and end date and time must follow the required format."
                    + EVENT_FORMAT);
        }
    }
}
