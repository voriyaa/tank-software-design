package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;

public class Tank implements GameObject, Movable, Shooter {

    private static final float RELOAD_TIME = 1f;
    private static final float BULLET_SPEED = 0.1f;
    private static final int BULLET_DAMAGE = 25;

    private final float movementSpeed;
    private final int maxHealth;
    private final GameField field;
    private final GridPoint2 coordinates;
    private final GridPoint2 destinationCoordinates;
    private float movementProgress = 1f;
    private Direction direction = Direction.RIGHT;
    private int health;
    private float timeSinceShot = RELOAD_TIME;

    public Tank(GridPoint2 coordinates, float movementSpeed, int health, GameField field) {
        this.movementSpeed = movementSpeed;
        this.health = health;
        maxHealth = health;
        this.field = field;
        this.coordinates = new GridPoint2(coordinates);
        destinationCoordinates = new GridPoint2(coordinates);
    }

    @Override
    public void move(Direction direction) {
        if (!isAlive() || !isEqual(movementProgress, 1f)) {
            return;
        }
        GridPoint2 target = direction.apply(coordinates);
        if (field.isFree(target)) {
            destinationCoordinates.set(target);
            movementProgress = 0f;
        }
        this.direction = direction;
    }

    @Override
    public void shoot() {
        if (!isAlive() || timeSinceShot < RELOAD_TIME) {
            return;
        }
        timeSinceShot = 0f;

        GridPoint2 start = direction.apply(coordinates);
        boolean reservedByMe = start.equals(destinationCoordinates);
        if (reservedByMe || field.isFree(start)) {
            field.add(new Bullet(start, direction, BULLET_SPEED, BULLET_DAMAGE, field));
        } else {
            field.hit(start, BULLET_DAMAGE);
        }
    }

    @Override
    public void live(float deltaTime) {
        movementProgress = continueProgress(movementProgress, deltaTime, movementSpeed);
        if (isEqual(movementProgress, 1f)) {
            coordinates.set(destinationCoordinates);
        }
        timeSinceShot += deltaTime;
    }

    @Override
    public boolean occupies(GridPoint2 point) {
        return coordinates.equals(point) || destinationCoordinates.equals(point);
    }

    @Override
    public void hit(int damage) {
        health -= damage;
        if (!isAlive()) {
            field.remove(this);
        }
    }

    public boolean isAlive() {
        return health > 0;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public GridPoint2 getCoordinates() {
        return new GridPoint2(coordinates);
    }

    public GridPoint2 getDestinationCoordinates() {
        return new GridPoint2(destinationCoordinates);
    }

    public float getMovementProgress() {
        return movementProgress;
    }

    public float getRotation() {
        return direction.getRotation();
    }
}
