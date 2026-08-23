/** Adds an event task. */
public class EventCommand extends AddCommand {
    private final String arguments;

    public EventCommand(String arguments) {
        this.arguments = arguments;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws ZenException {
        addTask(Parser.parseEvent(arguments), tasks, ui, storage);
    }
}
