package ru.mipt.bit.platformer.control;

import java.util.List;

/** Application, driving port: where commands for a tank come from. */
public interface ControlStrategy {
    List<Command> getCommands();
}
