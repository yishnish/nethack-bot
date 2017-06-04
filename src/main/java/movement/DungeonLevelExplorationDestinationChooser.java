package movement;

import level.NethackLevel;
import locations.Coordinates;
import mapitems.DungeonThing;

import java.util.*;

public class DungeonLevelExplorationDestinationChooser {

    public Coordinates chooseDestination(NethackLevel level) {
        Coordinates heroLocation = level.getHeroLocation();
        DungeonLevelItemsFinder emptySpacesFinder = new DungeonLevelItemsFinder();
        Set<Coordinates> allEmptySpaces = emptySpacesFinder.findAll(DungeonThing.VACANT, level);


        Set<Coordinates> coordinates = withGreatestDistance(heroLocation, allEmptySpaces);
        return coordinates.stream().findFirst().orElse(null);
    }

    private Set<Coordinates> withGreatestDistance(Coordinates hero, Set<Coordinates> destinations) {
        Map<Double, Set<Coordinates>> distancesAndCoordinates  = new HashMap<>();
        for (Coordinates coordinates : destinations) {
            double distance = distanceBetween(hero, coordinates);
            Set<Coordinates> distances = distancesAndCoordinates.get(distance);
            if (distances == null) {
                distances = new LinkedHashSet<>();
                distancesAndCoordinates.put(distance, distances);
            }
            distances.add(coordinates);
        }

        Optional<Double> longestDistance = distancesAndCoordinates.keySet().stream().max(Double::compareTo);
        if (!longestDistance.isPresent()) {
            return new LinkedHashSet<>();
        }
        return distancesAndCoordinates.get(longestDistance.get());
    }

    private double distanceBetween(Coordinates hero, Coordinates destination) {
        int dx = hero.getColumn() - destination.getColumn();
        int dy = hero.getRow() - destination.getRow();
        return Math.floor(Math.sqrt(dx * dx + dy * dy));
    }
}
