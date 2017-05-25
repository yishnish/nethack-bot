package level;

import screenbufferinterpreter.NethackScreenBufferInterpreter;
import screenbufferinterpreter.NoLinesScreenTrimmer;
import terminal.ScreenBuffer;

public class NethackLevelCreator {

    private static final NethackScreenBufferInterpreter bufferInterpreter = new NethackScreenBufferInterpreter(new NoLinesScreenTrimmer());

    public static NethackLevel nethackLevelFor(char[][] bufferData) {
        return bufferInterpreter.interpret(new ScreenBuffer(bufferData));
    }
}
