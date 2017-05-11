package bot;

import command.NethackCommand;
import level.NethackLevel;
import locations.Coordinates;
import movement.ActionFilter;
import movement.MovementStrategy;
import movement.PrioritizeNewLocationsFilter;
import movement.SingleSpaceCorporealMovementStrategy;
import screen.NethackScreen;
import terminal.TimePiece;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class NethackBot {

    public static final long TIME_UNTIL_LEVEL_CONSIDERED_STABLE = 500L;

    private final TimePiece timePiece;

    private long lastRequestTimestamp = 0L;

    private MovementStrategy movementStrategy;

    private Set<ActionFilter<Coordinates>> movementFilters = new HashSet<ActionFilter<Coordinates>>();

    public NethackBot(TimePiece timePiece) {
        this(timePiece, new SingleSpaceCorporealMovementStrategy());
    }

    public NethackBot(TimePiece timePiece, MovementStrategy movementStrategy) {
        this.timePiece = timePiece;
        this.movementStrategy = movementStrategy;
    }

    public NethackBot(TimePiece timePiece,
                      MovementStrategy movementStrategy,
                      PrioritizeNewLocationsFilter movementFilter) {

        this.timePiece = timePiece;
        this.movementStrategy = movementStrategy;
        this.movementFilters.add(movementFilter);
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
        updateVisitedLocationsWithCurrentLocation(level);
        Set<Coordinates> availableMoveLocations = getAvailableMoveLocations(level);
        if (availableMoveLocations.size() == 0) {
            return NethackCommand.WAIT;
        }
        for (ActionFilter<Coordinates> filter : movementFilters) {
            availableMoveLocations = filter.filter(availableMoveLocations);
        }
        int randomElementPosition = (int) (Math.floor(availableMoveLocations.size() * Math.random()));
        Coordinates moveTo = (Coordinates) Arrays.asList(availableMoveLocations.toArray()).get(randomElementPosition);

        return NethackCommand.forDelta(level.getHeroLocation().to(moveTo));
    }

    private void updateVisitedLocationsWithCurrentLocation(NethackLevel level) {
        for (ActionFilter<Coordinates> movementFilter : movementFilters) {
            movementFilter.markVisited(level.getHeroLocation());
        }
    }
}
