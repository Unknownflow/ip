package zen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Tests user-facing behavior provided by Zen. */
public class ZenTest {
    @TempDir
    private Path temporaryDirectory;

    @Test
    public void getGreeting_returnsCalmTaskCompanionGreeting() {
        Zen zen = new Zen(temporaryDirectory.resolve("tasks.txt").toString());

        assertEquals("Hello! I'm Zen, your calm task companion.\nWhat can I do for you?", zen.getGreeting());
    }

    @Test
    public void getResponse_missingFindKeywordOrByeArguments_returnsValidationError() {
        Zen zen = new Zen(temporaryDirectory.resolve("tasks.txt").toString());

        assertEquals("The find command requires a keyword.", zen.getResponse("find"));
        assertEquals("The bye command does not take arguments.", zen.getResponse("bye later"));
        assertFalse(zen.hasExited());
    }

    @Test
    public void getResponse_bye_returnsFarewellAndEndsSession() {
        Zen zen = new Zen(temporaryDirectory.resolve("tasks.txt").toString());

        assertEquals("Bye. See you again soon!", zen.getResponse("bye"));
        assertTrue(zen.hasExited());
    }

    @Test
    public void getResponse_invalidStorage_endsSessionAndDoesNotOverwriteData() throws IOException {
        Path filePath = temporaryDirectory.resolve("tasks.txt");
        String invalidRecord = "T | 0 | damaged | value | none";
        Files.writeString(filePath, invalidRecord);
        Zen zen = new Zen(filePath.toString());

        assertTrue(zen.hasExited());
        assertEquals("Unable to load tasks because line 1 is invalid. The file was not changed.",
                zen.getInitializationError());
        assertEquals("", zen.getResponse("todo replacement"));
        assertEquals(invalidRecord, Files.readString(filePath));
    }
}
