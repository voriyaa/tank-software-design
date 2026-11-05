package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.model.Tree;

import java.util.List;

public class Level {

    private final int width;
    private final int height;
    private final GridPoint2 playerPosition;
    private final List<Tree> trees;

    public Level(int width, int height, GridPoint2 playerPosition, List<Tree> trees) {
        this.width = width;
        this.height = height;
        this.playerPosition = playerPosition;
        this.trees = trees;
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
}
