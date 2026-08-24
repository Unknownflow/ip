package zen.command;

import zen.ZenException;
import zen.storage.Storage;
import zen.task.Task;
import zen.task.TaskList;
import zen.ui.Ui;

/** Base command for adding a newly created task to the task list. */
public abstract class AddCommand extends Command {
    /**
     * Adds, saves, and displays the supplied task.
     *
     * @param task task to add
     * @param tasks task list to update
     * @param ui console interface used to display the result
     * @param storage persistence service used to save the updated list
     * @throws ZenException if the updated list cannot be saved
     */
    protected void addTask(Task task, TaskList tasks, Ui ui, Storage storage) throws ZenException {
        tasks.addTask(task);
        storage.save(tasks);
        ui.printAddTask(task, tasks.size());
    }
}
