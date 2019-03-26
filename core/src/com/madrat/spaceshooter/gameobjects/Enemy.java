package com.madrat.spaceshooter.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Pool;

public class Enemy extends SpaceShip {
    public Enemy(Texture shipAnimations, float currentHealth, float maxHealth, int damage, float delayBetweenShoots, float bulletSpeed, float speed, String handle, int realShipWidth, int realShipHeight, int prefferedShipWidth, int prefferedShipHeight, float x, float y) {
        super(currentHealth, maxHealth, damage, delayBetweenShoots, bulletSpeed, speed, handle, realShipWidth, realShipHeight, prefferedShipWidth, prefferedShipHeight);

        this.x = x;
        this.y = y;
    }
}
