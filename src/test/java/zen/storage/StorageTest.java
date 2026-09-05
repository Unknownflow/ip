package zen.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import zen.ZenException;
import zen.task.Deadline;
import zen.task.TaskList;
import zen.task.Todo;
/**
 * Tests persistence of task lists through {@link Storage}.
 */
public class StorageTest {
    @TempDir
    private Path temporaryDirectory;

    // AI-assisted
    @Test
    public void save_newNestedPath_createsFileAndWritesTaskRecords() throws ZenException, IOException {
        Path filePath = temporaryDirectory.resolve("data/tasks.txt");
        TaskList taskList = new TaskList();
        taskList.addTask(new Todo("buy milk"));
        taskList.addTask(new Deadline("submit report", LocalDateTime.of(2026, 10, 10, 10, 30)));

        new Storage(filePath.toString()).save(taskList);

        assertTrue(Files.exists(filePath));
        assertEquals("T | 0 | buy milk\nD | 0 | submit report | 2026-10-10T10:30", Files.readString(filePath));
    }

    // AI-assisted
    @Test
    public void save_existingFile_replacesPreviousContents() throws ZenException, IOException {
        Path filePath = temporaryDirectory.resolve("tasks.txt");
        Storage storage = new Storage(filePath.toString());
        TaskList firstList = new TaskList();
        firstList.addTask(new Todo("old task"));
        TaskList replacementList = new TaskList();
        replacementList.addTask(new Todo("new task"));

        storage.save(firstList);
        storage.save(replacementList);

        assertEquals("T | 0 | new task", Files.readString(filePath));
    }

    // AI-assisted
    @Test
    public void load_missingFile_createsAndReturnsEmptyTaskList() throws ZenException, IOException {
        Path filePath = temporaryDirectory.resolve("data/tasks.txt");

        TaskList loadedTasks = new Storage(filePath.toString()).load();

        assertTrue(Files.exists(filePath));
        assertTrue(loadedTasks.isEmpty());
        assertEquals("", Files.readString(filePath));
    }

    // AI-assisted
    @Test
    public void load_savedTaskTypesAndStatuses_restoresAllTaskRecords() throws ZenException, IOException {
        Path filePath = temporaryDirectory.resolve("tasks.txt");
        String records = String.join("\n",
                "T | 1 | read book",
                "D | 0 | submit report | 2026-10-10T10:30",
                "E | 1 | project meeting | 2026-10-10T09:00 to 2026-10-10T10:00");
        Files.writeString(filePath, records + "\n\n");

        TaskList loadedTasks = new Storage(filePath.toString()).load();

        assertEquals(3, loadedTasks.size());
        assertEquals(records, loadedTasks.toStorageString());
    }

    // AI-assisted
    @Test
    public void load_unknownTaskType_throwsZenException() throws IOException {
        Path filePath = temporaryDirectory.resolve("tasks.txt");
        Files.writeString(filePath, "X | 0 | unsupported task");

        ZenException exception = assertThrows(ZenException.class, () ->
                new Storage(filePath.toString()).load());

        assertEquals("Unable to load tasks. A new task list is created instead.", exception.getMessage());
    }
}
