package level;

import locations.Coordinates;
import mapitems.DungeonThing;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;

public class NethackLevelTest {

    @Test
    public void testLevelTakesOnTheDimensionsOfTheScreenBufferItIsCreatedWith() {
        char[][] screenBuffer = new char[24][80];
        NethackLevel level = new NethackLevel(screenBuffer);
        assertThat(level.getNumRows(), equalTo(screenBuffer.length));
        assertThat(level.getNumColumns(), equalTo(screenBuffer[0].length));
    }

    @Test
    public void testLevelKnowsTheCoordinatesForTheHeroLocation(){
        NethackLevel level = new NethackLevel(new char[24][80]);
        Coordinates heroLocation = new Coordinates(1, 3);
        level.setHeroLocation(heroLocation);

        assertThat(level.getHeroLocation(), equalTo(heroLocation));
    }

    @Test
    public void testLevelAreEqualIfAllDungeonItemsAndHeroLocationAreTheSame() {
        NethackLevel levelA = new NethackLevel(new char[1][2]);
        NethackLevel levelB = new NethackLevel(new char[1][2]);

        levelA.setThingAt(new Coordinates(0, 0), DungeonThing.HERO);
        levelA.setThingAt(new Coordinates(0, 1), DungeonThing.VACANT);
        levelA.setHeroLocation(new Coordinates(0, 0));

        levelB.setThingAt(new Coordinates(0, 0), DungeonThing.HERO);
        levelB.setThingAt(new Coordinates(0, 1), DungeonThing.VACANT);
        levelB.setHeroLocation(new Coordinates(0, 0));

        assertThat(levelA, equalTo(levelB));
    }

    @Test
    public void testLevelAreNotEqualIfAnyDungeonItemsAreDifferent() {
        NethackLevel levelA = new NethackLevel(new char[1][2]);
        NethackLevel levelB = new NethackLevel(new char[1][2]);

        levelA.setThingAt(new Coordinates(0, 0), DungeonThing.VACANT);
        levelA.setThingAt(new Coordinates(0, 1), DungeonThing.VACANT);
        levelA.setHeroLocation(new Coordinates(0, 0));

        levelB.setThingAt(new Coordinates(0, 0), DungeonThing.HERO);
        levelB.setThingAt(new Coordinates(0, 1), DungeonThing.VACANT);
        levelB.setHeroLocation(new Coordinates(0, 0));

        assertThat(levelA, not(equalTo(levelB)));
    }

    @Test
    public void testLevelAreNotEqualIfHeroLocationIsDifferent() {
        NethackLevel levelA = new NethackLevel(new char[1][2]);
        NethackLevel levelB = new NethackLevel(new char[1][2]);

        levelA.setThingAt(new Coordinates(0, 0), DungeonThing.HERO);
        levelA.setThingAt(new Coordinates(0, 1), DungeonThing.VACANT);
        levelA.setHeroLocation(new Coordinates(0, 1));

        levelB.setThingAt(new Coordinates(0, 0), DungeonThing.HERO);
        levelB.setThingAt(new Coordinates(0, 1), DungeonThing.VACANT);
        levelB.setHeroLocation(new Coordinates(0, 0));

        assertThat(levelA, not(equalTo(levelB)));
    }
}