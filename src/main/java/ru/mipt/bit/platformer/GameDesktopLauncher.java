package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.control.Command;
import ru.mipt.bit.platformer.control.ControlStrategy;
import ru.mipt.bit.platformer.control.KeyboardStrategy;
import ru.mipt.bit.platformer.control.MoveCommand;
import ru.mipt.bit.platformer.control.RandomStrategy;
import ru.mipt.bit.platformer.control.ShootCommand;
import ru.mipt.bit.platformer.control.ToggleHealthBarsCommand;
import ru.mipt.bit.platformer.graphics.BulletGraphics;
import ru.mipt.bit.platformer.graphics.GraphicsLevel;
import ru.mipt.bit.platformer.graphics.HealthBarDecorator;
import ru.mipt.bit.platformer.graphics.HealthBarToggle;
import ru.mipt.bit.platformer.graphics.TankGraphics;
import ru.mipt.bit.platformer.graphics.TreeGraphics;
import ru.mipt.bit.platformer.level.FileLevelGenerator;
import ru.mipt.bit.platformer.level.Level;
import ru.mipt.bit.platformer.level.LevelGenerator;
import ru.mipt.bit.platformer.level.RandomLevelGenerator;
import ru.mipt.bit.platformer.model.Bullet;
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
import static ru.mipt.bit.platformer.util.GdxGameUtils.createCircleTexture;
import static ru.mipt.bit.platformer.util.GdxGameUtils.createPixelTexture;

public class GameDesktopLauncher implements ApplicationListener {

    private static final float TANK_MOVEMENT_SPEED = 0.4f;
    private static final int TANK_HEALTH = 100;
    private static final int RANDOM_TREE_COUNT = 10;
    private static final int AI_TANK_COUNT = 3;

    private final boolean fileLevel;
    private final Random random = new Random();

    private Batch batch;
    private GraphicsLevel graphicsLevel;
    private GameField field;

    private final List<ControlStrategy> strategies = new ArrayList<>();

    public GameDesktopLauncher(boolean fileLevel) {
        this.fileLevel = fileLevel;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        HealthBarToggle healthBarToggle = new HealthBarToggle();
        graphicsLevel = new GraphicsLevel("level.tmx", batch);
        registerGraphics(healthBarToggle);

        Level level = createLevelGenerator().generate();
        field = new GameField(level.getWidth(), level.getHeight());
        field.addListener(graphicsLevel);
        for (Tree tree : level.getTrees()) {
            field.add(tree);
        }

        Tank player = createTank(level.getPlayerPosition());
        KeyboardStrategy keyboard = new KeyboardStrategy(Gdx.input::isKeyPressed);
        addMoveCommand(keyboard, player, Direction.UP, UP, W);
        addMoveCommand(keyboard, player, Direction.LEFT, LEFT, A);
        addMoveCommand(keyboard, player, Direction.DOWN, DOWN, S);
        addMoveCommand(keyboard, player, Direction.RIGHT, RIGHT, D);
        keyboard.addCommand(SPACE, new ShootCommand(player));
        strategies.add(keyboard);

        KeyboardStrategy keyPresses = new KeyboardStrategy(Gdx.input::isKeyJustPressed);
        keyPresses.addCommand(L, new ToggleHealthBarsCommand(healthBarToggle));
        strategies.add(keyPresses);

        for (GridPoint2 position : level.getAiTankPositions()) {
            Tank tank = createTank(position);
            List<Command> commands = new ArrayList<>();
            for (Direction direction : Direction.values()) {
                commands.add(new MoveCommand(tank, direction));
            }
            commands.add(new ShootCommand(tank));
            strategies.add(new RandomStrategy(commands, random));
        }
    }

    private void registerGraphics(HealthBarToggle healthBarToggle) {
        TileMovement tileMovement = graphicsLevel.getTileMovement();
        TiledMapTileLayer groundLayer = graphicsLevel.getGroundLayer();
        Texture tankTexture = graphicsLevel.addTexture(new Texture("images/tank_blue.png"));
        Texture treeTexture = graphicsLevel.addTexture(new Texture("images/greenTree.png"));
        Texture bulletTexture = graphicsLevel.addTexture(createCircleTexture(20, Color.YELLOW));
        Texture healthBarTexture = graphicsLevel.addTexture(createPixelTexture(Color.WHITE));

        graphicsLevel.register(Tank.class, tank -> new HealthBarDecorator(
                new TankGraphics(tank, tileMovement, new TextureRegion(tankTexture)), tank, healthBarToggle, healthBarTexture));
        graphicsLevel.register(Tree.class, tree -> new TreeGraphics(tree, groundLayer, new TextureRegion(treeTexture)));
        graphicsLevel.register(Bullet.class, bullet -> new BulletGraphics(bullet, tileMovement, new TextureRegion(bulletTexture)));
    }

    private LevelGenerator createLevelGenerator() {
        TiledMapTileLayer groundLayer = graphicsLevel.getGroundLayer();
        if (fileLevel) {
            return new FileLevelGenerator(Gdx.files.internal("level.txt").reader(),
                    groundLayer.getWidth(), groundLayer.getHeight());
        }
        return new RandomLevelGenerator(groundLayer.getWidth(), groundLayer.getHeight(),
                RANDOM_TREE_COUNT, AI_TANK_COUNT, random);
    }

    private Tank createTank(GridPoint2 position) {
        Tank tank = new Tank(position, TANK_MOVEMENT_SPEED, TANK_HEALTH, field);
        field.add(tank);
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

        List<Command> commands = new ArrayList<>();
        for (ControlStrategy strategy : strategies) {
            commands.addAll(strategy.getCommands());
        }
        for (Command command : commands) {
            command.execute();
        }

        field.live(deltaTime);

        graphicsLevel.render();
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
        graphicsLevel.dispose();
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
