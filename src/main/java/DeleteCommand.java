/** Deletes a task. */
public class DeleteCommand extends Command {
    private final String arguments;

    public DeleteCommand(String arguments) {
        this.arguments = arguments;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws ZenException {
        Task deletedTask = tasks.deleteTask(Parser.parseTaskNumber(arguments));
        storage.save(tasks);
        ui.printDeleteTask(deletedTask, tasks.size());
    }
}
