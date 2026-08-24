package zen.task;

import zen.ZenException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Manages the list of tasks in the application. */
public class TaskList {
    private List<Task> tasks;

    /** Creates an empty task list. */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Appends a task to this list.
     *
     * @param task task to add
     */
    public void addTask(Task task) {
        this.tasks.add(task);
    }

    /**
     * Removes and returns the task at a one-based task number.
     *
     * @param taskNum one-based number of the task to remove
     * @return the removed task
     * @throws ZenException if the list is empty or the number is invalid
     */
    public Task deleteTask(int taskNum) throws ZenException {
        if (!isValidTaskNumber(taskNum)) {
            if (this.isEmpty()) {
                throw new ZenException("Task list is empty. Add new tasks into task list.");
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
     * Marks the task at a one-based task number as complete.
     *
     * @param taskNum one-based number of the task to mark
     * @return the marked task
     * @throws ZenException if the list is empty or the number is invalid
     */
    public Task markTask(int taskNum) throws ZenException {
        if (!isValidTaskNumber(taskNum)) {
            if (this.isEmpty()) {
                throw new ZenException("Task list is empty. Add new tasks into task list.");
            } else {
                throw new ZenException("Maximum task number is " + this.size() + ".");
            }
        }
        Task task = this.tasks.get(taskNum - 1);
        task.markAsDone();
        return task;
    }

    /**
     * Marks the task at a one-based task number as incomplete.
     *
     * @param taskNum one-based number of the task to unmark
     * @return the unmarked task
     * @throws ZenException if the list is empty or the number is invalid
     */
    public Task unmarkTask(int taskNum) throws ZenException {
        if (!isValidTaskNumber(taskNum)) {
            if (this.isEmpty()) {
                throw new ZenException("Task list is empty. Add new tasks into task list.");
            } else {
                throw new ZenException("Maximum task number is " + this.size() + ".");
            }
        }
        Task task = this.tasks.get(taskNum - 1);
        task.markAsNotDone();
        return task;
    }

    /**
     * Returns the number of tasks in this list.
     *
     * @return task count
     */
    public int size() {
        return this.tasks.size();
    }


    /**
     * Returns whether this list contains no tasks.
     *
     * @return true if the list is empty
     */
    public boolean isEmpty() {
        return this.tasks.isEmpty();
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
     * Returns all tasks as newline-separated records for storage.
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

    /**
     * Returns all tasks as a numbered, newline-separated list.
     *
     * @return formatted task list
     */
    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < this.tasks.size(); i++) {
            output.append(String.format("%d.%s", i + 1, tasks.get(i)));

            // add a new line to the end of the string except for the last item
            // in the task list
            if (i < this.tasks.size() - 1) {
                output.append("\n");
            }
        }

        return output.toString();
    }
}
