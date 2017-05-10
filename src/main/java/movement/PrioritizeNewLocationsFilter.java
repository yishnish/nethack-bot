package movement;

import collections.DefaultValueHashMap;
import locations.Coordinates;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PrioritizeNewLocationsFilter implements ActionFilter<Coordinates> {

    private Map<Coordinates, Integer> visited = new DefaultValueHashMap<Coordinates, Integer>(0);

    public void markVisited(Coordinates coordinates) {
        Integer visitCount = visited.get(coordinates);
        visited.put(coordinates, visitCount + 1);
    }

    public Set<Coordinates> filter(Set<Coordinates> moveLocationCandidates) {
        Integer minVisited = findLeastVisitedValue(moveLocationCandidates);

        Set<Coordinates> filtered = new HashSet<Coordinates>();
        for (Coordinates coordinates : moveLocationCandidates) {
            if (visited.get(coordinates).equals(minVisited)) {
                filtered.add(coordinates);
            }
        }

        return filtered;
    }

    private Integer findLeastVisitedValue(Set<Coordinates> moveLocationCandidates) {
        Integer minVisited = Integer.MAX_VALUE;
        for (Coordinates coordinates : moveLocationCandidates) {
            minVisited = Math.min(minVisited, visited.get(coordinates));
        }
        return minVisited;
    }
}
