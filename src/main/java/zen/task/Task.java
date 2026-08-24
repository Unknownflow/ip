package zen.task;

import java.time.LocalDate;

/** Defines the common state and behavior shared by every task type. */
public abstract class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Creates an incomplete task with the given description.
     *
     * @param description task description
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the character used to display this task's completion status.
     *
     * @return {@code X} if the task is done; otherwise a space
     */
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

    /**
     * Returns this task in the format used for persistent storage.
     *
     * @return pipe-delimited storage record
     */
    public abstract String toStorageString();

    /**
     * Returns whether this task occurs on the supplied date.
     *
     * @param date date to check
     * @return true if this task occurs on {@code date}
     */
    public abstract boolean occursOn(LocalDate date);

    /**
     * Returns the task description together with its completion status.
     *
     * @return human-readable task text
     */
    @Override
    public String toString() {
        return String.format("[%s] %s", this.getStatusIcon(), this.description);
    }
}
