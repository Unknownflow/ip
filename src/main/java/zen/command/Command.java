package zen.command;

import zen.ZenException;
import zen.storage.Storage;
import zen.task.TaskList;
import zen.ui.Ui;

/**
 * Represents one user command that can operate on the application's state.
 */
public abstract class Command {
    /** Executes this command using the application's collaborators. */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws ZenException;

    /** Returns whether executing this command ends the application loop. */
    public boolean isExit() {
        return false;
    }
}
