package ru.mipt.bit.platformer.input;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Direction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MoveActionTest {

    @Test
    void executeMovesInDirection() {
        List<Direction> moves = new ArrayList<>();

        new MoveAction(moves::add, Direction.DOWN).execute();

        assertEquals(Collections.singletonList(Direction.DOWN), moves);
    }
}
