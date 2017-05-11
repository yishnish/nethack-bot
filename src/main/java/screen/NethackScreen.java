package screen;

import screenbufferinterpreter.NethackScreenBufferInterpreter;
import level.NethackLevel;
import terminal.VTerminal;

public class NethackScreen {

    private final VTerminal terminal;

    private final NethackScreenBufferInterpreter screenInterpreter;

    public NethackScreen(VTerminal terminal, NethackScreenBufferInterpreter screenInterpreter) {
        this.terminal = terminal;
        this.screenInterpreter = screenInterpreter;
    }

    public NethackLevel getSuspectedNewAndStableLevel(long lastCheckTime, long minStableTime) {
        long lastUpdateTime = terminal.getLastUpdateTime();
        long unchangedFor = terminal.unchangedFor();
        if (lastUpdateTime < lastCheckTime || unchangedFor < minStableTime) {
            return null;
        }
        return screenInterpreter.interpret(terminal.getScreenBuffer());
    }
}
