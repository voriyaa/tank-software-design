package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Movable;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomStrategy implements ControlStrategy {

    private final Movable movable;
    private final Random random;

    public RandomStrategy(Movable movable, Random random) {
        this.movable = movable;
        this.random = random;
    }

    @Override
    public List<Command> getCommands() {
        Direction[] directions = Direction.values();
        Direction direction = directions[random.nextInt(directions.length)];
        return Collections.singletonList(new MoveCommand(movable, direction));
    }
}
