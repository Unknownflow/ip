package zen.task;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests task-list filtering behaviour.
 */
public class TaskListTest {

    @Test
    public void getAllTasksBasedOnDescription_matchingKeyword_returnsMatchingTasksInOriginalOrder() {
        TaskList tasks = new TaskList();
        tasks.addTask(new Todo("read book"));
        tasks.addTask(new Deadline("return book", LocalDateTime.of(2026, 6, 6, 18, 0)));
        tasks.addTask(new Event("book club", LocalDateTime.of(2026, 6, 7, 18, 0),
                LocalDateTime.of(2026, 6, 7, 19, 0)));
        tasks.addTask(new Todo("write essay"));

        TaskList matchingTasks = tasks.getAllTasksBasedOnDescription("book");

        assertEquals(3, matchingTasks.size());
        assertEquals("1.[T][ ] read book\n2.[D][ ] return book (by: Jun 06 2026 18:00:00)\n"
                        + "3.[E][ ] book club (from: Jun 07 2026 18:00:00 to: Jun 07 2026 19:00:00)",
                matchingTasks.toString());
    }

    @Test
    public void getAllTasksBasedOnDescription_noMatchOrDifferentCase_returnsEmptyTaskList() {
        TaskList tasks = new TaskList();
        tasks.addTask(new Todo("read Book"));

        TaskList noKeywordMatch = tasks.getAllTasksBasedOnDescription("notes");
        TaskList differentCaseMatch = tasks.getAllTasksBasedOnDescription("book");

        assertTrue(noKeywordMatch.isEmpty());
        assertTrue(differentCaseMatch.isEmpty());
    }
}
