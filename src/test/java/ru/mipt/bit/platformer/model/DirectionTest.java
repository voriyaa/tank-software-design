package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DirectionTest {

    @Test
    void applyShiftsPointByDirectionVector() {
        GridPoint2 point = new GridPoint2(2, 2);

        assertEquals(new GridPoint2(2, 3), Direction.UP.apply(point));
        assertEquals(new GridPoint2(1, 2), Direction.LEFT.apply(point));
        assertEquals(new GridPoint2(2, 1), Direction.DOWN.apply(point));
        assertEquals(new GridPoint2(3, 2), Direction.RIGHT.apply(point));
    }

    @Test
    void applyDoesNotChangeOriginalPoint() {
        GridPoint2 point = new GridPoint2(2, 2);

        Direction.UP.apply(point);

        assertEquals(new GridPoint2(2, 2), point);
    }

    @Test
    void rotation() {
        assertEquals(90f, Direction.UP.getRotation());
        assertEquals(-180f, Direction.LEFT.getRotation());
        assertEquals(-90f, Direction.DOWN.getRotation());
        assertEquals(0f, Direction.RIGHT.getRotation());
    }
}
