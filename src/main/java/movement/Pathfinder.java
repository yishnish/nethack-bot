package movement;

import level.NethackLevel;
import locations.Coordinates;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Pathfinder {

    private Logger logger = LogManager.getLogger(Pathfinder.class);

    private MovementStrategy movementStrategy = new SingleSpaceCorporealMovementStrategy();

    public Coordinates getHeroLocation(NethackLevel level) {
        return level.getHeroLocation();
    }

    public Set<Coordinates> getPath(Coordinates from, Coordinates to, NethackLevel level) {
        Integer[][] weightedMap = createWeightedMovementMap(from, to, level);
        return buildPath(level, weightedMap, from, to);
    }

    private Set<Coordinates> buildPath(NethackLevel level, Integer[][] weightedMap, Coordinates from, Coordinates to) {
        return buildPathRecursive(level, weightedMap, from, to, new LinkedHashSet<>());
    }

    private Set<Coordinates> buildPathRecursive(NethackLevel level, Integer[][] weightedMap, Coordinates from, Coordinates to, Set<Coordinates> path) {
        Integer shortest = Integer.MAX_VALUE;
        Set<Coordinates> moveLocations = movementStrategy.getAvailableMoveLocations(level, from);

        Coordinates lowestWeightedSpace = null;
        Set<Coordinates> coordinates = moveLocations.stream().filter(it -> it != null).collect(Collectors.toSet());
        for (Coordinates moveLocation : coordinates) {
            if (moveLocation.equals(to)) {
                path.add(moveLocation);
                return path;
            }
            Integer weight = weightedMap[moveLocation.getRow()][moveLocation.getColumn()];
            if (weight != null && weight < shortest) {
                shortest = weight;
                lowestWeightedSpace = moveLocation;
            }
        }
        if (lowestWeightedSpace == null) {
            return path;
        }
        path.add(lowestWeightedSpace);
        return buildPathRecursive(level, weightedMap, lowestWeightedSpace, to, path);
    }

    private Integer[][] createWeightedMovementMap(Coordinates from, Coordinates to, NethackLevel level) {
        Integer[][] weightedMap = new Integer[level.getNumRows()][level.getNumColumns()];

        populateMapWithWeights(weightedMap, level, movementStrategy.getAvailableMoveLocations(level, to), from, 1);
        return weightedMap;
    }

    private void populateMapWithWeights(Integer[][] weightedMap, NethackLevel dungeonLevel, Set<Coordinates> moveLocations, Coordinates origin, int moveWeight) {
        for (Coordinates coordinates : moveLocations) {
            if (coordinates.equals(origin)) {
                return;
            }
            Integer weightAtLocation = weightedMap[coordinates.getRow()][coordinates.getColumn()];
            if (weightAtLocation == null || weightAtLocation > moveWeight) {
                weightedMap[coordinates.getRow()][coordinates.getColumn()] = moveWeight;
                populateMapWithWeights(weightedMap, dungeonLevel, movementStrategy.getAvailableMoveLocations(dungeonLevel, coordinates), origin, moveWeight + 1);
            }

        }
    }

}
