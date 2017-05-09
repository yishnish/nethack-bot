package command;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class NethackCommandTest {

    @Test
    public void testMappingMoveDeltasToNethackCommands() {
        MoveDelta left = new MoveDelta(0, -1);
        MoveDelta up = new MoveDelta(-1, 0);
        MoveDelta diagonalDownRight = new MoveDelta(1, 1);

        assertThat(NethackCommand.forDelta(left), equalTo(NethackCommand.MOVE_LEFT));
        assertThat(NethackCommand.forDelta(up), equalTo(NethackCommand.MOVE_UP));
        assertThat(NethackCommand.forDelta(diagonalDownRight), equalTo(NethackCommand.MOVE_DOWN_RIGHT));
    }
}