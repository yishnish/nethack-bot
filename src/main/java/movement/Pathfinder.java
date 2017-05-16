package movement;

import collections.TreeNode;
import level.NethackLevel;
import locations.Coordinates;

import java.util.HashSet;
import java.util.Set;

public class Pathfinder {
    public Coordinates getHeroLocation(NethackLevel level) {
        return level.getHeroLocation();
    }

    public Set<Coordinates> getPath(Coordinates from, Coordinates to, NethackLevel level) {
        TreeNode<Coordinates> root = new TreeNode<>(from);
        buildAllPaths(root, to, level);

        Set<Coordinates> aShortestBranch = getShortestBranch(root.getCoordinateStepsForAllPathsToLocation(to));
        aShortestBranch.remove(from);

        return aShortestBranch;
    }

    private Set<Coordinates> getShortestBranch(Set<Set<Coordinates>> branches) {
        return branches.stream().sorted((b1, b2) -> Integer.compare(b1.size(), b2.size())).findFirst().orElse(new HashSet<>());
    }

    private void buildAllPaths(TreeNode<Coordinates> root, Coordinates to, NethackLevel level) {
        if (root.getData().equals(to)) {
            return;
        }
        SingleSpaceCorporealMovementStrategy movementStrategy = new SingleSpaceCorporealMovementStrategy();
        Set<Coordinates> movableLocations = movementStrategy.getAvailableMoveLocations(level, root.getData());
        movableLocations.stream().filter(movableLocation -> !root.branchContains(movableLocation)).forEach(movableLocation -> {
            TreeNode<Coordinates> child = new TreeNode<>(movableLocation);
            root.addChild(child);
            buildAllPaths(child, to, level);
        });
    }
}
