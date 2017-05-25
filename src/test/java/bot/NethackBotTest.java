package bot;

import command.NethackCommand;
import level.NethackLevel;
import locations.Coordinates;
import movement.PrioritizeNewLocationsFilter;
import org.junit.Before;
import org.junit.Test;
import screen.NethackScreen;
import screenbufferinterpreter.NethackScreenBufferInterpreter;
import screenbufferinterpreter.NoLinesScreenTrimmer;
import terminal.ScreenBuffer;
import terminal.TimePiece;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

public class NethackBotTest {

    NethackBot nethackBot;

    NethackScreen nethackScreen;
    TimePiece timePiece;
    private NethackScreenBufferInterpreter screenInterpreter;

    @Before
    public void setUp() {
        nethackScreen = mock(NethackScreen.class);
        timePiece = mock(TimePiece.class);
        nethackBot = new NethackBot(timePiece);
        screenInterpreter = new NethackScreenBufferInterpreter(new NoLinesScreenTrimmer());
    }

    @Test
    public void nethackBotShouldAskForANethackLevelUntilItGetsOne() {
        when(nethackScreen.getSuspectedNewAndStableLevel(anyLong(), anyLong()))
                .thenReturn(null).thenReturn(new NethackLevel(new char[1][1]));
        NethackLevel level = nethackBot.getLevelFromScreen(nethackScreen);
        verify(nethackScreen, times(2)).getSuspectedNewAndStableLevel(anyLong(), anyLong());
        assertThat(level, not(nullValue()));
    }

    @Test
    public void nethackBotShouldOnlyAskForNethackLevelsThatAreCreatedAfterTheLastSuccessfulRequest() {
        when(nethackScreen.getSuspectedNewAndStableLevel(anyLong(), anyLong()))
                .thenReturn(null).thenReturn(new NethackLevel(new char[1][1]));
        when(timePiece.getTimeMillis()).thenReturn(1L).thenReturn(2L);

        nethackBot.getLevelFromScreen(nethackScreen);

        verify(nethackScreen, times(2)).getSuspectedNewAndStableLevel(0L, NethackBot.TIME_UNTIL_LEVEL_CONSIDERED_STABLE);
    }

    @Test
    public void givenALevelYouShouldBeAbleToAskNethackBotForTheNextMoveItWantsToMake(){
        ScreenBuffer screen = new ScreenBuffer(new char[][]{{'@', '.'}});
        NethackLevel level = screenInterpreter.interpret(screen);
        NethackCommand nextMove = nethackBot.getNextMove(level);

        assertThat(nextMove, not(nullValue()));
    }

    @Test
    public void setNethackBotCanUseAMovementFilter(){
        ScreenBuffer screenBuffer = new ScreenBuffer(new char[][]{
                {'@', '.'},
                {'.', '.'},
        });
        NethackLevel level = screenInterpreter.interpret(screenBuffer);

        PrioritizeNewLocationsFilter movementFilter = new PrioritizeNewLocationsFilter();
        Coordinates topRight = new Coordinates(0, 1);
        Coordinates bottomLeft = new Coordinates(1, 0);
        Coordinates bottomRight = new Coordinates(1, 1);

        /**
         *  [@, 1]
         *  [1, 0]
         */
        movementFilter.markVisited(topRight);
        movementFilter.markVisited(bottomLeft);

        nethackBot = new NethackBot(timePiece, movementFilter);
        NethackCommand nextMove = nethackBot.getNextMove(level);

        assertThat(nextMove, equalTo(NethackCommand.MOVE_DOWN_RIGHT));

        /**
         *  [@, 1]
         *  [2, 2]
         */
        movementFilter.markVisited(bottomRight);
        movementFilter.markVisited(bottomRight);
        movementFilter.markVisited(bottomLeft);

        assertThat(nethackBot.getNextMove(level), equalTo(NethackCommand.MOVE_RIGHT));
    }

    @Test
    public void ifThereIsNowhereToMoveNethackBotWillWaitATurn(){
        NethackLevel level = screenInterpreter.interpret(new ScreenBuffer(new char[][]{
                {'@', '+'}
        }));

        assertThat(nethackBot.getNextMove(level), equalTo(NethackCommand.WAIT));
    }
}
