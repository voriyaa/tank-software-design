package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;

import java.util.List;

public class GameField {

    private final List<Tree> trees;

    public GameField(List<Tree> trees) {
        this.trees = trees;
    }

    public boolean isFree(GridPoint2 point) {
        for (Tree tree : trees) {
            if (tree.getCoordinates().equals(point)) {
                return false;
            }
        }
        return true;
    }
}
