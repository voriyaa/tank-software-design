package ru.mipt.bit.platformer.model;

/** Domain, port for commands: something that can move. */
public interface Movable {
    void move(Direction direction);
}
