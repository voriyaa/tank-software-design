package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.List;

public class GameField implements CollisionDetector {

    private final int width;
    private final int height;
    private final List<Obstacle> obstacles = new ArrayList<>();

    public GameField(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void addObstacle(Obstacle obstacle) {
        obstacles.add(obstacle);
    }

    @Override
    public boolean isFree(GridPoint2 point) {
        if (point.x < 0 || point.x >= width || point.y < 0 || point.y >= height) {
            return false;
        }
        for (Obstacle obstacle : obstacles) {
            if (obstacle.occupies(point)) {
                return false;
            }
        }
        return true;
    }
}
