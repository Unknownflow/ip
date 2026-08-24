package zen.command;

import zen.ZenException;
import zen.storage.Storage;
import zen.task.TaskList;
import zen.ui.Ui;

/** Adds a deadline task. */
public class DeadlineCommand extends AddCommand {
    private final String arguments;

    /**
     * Creates a command using the supplied deadline details.
     *
     * @param arguments description and due by details to parse
     */
    public DeadlineCommand(String arguments) {
        this.arguments = arguments;
    }

    /** Adds the deadline described by this command. */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws ZenException {
        addTask(Parser.parseDeadline(arguments), tasks, ui, storage);
    }
}
