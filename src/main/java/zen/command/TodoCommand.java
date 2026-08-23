package zen.command;

import zen.ZenException;
import zen.storage.Storage;
import zen.task.TaskList;
import zen.task.Todo;
import zen.ui.Ui;

/** Adds a todo task. */
public class TodoCommand extends AddCommand {
    private final String description;

    public TodoCommand(String description) {
        this.description = description;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws ZenException {
        if (description.isEmpty()) {
            throw new ZenException("The description of a Todo cannot be empty. Please try again!");
        }
        addTask(new Todo(description), tasks, ui, storage);
    }
}
