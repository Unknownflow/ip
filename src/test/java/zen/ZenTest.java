package zen;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
