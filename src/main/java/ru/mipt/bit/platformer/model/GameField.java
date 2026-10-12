package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import java.util.List;

public class GameField implements CollisionDetector {

    private final List<? extends Obstacle> obstacles;

    public GameField(List<? extends Obstacle> obstacles) {
        this.obstacles = obstacles;
    }

    @Override
    public boolean isFree(GridPoint2 point) {
        for (Obstacle obstacle : obstacles) {
            if (obstacle.getCoordinates().equals(point)) {
                return false;
            }
        }
        return true;
    }
}
