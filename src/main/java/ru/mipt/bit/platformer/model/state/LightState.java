package ru.mipt.bit.platformer.model.state;

/** Domain: tank without much damage. */
public class LightState implements TankState {

    @Override
    public float getSpeed() {
        return 1f;
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
