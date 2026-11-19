package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;

public interface Renderable {
    void render(Batch batch);

    Rectangle getRectangle();
}
