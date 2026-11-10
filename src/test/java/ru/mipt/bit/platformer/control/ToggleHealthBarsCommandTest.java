package ru.mipt.bit.platformer.control;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.graphics.HealthBarToggle;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ToggleHealthBarsCommandTest {

    @Test
    void executeTogglesHealthBars() {
        HealthBarToggle toggle = new HealthBarToggle();
        ToggleHealthBarsCommand command = new ToggleHealthBarsCommand(toggle);

        command.execute();
        assertTrue(toggle.isVisible());

        command.execute();
        assertFalse(toggle.isVisible());
    }
}
