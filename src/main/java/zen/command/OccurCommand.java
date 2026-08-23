package zen.command;

import zen.ZenException;
import zen.storage.Storage;
import zen.task.TaskList;
import zen.ui.Ui;

import java.time.LocalDate;

/** Displays dated tasks that occur on the requested date. */
public class OccurCommand extends Command {
    private final String arguments;

    public OccurCommand(String arguments) {
        this.arguments = arguments;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws ZenException {
        LocalDate date = Parser.parseDate(arguments);
        ui.echo("Tasks for " + date);
        ui.printTaskList(tasks.getAllTasksOnDate(date));
    }
}
