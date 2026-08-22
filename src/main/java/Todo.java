public class Todo extends Task {
    public Todo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return String.format("[T]%s", super.toString());
    }

    /**
     * Returns this todo in the file format used for persistence.
     *
     * @return a pipe-delimited todo record
     */
    @Override
    public String toStorageString() {
        return String.format("T | %d | %s", this.isDone ? 1 : 0, this.description);
    }
}
