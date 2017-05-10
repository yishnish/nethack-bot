package movement;

import interpreter.NethackScreenInterpreter;
import interpreter.NoLinesScreenTrimmer;
import level.NethackLevel;
import locations.Coordinates;
import mapitems.DungeonThing;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;

public class SingleSpaceCorporealMovementStrategyTest {

    SingleSpaceCorporealMovementStrategy movementStrategy;

    NethackScreenInterpreter screenInterpreter;

    @Before
    public void setUp() {
        screenInterpreter = new NethackScreenInterpreter(new NoLinesScreenTrimmer());
        movementStrategy = new SingleSpaceCorporealMovementStrategy();
    }

    @Test
    public void givenANethackLevelItShouldProvideAllMovableLocationsWithinDistanceOneOfTheHero() {
        char[][] dungeonMap = new char[][]{{'@', '.'}};
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

        NethackLevel level = screenInterpreter.interpret(heroInCorner);

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

        NethackLevel level = screenInterpreter.interpret(heroInMiddle);

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
        char[][] dungeonMap = new char[][]{{'@', '.'}};
        NethackLevel level = screenInterpreter.interpret(dungeonMap);
        DungeonThing[] dungeonThings = new DungeonThing[]{DungeonThing.VERTICAL_WALL, DungeonThing.CLOSED_DOOR};
        for (DungeonThing dungeonThing : dungeonThings) {
            level.setThingAt(new Coordinates(0, 1), dungeonThing);
            assertThat(movementStrategy.getAvailableMoveLocations(level).size(), equalTo(0));
        }
    }
}
