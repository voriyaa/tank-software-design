package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import ru.mipt.bit.platformer.graphics.LevelGraphics;
import ru.mipt.bit.platformer.graphics.Renderable;
import ru.mipt.bit.platformer.graphics.TankGraphics;
import ru.mipt.bit.platformer.graphics.TreeGraphics;
import ru.mipt.bit.platformer.input.InputController;
import ru.mipt.bit.platformer.input.MoveAction;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameField;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.badlogic.gdx.Input.Keys.*;
import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

public class GameDesktopLauncher implements ApplicationListener {

    private static final float TANK_MOVEMENT_SPEED = 0.4f;

    private Batch batch;

    private Tank tank;
    private InputController inputController;

    private LevelGraphics levelGraphics;
    private TankGraphics tankGraphics;
    private List<Renderable> renderables;

    @Override
    public void create() {
        batch = new SpriteBatch();
        levelGraphics = new LevelGraphics("level.tmx", batch);
        TileMovement tileMovement = new TileMovement(levelGraphics.getGroundLayer(), Interpolation.smooth);

        Tree tree = new Tree(new GridPoint2(1, 3));
        GameField field = new GameField(Collections.singletonList(tree));
        tank = new Tank(new GridPoint2(1, 1), TANK_MOVEMENT_SPEED, field);

        tankGraphics = new TankGraphics(tank, tileMovement, "images/tank_blue.png");
        TreeGraphics treeGraphics = new TreeGraphics(tree, levelGraphics.getGroundLayer(), "images/greenTree.png");
        renderables = Arrays.asList(tankGraphics, treeGraphics);

        inputController = new InputController(Gdx.input::isKeyPressed);
        addMoveAction(Direction.UP, UP, W);
        addMoveAction(Direction.LEFT, LEFT, A);
        addMoveAction(Direction.DOWN, DOWN, S);
        addMoveAction(Direction.RIGHT, RIGHT, D);
    }

    private void addMoveAction(Direction direction, int... keys) {
        MoveAction action = new MoveAction(tank, direction);
        for (int key : keys) {
            inputController.addAction(key, action);
        }
    }

    @Override
    public void render() {
        // clear the screen
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        // get time passed since the last render
        float deltaTime = Gdx.graphics.getDeltaTime();

        inputController.handleInput();
        tankGraphics.updatePosition();
        tank.update(deltaTime);

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
        new Lwjgl3Application(new GameDesktopLauncher(), config);
    }
}
