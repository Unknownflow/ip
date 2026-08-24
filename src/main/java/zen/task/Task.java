package zen.task;

import java.time.LocalDate;

public abstract class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    public void markAsDone() {
        this.isDone = true;
    }

    public void markAsNotDone() {
        this.isDone = false;
    }

    /**
     * Returns whether this task's description contains the specified keyword.
     *
     * @param keyword the case-sensitive keyword to search for
     * @return true if the description contains the keyword, false otherwise
     */
    public boolean descriptionContains(String keyword) {
        return description.contains(keyword);
    }

    public abstract String toStorageString();

    public abstract boolean occursOn(LocalDate date);

    @Override
    public String toString() {
        return String.format("[%s] %s", this.getStatusIcon(), this.description);
    }
}
