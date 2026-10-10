package ru.mipt.bit.platformer.input;

import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameField;
import ru.mipt.bit.platformer.model.Tank;

public class MoveAction implements Action {

    private final Tank tank;
    private final Direction direction;
    private final GameField field;

    public MoveAction(Tank tank, Direction direction, GameField field) {
        this.tank = tank;
        this.direction = direction;
        this.field = field;
    }

    @Override
    public void execute() {
        tank.move(direction, field);
    }
}
