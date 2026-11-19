package ru.mipt.bit.platformer.level;

/** Application, driven port: where a level comes from. */
public interface LevelGenerator {
    Level generate();
}
