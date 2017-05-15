package screenbufferinterpreter;

import level.NethackLevel;
import locations.Coordinates;
import mapitems.DungeonThing;
import terminal.ScreenBuffer;

public class NethackScreenBufferInterpreter {

    private ScreenTrimmer screenTrimmer;

    public NethackScreenBufferInterpreter() {
        this(new TextLinesScreenTrimmer());
    }

    public NethackScreenBufferInterpreter(ScreenTrimmer screenTrimmerStrategy) {
        this.screenTrimmer = screenTrimmerStrategy;
    }

    public NethackLevel interpret(ScreenBuffer screenBuffer) {
        char[][] trimmedScreenBuffer = screenTrimmer.trim(screenBuffer.getScreenData());
        NethackLevel level = new NethackLevel(trimmedScreenBuffer);
        Coordinates heroLocation = getHeroLocation(trimmedScreenBuffer);
        for (int i = 0; i < trimmedScreenBuffer.length; i++) {
            char[] row = trimmedScreenBuffer[i];
            for (int j = 0; j < row.length; j++) {
                level.setThingAt(new Coordinates(i, j), DungeonThing.forCharacter(row[j]));
            }
        }
        level.setHeroLocation(heroLocation);
        return level;
    }

    private Coordinates getHeroLocation(char[][] screenBuffer) {
        for (int i = 0; i < screenBuffer.length; i++) {
            char[] row = screenBuffer[i];
            for (int j = 0; j < row.length; j++) {
                if (row[j] == '@') {
                    return new Coordinates(i, j);
                }
            }
        }
        return Coordinates.UNKNOWN;
    }

}
