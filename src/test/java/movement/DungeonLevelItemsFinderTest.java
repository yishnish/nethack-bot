package movement;

import level.NethackLevel;
import mapitems.DungeonThing;
import org.junit.Test;

import static collections.CollectionsHelpers.setOf;
import static level.NethackLevelCreator.nethackLevelFor;
import static locations.Coordinates.coordinates;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class DungeonLevelItemsFinderTest {

    @Test
    public void testFindingTheLocationOfATypeOfThingOnADungeonLevel() {
        NethackLevel level = nethackLevelFor(new char[][]{
                {'.', '$'},
        });

        DungeonLevelItemsFinder dungeonLevelItemsFinder = new DungeonLevelItemsFinder();

        assertThat(dungeonLevelItemsFinder.findAll(DungeonThing.VACANT, level), equalTo(setOf(coordinates(0, 0))));
        assertThat(dungeonLevelItemsFinder.findAll(DungeonThing.GOLD, level), equalTo(setOf(coordinates(0, 1))));
    }

    @Test
    public void testFindingLocationsOfManyOfATypeOfThingOnADungeonLevel() {
        NethackLevel level = nethackLevelFor(new char[][]{
                {'.', '$', '.', 'B', '$'},
        });

        DungeonLevelItemsFinder dungeonLevelItemsFinder = new DungeonLevelItemsFinder();

        assertThat(dungeonLevelItemsFinder.findAll(DungeonThing.VACANT, level), equalTo(setOf(coordinates(0, 0), coordinates(0, 2))));
        assertThat(dungeonLevelItemsFinder.findAll(DungeonThing.GOLD, level), equalTo(setOf(coordinates(0, 1), coordinates(0, 4))));
    }
}
