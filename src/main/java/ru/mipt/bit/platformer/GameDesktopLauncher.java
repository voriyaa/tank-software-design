package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import ru.mipt.bit.platformer.control.Command;
import ru.mipt.bit.platformer.control.ControlStrategy;
import ru.mipt.bit.platformer.control.KeyboardStrategy;
import ru.mipt.bit.platformer.control.MoveCommand;
import ru.mipt.bit.platformer.control.RandomStrategy;
import ru.mipt.bit.platformer.graphics.LevelGraphics;
import ru.mipt.bit.platformer.graphics.Renderable;
import ru.mipt.bit.platformer.graphics.TankGraphics;
import ru.mipt.bit.platformer.graphics.TreeGraphics;
import ru.mipt.bit.platformer.level.FileLevelGenerator;
import ru.mipt.bit.platformer.level.Level;
import ru.mipt.bit.platformer.level.LevelGenerator;
import ru.mipt.bit.platformer.level.RandomLevelGenerator;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameField;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.badlogic.gdx.Input.Keys.*;
import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

public class GameDesktopLauncher implements ApplicationListener {

    private static final float TANK_MOVEMENT_SPEED = 0.4f;
    private static final int RANDOM_TREE_COUNT = 10;
    private static final int AI_TANK_COUNT = 3;

    private final boolean fileLevel;
    private final Random random = new Random();

    private Batch batch;
    private LevelGraphics levelGraphics;
    private TileMovement tileMovement;
    private GameField field;

    private final List<Tank> tanks = new ArrayList<>();
    private final List<ControlStrategy> strategies = new ArrayList<>();
    private final List<Renderable> renderables = new ArrayList<>();

    public GameDesktopLauncher(boolean fileLevel) {
        this.fileLevel = fileLevel;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        levelGraphics = new LevelGraphics("level.tmx", batch);
        TiledMapTileLayer groundLayer = levelGraphics.getGroundLayer();
        tileMovement = new TileMovement(groundLayer, Interpolation.smooth);
        field = new GameField(groundLayer.getWidth(), groundLayer.getHeight());

        Level level = createLevelGenerator().generate();
        for (Tree tree : level.getTrees()) {
            field.addObstacle(tree);
            renderables.add(new TreeGraphics(tree, groundLayer, "images/greenTree.png"));
        }

        Tank player = addTank(level.getPlayerPosition());
        KeyboardStrategy keyboard = new KeyboardStrategy(Gdx.input::isKeyPressed);
        addMoveCommand(keyboard, player, Direction.UP, UP, W);
        addMoveCommand(keyboard, player, Direction.LEFT, LEFT, A);
        addMoveCommand(keyboard, player, Direction.DOWN, DOWN, S);
        addMoveCommand(keyboard, player, Direction.RIGHT, RIGHT, D);
        strategies.add(keyboard);

        for (GridPoint2 position : level.getAiTankPositions()) {
            strategies.add(new RandomStrategy(addTank(position), random));
        }
    }

    private LevelGenerator createLevelGenerator() {
        if (fileLevel) {
            return new FileLevelGenerator(Gdx.files.internal("level.txt").reader());
        }
        TiledMapTileLayer groundLayer = levelGraphics.getGroundLayer();
        return new RandomLevelGenerator(groundLayer.getWidth(), groundLayer.getHeight(),
                RANDOM_TREE_COUNT, AI_TANK_COUNT, random);
    }

    private Tank addTank(GridPoint2 position) {
        Tank tank = new Tank(position, TANK_MOVEMENT_SPEED, field);
        field.addObstacle(tank);
        tanks.add(tank);
        renderables.add(new TankGraphics(tank, tileMovement, "images/tank_blue.png"));
        return tank;
    }

    private void addMoveCommand(KeyboardStrategy keyboard, Tank tank, Direction direction, int... keys) {
        MoveCommand command = new MoveCommand(tank, direction);
        for (int key : keys) {
            keyboard.addCommand(key, command);
        }
    }

    @Override
    public void render() {
        // clear the screen
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        // get time passed since the last render
        float deltaTime = Gdx.graphics.getDeltaTime();

        for (ControlStrategy strategy : strategies) {
            for (Command command : strategy.getCommands()) {
                command.execute();
            }
        }
        for (Tank tank : tanks) {
            tank.update(deltaTime);
        }

        // render each tile of the level
        levelGraphics.render();

        // start recording all drawing commands
        batch.begin();

        for (Renderable renderable : renderables) {
            renderable.render(batch);
        }

        // submit all drawing requests
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        // do not react to window resizing
    }

    @Override
    public void pause() {
        // game doesn't get paused
    }

    @Override
    public void resume() {
        // game doesn't get paused
    }

    @Override
    public void dispose() {
        // dispose of all the native resources (classes which implement com.badlogic.gdx.utils.Disposable)
        for (Renderable renderable : renderables) {
            renderable.dispose();
        }
        levelGraphics.dispose();
        batch.dispose();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        // level width: 10 tiles x 128px, height: 8 tiles x 128px
        config.setWindowedMode(1280, 1024);
        boolean fileLevel = args.length > 0 && args[0].equals("file");
        new Lwjgl3Application(new GameDesktopLauncher(fileLevel), config);
    }
}
