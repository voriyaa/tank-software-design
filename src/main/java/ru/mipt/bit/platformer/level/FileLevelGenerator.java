package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.model.Tree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileLevelGenerator implements LevelGenerator {

    private static final char TREE = 'T';
    private static final char PLAYER = 'X';
    private static final char EMPTY = '_';

    private final Reader reader;

    public FileLevelGenerator(Reader reader) {
        this.reader = reader;
    }

    @Override
    public Level generate() {
        List<String> lines = readLines();
        GridPoint2 playerPosition = null;
        List<Tree> trees = new ArrayList<>();

        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            int y = lines.size() - 1 - row;
            for (int x = 0; x < line.length(); x++) {
                char cell = line.charAt(x);
                if (cell == TREE) {
                    trees.add(new Tree(new GridPoint2(x, y)));
                } else if (cell == PLAYER) {
                    if (playerPosition != null) {
                        throw new IllegalArgumentException("Level has more than one player");
                    }
                    playerPosition = new GridPoint2(x, y);
                } else if (cell != EMPTY) {
                    throw new IllegalArgumentException("Unknown symbol '" + cell + "' at line " + (row + 1));
                }
            }
        }

        if (playerPosition == null) {
            throw new IllegalArgumentException("Level has no player");
        }
        return new Level(playerPosition, trees, Collections.emptyList());
    }

    private List<String> readLines() {
        List<String> lines = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(reader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (!line.isEmpty()) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return lines;
    }
}
