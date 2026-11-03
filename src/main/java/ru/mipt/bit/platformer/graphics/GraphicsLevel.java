package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Interpolation;
import ru.mipt.bit.platformer.model.Bullet;
import ru.mipt.bit.platformer.model.FieldListener;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.LinkedHashMap;
import java.util.Map;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createSingleLayerMapRenderer;
import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;

public class GraphicsLevel implements FieldListener {

    private final Batch batch;
    private final TiledMap level;
    private final MapRenderer levelRenderer;
    private final TiledMapTileLayer groundLayer;
    private final TileMovement tileMovement;

    private final Texture tankTexture;
    private final Texture treeTexture;
    private final Texture bulletTexture;

    private final Map<GameObject, Renderable> graphics = new LinkedHashMap<>();

    public GraphicsLevel(String mapPath, Batch batch) {
        this.batch = batch;
        level = new TmxMapLoader().load(mapPath);
        levelRenderer = createSingleLayerMapRenderer(level, batch);
        groundLayer = getSingleLayer(level);
        tileMovement = new TileMovement(groundLayer, Interpolation.smooth);

        tankTexture = new Texture("images/tank_blue.png");
        treeTexture = new Texture("images/greenTree.png");
        bulletTexture = createBulletTexture();
    }

    private static Texture createBulletTexture() {
        Pixmap pixmap = new Pixmap(20, 20, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.YELLOW);
        pixmap.fillCircle(10, 10, 9);
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    public TiledMapTileLayer getGroundLayer() {
        return groundLayer;
    }

    @Override
    public void onObjectAdded(GameObject object) {
        if (object instanceof Tank) {
            graphics.put(object, new TankGraphics((Tank) object, tileMovement, new TextureRegion(tankTexture)));
        } else if (object instanceof Tree) {
            graphics.put(object, new TreeGraphics((Tree) object, groundLayer, new TextureRegion(treeTexture)));
        } else if (object instanceof Bullet) {
            graphics.put(object, new BulletGraphics((Bullet) object, tileMovement, new TextureRegion(bulletTexture)));
        }
    }

    @Override
    public void onObjectRemoved(GameObject object) {
        graphics.remove(object);
    }

    public void render() {
        levelRenderer.render();

        batch.begin();
        for (Renderable renderable : graphics.values()) {
            renderable.render(batch);
        }
        batch.end();
    }

    public void dispose() {
        tankTexture.dispose();
        treeTexture.dispose();
        bulletTexture.dispose();
        level.dispose();
    }
}
