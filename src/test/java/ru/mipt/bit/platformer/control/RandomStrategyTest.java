package ru.mipt.bit.platformer.control;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomStrategyTest {

    private final Command up = () -> { };
    private final Command down = () -> { };
    private final Command shoot = () -> { };
    private final List<Command> commands = Arrays.asList(up, down, shoot);

    @Test
    void returnsOneOfGivenCommands() {
        RandomStrategy strategy = new RandomStrategy(commands, new Random(1));

        List<Command> result = strategy.getCommands();

        assertEquals(1, result.size());
        assertTrue(commands.contains(result.get(0)));
    }

    @Test
    void choosesEveryCommandSometimes() {
        RandomStrategy strategy = new RandomStrategy(commands, new Random(1));
        List<Command> chosen = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            chosen.addAll(strategy.getCommands());
        }

        assertEquals(new HashSet<>(commands), new HashSet<>(chosen));
    }

    @Test
    void sameSeedGivesSameCommands() {
        RandomStrategy strategy = new RandomStrategy(commands, new Random(5));
        RandomStrategy otherStrategy = new RandomStrategy(commands, new Random(5));

        for (int i = 0; i < 10; i++) {
            assertEquals(strategy.getCommands(), otherStrategy.getCommands());
        }
    }
}
