package ru.mipt.bit.platformer.control;

import java.util.List;

public interface ControlStrategy {
    List<Command> getCommands();
}
