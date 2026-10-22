package ru.mipt.bit.platformer.control;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Direction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MoveCommandTest {

    @Test
    void executeMovesInDirection() {
        List<Direction> moves = new ArrayList<>();

        new MoveCommand(moves::add, Direction.DOWN).execute();

        assertEquals(Collections.singletonList(Direction.DOWN), moves);
    }
}
