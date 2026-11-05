package ru.mipt.bit.platformer.control;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KeyboardStrategyTest {

    private final Set<Integer> pressed = new HashSet<>();
    private final KeyboardStrategy keyboard = new KeyboardStrategy(pressed::contains);
    private final Command first = () -> { };
    private final Command second = () -> { };
    private final Command third = () -> { };

    @Test
    void returnsCommandOfPressedKey() {
        keyboard.addCommand(1, first);
        keyboard.addCommand(2, second);
        pressed.add(2);

        assertEquals(Collections.singletonList(second), keyboard.getCommands());
    }

    @Test
    void nothingPressedNoCommands() {
        keyboard.addCommand(1, first);

        assertTrue(keyboard.getCommands().isEmpty());
    }

    @Test
    void returnsCommandsInOrderOfAdding() {
        keyboard.addCommand(3, third);
        keyboard.addCommand(1, first);
        keyboard.addCommand(2, second);
        pressed.addAll(Arrays.asList(1, 2, 3));

        assertEquals(Arrays.asList(third, first, second), keyboard.getCommands());
    }
}
