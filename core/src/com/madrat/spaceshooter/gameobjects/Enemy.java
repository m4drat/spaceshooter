package com.madrat.spaceshooter.gameobjects;

import com.badlogic.gdx.graphics.Texture;

public class Enemy extends SpaceShip {
    public Enemy(Texture shipAnimations, int lives, int maxLives, int damage, float delayBetweenShoots, float bulletSpeed, float speed, String handle) {
        super(lives, maxLives, damage, delayBetweenShoots, bulletSpeed, speed, handle);
    }
}
