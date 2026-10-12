package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Tree;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomLevelGeneratorTest {

    private static boolean isInside(GridPoint2 point, int width, int height) {
        return point.x >= 0 && point.x < width && point.y >= 0 && point.y < height;
    }

    @Test
    void generatesRequestedNumberOfTrees() {
        Level level = new RandomLevelGenerator(10, 8, 15, new Random(1)).generate();

        assertEquals(15, level.getTrees().size());
    }

    @Test
    void everythingIsInsideAndNothingOverlaps() {
        for (long seed = 0; seed < 100; seed++) {
            Level level = new RandomLevelGenerator(10, 8, 20, new Random(seed)).generate();

            Set<GridPoint2> trees = new HashSet<>();
            for (Tree tree : level.getTrees()) {
                assertTrue(isInside(tree.getCoordinates(), 10, 8));
                trees.add(tree.getCoordinates());
            }
            assertEquals(20, trees.size());
            assertTrue(isInside(level.getPlayerPosition(), 10, 8));
            assertFalse(trees.contains(level.getPlayerPosition()));
        }
    }

    @Test
    void sameSeedGivesSameLevel() {
        Level first = new RandomLevelGenerator(10, 8, 5, new Random(42)).generate();
        Level second = new RandomLevelGenerator(10, 8, 5, new Random(42)).generate();

        assertEquals(first.getPlayerPosition(), second.getPlayerPosition());
        for (int i = 0; i < 5; i++) {
            assertEquals(first.getTrees().get(i).getCoordinates(), second.getTrees().get(i).getCoordinates());
        }
    }

    @Test
    void canFillAllCellsExceptPlayer() {
        Level level = new RandomLevelGenerator(3, 2, 5, new Random(7)).generate();

        assertEquals(5, level.getTrees().size());
    }

    @Test
    void failsWhenTreesDoNotFit() {
        assertThrows(IllegalArgumentException.class, () -> new RandomLevelGenerator(3, 2, 6, new Random()));
    }

    @Test
    void failsOnNegativeTreeCount() {
        assertThrows(IllegalArgumentException.class, () -> new RandomLevelGenerator(3, 2, -1, new Random()));
    }
}
