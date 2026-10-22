package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.model.Tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomLevelGenerator implements LevelGenerator {

    private final int width;
    private final int height;
    private final int treeCount;
    private final int aiTankCount;
    private final Random random;

    public RandomLevelGenerator(int width, int height, int treeCount, int aiTankCount, Random random) {
        if (treeCount < 0 || aiTankCount < 0 || treeCount + aiTankCount >= width * height) {
            throw new IllegalArgumentException("Can't place " + treeCount + " trees and " + aiTankCount
                    + " tanks on " + width + "x" + height + " level");
        }
        this.width = width;
        this.height = height;
        this.treeCount = treeCount;
        this.aiTankCount = aiTankCount;
        this.random = random;
    }

    @Override
    public Level generate() {
        List<GridPoint2> cells = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells.add(new GridPoint2(x, y));
            }
        }
        Collections.shuffle(cells, random);

        List<Tree> trees = new ArrayList<>();
        for (GridPoint2 cell : cells.subList(1, treeCount + 1)) {
            trees.add(new Tree(cell));
        }
        List<GridPoint2> aiTankPositions = new ArrayList<>(cells.subList(treeCount + 1, treeCount + aiTankCount + 1));
        return new Level(cells.get(0), trees, aiTankPositions);
    }
}
