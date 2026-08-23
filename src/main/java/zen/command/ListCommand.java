package zen.command;

import zen.ZenException;
import zen.storage.Storage;
import zen.task.TaskList;
import zen.ui.Ui;

/** Displays all tasks. */
public class ListCommand extends Command {
    private final String arguments;

    public ListCommand(String arguments) {
        this.arguments = arguments;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws ZenException {
        Parser.requireNoArguments(arguments, "list");
        ui.printTaskList(tasks);
    }
}
