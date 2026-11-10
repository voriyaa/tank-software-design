package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.Tank;

public class HealthBarDecorator implements Renderable {

    private static final float BAR_HEIGHT = 8f;
    private static final float BAR_OFFSET = 6f;

    private final Renderable renderable;
    private final Tank tank;
    private final HealthBarToggle toggle;
    private final Texture texture;

    public HealthBarDecorator(Renderable renderable, Tank tank, HealthBarToggle toggle, Texture texture) {
        this.renderable = renderable;
        this.tank = tank;
        this.toggle = toggle;
        this.texture = texture;
    }

    @Override
    public void render(Batch batch) {
        renderable.render(batch);
        if (!toggle.isVisible()) {
            return;
        }

        Rectangle rectangle = getRectangle();
        float x = rectangle.x;
        float y = rectangle.y + rectangle.height + BAR_OFFSET;
        float health = (float) tank.getHealth() / tank.getMaxHealth();

        batch.setColor(Color.RED);
        batch.draw(texture, x, y, rectangle.width, BAR_HEIGHT);
        batch.setColor(Color.GREEN);
        batch.draw(texture, x, y, rectangle.width * health, BAR_HEIGHT);
        batch.setColor(Color.WHITE);
    }

    @Override
    public Rectangle getRectangle() {
        return renderable.getRectangle();
    }
}
