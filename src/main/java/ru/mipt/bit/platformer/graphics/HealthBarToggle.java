package ru.mipt.bit.platformer.graphics;

/** Infrastructure: keeps whether health bars are shown. */
public class HealthBarToggle {

    private boolean visible;

    public boolean isVisible() {
        return visible;
    }

    public void toggle() {
        visible = !visible;
    }
}
