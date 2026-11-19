package ru.mipt.bit.platformer.model.state;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TankStateTest {

    private static final float EPSILON = 1e-6f;

    @Test
    void lightStateIsLikeWithoutDamage() {
        TankState state = new LightState();

        assertEquals(1f, state.getSpeed(), EPSILON);
        assertEquals(1f, state.getReloadSpeed(), EPSILON);
        assertTrue(state.canShoot());
    }

    @Test
    void mediumStateIsTwiceSlower() {
        TankState state = new MediumState();

        assertEquals(0.5f, state.getSpeed(), EPSILON);
        assertTrue(state.canShoot());
    }

    @Test
    void heavyStateIsThreeTimesSlowerAndCannotShoot() {
        TankState state = new HeavyState();

        assertEquals(1f / 3, state.getSpeed(), EPSILON);
        assertFalse(state.canShoot());
    }
}
