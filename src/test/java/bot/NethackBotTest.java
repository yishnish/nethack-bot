package bot;

import interpreter.NethackScreenInterpreter;
import interpreter.NoLinesScreenTrimmer;
import level.NethackLevel;
import locations.Coordinates;
import mapitems.DungeonThing;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import screen.NethackScreen;
import terminal.TimePiece;

import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
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
        nethackBot.getLevelFromScreen(nethackScreen);
        verify(nethackScreen, times(2)).getSuspectedNewAndStableLevel(anyLong(), anyLong());
    }

    @Test
    public void nethackBotShouldOnlyAskForNethackLevelsThatAreCreatedAfterTheLastRequest() {
        InOrder inOrder = Mockito.inOrder(nethackScreen);
        when(nethackScreen.getSuspectedNewAndStableLevel(anyLong(), anyLong()))
                .thenReturn(null).thenReturn(new NethackLevel(new char[1][1]));
        when(timePiece.getTimeMillis()).thenReturn(1L).thenReturn(2L).thenReturn(3L);

        NethackBot nethackBot = new NethackBot(timePiece);
        nethackBot.getLevelFromScreen(nethackScreen);

        inOrder.verify(nethackScreen).getSuspectedNewAndStableLevel(0L, NethackBot.TIME_UNTIL_LEVEL_CONSIDERED_STABLE);
        inOrder.verify(nethackScreen).getSuspectedNewAndStableLevel(1L, NethackBot.TIME_UNTIL_LEVEL_CONSIDERED_STABLE);
    }

    @Test
    public void nethackBotShouldIdentifyAdjacentSpacesThatCanBeMovedTo() {
        char[][] dungeonMap = new char[][]{{'@', '.'}};
        NethackLevel level = screenInterpreter.interpret(dungeonMap);
        DungeonThing[] dungeonThings = new DungeonThing[]{DungeonThing.STAIRWAY_UP};
        for (DungeonThing dungeonThing : dungeonThings) {
            level.setThingAt(new Coordinates(0, 1), dungeonThing);
            assertThat(nethackBot.getAvailableMoveLocations(level).size(), equalTo(1));
        }
    }

    @Test
    public void testIdentifyingPlaceToMoveFromCorner() {
        char[][] heroInCorner = new char[][]{
                {'.', '.', '.'},
                {'.', '.', '.'},
                {'@', '.', '.'},
        };

        NethackLevel level = screenInterpreter.interpret(heroInCorner);

        Set<Coordinates> availableMoveLocations = nethackBot.getAvailableMoveLocations(level);
        assertThat(availableMoveLocations.size(), equalTo(3));
        assertThat(availableMoveLocations, hasItems(new Coordinates(1, 0), new Coordinates(1, 1), new Coordinates(2, 1)));
    }

    @Test
    public void testIdentifyingPlaceToMoveAllAdjacentSidesOpen() {
        char[][] heroInMiddle = new char[][]{
                {'.', '.', '.'},
                {'.', '@', '.'},
                {'.', '.', '.'},
        };

        NethackLevel level = screenInterpreter.interpret(heroInMiddle);

        Set<Coordinates> availableMoveLocations = nethackBot.getAvailableMoveLocations(level);
        assertThat(availableMoveLocations.size(), equalTo(8));
        assertThat(availableMoveLocations, hasItems(
                        new Coordinates(0, 0), new Coordinates(0, 1),   new Coordinates(0, 2),
                        new Coordinates(1, 0),                          new Coordinates(1, 2),
                        new Coordinates(2, 0), new Coordinates(2, 1),   new Coordinates(2, 2)
                )
        );
    }

    @Test
    public void nethackBotShouldNotWantToMoveToUnmovableSpaces() {
        char[][] dungeonMap = new char[][]{{'@', '.'}};
        NethackLevel level = screenInterpreter.interpret(dungeonMap);
        DungeonThing[] dungeonThings = new DungeonThing[]{DungeonThing.VERTICAL_WALL, DungeonThing.CLOSED_DOOR};
        for (DungeonThing dungeonThing : dungeonThings) {
            level.setThingAt(new Coordinates(0, 1), dungeonThing);
            assertThat(nethackBot.getAvailableMoveLocations(level).size(), equalTo(0));
        }
    }
}
