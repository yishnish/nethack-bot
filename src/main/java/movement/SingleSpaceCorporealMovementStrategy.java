package movement;

import level.NethackLevel;
import locations.Coordinates;
import mapitems.DungeonThing;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SingleSpaceCorporealMovementStrategy implements MovementStrategy {
    private static final int BASIC_MOVE_DX = 1;
    private static final int BASIC_MOVE_DY = 1;
    public static final List<DungeonThing> SAFE_MOVE_SPOTS =
            Arrays.asList(DungeonThing.VACANT, DungeonThing.STAIRWAY_UP,
                    DungeonThing.HALLWAY, DungeonThing.GOLD, DungeonThing.FOOD,
                DungeonThing.WAND, DungeonThing.SCROLL);

    public Set<Coordinates> getAvailableMoveLocations(NethackLevel level) {
        Coordinates heroLocation = level.getHeroLocation();

        int levelRows = level.getNumRows();
        int levelColumns = level.getNumColumns();

        Set<Coordinates> placesToMoveTo = new HashSet<Coordinates>();
        for (int dx = BASIC_MOVE_DX * (-1); dx <= BASIC_MOVE_DX; dx++) {
            for (int dy = BASIC_MOVE_DY * (-1); dy <= BASIC_MOVE_DY; dy++) {
                int potentialRow = Math.max(0, Math.min(levelRows - 1, heroLocation.getRow() + dy));
                int potentialColumn = Math.max(0, Math.min(levelColumns - 1, heroLocation.getColumn() + dx));
                Coordinates potentialMoveSpot = new Coordinates(potentialRow, potentialColumn);
                if (SAFE_MOVE_SPOTS.contains(level.thingAt(potentialMoveSpot))) {
                    placesToMoveTo.add(potentialMoveSpot);
                }
            }
        }
        placesToMoveTo.remove(heroLocation);
        return placesToMoveTo;
    }
}