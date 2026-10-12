package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TankTest {

    private static final float EPSILON = 1e-6f;

    private GameField field;
    private Tank tank;

    @BeforeEach
    void setUp() {
        field = new GameField(Collections.singletonList(new Tree(new GridPoint2(1, 3))));
        tank = new Tank(new GridPoint2(1, 1), 0.4f, field);
    }

    @Test
    void initialState() {
        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertEquals(new GridPoint2(1, 1), tank.getDestinationCoordinates());
        assertEquals(1f, tank.getMovementProgress());
        assertEquals(0f, tank.getRotation());
    }

    @Test
    void doesNotKeepReferenceToStartPoint() {
        GridPoint2 start = new GridPoint2(1, 1);
        Tank tank = new Tank(start, 0.4f, field);

        start.set(5, 5);

        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
    }

    @Test
    void moveToFreeCellStartsMovement() {
        tank.move(Direction.RIGHT);

        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertEquals(new GridPoint2(2, 1), tank.getDestinationCoordinates());
        assertEquals(0f, tank.getMovementProgress());
        assertEquals(0f, tank.getRotation());
    }

    @Test
    void moveToTreeOnlyRotates() {
        tank.move(Direction.UP);
        tank.update(0.4f);
        tank.move(Direction.UP);

        assertEquals(new GridPoint2(1, 2), tank.getDestinationCoordinates());
        assertEquals(1f, tank.getMovementProgress());
        assertEquals(90f, tank.getRotation());
    }

    @Test
    void moveIsIgnoredWhileMoving() {
        tank.move(Direction.RIGHT);
        tank.move(Direction.DOWN);

        assertEquals(new GridPoint2(2, 1), tank.getDestinationCoordinates());
        assertEquals(0f, tank.getRotation());
    }

    @Test
    void updateAdvancesProgress() {
        tank.move(Direction.LEFT);
        tank.update(0.2f);

        assertEquals(0.5f, tank.getMovementProgress(), EPSILON);
        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
    }

    @Test
    void movementSpeedIsTakenFromConstructor() {
        Tank slowTank = new Tank(new GridPoint2(1, 1), 0.8f, field);

        slowTank.move(Direction.RIGHT);
        slowTank.update(0.2f);

        assertEquals(0.25f, slowTank.getMovementProgress(), EPSILON);
    }

    @Test
    void updateFinishesMovement() {
        tank.move(Direction.LEFT);
        tank.update(0.2f);
        tank.update(0.3f);

        assertEquals(1f, tank.getMovementProgress());
        assertEquals(new GridPoint2(0, 1), tank.getCoordinates());
        assertEquals(-180f, tank.getRotation());
    }
}
