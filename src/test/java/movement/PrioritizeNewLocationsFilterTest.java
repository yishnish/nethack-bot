package movement;

import locations.Coordinates;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class PrioritizeNewLocationsFilterTest {

    PrioritizeNewLocationsFilter filter;

    @Before
    public void setUp() {
        filter = new PrioritizeNewLocationsFilter();
    }

    @Test
    public void testFiltersPotentialMoveCoordinatesToOnlyHaveTheLeastVisitedLocation() {
        Coordinates once = new Coordinates(0, 1);
        Coordinates twice = new Coordinates(0, 2);

        List<Coordinates> visited = new ArrayList<Coordinates>(Arrays.asList(once, twice, twice));
        for (Coordinates coordinates : visited) {
            filter.markVisited(coordinates);
        }
        Set<Coordinates> moveLocationCandidates = new HashSet<Coordinates>(Arrays.asList(once, twice));

        assertThat(filter.filter(moveLocationCandidates),
                equalTo(((Set<Coordinates>)(new HashSet<Coordinates>(singletonList(once))))));
    }

    @Test
    public void ifThereAreMultipleLeastVisitedLocationsWithSameVisitedCountItReturnsThemAll(){
        Coordinates twiceOne = new Coordinates(0, 1);
        Coordinates twiceTwo = new Coordinates(0, 2);

        List<Coordinates> visited = new ArrayList<Coordinates>(Arrays.asList(twiceOne, twiceOne, twiceTwo, twiceTwo));
        for (Coordinates coordinates : visited) {
            filter.markVisited(coordinates);
        }
        Set<Coordinates> moveLocationCandidates = new HashSet<Coordinates>(Arrays.asList(twiceOne, twiceTwo));

        assertThat(filter.filter(moveLocationCandidates),
                equalTo(((Set<Coordinates>)(new HashSet<Coordinates>(Arrays.asList(twiceOne, twiceTwo))))));
    }
}
