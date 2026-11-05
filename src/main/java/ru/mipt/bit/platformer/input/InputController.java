package ru.mipt.bit.platformer.input;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.IntPredicate;

public class InputController {

    private final IntPredicate isKeyPressed;
    private final Map<Integer, Action> actions = new LinkedHashMap<>();

    public InputController(IntPredicate isKeyPressed) {
        this.isKeyPressed = isKeyPressed;
    }

    public void addAction(int key, Action action) {
        actions.put(key, action);
    }

    public void handleInput() {
        for (Map.Entry<Integer, Action> entry : actions.entrySet()) {
            if (isKeyPressed.test(entry.getKey())) {
                entry.getValue().execute();
            }
        }
    }
}
