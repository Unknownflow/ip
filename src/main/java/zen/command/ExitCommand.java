package zen.command;

import zen.storage.Storage;
import zen.task.TaskList;
import zen.ui.Ui;

/** Ends the application after displaying the farewell message. */
public class ExitCommand extends Command {
    /** Displays the farewell message. */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.printExitMessage();
    }

    /**
     * Indicates that this command ends the application loop.
     *
     * @return true
     */
    @Override
    public boolean isExit() {
        return true;
    }
}
