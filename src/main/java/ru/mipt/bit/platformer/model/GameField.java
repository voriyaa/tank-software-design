package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import java.util.List;

public class GameField implements CollisionDetector {

    private final int width;
    private final int height;
    private final List<? extends Obstacle> obstacles;

    public GameField(int width, int height, List<? extends Obstacle> obstacles) {
        this.width = width;
        this.height = height;
        this.obstacles = obstacles;
    }

    @Override
    public boolean isFree(GridPoint2 point) {
        if (point.x < 0 || point.x >= width || point.y < 0 || point.y >= height) {
            return false;
        }
        for (Obstacle obstacle : obstacles) {
            if (obstacle.getCoordinates().equals(point)) {
                return false;
            }
        }
        return true;
    }
}
