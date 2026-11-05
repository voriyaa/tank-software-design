package ru.mipt.bit.platformer.control;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Direction;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RandomStrategyTest {

    private final List<Direction> moves = new ArrayList<>();

    @Test
    void returnsOneMoveCommand() {
        RandomStrategy strategy = new RandomStrategy(moves::add, new Random(1));

        List<Command> commands = strategy.getCommands();
        assertEquals(1, commands.size());

        commands.get(0).execute();
        assertEquals(1, moves.size());
    }

    @Test
    void choosesEveryDirection() {
        RandomStrategy strategy = new RandomStrategy(moves::add, new Random(1));

        for (int i = 0; i < 100; i++) {
            strategy.getCommands().get(0).execute();
        }

        assertEquals(EnumSet.allOf(Direction.class), new HashSet<>(moves));
    }

    @Test
    void sameSeedGivesSameDirections() {
        List<Direction> otherMoves = new ArrayList<>();
        RandomStrategy strategy = new RandomStrategy(moves::add, new Random(5));
        RandomStrategy otherStrategy = new RandomStrategy(otherMoves::add, new Random(5));

        for (int i = 0; i < 10; i++) {
            strategy.getCommands().get(0).execute();
            otherStrategy.getCommands().get(0).execute();
        }

        assertEquals(moves, otherMoves);
    }
}
