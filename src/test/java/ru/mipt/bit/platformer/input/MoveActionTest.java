package ru.mipt.bit.platformer.input;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameField;
import ru.mipt.bit.platformer.model.Tank;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MoveActionTest {

    @Test
    void executeMovesTankInDirection() {
        Tank tank = new Tank(new GridPoint2(1, 1));
        GameField field = new GameField(Collections.emptyList());

        new MoveAction(tank, Direction.DOWN, field).execute();

        assertEquals(new GridPoint2(1, 0), tank.getDestinationCoordinates());
        assertEquals(-90f, tank.getRotation());
    }
}
