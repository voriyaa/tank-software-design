package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TreeTest {

    @Test
    void storesCoordinates() {
        Tree tree = new Tree(new GridPoint2(1, 3));

        assertEquals(new GridPoint2(1, 3), tree.getCoordinates());
    }
}
