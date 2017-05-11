package screen;

import command.CarriageReturnCommand;
import command.CharacterWriteCommand;
import screenbufferinterpreter.NethackScreenBufferInterpreter;
import screenbufferinterpreter.NoLinesScreenTrimmer;
import org.junit.Before;
import org.junit.Test;
import terminal.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NethackScreenTest {
    Display display;
    VTerminal terminal;
    NethackScreen nethackScreen;
    NethackScreenBufferInterpreter screenInterpreter;
    TimePiece timePiece;

    @Before
    public void setUp(){
        display = new BlankDisplay();
        terminal = new Vermont(1, 1, display);
        screenInterpreter = new NethackScreenBufferInterpreter(new NoLinesScreenTrimmer());
        terminal.moveCursor(CursorPosition.HOME);
        timePiece = mock(TimePiece.class);
    }

    @Test
    public void testGettingANethackLevelWhenTheTerminalUpdatedSinceTheLastCheckAndNoUpdatesHaveHappenedRecently() {
        final long safeInterval = 500L;
        final long lastCheckTime = 100000L;
        final long lastUpdatedTime = lastCheckTime + 1L;
        final long now = lastUpdatedTime + safeInterval;

        when(timePiece.getTimeMillis()).thenReturn(lastUpdatedTime).thenReturn(now);
        terminal = new Vermont(1, 1, display, timePiece);
        nethackScreen = new NethackScreen(terminal, screenInterpreter);
        terminal.accept(new CharacterWriteCommand('+'));

        assertThat(nethackScreen.getSuspectedNewAndStableLevel(lastCheckTime, safeInterval), not(nullValue()));
    }

    @Test
    public void testYouDontGetANethackLevelWhenTheTerminalHasNotUpdatedSinceTheLastCheck() {
        final long safeInterval = 500L;
        final long lastCheckTime = 100000L;
        final long lastUpdatedTime = lastCheckTime - 1;
        final long now = lastUpdatedTime + safeInterval;

        when(timePiece.getTimeMillis()).thenReturn(lastUpdatedTime).thenReturn(now);
        terminal = new Vermont(1, 1, display, timePiece);
        nethackScreen = new NethackScreen(terminal, screenInterpreter);

        terminal.accept(new CarriageReturnCommand());

        assertThat(nethackScreen.getSuspectedNewAndStableLevel(lastCheckTime, safeInterval), nullValue());
    }

    @Test
    public void testYouGetANethackLevelWhenTheTerminalUpdatedAtTheSameTimeYouMadeYourLastCheck() {
        final long safeInterval = 500L;
        final long lastCheckTime = 100000L;
        final long lastUpdatedTime = lastCheckTime;
        final long now = lastUpdatedTime + safeInterval;

        when(timePiece.getTimeMillis()).thenReturn(lastUpdatedTime).thenReturn(now);
        terminal = new Vermont(1, 1, display, timePiece);
        nethackScreen = new NethackScreen(terminal, screenInterpreter);

        terminal.accept(new CarriageReturnCommand());

        assertThat(nethackScreen.getSuspectedNewAndStableLevel(lastCheckTime, safeInterval), not(nullValue()));
    }

    @Test
    public void testYouDontGetANethackLevelWhenTheTerminalHasUpdatedSinceTheLastCheckButMightStillBeChanging() {
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

    @Test
    public void testSuccessfullyGettingALevelReturnsALevelWithTheMostRecentChanges(){
        final long safeInterval = 500L;
        final long lastCheckTime = 1000L;
        char[][] firstScreen = new char[][]{
                {'@', '.'}
        };

        char[][] updatedScreen = new char[][]{
                {'.', '@'}
        };
        terminal = mock(VTerminal.class);
        nethackScreen = new NethackScreen(terminal, screenInterpreter);

        when(terminal.getLastUpdateTime()).thenReturn(lastCheckTime + 1L);
        when(terminal.unchangedFor()).thenReturn(safeInterval);
        when(terminal.getScreenBuffer()).thenReturn(firstScreen).thenReturn(updatedScreen);

        assertThat(nethackScreen.getSuspectedNewAndStableLevel(lastCheckTime, safeInterval),
                equalTo(screenInterpreter.interpret(firstScreen)));

        assertThat(nethackScreen.getSuspectedNewAndStableLevel(lastCheckTime, safeInterval),
                equalTo(screenInterpreter.interpret(updatedScreen)));
    }

}
