package zen.task;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import zen.ZenException;

public class TaskList {
    private List<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public void addTask(Task task) {
        this.tasks.add(task);
    }

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

    private boolean isValidTaskNumber(int taskNum) {
        return taskNum > 0 && taskNum <= tasks.size();
    }

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

    public int size() {
        return this.tasks.size();
    }

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

            // add a new line to the end of the string except for the last item
            // in the task list
            if (i < this.tasks.size() - 1) {
                output.append("\n");
            }
        }

        return output.toString();
    }
}
