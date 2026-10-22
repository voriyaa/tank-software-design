package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.model.Tree;

import java.util.List;

public class Level {

    private final GridPoint2 playerPosition;
    private final List<Tree> trees;
    private final List<GridPoint2> aiTankPositions;

    public Level(GridPoint2 playerPosition, List<Tree> trees, List<GridPoint2> aiTankPositions) {
        this.playerPosition = playerPosition;
        this.trees = trees;
        this.aiTankPositions = aiTankPositions;
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
