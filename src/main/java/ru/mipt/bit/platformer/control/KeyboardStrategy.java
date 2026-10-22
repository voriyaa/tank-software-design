package ru.mipt.bit.platformer.control;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntPredicate;

public class KeyboardStrategy implements ControlStrategy {

    private final IntPredicate isKeyPressed;
    private final Map<Integer, Command> commands = new LinkedHashMap<>();

    public KeyboardStrategy(IntPredicate isKeyPressed) {
        this.isKeyPressed = isKeyPressed;
    }

    public void addCommand(int key, Command command) {
        commands.put(key, command);
    }

    @Override
    public List<Command> getCommands() {
        List<Command> result = new ArrayList<>();
        for (Map.Entry<Integer, Command> entry : commands.entrySet()) {
            if (isKeyPressed.test(entry.getKey())) {
                result.add(entry.getValue());
            }
        }
        return result;
    }
}
