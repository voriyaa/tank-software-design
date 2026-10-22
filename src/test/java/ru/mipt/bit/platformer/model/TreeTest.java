package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TreeTest {

    @Test
    void storesCoordinates() {
        Tree tree = new Tree(new GridPoint2(1, 3));

        assertEquals(new GridPoint2(1, 3), tree.getCoordinates());
    }

    @Test
    void occupiesOnlyItsCell() {
        Tree tree = new Tree(new GridPoint2(1, 3));

        assertTrue(tree.occupies(new GridPoint2(1, 3)));
        assertFalse(tree.occupies(new GridPoint2(1, 2)));
    }
}
