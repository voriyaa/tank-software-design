package ru.mipt.bit.platformer.control;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShootCommandTest {

    @Test
    void executeShoots() {
        List<String> shots = new ArrayList<>();

        new ShootCommand(() -> shots.add("shot")).execute();

        assertEquals(1, shots.size());
    }
}
