package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Tree;

import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileLevelGeneratorTest {

    private static Level generate(String text) {
        return new FileLevelGenerator(new StringReader(text), 10, 8).generate();
    }

    private static Set<GridPoint2> treePositions(Level level) {
        Set<GridPoint2> positions = new HashSet<>();
        for (Tree tree : level.getTrees()) {
            positions.add(tree.getCoordinates());
        }
        return positions;
    }

    @Test
    void readsLevelFromTask() {
        Level level = generate(
                "___T__T___\n" +
                "__TT__TTTT\n" +
                "_________T\n" +
                "TTTT__T__T\n" +
                "_____X____\n" +
                "__________\n");

        assertEquals(10, level.getWidth());
        assertEquals(6, level.getHeight());
        assertEquals(new GridPoint2(5, 1), level.getPlayerPosition());
        assertEquals(15, level.getTrees().size());
        assertTrue(treePositions(level).contains(new GridPoint2(3, 5)));
        assertTrue(treePositions(level).contains(new GridPoint2(0, 2)));
        assertTrue(treePositions(level).contains(new GridPoint2(9, 3)));
    }

    @Test
    void bottomLineHasZeroY() {
        Level level = generate("T_\n_X\n");

        assertEquals(new GridPoint2(1, 0), level.getPlayerPosition());
        assertEquals(new GridPoint2(0, 1), level.getTrees().get(0).getCoordinates());
    }

    @Test
    void levelWithoutTrees() {
        Level level = generate("X__\n");

        assertEquals(new GridPoint2(0, 0), level.getPlayerPosition());
        assertTrue(level.getTrees().isEmpty());
    }

    @Test
    void ignoresEmptyLines() {
        Level level = generate("\nT_\n\n_X\n\n");

        assertEquals(new GridPoint2(1, 0), level.getPlayerPosition());
        assertEquals(new GridPoint2(0, 1), level.getTrees().get(0).getCoordinates());
    }

    @Test
    void failsWithoutPlayer() {
        assertThrows(IllegalArgumentException.class, () -> generate("__T\n___\n"));
    }

    @Test
    void failsWithTwoPlayers() {
        assertThrows(IllegalArgumentException.class, () -> generate("X__\n__X\n"));
    }

    @Test
    void failsOnUnknownSymbol() {
        assertThrows(IllegalArgumentException.class, () -> generate("X_?\n"));
    }

    @Test
    void failsWhenLevelIsWiderThanMap() {
        assertThrows(IllegalArgumentException.class, () -> generate("X__________\n"));
    }

    @Test
    void failsWhenLevelIsHigherThanMap() {
        assertThrows(IllegalArgumentException.class, () -> generate("X\n_\n_\n_\n_\n_\n_\n_\n_\n"));
    }

    @Test
    void levelOfMapSizeIsAccepted() {
        Level level = generate("X_________\n_\n_\n_\n_\n_\n_\n_\n");

        assertEquals(10, level.getWidth());
        assertEquals(8, level.getHeight());
    }
}
