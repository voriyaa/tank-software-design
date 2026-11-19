package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.graphics.HealthBarToggle;

/** Application: shows or hides health bars. */
public class ToggleHealthBarsCommand implements Command {

    private final HealthBarToggle toggle;

    public ToggleHealthBarsCommand(HealthBarToggle toggle) {
        this.toggle = toggle;
    }

    @Override
    public void execute() {
        toggle.toggle();
    }
}
