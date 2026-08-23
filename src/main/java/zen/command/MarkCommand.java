package zen.command;

import zen.ZenException;
import zen.storage.Storage;
import zen.task.Task;
import zen.task.TaskList;
import zen.ui.Ui;

/** Marks a task as done. */
public class MarkCommand extends Command {
    private final String arguments;

    public MarkCommand(String arguments) {
        this.arguments = arguments;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws ZenException {
        Task task = tasks.markTask(Parser.parseTaskNumber(arguments));
        storage.save(tasks);
        ui.printMarkTaskDone(task);
    }
}
