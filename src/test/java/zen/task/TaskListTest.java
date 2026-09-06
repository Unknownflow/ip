package zen.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

/**
 * Tests task-list filtering behaviour.
 */
public class TaskListTest {

    @Test
    public void findTasksByDescription_matchingKeyword_returnsMatchingTasksInPriorityOrder() {
        TaskList tasks = new TaskList();
        tasks.addTask(new Todo("read book"));
        tasks.addTask(new Deadline("return book", LocalDateTime.of(2026, 6, 6, 18, 0), Priority.HIGH));
        tasks.addTask(new Event("book club", LocalDateTime.of(2026, 6, 7, 18, 0),
                LocalDateTime.of(2026, 6, 7, 19, 0), Priority.MEDIUM));
        tasks.addTask(new Todo("write essay"));

        TaskList matchingTasks = tasks.findTasksByDescription("book");

        assertEquals(3, matchingTasks.size());
        assertEquals("1.[D][ ] return book (by: Jun 06 2026 18:00:00) (priority: high)\n"
                        + "2.[E][ ] book club (from: Jun 07 2026 18:00:00 to: Jun 07 2026 19:00:00) "
                        + "(priority: medium)\n3.[T][ ] read book (priority: none)",
                matchingTasks.toString());
    }

    @Test
    public void getAllTasksBasedOnDescription_noMatchOrDifferentCase_returnsEmptyTaskList() {
        TaskList tasks = new TaskList();
        tasks.addTask(new Todo("read Book"));

        TaskList noKeywordMatch = tasks.findTasksByDescription("notes");
        TaskList differentCaseMatch = tasks.findTasksByDescription("book");

        assertTrue(noKeywordMatch.isEmpty());
        assertTrue(differentCaseMatch.isEmpty());
    }

    @Test
    public void addTask_mixedPriorities_keepsPriorityOrderAndStableTies() {
        TaskList tasks = new TaskList();
        tasks.addTask(new Todo("first low", Priority.LOW));
        tasks.addTask(new Todo("first high", Priority.HIGH));
        tasks.addTask(new Todo("second high", Priority.HIGH));
        tasks.addTask(new Todo("no priority"));

        assertEquals("1.[T][ ] first high (priority: high)\n2.[T][ ] second high (priority: high)\n"
                + "3.[T][ ] first low (priority: low)\n4.[T][ ] no priority (priority: none)", tasks.toString());
    }
}
