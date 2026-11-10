package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TankTest {

    private static final float EPSILON = 1e-6f;

    private GameField field;
    private Tank tank;
    private final List<GameObject> added = new ArrayList<>();

    @BeforeEach
    void setUp() {
        field = new GameField(10, 8);
        field.add(new Tree(new GridPoint2(1, 3)));
        tank = new Tank(new GridPoint2(1, 1), 0.4f, 3, field);
        field.add(tank);
        field.addListener(new FieldListener() {
            @Override
            public void onObjectAdded(GameObject object) {
                added.add(object);
            }

            @Override
            public void onObjectRemoved(GameObject object) {
            }
        });
    }

    @Test
    void initialState() {
        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertEquals(new GridPoint2(1, 1), tank.getDestinationCoordinates());
        assertEquals(1f, tank.getMovementProgress());
        assertEquals(0f, tank.getRotation());
    }

    @Test
    void doesNotKeepReferenceToStartPoint() {
        GridPoint2 start = new GridPoint2(1, 1);
        Tank tank = new Tank(start, 0.4f, 3, field);

        start.set(5, 5);

        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
    }

    @Test
    void returnedCoordinatesAreCopies() {
        tank.getCoordinates().set(5, 5);
        tank.getDestinationCoordinates().set(5, 5);

        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertEquals(new GridPoint2(1, 1), tank.getDestinationCoordinates());
    }

    @Test
    void moveToFreeCellStartsMovement() {
        tank.move(Direction.RIGHT);

        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertEquals(new GridPoint2(2, 1), tank.getDestinationCoordinates());
        assertEquals(0f, tank.getMovementProgress());
        assertEquals(0f, tank.getRotation());
    }

    @Test
    void moveToTreeOnlyRotates() {
        tank.move(Direction.UP);
        tank.live(0.4f);
        tank.move(Direction.UP);

        assertEquals(new GridPoint2(1, 2), tank.getDestinationCoordinates());
        assertEquals(1f, tank.getMovementProgress());
        assertEquals(90f, tank.getRotation());
    }

    @Test
    void moveIsIgnoredWhileMoving() {
        tank.move(Direction.RIGHT);
        tank.move(Direction.DOWN);

        assertEquals(new GridPoint2(2, 1), tank.getDestinationCoordinates());
        assertEquals(0f, tank.getRotation());
    }

    @Test
    void liveAdvancesProgress() {
        tank.move(Direction.LEFT);
        tank.live(0.2f);

        assertEquals(0.5f, tank.getMovementProgress(), EPSILON);
        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
    }

    @Test
    void movementSpeedIsTakenFromConstructor() {
        Tank slowTank = new Tank(new GridPoint2(1, 1), 0.8f, 3, field);

        slowTank.move(Direction.RIGHT);
        slowTank.live(0.2f);

        assertEquals(0.25f, slowTank.getMovementProgress(), EPSILON);
    }

    @Test
    void liveFinishesMovement() {
        tank.move(Direction.LEFT);
        tank.live(0.2f);
        tank.live(0.3f);

        assertEquals(1f, tank.getMovementProgress());
        assertEquals(new GridPoint2(0, 1), tank.getCoordinates());
        assertEquals(-180f, tank.getRotation());
    }

    @Test
    void stopsAtLevelBorder() {
        Tank cornerTank = new Tank(new GridPoint2(0, 0), 0.4f, 3, field);

        cornerTank.move(Direction.LEFT);
        cornerTank.move(Direction.DOWN);

        assertEquals(new GridPoint2(0, 0), cornerTank.getDestinationCoordinates());
        assertEquals(1f, cornerTank.getMovementProgress());
        assertEquals(-90f, cornerTank.getRotation());
    }

    @Test
    void occupiesBothCellsWhileMoving() {
        tank.move(Direction.RIGHT);

        assertTrue(tank.occupies(new GridPoint2(1, 1)));
        assertTrue(tank.occupies(new GridPoint2(2, 1)));
        assertFalse(tank.occupies(new GridPoint2(3, 1)));
    }

    @Test
    void occupiesOnlyNewCellAfterMovement() {
        tank.move(Direction.RIGHT);
        tank.live(0.4f);

        assertFalse(tank.occupies(new GridPoint2(1, 1)));
        assertTrue(tank.occupies(new GridPoint2(2, 1)));
    }

    @Test
    void cannotMoveToCellsOfMovingTank() {
        Tank left = new Tank(new GridPoint2(0, 1), 0.4f, 3, field);
        Tank right = new Tank(new GridPoint2(3, 1), 0.4f, 3, field);

        tank.move(Direction.RIGHT);
        left.move(Direction.RIGHT);
        right.move(Direction.LEFT);

        assertEquals(new GridPoint2(0, 1), left.getDestinationCoordinates());
        assertEquals(new GridPoint2(3, 1), right.getDestinationCoordinates());
    }

    @Test
    void shootCreatesBulletInFrontOfTank() {
        tank.shoot();

        assertEquals(1, added.size());
        Bullet bullet = (Bullet) added.get(0);
        assertEquals(new GridPoint2(2, 1), bullet.getCoordinates());
        assertEquals(Direction.RIGHT, bullet.getDirection());
    }

    @Test
    void bulletFliesWhereTankLooks() {
        Tank other = new Tank(new GridPoint2(5, 5), 0.4f, 3, field);
        other.move(Direction.DOWN);
        other.live(0.4f);

        other.shoot();

        Bullet bullet = (Bullet) added.get(0);
        assertEquals(new GridPoint2(5, 3), bullet.getCoordinates());
        assertEquals(Direction.DOWN, bullet.getDirection());
    }

    @Test
    void cannotShootWhileReloading() {
        tank.shoot();
        tank.shoot();
        assertEquals(1, added.size());

        field.remove(added.get(0));
        tank.live(1f);
        tank.shoot();
        assertEquals(2, added.size());
    }

    @Test
    void shootWhileMovingStartsFromCurrentPosition() {
        tank.move(Direction.RIGHT);

        tank.shoot();

        Bullet bullet = (Bullet) added.get(0);
        assertEquals(new GridPoint2(2, 1), bullet.getCoordinates());
        assertEquals(3, tank.getHealth());
    }

    @Test
    void bulletDoesNotHitItsOwner() {
        tank.move(Direction.RIGHT);
        tank.shoot();
        Bullet bullet = (Bullet) added.get(0);

        for (int i = 0; i < 5; i++) {
            field.live(0.1f);
        }

        assertEquals(new GridPoint2(2, 1), tank.getCoordinates());
        assertEquals(3, tank.getHealth());
        assertTrue(bullet.getCoordinates().x > 2);
    }

    @Test
    void shootAtAdjacentTankDamagesItWithoutBullet() {
        Tank enemy = new Tank(new GridPoint2(2, 1), 0.4f, 3, field);
        field.add(enemy);
        added.clear();

        tank.shoot();

        assertEquals(2, enemy.getHealth());
        assertTrue(added.isEmpty());
    }

    @Test
    void shootAtAdjacentTankCanKillIt() {
        Tank enemy = new Tank(new GridPoint2(2, 1), 0.4f, 1, field);
        field.add(enemy);

        tank.shoot();

        assertFalse(enemy.isAlive());
        assertFalse(field.contains(enemy));
    }

    @Test
    void shootIntoBorderCreatesNoBullet() {
        Tank borderTank = new Tank(new GridPoint2(9, 1), 0.4f, 3, field);

        borderTank.shoot();

        assertTrue(added.isEmpty());
    }

    @Test
    void hitReducesHealth() {
        tank.hit(1);

        assertEquals(2, tank.getHealth());
        assertTrue(tank.isAlive());
        assertTrue(field.contains(tank));
    }

    @Test
    void tankWithoutHealthIsRemoved() {
        tank.hit(3);

        assertFalse(tank.isAlive());
        assertFalse(field.contains(tank));
    }

    @Test
    void deadTankDoesNotMoveAndShoot() {
        tank.hit(3);

        tank.move(Direction.RIGHT);
        tank.shoot();

        assertEquals(new GridPoint2(1, 1), tank.getDestinationCoordinates());
        assertTrue(added.isEmpty());
    }
}
