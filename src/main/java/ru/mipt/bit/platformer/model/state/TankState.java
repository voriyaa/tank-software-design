package ru.mipt.bit.platformer.model.state;

/** Domain: tank state, decides how fast a tank moves and reloads and whether it can shoot. */
public interface TankState {
    float getSpeed();

    float getReloadSpeed();

    boolean canShoot();
}
