package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameFieldTest {

    @Test
    void cellWithTreeIsNotFree() {
        GameField field = new GameField(Arrays.asList(new Tree(new GridPoint2(1, 3)), new Tree(new GridPoint2(4, 4))));

        assertFalse(field.isFree(new GridPoint2(1, 3)));
        assertFalse(field.isFree(new GridPoint2(4, 4)));
    }

    @Test
    void cellWithoutTreeIsFree() {
        GameField field = new GameField(Collections.singletonList(new Tree(new GridPoint2(1, 3))));

        assertTrue(field.isFree(new GridPoint2(1, 2)));
    }

    @Test
    void emptyFieldIsFree() {
        GameField field = new GameField(Collections.emptyList());

        assertTrue(field.isFree(new GridPoint2(0, 0)));
    }
}
