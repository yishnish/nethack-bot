package screen;

import interpreter.NethackScreenInterpreter;
import level.NethackLevel;
import terminal.VTerminal;

public class NethackScreen {

    private final VTerminal terminal;

    private final NethackScreenInterpreter screenInterpreter;

    public NethackScreen(VTerminal terminal, NethackScreenInterpreter screenInterpreter) {
        this.terminal = terminal;
        this.screenInterpreter = screenInterpreter;
    }

    public NethackLevel getSuspectedNewAndStableLevel(long lastCheckTime, long minStableTime) {
        long lastUpdateTime = terminal.getLastUpdateTime();
        long unchangedFor = terminal.unchangedFor();
        if (lastUpdateTime <= lastCheckTime || unchangedFor < minStableTime) {
            return null;
        }
        return screenInterpreter.interpret(terminal.getScreenBuffer());
    }
}
