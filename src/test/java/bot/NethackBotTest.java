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

import static locations.Coordinates.coordinates;
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

        /**
         *  [@, 1]
         *  [1, 0]
         */
        movementFilter.markVisited(topRight);
        movementFilter.markVisited(bottomLeft);

        nethackBot = new NethackBot(timePiece, movementFilter);
        NethackCommand nextMove = nethackBot.getNextMove(level);

        assertThat(nextMove, equalTo(NethackCommand.MOVE_DOWN_RIGHT));

    }

    @Test
    public void ifThereIsNowhereToMoveNethackBotWillWaitATurn(){
        NethackLevel levelWithNoOpenSpaces = screenInterpreter.interpret(new ScreenBuffer(new char[][]{
                {'@', '+'}
        }));
        NethackLevel levelWithBlockedPath = screenInterpreter.interpret(new ScreenBuffer(new char[][]{
                {'@', '+', '.'}
        }));
        assertThat(nethackBot.getNextMove(levelWithNoOpenSpaces), equalTo(NethackCommand.WAIT));
        assertThat(nethackBot.getNextMove(levelWithBlockedPath), equalTo(NethackCommand.WAIT));
    }

    @Test
    public void netHackBotShouldRememberWhereItIsGoingTo() {
        NethackLevel level = screenInterpreter.interpret(new ScreenBuffer(new char[][]{
                {'@', '.'}
        }));
        assertThat(nethackBot.getDestination(), equalTo(Coordinates.UNKNOWN));
        nethackBot.getNextMove(level);
        assertThat(nethackBot.getDestination(), equalTo(coordinates(0, 1)));
    }

    @Test
    public void testNethackBotShouldKeepTryingToMoveToItsDestination(){
        NethackLevel levelBeforeFirstMove = screenInterpreter.interpret(new ScreenBuffer(new char[][]{
                {'@', 'B', '.'}
        }));
        nethackBot.getNextMove(levelBeforeFirstMove);
        Coordinates originalDestination = coordinates(0, 2);
        assertThat(nethackBot.getDestination(), equalTo(originalDestination));
        NethackLevel levelBeforeNextMove = screenInterpreter.interpret(new ScreenBuffer(new char[][]{
                {'.', '@', '.'}
        }));
        nethackBot.getNextMove(levelBeforeNextMove);
        assertThat(nethackBot.getDestination(), equalTo(originalDestination));
    }

    @Test
    public void nethackBotShouldPickANewDestinationAfterArrival(){
        NethackLevel levelBeforeFirstMove = screenInterpreter.interpret(new ScreenBuffer(new char[][]{
                {'@', '.'}
        }));
        nethackBot.getNextMove(levelBeforeFirstMove);
        Coordinates destination = coordinates(0, 1);
        assertThat(nethackBot.getDestination(), equalTo(destination));
        NethackLevel levelAfterMove = screenInterpreter.interpret(new ScreenBuffer(new char[][]{
                {'.', '@'}
        }));
        nethackBot.getNextMove(levelAfterMove);
        Coordinates nextDestination = coordinates(0, 0);
        assertThat(nethackBot.getDestination(), equalTo(nextDestination));
    }
}
