package movement;

import level.NethackLevel;
import locations.Coordinates;
import mapitems.DungeonThing;

import java.util.LinkedHashSet;
import java.util.Set;

import static locations.Coordinates.coordinates;

public class DungeonLevelItemsFinder {
    public Set<Coordinates> findAll(DungeonThing itemType, NethackLevel level) {

        DungeonThing[][] dungeonMap = level.getDungeonMap();
        Set<Coordinates> allMatching = new LinkedHashSet<>();
        for (int y = 0; y < dungeonMap.length; y++) {
            DungeonThing[] row = dungeonMap[y];
            for (int x = 0; x < row.length; x++) {
                if (row[x] == itemType) {
                    allMatching.add(coordinates(y, x));
                }
            }
        }

        return allMatching;
    }

}
