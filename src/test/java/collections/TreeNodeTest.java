package collections;

import locations.Coordinates;
import org.junit.Test;

import static collections.CollectionsHelpers.setOf;
import static locations.Coordinates.coordinates;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

public class TreeNodeTest {

    @Test
    public void testHasData(){
        TreeNode<Coordinates> root = new TreeNode<Coordinates>(coordinates(0, 0));
        assertThat(root.getData(), equalTo(coordinates(0, 0)));
    }

    @Test
    public void testAddingAChild() {
        TreeNode<Coordinates> root = new TreeNode<Coordinates>(coordinates(0, 0));
        TreeNode<Coordinates> child = new TreeNode<Coordinates>(coordinates(0, 1));

        root.addChild(child);

        assertThat(root.getChildren(), contains((TreeNode)child));
    }

    @Test
    public void testFindingItsParent(){
        TreeNode<Coordinates> root = new TreeNode<Coordinates>(coordinates(0, 0));
        TreeNode<Coordinates> child = new TreeNode<Coordinates>(coordinates(0, 1));

        root.addChild(child);

        assertThat(child.getParent(), equalTo(root));
    }

    @Test
    public void testFindingLengthFromLeafToRoot(){
        TreeNode<Coordinates> root = new TreeNode<Coordinates>(coordinates(0, 0));
        TreeNode<Coordinates> child = new TreeNode<Coordinates>(coordinates(0, 1));
        TreeNode<Coordinates> anotherChild = new TreeNode<Coordinates>(coordinates(0, 2));
        TreeNode<Coordinates> grandchild = new TreeNode<Coordinates>(coordinates(0, 3));

        assertThat(root.branchLength(), equalTo(1));

        root.addChild(child);

        assertThat(child.branchLength(), equalTo(2));

        root.addChild(anotherChild);

        assertThat(anotherChild.branchLength(), equalTo(2));

        child.addChild(grandchild);

        assertThat(grandchild.branchLength(), equalTo(3));
    }

    @Test
    public void testKnowsIfAnyBranchNodeContainsAParticularDataValue() {
        TreeNode<Coordinates> root = new TreeNode<Coordinates>(coordinates(0, 0));

        assertThat(root.branchContains(coordinates(0, 0)), is(true));
        assertThat(root.branchContains(coordinates(0, 1)), is(false));

        TreeNode<Coordinates> child = new TreeNode<Coordinates>(coordinates(0, 1));
        root.addChild(child);

        assertThat(child.branchContains(coordinates(0, 0)), is(true));
        assertThat(child.branchContains(coordinates(0, 1)), is(true));
    }

    @Test
    public void testGettingAllLeafNodes(){
        TreeNode<Coordinates> root = new TreeNode<Coordinates>(coordinates(0, 0));
        TreeNode<Coordinates> child1 = new TreeNode<Coordinates>(coordinates(0, 2));
        TreeNode<Coordinates> child2 = new TreeNode<Coordinates>(coordinates(0, 1));
        TreeNode<Coordinates> grandchild = new TreeNode<Coordinates>(coordinates(0, 2));

        root.addChild(child1);
        root.addChild(child2);
        child2.addChild(grandchild);

        assertThat(root.getLeafNodes(), equalTo(setOf(child1, grandchild)));
    }

    @Test
    public void testGettingAllBranchesWhereLeafNodeMatchesACriteria(){
        TreeNode<Coordinates> root = new TreeNode<Coordinates>(coordinates(0, 0));
        TreeNode<Coordinates> child1 = new TreeNode<Coordinates>(coordinates(0, 2));
        TreeNode<Coordinates> child2 = new TreeNode<Coordinates>(coordinates(0, 1));
        TreeNode<Coordinates> child3 = new TreeNode<Coordinates>(coordinates(0, 3));
        TreeNode<Coordinates> grandchild = new TreeNode<Coordinates>(coordinates(0, 2));

        root.addChild(child1);
        root.addChild(child2);
        root.addChild(child3);
        child2.addChild(grandchild);

        assertThat(root.getCoordinateStepsForAllPathsToLocation(coordinates(0, 2)), equalTo(
                setOf(
                        setOf(root.getData(), child1.getData()),
                        setOf(root.getData(), child2.getData(), grandchild.getData())
                )
        ));
    }
}
