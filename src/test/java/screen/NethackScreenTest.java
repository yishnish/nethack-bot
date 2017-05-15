package screen;

import command.CarriageReturnCommand;
import command.CharacterWriteCommand;
import locations.Coordinates;
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
    TimePiece terminalTimePiece;
    TimePiece screenTimePiece;

    @Before
    public void setUp(){
        display = new BlankDisplay();
        terminal = new Vermont(1, 1, display);
        screenInterpreter = new NethackScreenBufferInterpreter(new NoLinesScreenTrimmer());
        terminal.moveCursor(Coordinates.HOME);
        terminalTimePiece = mock(TimePiece.class);
        screenTimePiece = mock(TimePiece.class);
    }

    @Test
    public void testGettingANethackLevelWhenTheTerminalUpdatedSinceTheLastCheckAndNoUpdatesHaveHappenedRecently() {
        final long safeInterval = 500L;
        final long lastCheckTime = 100000L;
        final long lastUpdatedTime = lastCheckTime + 1L;
        final long now = lastUpdatedTime + safeInterval;

        when(terminalTimePiece.getTimeMillis()).thenReturn(lastUpdatedTime).thenReturn(now);
        when(screenTimePiece.getTimeMillis()).thenReturn(now);
        terminal = new Vermont(1, 1, display, terminalTimePiece);
        nethackScreen = new NethackScreen(terminal, screenInterpreter, screenTimePiece);
        terminal.accept(new CharacterWriteCommand('+'));

        assertThat(nethackScreen.getSuspectedNewAndStableLevel(lastCheckTime, safeInterval), not(nullValue()));
    }

    @Test
    public void testYouDontGetANethackLevelWhenTheTerminalHasNotUpdatedSinceTheLastCheck() {
        final long safeInterval = 500L;
        final long lastCheckTime = 100000L;
        final long lastUpdatedTime = lastCheckTime - 1;
        final long now = lastUpdatedTime + safeInterval;

        when(terminalTimePiece.getTimeMillis()).thenReturn(lastUpdatedTime).thenReturn(now);
        terminal = new Vermont(1, 1, display, terminalTimePiece);
        nethackScreen = new NethackScreen(terminal, screenInterpreter, screenTimePiece);

        terminal.accept(new CarriageReturnCommand());

        assertThat(nethackScreen.getSuspectedNewAndStableLevel(lastCheckTime, safeInterval), nullValue());
    }

    @Test
    public void testYouGetANethackLevelWhenTheTerminalUpdatedAtTheSameTimeYouMadeYourLastCheck() {
        final long safeInterval = 500L;
        final long lastCheckTime = 100000L;
        final long lastUpdatedTime = lastCheckTime;
        final long now = lastUpdatedTime + safeInterval;

        when(terminalTimePiece.getTimeMillis()).thenReturn(lastUpdatedTime).thenReturn(now);
        when(screenTimePiece.getTimeMillis()).thenReturn(lastCheckTime + safeInterval);
        terminal = new Vermont(1, 1, display, terminalTimePiece);
        nethackScreen = new NethackScreen(terminal, screenInterpreter, screenTimePiece);

        terminal.accept(new CarriageReturnCommand());

        assertThat(nethackScreen.getSuspectedNewAndStableLevel(lastCheckTime, safeInterval), not(nullValue()));
    }

    @Test
    public void testYouDontGetANethackLevelWhenTheTerminalHasUpdatedSinceTheLastCheckButMightStillBeChanging() {
        final long safeInterval = 500L;
        final long lastCheckTime = 100000L;
        final long lastUpdatedTime = lastCheckTime + 1;
        final long now = lastUpdatedTime + safeInterval - 1;

        when(terminalTimePiece.getTimeMillis()).thenReturn(lastUpdatedTime).thenReturn(now);
        terminal = new Vermont(1, 1, display, terminalTimePiece);
        nethackScreen = new NethackScreen(terminal, screenInterpreter, screenTimePiece);

        terminal.accept(new CarriageReturnCommand());

        assertThat(nethackScreen.getSuspectedNewAndStableLevel(lastCheckTime, safeInterval), nullValue());
    }

    @Test
    public void testSuccessfullyGettingALevelReturnsALevelWithTheMostRecentChanges(){
        final long safeInterval = 500L;
        final long firstUpdateTime = 1000L;
        final long secondUpdateTime = 2000L;

        ScreenBuffer firstScreen = new ScreenBuffer(new char[][]{
                {'@', '.'}
        }, new BufferMetadata(firstUpdateTime));

        ScreenBuffer updatedScreen = new ScreenBuffer(new char[][]{
                {'.', '@'}
        }, new BufferMetadata(secondUpdateTime));

        terminal = mock(VTerminal.class);
        nethackScreen = new NethackScreen(terminal, screenInterpreter, screenTimePiece);
        when(terminal.getScreenBuffer()).thenReturn(firstScreen).thenReturn(firstScreen).thenReturn(updatedScreen);

        when(screenTimePiece.getTimeMillis()).thenReturn(1500L).thenReturn(1501L).thenReturn(2500L);

        assertThat(nethackScreen.getSuspectedNewAndStableLevel(firstUpdateTime, safeInterval),
                equalTo(screenInterpreter.interpret(firstScreen)));

        assertThat(nethackScreen.getSuspectedNewAndStableLevel(firstUpdateTime + 1, safeInterval),
                equalTo(null));

        assertThat(nethackScreen.getSuspectedNewAndStableLevel(secondUpdateTime, safeInterval),
                equalTo(screenInterpreter.interpret(updatedScreen)));
    }

}
