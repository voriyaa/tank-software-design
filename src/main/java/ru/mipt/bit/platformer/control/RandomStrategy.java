package ru.mipt.bit.platformer.control;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomStrategy implements ControlStrategy {

    private final List<Command> commands;
    private final Random random;

    public RandomStrategy(List<Command> commands, Random random) {
        this.commands = commands;
        this.random = random;
    }

    @Override
    public List<Command> getCommands() {
        return Collections.singletonList(commands.get(random.nextInt(commands.size())));
    }
}
