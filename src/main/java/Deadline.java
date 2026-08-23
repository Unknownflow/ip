import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task {
    protected LocalDateTime dueBy;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm:ss");

    public Deadline(String description, LocalDateTime dueBy) {
        super(description);
        this.dueBy = dueBy;
    }

    /**
     * Returns this deadline in the file format used for persistence.
     *
     * @return a pipe-delimited deadline record
     */
    @Override
    public String toStorageString() {
        return String.format("D | %d | %s | %s", this.isDone ? 1 : 0, this.description, this.dueBy.toString());
    }

    @Override
    public String toString() {
        return String.format("[D]%s (by: %s)", super.toString(), this.dueBy.format(FORMATTER));
    }
}
