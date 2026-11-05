package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.Bullet;
import ru.mipt.bit.platformer.util.TileMovement;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;

public class BulletGraphics implements Renderable {

    private final Bullet bullet;
    private final TileMovement tileMovement;
    private final TextureRegion graphics;
    private final Rectangle rectangle;

    public BulletGraphics(Bullet bullet, TileMovement tileMovement, TextureRegion graphics) {
        this.bullet = bullet;
        this.tileMovement = tileMovement;
        this.graphics = graphics;
        rectangle = createBoundingRectangle(graphics);
    }

    @Override
    public void render(Batch batch) {
        tileMovement.moveRectangleBetweenTileCenters(rectangle, bullet.getCoordinates(),
                bullet.getDestinationCoordinates(), bullet.getMovementProgress());
        drawTextureRegionUnscaled(batch, graphics, rectangle, bullet.getDirection().getRotation());
    }
}
