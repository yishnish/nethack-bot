package bot;

import interpreter.NethackScreenInterpreter;
import interpreter.NoLinesScreenTrimmer;
import level.NethackLevel;
import locations.Coordinates;
import org.junit.Before;
import org.junit.Test;
import screen.NethackScreen;
import terminal.TimePiece;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

public class NethackBotTest {

    NethackBot nethackBot;

    NethackScreen nethackScreen;
    TimePiece timePiece;
    private NethackScreenInterpreter screenInterpreter;

    @Before
    public void setUp() {
        nethackScreen = mock(NethackScreen.class);
        timePiece = mock(TimePiece.class);
        nethackBot = new NethackBot(timePiece);
        screenInterpreter = new NethackScreenInterpreter(new NoLinesScreenTrimmer());
    }

    @Test
    public void nethackBotShouldAskForANethackLevelUntilItGetsOne() {
        when(nethackScreen.getSuspectedNewAndStableLevel(anyLong(), anyLong()))
                .thenReturn(null).thenReturn(new NethackLevel(new char[1][1]));
        NethackBot nethackBot = new NethackBot(timePiece);
        NethackLevel level = nethackBot.getLevelFromScreen(nethackScreen);
        verify(nethackScreen, times(2)).getSuspectedNewAndStableLevel(anyLong(), anyLong());
        assertThat(level, not(nullValue()));
    }

    @Test
    public void nethackBotShouldOnlyAskForNethackLevelsThatAreCreatedAfterTheLastSuccessfulRequest() {
        when(nethackScreen.getSuspectedNewAndStableLevel(anyLong(), anyLong()))
                .thenReturn(null).thenReturn(new NethackLevel(new char[1][1]));
        when(timePiece.getTimeMillis()).thenReturn(1L).thenReturn(2L);

        NethackBot nethackBot = new NethackBot(timePiece);
        nethackBot.getLevelFromScreen(nethackScreen);

        verify(nethackScreen, times(2)).getSuspectedNewAndStableLevel(0L, NethackBot.TIME_UNTIL_LEVEL_CONSIDERED_STABLE);
    }

    @Test
    public void nethackBotCanChooseLocationsToMoveTo() {
        nethackBot = new NethackBot(timePiece);
        NethackLevel level = screenInterpreter.interpret(new char[][]{{'@', '.'}});
        assertThat(nethackBot.getAvailableMoveLocations(level), hasItem(new Coordinates(0, 1)));
    }
}
