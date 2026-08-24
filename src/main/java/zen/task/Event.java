package zen.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Represents a task that occurs between a start and end date-time. */
public class Event extends Task {
    protected LocalDateTime start;
    protected LocalDateTime end;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm:ss");

    /**
     * Creates an event with the given description and time range.
     *
     * @param description task description
     * @param start event start date and time
     * @param end event end date and time
     */
    public Event(String description, LocalDateTime start, LocalDateTime end) {
        super(description);
        this.start = start;
        this.end = end;
    }

    /**
     * Returns this event in the file format used for storage.
     *
     * @return a pipe-delimited event record
     */
    @Override
    public String toStorageString() {
        return String.format("E | %d | %s | %s to %s", this.isDone ? 1 : 0,
                this.description, this.start.toString(), this.end.toString());
    }

    /**
     * Returns true if the given date falls within the event's start date
     * and end date (inclusive).
     *
     * @param date the date to compare against this event's start and end date
     * @return true if the given date falls within this event start and end date, false otherwise
     */
    @Override
    public boolean occursOn(LocalDate date) {
        LocalDate startDate = start.toLocalDate();
        LocalDate endDate = end.toLocalDate();

        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    /**
     * Returns a human-readable representation of this event.
     *
     * @return event text including its start and end times
     */
    @Override
    public String toString() {
        return String.format("[E]%s (from: %s to: %s)", super.toString(), this.start.format(FORMATTER),
                this.end.format(FORMATTER));
    }
}
