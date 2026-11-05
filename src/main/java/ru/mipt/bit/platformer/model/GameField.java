package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.List;

public class GameField {

    private final int width;
    private final int height;
    private final List<GameObject> objects = new ArrayList<>();
    private final List<FieldListener> listeners = new ArrayList<>();

    public GameField(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void addListener(FieldListener listener) {
        listeners.add(listener);
    }

    public void add(GameObject object) {
        objects.add(object);
        for (FieldListener listener : listeners) {
            listener.onObjectAdded(object);
        }
    }

    public void remove(GameObject object) {
        if (objects.remove(object)) {
            for (FieldListener listener : listeners) {
                listener.onObjectRemoved(object);
            }
        }
    }

    public boolean contains(GameObject object) {
        return objects.contains(object);
    }

    public boolean isFree(GridPoint2 point) {
        if (point.x < 0 || point.x >= width || point.y < 0 || point.y >= height) {
            return false;
        }
        for (GameObject object : objects) {
            if (object.occupies(point)) {
                return false;
            }
        }
        return true;
    }

    public void hit(GridPoint2 point, int damage) {
        for (GameObject object : new ArrayList<>(objects)) {
            if (object.occupies(point)) {
                object.hit(damage);
            }
        }
    }

    public void live(float deltaTime) {
        for (GameObject object : new ArrayList<>(objects)) {
            if (objects.contains(object)) {
                object.live(deltaTime);
            }
        }
    }
}
