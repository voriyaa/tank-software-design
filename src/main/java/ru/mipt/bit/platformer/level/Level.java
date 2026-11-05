package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.model.Tree;

import java.util.List;

public class Level {

    private final int width;
    private final int height;
    private final GridPoint2 playerPosition;
    private final List<Tree> trees;
    private final List<GridPoint2> aiTankPositions;

    public Level(int width, int height, GridPoint2 playerPosition, List<Tree> trees, List<GridPoint2> aiTankPositions) {
        this.width = width;
        this.height = height;
        this.playerPosition = playerPosition;
        this.trees = trees;
        this.aiTankPositions = aiTankPositions;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public GridPoint2 getPlayerPosition() {
        return playerPosition;
    }

    public List<Tree> getTrees() {
        return trees;
    }

    public List<GridPoint2> getAiTankPositions() {
        return aiTankPositions;
    }
}
