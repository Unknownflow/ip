/** Marks a task as not done. */
public class UnmarkCommand extends Command {
    private final String arguments;

    public UnmarkCommand(String arguments) {
        this.arguments = arguments;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws ZenException {
        Task task = tasks.unmarkTask(Parser.parseTaskNumber(arguments));
        storage.save(tasks);
        ui.printMarkTaskNotDone(task);
    }
}
