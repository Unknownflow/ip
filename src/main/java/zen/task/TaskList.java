package zen.task;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import zen.ZenException;

/** Stores and manages the application's ordered tasks. */
public class TaskList {
    private final List<Task> tasks;

    /** Creates an empty task list. */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Adds a task to the end of this list.
     *
     * @param task the task to add.
     */
    public void addTask(Task task) {
        this.tasks.add(task);
    }

    /**
     * Deletes a task identified by its one-based task number.
     *
     * @param taskNum the one-based task number.
     * @return the deleted task.
     * @throws ZenException if no task has the supplied number.
     */
    public Task deleteTask(int taskNum) throws ZenException {
        if (!isValidTaskNumber(taskNum)) {
            if (this.isEmpty()) {
                throw new ZenException("The task list is empty. Add a task first.");
            } else {
                throw new ZenException("Maximum task number is " + this.size() + ".");
            }
        }
        return this.tasks.remove(taskNum - 1);
    }

    /**
     * Checks whether a one-based task number identifies an existing task.
     *
     * @param taskNum task number to check
     * @return true if the number is within this list's bounds
     */
    private boolean isValidTaskNumber(int taskNum) {
        return taskNum > 0 && taskNum <= tasks.size();
    }

    /**
     * Marks a task identified by its one-based task number as complete.
     *
     * @param taskNum the one-based task number.
     * @return the completed task.
     * @throws ZenException if no task has the supplied number.
     */
    public Task markTask(int taskNum) throws ZenException {
        if (!isValidTaskNumber(taskNum)) {
            if (this.isEmpty()) {
                throw new ZenException("The task list is empty. Add a task first.");
            } else {
                throw new ZenException("Maximum task number is " + this.size() + ".");
            }
        }
        Task task = this.tasks.get(taskNum - 1);
        task.markAsDone();
        return task;
    }

    /**
     * Marks a task identified by its one-based task number as incomplete.
     *
     * @param taskNum the one-based task number.
     * @return the incomplete task.
     * @throws ZenException if no task has the supplied number.
     */
    public Task unmarkTask(int taskNum) throws ZenException {
        if (!isValidTaskNumber(taskNum)) {
            if (this.isEmpty()) {
                throw new ZenException("The task list is empty. Add a task first.");
            } else {
                throw new ZenException("Maximum task number is " + this.size() + ".");
            }
        }
        Task task = this.tasks.get(taskNum - 1);
        task.markAsNotDone();
        return task;
    }

    /** Returns the number of tasks in this list. */
    public int size() {
        return this.tasks.size();
    }

    /** Returns whether this list has no tasks. */
    public boolean isEmpty() {
        return this.tasks.isEmpty();
    }

    /**
     * Returns a new task list containing only the tasks that have the keyword
     * present in the description, preserving their original order.
     *
     * @param keyword the case-sensitive description keyword to filter tasks by
     * @return a task list containing only tasks whose descriptions contain the keyword
     */
    public TaskList getAllTasksBasedOnDescription(String keyword) {
        TaskList matchingTasks = new TaskList();

        for (Task task : tasks) {
            if (task.descriptionContains(keyword)) {
                matchingTasks.addTask(task);
            }
        }

        return matchingTasks;
    }

    /**
     * Returns a new task list containing only the tasks that occur on the
     * given date, preserving their original order.
     *
     * @param date the date to filter tasks by
     * @return a task list containing only tasks that occur on the specified date
     */
    public TaskList getAllTasksOnDate(LocalDate date) {
        TaskList newTaskList = new TaskList();

        for (Task task : tasks) {
            if (task.occursOn(date)) {
                newTaskList.addTask(task);
            }
        }

        return newTaskList;
    }

    /**
     * Returns all tasks as newline-separated records for persistent storage.
     *
     * @return pipe-delimited task records, one per line
     */
    public String toStorageString() {
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < this.tasks.size(); i++) {
            output.append(this.tasks.get(i).toStorageString());
            if (i < this.tasks.size() - 1) {
                output.append("\n");
            }
        }

        return output.toString();
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < this.tasks.size(); i++) {
            output.append(String.format("%d.%s", i + 1, tasks.get(i)));

            // Add a new line after each item except the last.
            if (i < this.tasks.size() - 1) {
                output.append("\n");
            }
        }

        return output.toString();
    }
}
