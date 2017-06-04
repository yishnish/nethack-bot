package movement;

import level.NethackLevel;
import locations.Coordinates;
import org.junit.Before;
import org.junit.Test;
import screenbufferinterpreter.NethackScreenBufferInterpreter;
import screenbufferinterpreter.NoLinesScreenTrimmer;

import java.util.Set;

import static collections.CollectionsHelpers.setOf;
import static level.NethackLevelCreator.nethackLevelFor;
import static locations.Coordinates.coordinates;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class DungeonLevelExplorationDestinationChooserTest {

    NethackScreenBufferInterpreter bufferInterpreter;

    @Before
    public void setUp() {
        bufferInterpreter = new NethackScreenBufferInterpreter(new NoLinesScreenTrimmer());
    }

    @Test
    public void testFindingADestination() {
        NethackLevel level = nethackLevelFor(new char[][]{
                {'.', '@'},
        });

        DungeonLevelExplorationDestinationChooser destinationChooser = new DungeonLevelExplorationDestinationChooser();

        assertThat(destinationChooser.chooseDestination(level), equalTo(coordinates(0, 0)));
    }

    @Test
    public void testFindingADestinationReturnsTheFarthestEmptySpot_forNow() {
        NethackLevel level = nethackLevelFor(new char[][]{
                {'.', '@'},
                {'.', '|'},
                {'.', '|'},
        });

        DungeonLevelExplorationDestinationChooser destinationChooser = new DungeonLevelExplorationDestinationChooser();

        assertThat(destinationChooser.chooseDestination(level), equalTo(coordinates(2, 0)));
    }

    @Test
    public void testFindingMoreThanOneDestinationOfEqualGreatestDistanceReturnsOneOfThem() {
        NethackLevel level = nethackLevelFor(new char[][]{
                {'.', '@', '.'},
                {'.', '|', '.'},
                {'.', '|', '.'},
        });

        DungeonLevelExplorationDestinationChooser destinationChooser = new DungeonLevelExplorationDestinationChooser();

        Set<Coordinates> possibleDestinations = setOf(coordinates(2, 0), coordinates(2, 2));
        assertThat(possibleDestinations, hasItem(destinationChooser.chooseDestination(level)));
    }

    @Test
    public void testReturnsNullWhenNoDestinationAreAvailable(){
        NethackLevel level = nethackLevelFor(new char[][]{
                {'B', '|', '@'}
        });

        DungeonLevelExplorationDestinationChooser destinationChooser = new DungeonLevelExplorationDestinationChooser();
        assertThat(destinationChooser.chooseDestination(level), nullValue());
    }
}
