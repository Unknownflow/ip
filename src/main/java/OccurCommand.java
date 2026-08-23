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
