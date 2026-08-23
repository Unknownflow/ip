package zen.command;

import zen.ZenException;
import zen.storage.Storage;
import zen.task.TaskList;
import zen.ui.Ui;

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
