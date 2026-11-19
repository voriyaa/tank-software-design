package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

public interface GameObject {
    boolean occupies(GridPoint2 point);

    void live(float deltaTime);

    void hit(int damage);
}
