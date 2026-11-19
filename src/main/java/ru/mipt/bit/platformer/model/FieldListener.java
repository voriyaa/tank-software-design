package ru.mipt.bit.platformer.model;

public interface FieldListener {
    void onObjectAdded(GameObject object);

    void onObjectRemoved(GameObject object);
}
