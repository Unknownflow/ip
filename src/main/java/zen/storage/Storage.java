package zen.storage;

import zen.ZenException;
import zen.task.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Saves and loads the task list at the specified file path.
 */
public class Storage {
    private final Path filePath;
    private final Path storageDirectory;

    public Storage(String filePath) {
        this.filePath = Path.of(filePath);
        this.storageDirectory = this.filePath.getParent();
    }

    /**
     * Saves the supplied task list in a pipe-delimited storage format.
     *
     * @param taskList task list whose current state should be written
     * @throws ZenException if the storage directory or file cannot be written
     */
    public void save(TaskList taskList) throws ZenException {
        try {
            Files.createDirectories(storageDirectory);
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }

            Files.writeString(filePath, taskList.toStorageString());
        } catch (IOException exception) {
            throw new ZenException("Unable to save tasks to " + filePath);
        }
    }

    /**
     * Loads tasks from the pipe-delimited storage file.
     *
     * @return a task list containing every task stored in the file
     * @throws ZenException if the storage directory or file is missing, or cannot be read
     */
    public TaskList load() throws ZenException {
        if (Files.exists(filePath)) {
            try {
                TaskList taskList = new TaskList();
                List<String> taskRecords = Files.readAllLines(filePath);

                for (String taskRecord : taskRecords) {
                    if (!taskRecord.isBlank()) {
                        taskList.addTask(parseTask(taskRecord));
                    }
                }

                return taskList;
            } catch (IOException exception) {
                throw new ZenException("Unable to load tasks from " + filePath + ".");
            }
        } else {
            TaskList taskList = new TaskList();
            save(taskList);
            return taskList;
        }
    }

    /**
     * Recreates one task from its pipe-delimited storage record.
     *
     * @param taskRecord one task record from the storage file
     * @return the reconstructed task, including its completion status
     */
    private Task parseTask(String taskRecord) {
        String[] fields = taskRecord.split("\\s*\\|\\s*");
        Task task = switch (fields[0]) {
            case "T" -> new Todo(fields[2]);
            case "D" -> new Deadline(fields[2], LocalDateTime.parse(fields[3]));
            case "E" -> createEvent(fields[2], fields[3]);
            default -> throw new IllegalArgumentException("Unknown task type: " + fields[0]);
        };

        if (fields[1].equals("1")) {
            task.markAsDone();
        }
        return task;
    }

    /**
     * Separates the event timing field saved by {@link Event#toStorageString()}.
     *
     * @param description event description
     * @param timing saved start and end timing text
     * @return the reconstructed event
     */
    private Event createEvent(String description, String timing) {
        String[] times = timing.split(" to ", 2);
        return new Event(description, LocalDateTime.parse(times[0]), LocalDateTime.parse(times[1]));
    }
}
