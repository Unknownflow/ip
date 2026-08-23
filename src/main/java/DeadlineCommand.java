/** Adds a deadline task. */
public class DeadlineCommand extends AddCommand {
    private final String arguments;

    public DeadlineCommand(String arguments) {
        this.arguments = arguments;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws ZenException {
        addTask(Parser.parseDeadline(arguments), tasks, ui, storage);
    }
}
