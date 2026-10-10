package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;

public class Tank {

    private static final float MOVEMENT_SPEED = 0.4f;

    private final GridPoint2 coordinates;
    private final GridPoint2 destinationCoordinates;
    private float movementProgress = 1f;
    private float rotation = 0f;

    public Tank(GridPoint2 coordinates) {
        this.coordinates = new GridPoint2(coordinates);
        destinationCoordinates = new GridPoint2(coordinates);
    }

    public void move(Direction direction, GameField field) {
        if (!isEqual(movementProgress, 1f)) {
            return;
        }
        GridPoint2 target = direction.apply(coordinates);
        if (field.isFree(target)) {
            destinationCoordinates.set(target);
            movementProgress = 0f;
        }
        rotation = direction.getRotation();
    }

    public void update(float deltaTime) {
        movementProgress = continueProgress(movementProgress, deltaTime, MOVEMENT_SPEED);
        if (isEqual(movementProgress, 1f)) {
            coordinates.set(destinationCoordinates);
        }
    }

    public GridPoint2 getCoordinates() {
        return coordinates;
    }

    public GridPoint2 getDestinationCoordinates() {
        return destinationCoordinates;
    }

    public float getMovementProgress() {
        return movementProgress;
    }

    public float getRotation() {
        return rotation;
    }
}
