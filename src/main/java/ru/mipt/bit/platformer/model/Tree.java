package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

public class Tree implements Obstacle {

    private final GridPoint2 coordinates;

    public Tree(GridPoint2 coordinates) {
        this.coordinates = coordinates;
    }

    public GridPoint2 getCoordinates() {
        return coordinates;
    }

    @Override
    public boolean occupies(GridPoint2 point) {
        return coordinates.equals(point);
    }
}
