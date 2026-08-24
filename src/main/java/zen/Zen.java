package zen;

import zen.command.Command;
import zen.command.Parser;
import zen.storage.Storage;
import zen.task.TaskList;
import zen.ui.Ui;

/** Runs the Zen command-line task manager. */
public class Zen {

    private Storage storage;
    private TaskList taskList;
    private Ui ui;

    /**
     * Creates the application and loads its saved tasks.
     *
     * @param filePath the storage location for tasks.
     */
    public Zen(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);

        try {
            taskList = storage.load();
        } catch (ZenException e) {
            ui.printErrorMessage(e.getMessage());
            taskList = new TaskList();
        }
    }

    /** Runs the command-processing loop until the user exits. */
    public void run() {
        ui.printGreeting();
        boolean isExit = false;

        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                ui.printDivider();
                Command command = Parser.parse(fullCommand);
                command.execute(taskList, ui, storage);
                isExit = command.isExit();
            } catch (ZenException e) {
                ui.echo(e.getMessage());
            } finally {
                ui.printDivider();
            }
        }
    }

    /**
     * Starts the application using its default task storage file.
     *
     * @param args command-line arguments, which are not used.
     * @throws ZenException if the application cannot initialize its task storage.
     */
    public static void main(String[] args) throws ZenException {
        new Zen("data/task_list.txt").run();
    }
}
