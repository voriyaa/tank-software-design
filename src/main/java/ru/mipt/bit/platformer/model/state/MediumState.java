package ru.mipt.bit.platformer.model.state;

/** Domain: damaged tank, moves twice slower. */
public class MediumState implements TankState {

    @Override
    public float getSpeed() {
        return 0.5f;
    }

    @Override
    public float getReloadSpeed() {
        return 1f;
    }

    @Override
    public boolean canShoot() {
        return true;
    }
}
