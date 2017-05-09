package bot;

import level.NethackLevel;
import locations.Coordinates;
import mapitems.DungeonThing;
import screen.NethackScreen;
import terminal.TimePiece;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NethackBot {

    public static final long TIME_UNTIL_LEVEL_CONSIDERED_STABLE = 500L;

    private final TimePiece timePiece;

    private long lastRequestTimestamp = 0L;


    private static final int BASIC_MOVE_DX = 1;
    private static final int BASIC_MOVE_DY = 1;
    private static final List<DungeonThing> SAFE_MOVE_SPOTS = Arrays.asList(DungeonThing.VACANT, DungeonThing.STAIRWAY_UP);

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

    public NethackBot(TimePiece timePiece) {
        this.timePiece = timePiece;
    }

    public void getLevelFromScreen(NethackScreen nethackScreen) {
        NethackLevel level;
        while ((level = nethackScreen.getSuspectedNewAndStableLevel(lastRequestTimestamp,
                TIME_UNTIL_LEVEL_CONSIDERED_STABLE)) == null) {
            lastRequestTimestamp = timePiece.getTimeMillis();
        }
    }
}
