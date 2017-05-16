package movement;

import level.NethackLevel;
import locations.Coordinates;
import org.junit.Test;
import screenbufferinterpreter.NethackScreenBufferInterpreter;
import screenbufferinterpreter.NoLinesScreenTrimmer;
import terminal.ScreenBuffer;

import java.util.Set;

import static collections.CollectionsHelpers.setOf;
import static java.util.Arrays.asList;
import static locations.Coordinates.coordinates;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;

public class PathfinderTest {

    @Test
    public void testFindingTheHeroOnTheLevel() {
        char[][] screen = new char[][]{{}};
        NethackLevel level = new NethackLevel(screen);
        level.setHeroLocation(coordinates(0, 1));
        Pathfinder pathfinder = new Pathfinder();

        assertThat(pathfinder.getHeroLocation(level), equalTo(coordinates(0, 1)));

        level.setHeroLocation(coordinates(3, 3));

        assertThat(pathfinder.getHeroLocation(level), equalTo(coordinates(3, 3)));
    }

    @Test
    public void testCreatesACollectionOfCoordinatesToMoveToToGetToADestination() {
        char[][] screen = new char[][]{{'@', '.'}};
        NethackLevel level = new NethackScreenBufferInterpreter(new NoLinesScreenTrimmer()).interpret(new ScreenBuffer(screen));
        level.setHeroLocation(coordinates(0, 0));
        Pathfinder pathfinder = new Pathfinder();
        Set<Coordinates> path = pathfinder.getPath(coordinates(0, 0), coordinates(0, 1), level);

        Set<Coordinates> expected = setOf(coordinates(0, 1));

        assertThat(path, equalTo(expected));
    }

    @Test
    public void testIfMultiplePathsExistItReturnsTheShortest() {
        char[][] screen = new char[][]{
                {'@', 'B', '.'},
                {'.', '.', '.'},
                {'.', '.', '.'},
        };
        NethackLevel level = new NethackScreenBufferInterpreter(new NoLinesScreenTrimmer()).interpret(new ScreenBuffer(screen));
        level.setHeroLocation(coordinates(0, 0));
        Pathfinder pathfinder = new Pathfinder();
        Set<Coordinates> path = pathfinder.getPath(coordinates(0, 0), coordinates(0, 2), level);

        Set<Coordinates> expected = setOf(coordinates(1, 1), coordinates(0, 2));

        assertThat(path, equalTo(expected));
    }

    @Test
    public void testIfMultipleShortestPathsOfTheSameLengthExistItReturnsEitherOne() {
        char[][] screen = new char[][]{
                {'@', '.', '.'},
                {'.', '.', '.'},
                {'.', '.', '.'},
        };
        NethackLevel level = new NethackScreenBufferInterpreter(new NoLinesScreenTrimmer()).interpret(new ScreenBuffer(screen));
        level.setHeroLocation(coordinates(0, 0));
        Pathfinder pathfinder = new Pathfinder();
        Set<Coordinates> path = pathfinder.getPath(coordinates(0, 0), coordinates(2, 1), level);

        Set<Coordinates> shortestOne = setOf(coordinates(1, 0), coordinates(2, 1));
        Set<Coordinates> shortestTwo = setOf(coordinates(1, 1), coordinates(2, 1));

        assertThat(asList(shortestOne, shortestTwo), hasItem(path));
    }

    @Test
    public void testReturnsEmptyPathIfThereIsNoPath() {
        char[][] screen = new char[][]{
                {'@', 'B'}
        };
        NethackLevel level = new NethackScreenBufferInterpreter(new NoLinesScreenTrimmer()).interpret(new ScreenBuffer(screen));
        level.setHeroLocation(coordinates(0, 0));
        Pathfinder pathfinder = new Pathfinder();
        Set<Coordinates> path = pathfinder.getPath(coordinates(0, 0), coordinates(0, 1), level);

        assertThat(path, is(empty()));
    }
}
