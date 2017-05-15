package screen;

import screenbufferinterpreter.NethackScreenBufferInterpreter;
import level.NethackLevel;
import terminal.ScreenBuffer;
import terminal.TimePiece;
import terminal.UTCTimePiece;
import terminal.VTerminal;

public class NethackScreen {

    private final VTerminal terminal;
    private final NethackScreenBufferInterpreter screenInterpreter;
    private final TimePiece timePiece;

    public NethackScreen(VTerminal terminal, NethackScreenBufferInterpreter screenInterpreter) {
        this(terminal, screenInterpreter, new UTCTimePiece());
    }

    public NethackScreen(VTerminal terminal, NethackScreenBufferInterpreter screenInterpreter, TimePiece timePiece) {
        this.terminal = terminal;
        this.screenInterpreter = screenInterpreter;
        this.timePiece = timePiece;
    }

    public NethackLevel getSuspectedNewAndStableLevel(long lastCheckTime, long minStableTime) {
        ScreenBuffer screenBuffer = terminal.getScreenBuffer();
        long lastUpdateTime = screenBuffer.getTimestamp();
        long unchangedFor = timePiece.getTimeMillis() - lastUpdateTime;
        if (lastUpdateTime < lastCheckTime || unchangedFor < minStableTime) {
            return null;
        }
        return screenInterpreter.interpret(screenBuffer);
    }
}
