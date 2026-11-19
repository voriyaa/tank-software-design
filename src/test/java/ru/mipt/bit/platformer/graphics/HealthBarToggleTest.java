package ru.mipt.bit.platformer.graphics;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HealthBarToggleTest {

    @Test
    void hiddenByDefault() {
        assertFalse(new HealthBarToggle().isVisible());
    }

    @Test
    void toggleShowsAndHides() {
        HealthBarToggle toggle = new HealthBarToggle();

        toggle.toggle();
        assertTrue(toggle.isVisible());

        toggle.toggle();
        assertFalse(toggle.isVisible());
    }
}
