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

    public NethackLevel getLevel() {
        return screenInterpreter.interpret(terminal.getScreenBuffer());
    }
}
