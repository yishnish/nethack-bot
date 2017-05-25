package movement;

import level.NethackLevel;
import level.NethackLevelCreator;
import locations.Coordinates;
import mapitems.DungeonThing;
import org.junit.Before;
import org.junit.Test;
import screenbufferinterpreter.NethackScreenBufferInterpreter;
import screenbufferinterpreter.NoLinesScreenTrimmer;
import terminal.ScreenBuffer;

import java.util.Set;

import static collections.CollectionsHelpers.setOf;
import static locations.Coordinates.coordinates;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;

public class SingleSpaceCorporealMovementStrategyTest {

    SingleSpaceCorporealMovementStrategy movementStrategy;

    NethackScreenBufferInterpreter screenInterpreter;

    @Before
    public void setUp() {
        screenInterpreter = new NethackScreenBufferInterpreter(new NoLinesScreenTrimmer());
        movementStrategy = new SingleSpaceCorporealMovementStrategy();
    }

    @Test
    public void givenANethackLevelItShouldProvideAllMovableLocationsWithinDistanceOneOfTheHero() {
        ScreenBuffer dungeonMap = new ScreenBuffer(new char[][]{{'@', '.'}});
        NethackLevel level = screenInterpreter.interpret(dungeonMap);
        DungeonThing[] dungeonThings = new DungeonThing[]{DungeonThing.STAIRWAY_UP};
        for (DungeonThing dungeonThing : dungeonThings) {
            level.setThingAt(new Coordinates(0, 1), dungeonThing);
            assertThat(movementStrategy.getAvailableMoveLocations(level).size(), equalTo(1));
        }
    }

    @Test
    public void testIdentifyingPlaceToMoveFromCorner() {
        char[][] heroInCorner = new char[][]{
                {'.', '.', '.'},
                {'.', '.', '.'},
                {'@', '.', '.'},
        };

        ScreenBuffer screen = new ScreenBuffer(heroInCorner);
        NethackLevel level = screenInterpreter.interpret(screen);

        Set<Coordinates> availableMoveLocations = movementStrategy.getAvailableMoveLocations(level);
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
        ScreenBuffer screen = new ScreenBuffer(heroInMiddle);
        NethackLevel level = screenInterpreter.interpret(screen);

        Set<Coordinates> availableMoveLocations = movementStrategy.getAvailableMoveLocations(level);
        assertThat(availableMoveLocations.size(), equalTo(8));
        assertThat(availableMoveLocations, hasItems(
                        new Coordinates(0, 0), new Coordinates(0, 1),   new Coordinates(0, 2),
                        new Coordinates(1, 0),                          new Coordinates(1, 2),
                        new Coordinates(2, 0), new Coordinates(2, 1),   new Coordinates(2, 2)
                )
        );
    }

    @Test
    public void itShouldNotConsiderMovingToUnmovableSpaces() {
        ScreenBuffer dungeonMap = new ScreenBuffer(new char[][]{{'@', '.'}});
        NethackLevel level = screenInterpreter.interpret(dungeonMap);
        DungeonThing[] dungeonThings = new DungeonThing[]{DungeonThing.VERTICAL_WALL, DungeonThing.CLOSED_DOOR};
        for (DungeonThing dungeonThing : dungeonThings) {
            level.setThingAt(new Coordinates(0, 1), dungeonThing);
            assertThat(movementStrategy.getAvailableMoveLocations(level).size(), equalTo(0));
        }
    }

    @Test
    public void itShouldConsiderMovingToMovableSpacesAsDefinedByTheStrategy() {
        ScreenBuffer dungeonMap = new ScreenBuffer(new char[][]{{'@', '.'}});
        NethackLevel level = screenInterpreter.interpret(dungeonMap);
        Set<Coordinates> expected = setOf(new Coordinates(0, 1));
        for (DungeonThing dungeonThing : SingleSpaceCorporealMovementStrategy.SAFE_MOVE_SPOTS) {
            level.setThingAt(new Coordinates(0, 1), dungeonThing);
            assertThat(movementStrategy.getAvailableMoveLocations(level), equalTo(expected));
        }
    }

    @Test
    public void itShouldTellYouIfASpotIsAbleToBeMovedTo() {
        NethackLevel level = NethackLevelCreator.nethackLevelFor(new char[][]{
                {'@', '|'},
                {'.', '|'},
        });
        assertThat(new SingleSpaceCorporealMovementStrategy().canMoveTo(coordinates(0, 1), level), equalTo(false));
        assertThat(new SingleSpaceCorporealMovementStrategy().canMoveTo(coordinates(1, 0), level), equalTo(true));
    }
}
