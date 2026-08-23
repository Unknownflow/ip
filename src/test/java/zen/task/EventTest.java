package zen.task;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class EventTest {
    private final LocalDateTime START_DATE_TIME = LocalDateTime.of(2026, 10, 10, 10, 30, 0);
    private final LocalDateTime END_DATE_TIME = LocalDateTime.of(2026, 10, 15, 10, 30, 0);
    private final LocalDate START_DATE = START_DATE_TIME.toLocalDate();
    private final LocalDate END_DATE = END_DATE_TIME.toLocalDate();
    private final String DESCRIPTION = "party";

    @Test
    public void toStorageString_isNotDone_formatsCorrectly() {
        Event Event = new Event(DESCRIPTION, START_DATE_TIME, END_DATE_TIME);
        String expectedOutput = String.format("E | %d | %s | %s to %s", 0, DESCRIPTION, START_DATE_TIME, END_DATE_TIME);
        assertEquals(expectedOutput, Event.toStorageString());
    }

    @Test
    public void toStorageString_isDone_formatsCorrectly() {
        Event Event = new Event(DESCRIPTION, START_DATE_TIME, END_DATE_TIME);
        Event.markAsDone();
        String expectedOutput = String.format("E | %d | %s | %s to %s", 1, DESCRIPTION, START_DATE_TIME, END_DATE_TIME);
        assertEquals(expectedOutput, Event.toStorageString());
    }

    @Test
    public void occursOn_startDate_returnsTrue() {
        Event Event = new Event(DESCRIPTION, START_DATE_TIME, END_DATE_TIME);
        assertTrue(Event.occursOn(START_DATE));
    }

    @Test
    public void occursOn_middleDate_returnsTrue() {
        Event Event = new Event(DESCRIPTION, START_DATE_TIME, END_DATE_TIME);
        assertTrue(Event.occursOn(START_DATE.plusDays(2)));
    }

    @Test
    public void occursOn_endDate_returnsTrue() {
        Event Event = new Event(DESCRIPTION, START_DATE_TIME, END_DATE_TIME);
        assertTrue(Event.occursOn(END_DATE));
    }

    @Test
    public void occursOn_dateBeforeStartDate_returnsFalse() {
        Event Event = new Event(DESCRIPTION, START_DATE_TIME, END_DATE_TIME);
        assertFalse(Event.occursOn(START_DATE.minusDays(1)));
    }

    @Test
    public void occursOn_dateAfterEndDate_returnsFalse() {
        Event Event = new Event(DESCRIPTION, START_DATE_TIME, END_DATE_TIME);
        assertFalse(Event.occursOn(END_DATE.plusDays(1)));
    }
}
