package command;

import java.util.Objects;

public class MoveDelta {

    private int dy;
    private int dx;

    public MoveDelta(int dy, int dx) {
        this.dy = dy;
        this.dx = dx;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoveDelta moveDelta = (MoveDelta) o;
        return Objects.equals(dy, moveDelta.dy) &&
                Objects.equals(dx, moveDelta.dx);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dy, dx);
    }
}
