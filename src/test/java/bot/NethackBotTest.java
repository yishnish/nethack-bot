package bot;

import command.NethackCommand;
import movement.PrioritizeNewLocationsFilter;
import movement.SingleSpaceCorporealMovementStrategy;
import screenbufferinterpreter.NethackScreenBufferInterpreter;
import screenbufferinterpreter.NoLinesScreenTrimmer;
import level.NethackLevel;
import locations.Coordinates;
import org.junit.Before;
import org.junit.Test;
import screen.NethackScreen;
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
    public void nethackBotCanChooseLocationsToMoveTo() {
        NethackLevel level = screenInterpreter.interpret(new char[][]{{'@', '.'}});
        assertThat(nethackBot.getAvailableMoveLocations(level), hasItem(new Coordinates(0, 1)));
    }

    @Test
    public void givenALevelYouShouldBeAbleToAskNethackBotForTheNextMoveItWantsToMake(){
        NethackLevel level = screenInterpreter.interpret(new char[][]{{'@', '.'}});
        NethackCommand nextMove = nethackBot.getNextMove(level);

        assertThat(nextMove, not(nullValue()));
    }

    @Test
    public void nethackBotShouldPreferMovingToLocationsItHasVisitedLeast(){
        NethackLevel level = screenInterpreter.interpret(new char[][]{
                {'@', '.'},
                {'.', '.'},
        });

        PrioritizeNewLocationsFilter movementFilter = new PrioritizeNewLocationsFilter();
        movementFilter.markVisited(new Coordinates(0, 1));
        movementFilter.markVisited(new Coordinates(1, 0));
        nethackBot = new NethackBot(timePiece, new SingleSpaceCorporealMovementStrategy(), movementFilter);
        NethackCommand nextMove = nethackBot.getNextMove(level);

        assertThat(nextMove, equalTo(NethackCommand.MOVE_DOWN_RIGHT));
    }

    @Test
    public void ifThereIsNowhereToMoveNethackBotWillWaitATurn(){
        NethackLevel level = screenInterpreter.interpret(new char[][]{
                {'@', '+'}
        });

        assertThat(nethackBot.getNextMove(level), equalTo(NethackCommand.WAIT));
    }
}
