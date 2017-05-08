package screen;

import command.CarriageReturnCommand;
import interpreter.NethackScreenInterpreter;
import interpreter.NoLinesScreenTrimmer;
import level.NethackLevel;
import locations.Coordinates;
import mapitems.DungeonThing;
import org.junit.Before;
import org.junit.Test;
import terminal.*;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NethackScreenTest {
    Display display;
    VTerminal terminal;
    NethackScreen nethackScreen;
    NethackScreenInterpreter screenInterpreter;

    @Before
    public void setUp(){
        display = new BlankDisplay();
        terminal = new Vermont(1, 1, display);
        screenInterpreter = new NethackScreenInterpreter(new NoLinesScreenTrimmer());
        terminal.moveCursor(CursorPosition.HOME);
        nethackScreen = new NethackScreen(terminal, screenInterpreter);
    }

    @Test
    public void testGettingANethackLevelWhenTheTerminalUpdatedSinceTheLastCheckAndNoUpdatesHaveHappenedRecently() {
        TimePiece timePiece = mock(TimePiece.class);

        final long safeInterval = 500L;
        final long lastCheckTime = 100000L;
        final long lastUpdatedTime = lastCheckTime + 1L;
        final long now = lastUpdatedTime + safeInterval;

        when(timePiece.getTimeMillis()).thenReturn(lastUpdatedTime).thenReturn(now);
        terminal = new Vermont(1, 1, display, timePiece);
        nethackScreen = new NethackScreen(terminal, screenInterpreter);
        terminal.accept(new CarriageReturnCommand());

        assertThat(nethackScreen.getSuspectedNewAndStableLevel(lastCheckTime, safeInterval), not(nullValue()));
    }

    @Test
    public void testYouDontGetANethackLevelWhenTheTerminalHasNotUpdatedSinceTheLastCheck() {
        TimePiece timePiece = mock(TimePiece.class);

        final long safeInterval = 500L;
        final long lastCheckTime = 100000L;
        final long lastUpdatedTime = lastCheckTime;
        final long now = lastUpdatedTime + safeInterval;

        when(timePiece.getTimeMillis()).thenReturn(lastUpdatedTime).thenReturn(now);
        terminal = new Vermont(1, 1, display, timePiece);
        nethackScreen = new NethackScreen(terminal, screenInterpreter);

        terminal.accept(new CarriageReturnCommand());

        assertThat(nethackScreen.getSuspectedNewAndStableLevel(lastCheckTime, safeInterval), nullValue());
    }

    @Test
    public void testYouDontGetANethackLevelWhenTheTerminalHasUpdatedSinceTheLastCheckButMightStillBeChanging() {
        TimePiece timePiece = mock(TimePiece.class);

        final long safeInterval = 500L;
        final long lastCheckTime = 100000L;
        final long lastUpdatedTime = lastCheckTime + 1;
        final long now = lastUpdatedTime + safeInterval - 1;

        when(timePiece.getTimeMillis()).thenReturn(lastUpdatedTime).thenReturn(now);
        terminal = new Vermont(1, 1, display, timePiece);
        nethackScreen = new NethackScreen(terminal, screenInterpreter);

        terminal.accept(new CarriageReturnCommand());

        assertThat(nethackScreen.getSuspectedNewAndStableLevel(lastCheckTime, safeInterval), nullValue());
    }

}
