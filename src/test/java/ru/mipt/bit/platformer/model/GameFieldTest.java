package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameFieldTest {

    private final GameField field = new GameField(10, 8);
    private final List<GameObject> added = new ArrayList<>();
    private final List<GameObject> removed = new ArrayList<>();

    @BeforeEach
    void setUp() {
        field.addListener(new FieldListener() {
            @Override
            public void onObjectAdded(GameObject object) {
                added.add(object);
            }

            @Override
            public void onObjectRemoved(GameObject object) {
                removed.add(object);
            }
        });
    }

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
        field.add(new Tree(new GridPoint2(1, 3)));

        assertFalse(field.isFree(new GridPoint2(1, 3)));
        assertTrue(field.isFree(new GridPoint2(1, 2)));
    }

    @Test
    void movingTankBlocksBothCells() {
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f, 3, field);
        field.add(tank);

        tank.move(Direction.RIGHT);

        assertFalse(field.isFree(new GridPoint2(1, 1)));
        assertFalse(field.isFree(new GridPoint2(2, 1)));
    }

    @Test
    void addNotifiesListener() {
        Tree tree = new Tree(new GridPoint2(1, 3));

        field.add(tree);

        assertTrue(field.contains(tree));
        assertEquals(Collections.singletonList(tree), added);
    }

    @Test
    void removeNotifiesListener() {
        Tree tree = new Tree(new GridPoint2(1, 3));
        field.add(tree);

        field.remove(tree);

        assertFalse(field.contains(tree));
        assertEquals(Collections.singletonList(tree), removed);
    }

    @Test
    void removingTwiceNotifiesOnce() {
        Tree tree = new Tree(new GridPoint2(1, 3));
        field.add(tree);

        field.remove(tree);
        field.remove(tree);

        assertEquals(1, removed.size());
    }

    @Test
    void liveMovesEveryObject() {
        Tank first = new Tank(new GridPoint2(1, 1), 0.4f, 3, field);
        Tank second = new Tank(new GridPoint2(5, 5), 0.4f, 3, field);
        field.add(first);
        field.add(second);
        first.move(Direction.RIGHT);
        second.move(Direction.UP);

        field.live(0.4f);

        assertEquals(new GridPoint2(2, 1), first.getCoordinates());
        assertEquals(new GridPoint2(5, 6), second.getCoordinates());
    }

    @Test
    void objectRemovedDuringLiveDoesNotLive() {
        Bullet first = new Bullet(new GridPoint2(2, 1), Direction.RIGHT, 0.1f, 1, field);
        Bullet second = new Bullet(new GridPoint2(3, 1), Direction.RIGHT, 0.1f, 1, field);
        field.add(first);
        field.add(second);

        field.live(0f);

        assertEquals(Arrays.asList(second, first), removed);
        assertEquals(new GridPoint2(3, 1), second.getDestinationCoordinates());
    }

    @Test
    void hitDamagesObjectsInCell() {
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f, 3, field);
        field.add(tank);

        field.hit(new GridPoint2(1, 1), 2);
        field.hit(new GridPoint2(5, 5), 2);

        assertEquals(1, tank.getHealth());
    }
}
