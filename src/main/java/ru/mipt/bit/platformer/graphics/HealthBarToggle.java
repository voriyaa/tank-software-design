package ru.mipt.bit.platformer.graphics;

public class HealthBarToggle {

    private boolean visible;

    public boolean isVisible() {
        return visible;
    }

    public void toggle() {
        visible = !visible;
    }
}
