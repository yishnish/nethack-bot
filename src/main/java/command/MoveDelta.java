package command;

import locations.Coordinates;

import java.util.Objects;

public class MoveDelta {

    private int dy;
    private int dx;

    private Coordinates from;

    public MoveDelta(Coordinates from){
        this.from = from;
    }

    public static MoveDelta from(Coordinates from) {
        return new MoveDelta(from);
    }

    public MoveDelta to(Coordinates destination) {
        this.dy = destination.getRow() - from.getRow();
        this.dx = destination.getColumn() - from.getColumn();
        return this;
    }

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
