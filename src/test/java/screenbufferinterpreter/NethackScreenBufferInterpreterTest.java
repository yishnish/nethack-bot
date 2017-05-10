package screenbufferinterpreter;

import level.NethackLevel;
import locations.Coordinates;
import mapitems.DungeonThing;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;

import static mapitems.DungeonThing.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class NethackScreenBufferInterpreterTest {

    NethackScreenBufferInterpreter interpreter;

    @Before
    public void setUp() {
        interpreter = new NethackScreenBufferInterpreter(new NoLinesScreenTrimmer());
    }

    @Test
    public void testSetsTheHeroLocationExplicitlyOnTheLevel() {
        NethackLevel level = interpreter.interpret(new char[][]{{'@'}});

        MatcherAssert.assertThat(level.getHeroLocation(), equalTo(new Coordinates(0, 0)));
    }

    @Test
    public void testAddsAMarkerForVacantSpacesOnTheLevelMap() {
        NethackLevel level = interpreter.interpret(new char[][]{{'.'}});

        MatcherAssert.assertThat(level.thingAt(new Coordinates(0, 0)), equalTo(VACANT));
    }

    @Test
    public void testMarksHeroLocationAsUnknownIfItCannotBeFound() {
        NethackLevel level = interpreter.interpret(new char[][]{{'.'}});

        MatcherAssert.assertThat(level.getHeroLocation(), equalTo(Coordinates.UNKNOWN));
    }

    @Test
    public void testAddsAMarkerForTheHeroOnTheLevelMap() {
        NethackLevel level = interpreter.interpret(new char[][]{{'@'}});

        MatcherAssert.assertThat(level.thingAt(new Coordinates(0, 0)), equalTo(HERO));
    }

    @Test
    public void testAddsAMarkerForHorizontalWallsOnTheLevelMap() {
        NethackLevel level = interpreter.interpret(new char[][]{{'-'}});

        MatcherAssert.assertThat(level.thingAt(new Coordinates(0, 0)), equalTo(DungeonThing.HORIZONTAL_WALL));
    }

    @Test
    public void testAddsAMarkerForVerticalWallsOnTheLevelMap() {
        NethackLevel level = interpreter.interpret(new char[][]{{'|'}});

        MatcherAssert.assertThat(level.thingAt(new Coordinates(0, 0)), equalTo(VERTICAL_WALL));
    }

    @Test
    public void testAddsAMarkerForStairwayUpOnTheLevelMap() {
        NethackLevel level = interpreter.interpret(new char[][]{{'<'}});

        MatcherAssert.assertThat(level.thingAt(new Coordinates(0, 0)), equalTo(STAIRWAY_UP));
    }

    @Test
    public void testAddsAMarkerForClosedDoorUpOnTheLevelMap() {
        NethackLevel level = interpreter.interpret(new char[][]{{'+'}});

        MatcherAssert.assertThat(level.thingAt(new Coordinates(0, 0)), equalTo(CLOSED_DOOR));
    }

    @Test
    public void testConfiguringToIgnoreRows(){

        char[][] dungeonRoomBuffer = new char[][]{
                {'-', '-', '-', '-', '-', '-', '-'},
                {'|', '.', '.', '.', '.', '.', '|'},
                {'|', '.', '.', '.', '.', '<', '+'},
                {'|', '.', '@', '.', '.', '.', '|'},
                {'-', '-', '-', '-', '-', '-', '-'},
        };

        DungeonThing[][] expectedInterpretedRoom = new DungeonThing[][]{
                {HORIZONTAL_WALL, HORIZONTAL_WALL, HORIZONTAL_WALL, HORIZONTAL_WALL, HORIZONTAL_WALL, HORIZONTAL_WALL, HORIZONTAL_WALL},
                {VERTICAL_WALL, VACANT, VACANT, VACANT, VACANT, VACANT, VERTICAL_WALL},
                {VERTICAL_WALL, VACANT, VACANT, VACANT, VACANT, STAIRWAY_UP, CLOSED_DOOR},
                {VERTICAL_WALL, VACANT, HERO, VACANT, VACANT, VACANT, VERTICAL_WALL},
                {HORIZONTAL_WALL, HORIZONTAL_WALL, HORIZONTAL_WALL, HORIZONTAL_WALL, HORIZONTAL_WALL, HORIZONTAL_WALL, HORIZONTAL_WALL},
        };

        NethackLevel level = interpreter.interpret(dungeonRoomBuffer);
        MatcherAssert.assertThat(level.getDungeonMap(), equalTo(expectedInterpretedRoom));
    }
    
    @Test
    public void testIgnoresTheTextInTheTopTwoAndBottomThreeRowsOfTheScreenByDefault() {

        char[] textRow1 = "1234567".toCharArray();
        char[] textRow2 = "1234567".toCharArray();
        char[] textRow3 = "1234567".toCharArray();
        char[] textRow4 = "1234567".toCharArray();
        char[] textRow5 = "1234567".toCharArray();

        char[][] dungeonRoomBuffer = new char[][]{
                textRow1,
                textRow2,
                {'-', '-', '-', '-', '-', '-', '-'},
                {'|', '.', '.', '.', '.', '.', '|'},
                {'|', '.', '.', '.', '.', '<', '+'},
                {'|', '.', '@', '.', '.', '.', '|'},
                {'-', '-', '-', '-', '-', '-', '-'},
                textRow3,
                textRow4,
                textRow5,

        };

        DungeonThing[][] expectedInterpretedRoom = new DungeonThing[][]{
                {HORIZONTAL_WALL, HORIZONTAL_WALL, HORIZONTAL_WALL, HORIZONTAL_WALL, HORIZONTAL_WALL, HORIZONTAL_WALL, HORIZONTAL_WALL},
                {VERTICAL_WALL, VACANT, VACANT, VACANT, VACANT, VACANT, VERTICAL_WALL},
                {VERTICAL_WALL, VACANT, VACANT, VACANT, VACANT, STAIRWAY_UP, CLOSED_DOOR},
                {VERTICAL_WALL, VACANT, HERO, VACANT, VACANT, VACANT, VERTICAL_WALL},
                {HORIZONTAL_WALL, HORIZONTAL_WALL, HORIZONTAL_WALL, HORIZONTAL_WALL, HORIZONTAL_WALL, HORIZONTAL_WALL, HORIZONTAL_WALL},
        };

        NethackScreenBufferInterpreter interpreter = new NethackScreenBufferInterpreter();
        NethackLevel level = interpreter.interpret(dungeonRoomBuffer);
        MatcherAssert.assertThat(level.getDungeonMap(), equalTo(expectedInterpretedRoom));
    }

}
