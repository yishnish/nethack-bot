package bot;

import command.NethackCommand;
import level.NethackLevel;
import locations.Coordinates;
import locations.MoveDelta;
import mapitems.DungeonThing;
import movement.ActionFilter;
import movement.DungeonLevelItemsFinder;
import movement.Pathfinder;
import movement.PrioritizeNewLocationsFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import screen.NethackScreen;
import terminal.TimePiece;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class NethackBot {

    public static final long TIME_UNTIL_LEVEL_CONSIDERED_STABLE = 500L;

    private final TimePiece timePiece;

    private long lastRequestTimestamp = 0L;

    private Set<ActionFilter<Coordinates>> movementFilters = new LinkedHashSet<>();

    private Logger logger = LogManager.getLogger(NethackBot.class);

    public NethackBot(TimePiece timePiece) {
        this.timePiece = timePiece;
    }

    public NethackBot(TimePiece timePiece,
                      PrioritizeNewLocationsFilter movementFilter) {

        this.timePiece = timePiece;
        this.movementFilters.add(movementFilter);
    }

    public NethackCommand getNextMove(NethackLevel level){
        notifyFiltersOfCurrentLevelState(level);

        Set<Coordinates> availableMoveLocations = getAvailableMoveLocations(level);

        availableMoveLocations = filterMovementOptions(availableMoveLocations);

        if (availableMoveLocations.size() == 0) {
            return NethackCommand.WAIT;
        }

        //locations should be able to be anywhere on the map, so next step is to build a path to the location
        //e.g. new Pathfinder().findPath(randomElementPosition, level)
        //below step is just to grab the first location from the path, assuming it is ordered by distance from hero
        Coordinates moveTo = chooseDestination(availableMoveLocations);

        Set<Coordinates> path = (new Pathfinder()).getPath(level.getHeroLocation(), moveTo, level);

        MoveDelta moveDelta = level.getHeroLocation().to((Coordinates) path.toArray()[0]);
        NethackCommand command = NethackCommand.forDelta(moveDelta);

        logger.debug("Moving hero from " + level.getHeroLocation() + " to " + path.toArray()[0]  + " in order to follow path " + path);

        return command;
    }

    public NethackLevel getLevelFromScreen(NethackScreen nethackScreen) {
        NethackLevel level;
        while ((level = nethackScreen.getSuspectedNewAndStableLevel(lastRequestTimestamp,
                TIME_UNTIL_LEVEL_CONSIDERED_STABLE)) == null) {
        }
        lastRequestTimestamp = timePiece.getTimeMillis();
        return level;
    }

    private Coordinates chooseDestination(Set<Coordinates> availableMoveLocations) {
        int randomElementPosition = (int) (Math.floor(availableMoveLocations.size() * Math.random()));
        return (Coordinates) Arrays.asList(availableMoveLocations.toArray()).get(randomElementPosition);
    }

    private Set<Coordinates> getAvailableMoveLocations(NethackLevel level) {
        return (new DungeonLevelItemsFinder()).findAll(DungeonThing.VACANT, level);
    }

    private Set<Coordinates> filterMovementOptions(Set<Coordinates> availableMoveLocations) {
        for (ActionFilter<Coordinates> filter : movementFilters) {
            availableMoveLocations = filter.filter(availableMoveLocations);
        }
        return availableMoveLocations;
    }

    private void notifyFiltersOfCurrentLevelState(NethackLevel level) {
        for (ActionFilter<Coordinates> movementFilter : movementFilters) {
            movementFilter.markVisited(level.getHeroLocation());
        }
    }
}
