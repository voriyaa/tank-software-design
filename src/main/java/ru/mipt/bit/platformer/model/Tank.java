package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.model.state.HeavyState;
import ru.mipt.bit.platformer.model.state.LightState;
import ru.mipt.bit.platformer.model.state.MediumState;
import ru.mipt.bit.platformer.model.state.TankState;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;

/** Domain entity: tank. */
public class Tank implements GameObject, Movable, Shooter {

    private static final float RELOAD_TIME = 1f;
    private static final float BULLET_SPEED = 0.1f;
    private static final int BULLET_DAMAGE = 30;
    private static final float LIGHT_HEALTH = 0.7f;
    private static final float HEAVY_HEALTH = 0.15f;

    private final float movementSpeed;
    private final int maxHealth;
    private final GameField field;
    private final GridPoint2 coordinates;
    private final GridPoint2 destinationCoordinates;
    private float movementProgress = 1f;
    private Direction direction = Direction.RIGHT;
    private int health;
    private float timeSinceShot = RELOAD_TIME;
    private TankState state = new LightState();

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
        if (!isAlive() || !state.canShoot() || timeSinceShot < RELOAD_TIME) {
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
        movementProgress = continueProgress(movementProgress, deltaTime * state.getSpeed(), movementSpeed);
        if (isEqual(movementProgress, 1f)) {
            coordinates.set(destinationCoordinates);
        }
        timeSinceShot += deltaTime * state.getReloadSpeed();
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
        } else {
            updateState();
        }
    }

    private void updateState() {
        float healthPart = (float) health / maxHealth;
        if (healthPart > LIGHT_HEALTH) {
            state = new LightState();
        } else if (healthPart >= HEAVY_HEALTH) {
            state = new MediumState();
        } else {
            state = new HeavyState();
        }
    }

    public TankState getState() {
        return state;
    }

    public void setState(TankState state) {
        this.state = state;
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
