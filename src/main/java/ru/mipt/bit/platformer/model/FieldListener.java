package ru.mipt.bit.platformer.model;

/** Domain, driven port: tells the outside world that objects were added or removed. */
public interface FieldListener {
    void onObjectAdded(GameObject object);

    void onObjectRemoved(GameObject object);
}
