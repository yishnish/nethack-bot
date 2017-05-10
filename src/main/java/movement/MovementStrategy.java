package movement;

import level.NethackLevel;
import locations.Coordinates;

import java.util.Set;

public interface MovementStrategy {
    Set<Coordinates> getAvailableMoveLocations(NethackLevel level);
}
