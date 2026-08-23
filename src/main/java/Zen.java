import java.time.LocalDate;
import java.util.Scanner;

public class Zen {

    private Storage storage;
    private TaskList taskList;
    private Parser parser;
    private Ui ui;

    public Zen(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        parser = new Parser();

        try {
            taskList = storage.load();
        } catch (ZenException e) {
            ui.printErrorMessage(e.getMessage());
            taskList = new TaskList();
        }
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        ui.printGreeting();

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                continue;
            }

            String[] parts = input.split("\\s+", 2);
            Command command = Command.fromString(parts[0]);
            String arguments = parts.length == 2 ? parts[1] : "";

            if (command == Command.BYE) {
                ui.printExitMessage();
                break;
            }

            ui.printDivider();

            try {
                // AI-assisted: AI suggested using switch instead of if statements
                switch (command) {
                    case LIST -> {
                        parser.requireNoArguments(arguments, "list");
                        ui.printTaskList(taskList);
                    }
                    case MARK -> {
                        int taskNum = parser.parseTaskNumber(arguments);
                        Task task = taskList.markTask(taskNum);
                        storage.save(taskList);
                        ui.printMarkTaskDone(task);
                    }
                    case UNMARK -> {
                        int taskNum = parser.parseTaskNumber(arguments);
                        Task task = taskList.unmarkTask(taskNum);
                        storage.save(taskList);
                        ui.printMarkTaskNotDone(task);
                    }
                    case OCCUR -> {
                        LocalDate date = parser.parseDate(arguments);
                        TaskList tasks = taskList.getAllTasksOnDate(date);
                        ui.echo("Tasks for " + date);
                        ui.printTaskList(tasks);
                    }
                    case DELETE -> {
                        int taskNum = parser.parseTaskNumber(arguments);
                        Task deletedTask = taskList.deleteTask(taskNum);
                        storage.save(taskList);
                        ui.printDeleteTask(deletedTask, taskList.size());
                    }
                    case TODO -> {
                        // input format: todo <description>
                        if (arguments.isEmpty()) {
                            throw new ZenException("The description of a Todo cannot be empty. Please try again!");
                        }

                        Task newTodo = new Todo(arguments);
                        taskList.addTask(newTodo);
                        storage.save(taskList);
                        ui.printAddTask(newTodo, taskList.size());
                    }
                    case DEADLINE -> {
                        // input format: deadline <description> /by <dueBy>
                        Task newDeadline = parser.parseDeadline(arguments);
                        taskList.addTask(newDeadline);
                        storage.save(taskList);
                        ui.printAddTask(newDeadline, taskList.size());
                    }
                    case EVENT -> {
                        // input format: event <description> /from <start> /to <end>
                        Task newEvent = parser.parseEvent(arguments);
                        taskList.addTask(newEvent);
                        storage.save(taskList);
                        ui.printAddTask(newEvent, taskList.size());
                    }
                    default -> {
                        // if the command is not valid, throw an exception
                        throw new ZenException("Command not found. Please try again!");
                    }
                }
            } catch (ZenException e) {
                ui.echo(e.getMessage());
            }

            ui.printDivider();
        }

        scanner.close();
    }

    public static void main(String[] args) throws ZenException {
        new Zen("data/task_list.txt").run();
    }
}
