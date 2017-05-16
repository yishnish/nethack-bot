package collections;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TreeNode<T> {

    private T data;
    private List<TreeNode<T>> children = new ArrayList<TreeNode<T>>();
    private TreeNode<T> parent;

    public TreeNode(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void addChild(TreeNode<T> child) {
        child.setParent(this);
        children.add(child);
    }

    public List<TreeNode<T>> getChildren() {
        return children;
    }

    public void setParent(TreeNode<T> parent) {
        this.parent = parent;
    }

    public TreeNode<T> getParent() {
        return parent;
    }

    public int branchLength() {
        int nodeCount = 1;
        TreeNode nextParent = parent;
        while(nextParent != null){
            nextParent = nextParent.getParent();
            nodeCount += 1;
        }
        return nodeCount;
    }

    public boolean branchContains(T data) {
        if (this.data.equals(data)) {
            return true;
        }
        TreeNode nextParent = parent;
        while (nextParent != null) {
            if (nextParent.getData().equals(data)) {
                return true;
            }
            nextParent = nextParent.getParent();
        }
        return false;
    }

    public Set<Set<T>> getCoordinateStepsForAllPathsToLocation(T data) {
        Set<TreeNode<T>> leaves = getLeafNodes();
        leaves = leaves.stream().filter((TreeNode<T> t) -> t.getData().equals(data)).collect(Collectors.toSet());

        Set<Set<T>> branches = new HashSet<>();

        for (TreeNode<T> leaf : leaves) {
            Set<T> branch = new HashSet<>();
            while (leaf != null) {
                branch.add(leaf.getData());
                leaf = leaf.getParent();
            }
            branches.add(branch);
        }

        return branches;
    }

    public Set<TreeNode<T>> getLeafNodes() {
        Set<TreeNode<T>> leaves = new HashSet<TreeNode<T>>();
        getLeafNodesRecursive(this, leaves);
        return leaves;
    }

    private void getLeafNodesRecursive(TreeNode<T> root, Set<TreeNode<T>> leaves) {
        if (root.getChildren().size() == 0) {
            leaves.add(root);
        } else{
            for (TreeNode<T> leaf : root.getChildren()) {
                getLeafNodesRecursive(leaf, leaves);
            }
        }
    }
}

