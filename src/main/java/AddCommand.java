/** Base command for adding a newly created task to the task list. */
public abstract class AddCommand extends Command {
    /** Adds, saves, and displays the supplied task. */
    protected void addTask(Task task, TaskList tasks, Ui ui, Storage storage) throws ZenException {
        tasks.addTask(task);
        storage.save(tasks);
        ui.printAddTask(task, tasks.size());
    }
}
