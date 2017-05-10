package bot;

import command.MoveDelta;
import command.NethackCommand;
import level.NethackLevel;
import locations.Coordinates;
import movement.MovementStrategy;
import movement.SingleSpaceCorporealMovementStrategy;
import screen.NethackScreen;
import terminal.TimePiece;

import java.util.Arrays;
import java.util.Set;

public class NethackBot {

    public static final long TIME_UNTIL_LEVEL_CONSIDERED_STABLE = 500L;

    private final TimePiece timePiece;

    private long lastRequestTimestamp = 0L;

    private MovementStrategy movementStrategy;

    public NethackBot(TimePiece timePiece) {
        this(timePiece, new SingleSpaceCorporealMovementStrategy());
    }

    public NethackBot(TimePiece timePiece, MovementStrategy movementStrategy) {
        this.timePiece = timePiece;
        this.movementStrategy = movementStrategy;
    }

    public Set<Coordinates> getAvailableMoveLocations(NethackLevel level) {
        return movementStrategy.getAvailableMoveLocations(level);
    }

    public NethackLevel getLevelFromScreen(NethackScreen nethackScreen) {
        NethackLevel level;
        while ((level = nethackScreen.getSuspectedNewAndStableLevel(lastRequestTimestamp,
                TIME_UNTIL_LEVEL_CONSIDERED_STABLE)) == null) {
        }
        lastRequestTimestamp = timePiece.getTimeMillis();
        return level;
    }

    public NethackCommand getNextMove(NethackLevel level){
        Set<Coordinates> availableMoveLocations = getAvailableMoveLocations(level);
        int randomElementPosition = (int) (Math.floor(availableMoveLocations.size() * Math.random()));
        Coordinates moveTo = (Coordinates) Arrays.asList(availableMoveLocations.toArray()).get(randomElementPosition);

        return NethackCommand.forDelta(MoveDelta.from(level.getHeroLocation()).to(moveTo));
    }
}
