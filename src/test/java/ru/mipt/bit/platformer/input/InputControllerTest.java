package ru.mipt.bit.platformer.input;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InputControllerTest {

    private final Set<Integer> pressed = new HashSet<>();
    private final List<String> executed = new ArrayList<>();
    private final InputController controller = new InputController(pressed::contains);

    @Test
    void executesActionOfPressedKey() {
        controller.addAction(1, () -> executed.add("a"));
        controller.addAction(2, () -> executed.add("b"));
        pressed.add(2);

        controller.handleInput();

        assertEquals(Collections.singletonList("b"), executed);
    }

    @Test
    void nothingPressedNothingExecuted() {
        controller.addAction(1, () -> executed.add("a"));

        controller.handleInput();

        assertEquals(Collections.emptyList(), executed);
    }

    @Test
    void executesActionsInOrderOfAdding() {
        controller.addAction(3, () -> executed.add("c"));
        controller.addAction(1, () -> executed.add("a"));
        controller.addAction(2, () -> executed.add("b"));
        pressed.addAll(Arrays.asList(1, 2, 3));

        controller.handleInput();

        assertEquals(Arrays.asList("c", "a", "b"), executed);
    }
}
