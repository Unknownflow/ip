public class Event extends Task {
    protected String start;
    protected String end;

    public Event(String description, String start, String end) {
        super(description);
        this.start = start;
        this.end = end;
    }

    /**
     * Returns this event in the file format used for persistence.
     *
     * @return a pipe-delimited event record
     */
    @Override
    public String toStorageString() {
        return String.format("E | %d | %s | %s to %s", this.isDone ? 1 : 0,
                this.description, this.start, this.end);
    }

    @Override
    public String toString() {
        return String.format("[E]%s (from: %s to: %s)", super.toString(), this.start, this.end);
    }
}
