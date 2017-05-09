package command;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum NethackCommand {

    MOVE_RIGHT(new MoveDelta(0, 1), "l"),
    MOVE_LEFT(new MoveDelta(0, -1), "h"),
    MOVE_UP(new MoveDelta(-1, 0), "k"),
    MOVE_DOWN(new MoveDelta(1, 0), "j"),
    MOVE_DOWN_RIGHT(new MoveDelta(1, 1), "n"),
    MOVE_UP_LEFT(new MoveDelta(-1, -1), "y"),
    MOVE_UP_RIGHT(new MoveDelta(-1, 1), "u"),
    MOVE_DOWN_LEFT(new MoveDelta(1, -1), "b");

    private final MoveDelta moveDelta;
    private final String command;

    private static final Map<MoveDelta, NethackCommand> deltaCommandMapper = Collections.unmodifiableMap(initializeMapper());

    private static Map<MoveDelta, NethackCommand> initializeMapper() {
        Map<MoveDelta, NethackCommand> mapping = new HashMap<MoveDelta, NethackCommand>();
        for (NethackCommand nethackCommand : values()) {
            mapping.put(nethackCommand.getMoveDelta(), nethackCommand);
        }
        return mapping;
    }

    NethackCommand(MoveDelta moveDelta, String command) {
        this.moveDelta = moveDelta;
        this.command = command;
    }


    public String getCommand() {
        return command;
    }

    public MoveDelta getMoveDelta() {
        return moveDelta;
    }

    public static NethackCommand forDelta(MoveDelta delta) {
        return deltaCommandMapper.get(delta);
    }
}
