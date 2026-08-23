/** Represents a command keyword that is not supported. */
public class UnknownCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws ZenException {
        throw new ZenException("Command not found. Please try again!");
    }
}
