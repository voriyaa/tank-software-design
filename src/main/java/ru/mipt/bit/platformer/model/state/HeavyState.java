package ru.mipt.bit.platformer.model.state;

/** Domain: badly damaged tank, moves three times slower and cannot shoot. */
public class HeavyState implements TankState {

    @Override
    public float getSpeed() {
        return 1f / 3;
    }

    @Override
    public float getReloadSpeed() {
        return 1f;
    }

    @Override
    public boolean canShoot() {
        return false;
    }
}
