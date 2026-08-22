public class Deadline extends Task {
    protected String dueBy;

    public Deadline(String description, String dueBy) {
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
        return String.format("D | %d | %s | %s", this.isDone ? 1 : 0, this.description, this.dueBy);
    }

    @Override
    public String toString() {
        return String.format("[D]%s (by: %s)", super.toString(), this.dueBy);
    }
}
