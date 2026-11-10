package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Interpolation;
import ru.mipt.bit.platformer.model.FieldListener;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createSingleLayerMapRenderer;
import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;

public class GraphicsLevel implements FieldListener {

    private final Batch batch;
    private final TiledMap level;
    private final MapRenderer levelRenderer;
    private final TiledMapTileLayer groundLayer;
    private final TileMovement tileMovement;

    private final Map<Class<?>, Function<GameObject, Renderable>> factories = new HashMap<>();
    private final List<Texture> textures = new ArrayList<>();
    private final Map<GameObject, Renderable> graphics = new LinkedHashMap<>();

    public GraphicsLevel(String mapPath, Batch batch) {
        this.batch = batch;
        level = new TmxMapLoader().load(mapPath);
        levelRenderer = createSingleLayerMapRenderer(level, batch);
        groundLayer = getSingleLayer(level);
        tileMovement = new TileMovement(groundLayer, Interpolation.smooth);
    }

    public TiledMapTileLayer getGroundLayer() {
        return groundLayer;
    }

    public TileMovement getTileMovement() {
        return tileMovement;
    }

    public Texture addTexture(Texture texture) {
        textures.add(texture);
        return texture;
    }

    public <T extends GameObject> void register(Class<T> type, Function<T, Renderable> factory) {
        factories.put(type, object -> factory.apply(type.cast(object)));
    }

    @Override
    public void onObjectAdded(GameObject object) {
        Function<GameObject, Renderable> factory = factories.get(object.getClass());
        if (factory == null) {
            throw new IllegalStateException("No graphics registered for " + object.getClass().getSimpleName());
        }
        graphics.put(object, factory.apply(object));
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
        for (Texture texture : textures) {
            texture.dispose();
        }
        level.dispose();
    }
}
