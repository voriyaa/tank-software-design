package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BulletTest {

    private GameField field;
    private Bullet bullet;

    @BeforeEach
    void setUp() {
        field = new GameField(10, 8);
        bullet = new Bullet(new GridPoint2(2, 1), Direction.RIGHT, 0.1f, 1, field);
        field.add(bullet);
    }

    @Test
    void startsFlyingToNextCell() {
        bullet.live(0f);

        assertEquals(new GridPoint2(2, 1), bullet.getCoordinates());
        assertEquals(new GridPoint2(3, 1), bullet.getDestinationCoordinates());
        assertTrue(bullet.occupies(new GridPoint2(2, 1)));
        assertTrue(bullet.occupies(new GridPoint2(3, 1)));
    }

    @Test
    void fliesInItsDirection() {
        bullet.live(0f);
        bullet.live(0.1f);

        assertEquals(new GridPoint2(3, 1), bullet.getCoordinates());
        assertEquals(new GridPoint2(4, 1), bullet.getDestinationCoordinates());
    }

    @Test
    void disappearsAtLevelBorder() {
        Bullet borderBullet = new Bullet(new GridPoint2(9, 1), Direction.RIGHT, 0.1f, 1, field);
        field.add(borderBullet);

        borderBullet.live(0f);

        assertFalse(field.contains(borderBullet));
    }

    @Test
    void disappearsAfterHittingTree() {
        Tree tree = new Tree(new GridPoint2(3, 1));
        field.add(tree);

        bullet.live(0f);

        assertFalse(field.contains(bullet));
        assertTrue(field.contains(tree));
    }

    @Test
    void damagesTank() {
        Tank tank = new Tank(new GridPoint2(3, 1), 0.4f, 3, field);
        field.add(tank);

        bullet.live(0f);

        assertEquals(2, tank.getHealth());
        assertFalse(field.contains(bullet));
    }

    @Test
    void damageIsTakenFromSettings() {
        Bullet strongBullet = new Bullet(new GridPoint2(5, 5), Direction.UP, 0.1f, 2, field);
        Tank tank = new Tank(new GridPoint2(5, 6), 0.4f, 3, field);
        field.add(strongBullet);
        field.add(tank);

        strongBullet.live(0f);

        assertEquals(1, tank.getHealth());
    }

    @Test
    void killsTankWithoutHealth() {
        Tank tank = new Tank(new GridPoint2(3, 1), 0.4f, 1, field);
        field.add(tank);

        bullet.live(0f);

        assertFalse(field.contains(tank));
    }

    @Test
    void bulletsDestroyEachOther() {
        Bullet other = new Bullet(new GridPoint2(3, 1), Direction.LEFT, 0.1f, 1, field);
        field.add(other);

        bullet.live(0f);

        assertFalse(field.contains(bullet));
        assertFalse(field.contains(other));
    }

    @Test
    void hitRemovesBullet() {
        bullet.hit(1);

        assertFalse(field.contains(bullet));
    }
}
