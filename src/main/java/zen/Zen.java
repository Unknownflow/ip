package zen;

import zen.command.Command;
import zen.command.Parser;
import zen.storage.Storage;
import zen.task.TaskList;
import zen.ui.Ui;

/** Start point of the Zen chatbot. */
public class Zen {

    private Storage storage;
    private TaskList taskList;
    private Ui ui;

    /**
     * Creates the chatbot and loads any tasks stored at the given path.
     *
     * @param filePath location of the task list file
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

    /** Runs the command loop until the user enters the exit command. */
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

    public static void main(String[] args) {
        new Zen("data/task_list.txt").run();
    }
}
