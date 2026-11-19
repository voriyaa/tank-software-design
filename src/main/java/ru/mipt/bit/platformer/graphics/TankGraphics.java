package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.util.TileMovement;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;

public class TankGraphics implements Renderable {

    private final Tank tank;
    private final TileMovement tileMovement;
    private final TextureRegion graphics;
    private final Rectangle rectangle;

    public TankGraphics(Tank tank, TileMovement tileMovement, TextureRegion graphics) {
        this.tank = tank;
        this.tileMovement = tileMovement;
        this.graphics = graphics;
        rectangle = createBoundingRectangle(graphics);
    }

    @Override
    public void render(Batch batch) {
        tileMovement.moveRectangleBetweenTileCenters(rectangle, tank.getCoordinates(),
                tank.getDestinationCoordinates(), tank.getMovementProgress());
        drawTextureRegionUnscaled(batch, graphics, rectangle, tank.getRotation());
    }
}
