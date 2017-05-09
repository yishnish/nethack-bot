package command;

import locations.Coordinates;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class MoveDeltaTest {

    @Test
    public void testCalculatingDeltasForTwoLocationsToTheLeft() {
        Coordinates from = new Coordinates(1, 1);
        Coordinates to = new Coordinates(1, 0);

        assertThat(MoveDelta.from(from).to(to), equalTo(new MoveDelta(0, -1)));
    }

    @Test
    public void testCalculatingDeltasForTwoLocationsToTheRight() {
        Coordinates from = new Coordinates(1, 1);
        Coordinates to = new Coordinates(1, 2);

        assertThat(MoveDelta.from(from).to(to), equalTo(new MoveDelta(0, 1)));
    }

    @Test
    public void testCalculatingDeltasForTwoLocationsToTheUp() {
        Coordinates from = new Coordinates(1, 0);
        Coordinates to = new Coordinates(0, 0);

        assertThat(MoveDelta.from(from).to(to), equalTo(new MoveDelta(-1, 0)));
    }

    @Test
    public void testCalculatingDeltasForTwoLocationsToTheDown() {
        Coordinates from = new Coordinates(1, 0);
        Coordinates to = new Coordinates(2, 0);

        assertThat(MoveDelta.from(from).to(to), equalTo(new MoveDelta(1, 0)));
    }

    @Test
    public void testCalculatingDeltasForTwoLocationsToTheDiagonalUpLeft() {
        Coordinates from = new Coordinates(1, 1);
        Coordinates to = new Coordinates(0, 0);

        assertThat(MoveDelta.from(from).to(to), equalTo(new MoveDelta(-1, -1)));
    }

    @Test
    public void testCalculatingDeltasForTwoLocationsToTheDiagonalUpRight() {
        Coordinates from = new Coordinates(1, 1);
        Coordinates to = new Coordinates(0, 2);

        assertThat(MoveDelta.from(from).to(to), equalTo(new MoveDelta(-1, 1)));
    }

    @Test
    public void testCalculatingDeltasForTwoLocationsToTheDiagonalDownLeft() {
        Coordinates from = new Coordinates(1, 1);
        Coordinates to = new Coordinates(2, 0);

        assertThat(MoveDelta.from(from).to(to), equalTo(new MoveDelta(1, -1)));
    }

    @Test
    public void testCalculatingDeltasForTwoLocationsToTheDiagonalDownRight() {
        Coordinates from = new Coordinates(1, 1);
        Coordinates to = new Coordinates(2, 2);

        assertThat(MoveDelta.from(from).to(to), equalTo(new MoveDelta(1, 1)));
    }

}
