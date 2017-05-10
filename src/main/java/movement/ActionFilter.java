package movement;

import locations.Coordinates;

import java.util.Set;

public interface ActionFilter<T> {

    Set<T> filter(Set<T> toFilter);

    void markVisited(Coordinates coordinates);
}
