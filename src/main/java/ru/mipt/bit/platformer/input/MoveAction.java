package ru.mipt.bit.platformer.input;

import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Movable;

public class MoveAction implements Action {

    private final Movable movable;
    private final Direction direction;

    public MoveAction(Movable movable, Direction direction) {
        this.movable = movable;
        this.direction = direction;
    }

    @Override
    public void execute() {
        movable.move(direction);
    }
}
