package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Tree;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomLevelGeneratorTest {

    private static boolean isInside(GridPoint2 point, int width, int height) {
        return point.x >= 0 && point.x < width && point.y >= 0 && point.y < height;
    }

    @Test
    void generatesRequestedNumberOfTreesAndTanks() {
        Level level = new RandomLevelGenerator(10, 8, 15, 3, new Random(1)).generate();

        assertEquals(15, level.getTrees().size());
        assertEquals(3, level.getAiTankPositions().size());
    }

    @Test
    void everythingIsInsideAndNothingOverlaps() {
        for (long seed = 0; seed < 100; seed++) {
            Level level = new RandomLevelGenerator(10, 8, 20, 5, new Random(seed)).generate();

            Set<GridPoint2> cells = new HashSet<>();
            cells.add(level.getPlayerPosition());
            for (Tree tree : level.getTrees()) {
                cells.add(tree.getCoordinates());
            }
            cells.addAll(level.getAiTankPositions());

            assertEquals(1 + 20 + 5, cells.size());
            for (GridPoint2 cell : cells) {
                assertTrue(isInside(cell, 10, 8));
            }
        }
    }

    @Test
    void sameSeedGivesSameLevel() {
        Level first = new RandomLevelGenerator(10, 8, 5, 2, new Random(42)).generate();
        Level second = new RandomLevelGenerator(10, 8, 5, 2, new Random(42)).generate();

        assertEquals(first.getPlayerPosition(), second.getPlayerPosition());
        for (int i = 0; i < 5; i++) {
            assertEquals(first.getTrees().get(i).getCoordinates(), second.getTrees().get(i).getCoordinates());
        }
        assertEquals(first.getAiTankPositions(), second.getAiTankPositions());
    }

    @Test
    void canFillAllCellsExceptPlayer() {
        Level level = new RandomLevelGenerator(3, 2, 3, 2, new Random(7)).generate();

        assertEquals(3, level.getTrees().size());
        assertEquals(2, level.getAiTankPositions().size());
    }

    @Test
    void levelWithoutAiTanks() {
        Level level = new RandomLevelGenerator(10, 8, 5, 0, new Random(1)).generate();

        assertTrue(level.getAiTankPositions().isEmpty());
    }

    @Test
    void failsWhenEverythingDoesNotFit() {
        assertThrows(IllegalArgumentException.class, () -> new RandomLevelGenerator(3, 2, 3, 3, new Random()));
    }

    @Test
    void failsOnNegativeCounts() {
        assertThrows(IllegalArgumentException.class, () -> new RandomLevelGenerator(3, 2, -1, 0, new Random()));
        assertThrows(IllegalArgumentException.class, () -> new RandomLevelGenerator(3, 2, 0, -1, new Random()));
    }
}
