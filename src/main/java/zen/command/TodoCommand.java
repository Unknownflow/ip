package zen.command;

import zen.ZenException;
import zen.storage.Storage;
import zen.task.TaskList;
import zen.task.Todo;
import zen.ui.Ui;

/** Adds a todo task. */
public class TodoCommand extends AddCommand {
    private final String description;

    /**
     * Creates a command using the supplied todo description.
     *
     * @param description description of the todo to add
     */
    public TodoCommand(String description) {
        this.description = description;
    }

    /** Validates the description and adds the requested todo. */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws ZenException {
        if (description.isEmpty()) {
            throw new ZenException("The description of a Todo cannot be empty. Please try again!");
        }
        addTask(new Todo(description), tasks, ui, storage);
    }
}
