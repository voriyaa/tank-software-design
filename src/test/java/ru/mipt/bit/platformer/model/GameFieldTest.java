package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameFieldTest {

    private final GameField field = new GameField(10, 8);

    @Test
    void emptyCellsInsideFieldAreFree() {
        assertTrue(field.isFree(new GridPoint2(0, 0)));
        assertTrue(field.isFree(new GridPoint2(9, 7)));
    }

    @Test
    void cellsOutsideFieldAreNotFree() {
        assertFalse(field.isFree(new GridPoint2(-1, 0)));
        assertFalse(field.isFree(new GridPoint2(0, -1)));
        assertFalse(field.isFree(new GridPoint2(10, 0)));
        assertFalse(field.isFree(new GridPoint2(0, 8)));
    }

    @Test
    void cellWithTreeIsNotFree() {
        field.addObstacle(new Tree(new GridPoint2(1, 3)));

        assertFalse(field.isFree(new GridPoint2(1, 3)));
        assertTrue(field.isFree(new GridPoint2(1, 2)));
    }

    @Test
    void anyObstacleBlocksCell() {
        Obstacle wall = point -> point.equals(new GridPoint2(2, 2));
        field.addObstacle(wall);

        assertFalse(field.isFree(new GridPoint2(2, 2)));
    }

    @Test
    void movingTankBlocksBothCells() {
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f, field);
        field.addObstacle(tank);

        tank.move(Direction.RIGHT);

        assertFalse(field.isFree(new GridPoint2(1, 1)));
        assertFalse(field.isFree(new GridPoint2(2, 1)));
    }
}
