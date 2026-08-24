package zen.task;

import java.time.LocalDate;

/** Represents a task that may be marked complete and saved to storage. */
public abstract class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Creates an incomplete task with the supplied description.
     *
     * @param description the task description.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /** Returns the icon representing this task's completion status. */
    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    /** Marks this task as complete. */
    public void markAsDone() {
        this.isDone = true;
    }

    /** Marks this task as incomplete. */
    public void markAsNotDone() {
        this.isDone = false;
    }

    /** Returns this task in the format used for persistent storage. */
    public abstract String toStorageString();

    /**
     * Returns whether this task occurs on a specified date.
     *
     * @param date the date to check.
     * @return true if this task occurs on {@code date}.
     */
    public abstract boolean occursOn(LocalDate date);

    @Override
    public String toString() {
        return String.format("[%s] %s", this.getStatusIcon(), this.description);
    }
}
