package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;

public class Bullet implements GameObject {

    private final Direction direction;
    private final float speed;
    private final int damage;
    private final GameField field;
    private final GridPoint2 coordinates;
    private final GridPoint2 destinationCoordinates;
    private float movementProgress = 1f;

    public Bullet(GridPoint2 coordinates, Direction direction, float speed, int damage, GameField field) {
        this.direction = direction;
        this.speed = speed;
        this.damage = damage;
        this.field = field;
        this.coordinates = new GridPoint2(coordinates);
        destinationCoordinates = new GridPoint2(coordinates);
    }

    @Override
    public void live(float deltaTime) {
        movementProgress = continueProgress(movementProgress, deltaTime, speed);
        if (!isEqual(movementProgress, 1f)) {
            return;
        }
        coordinates.set(destinationCoordinates);

        GridPoint2 next = direction.apply(coordinates);
        if (field.isFree(next)) {
            destinationCoordinates.set(next);
            movementProgress = 0f;
        } else {
            field.hit(next, damage);
            field.remove(this);
        }
    }

    @Override
    public boolean occupies(GridPoint2 point) {
        return coordinates.equals(point) || destinationCoordinates.equals(point);
    }

    @Override
    public void hit(int damage) {
        field.remove(this);
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

    public Direction getDirection() {
        return direction;
    }
}
