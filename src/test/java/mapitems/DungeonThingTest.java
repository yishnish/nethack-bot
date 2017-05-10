package mapitems;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

public class DungeonThingTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testLookupByDisplayValue() {
        char hero = '@';
        assertThat(DungeonThing.forCharacter(hero), equalTo(DungeonThing.HERO));
    }

    @Test
    public void testThrowsErrorIfUnknownCharacterIsUsed() {
        thrown.expect(UnknownDungeonItemException.class);
        char unknown = '\u8080';
        DungeonThing.forCharacter(unknown);
    }
}