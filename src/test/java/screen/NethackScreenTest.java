package screen;

import interpreter.NethackScreenInterpreter;
import interpreter.NoLinesScreenTrimmer;
import level.NethackLevel;
import locations.Coordinates;
import mapitems.DungeonThing;
import org.junit.Before;
import org.junit.Test;
import screen.NethackScreen;
import terminal.*;


import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class NethackScreenTest {
    Display display;
    VTerminal terminal;
    NethackScreen nethackScreen;
    NethackScreenInterpreter screenInterpreter;

    @Before
    public void setUp(){
        display = new BlankDisplay();
        terminal = new Vermont(1, 1, display);
        screenInterpreter = new NethackScreenInterpreter(new NoLinesScreenTrimmer());
        terminal.moveCursor(CursorPosition.HOME);
        nethackScreen = new NethackScreen(terminal, screenInterpreter);
    }

    @Test
    public void testGettingTheCurrentLevelFromATerminal(){
        terminal.write('+');
        NethackLevel interpretedLevel = nethackScreen.getLevel();

        assertThat(interpretedLevel.thingAt(new Coordinates(0, 0)), equalTo(DungeonThing.CLOSED_DOOR));
    }

}
