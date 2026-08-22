import java.util.Scanner;

public class Zen {
    public static void main(String[] args) throws ZenException {
        TaskList taskList = new TaskList();
        Parser parser = new Parser();
        Ui ui = new Ui();
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
                        ui.printMarkTaskDone(task);
                    }
                    case UNMARK -> {
                        int taskNum = parser.parseTaskNumber(arguments);
                        Task task = taskList.unmarkTask(taskNum);
                        ui.printMarkTaskNotDone(task);
                    }
                    case DELETE -> {
                        int taskNum = parser.parseTaskNumber(arguments);
                        Task deletedTask = taskList.deleteTask(taskNum);
                        ui.printDeleteTask(deletedTask, taskList.size());
                    }
                    case TODO -> {
                        // input format: todo <description>
                        if (arguments.isEmpty()) {
                            throw new ZenException("The description of a Todo cannot be empty. Please try again!");
                        }

                        Task newTodo = new Todo(arguments);
                        taskList.addTask(newTodo);
                        ui.printAddTask(newTodo, taskList.size());
                    }
                    case DEADLINE -> {
                        // input format: deadline <description> /by <dueBy>
                        Task newDeadline = parser.parseDeadline(arguments);
                        taskList.addTask(newDeadline);
                        ui.printAddTask(newDeadline, taskList.size());
                    }
                    case EVENT -> {
                        // input format: event <description> /from <start> /to <end>
                        Task newEvent = parser.parseEvent(arguments);
                        taskList.addTask(newEvent);
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
        ui.printExitMessage();
    }
}
