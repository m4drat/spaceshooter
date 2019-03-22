package com.madrat.spaceshooter.gameobjects;

import com.badlogic.gdx.math.Rectangle;

public class SpaceShip {
    public int SHIP_WIDTH = 24;
    public int SHIP_HEIGHT = 23;

    public int preferred_SHIP_WIDTH = 60;
    public int preferred_SHIP_HEIGHT = 50;

    protected float x;
    protected float y;
    protected float speed;
    protected float delayBetweenShoots;
    protected float bulletsSpeed;

    protected float lastShoot;
    protected int currentLives, maxLives, damage;
    protected String handle; // aka ship name

    public boolean needToShow, isAlive;

    public SpaceShip(int lives, int maxLives, int damage, float delayBetweenShoots, float bulletsSpeed, float speed, String handle) {
        this.currentLives = lives;
        this.maxLives = maxLives;
        this.delayBetweenShoots = delayBetweenShoots;
        this.bulletsSpeed = bulletsSpeed;
        this.lastShoot = 0;
        this.damage = damage;
        this.speed = speed;
        this.handle = handle;
        this.needToShow = false;
        this.isAlive = true;
        // this.collisionMask = new Rectangle();
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getSpeed() {
        return speed;
    }

    public float getDelayBetweenShoots() {
        return delayBetweenShoots;
    }

    public int getCurrentLives() {
        return currentLives;
    }

    public int getMaxLives() {
        return maxLives;
    }

    public int getDamage() {
        return damage;
    }

    public String getHandle() {
        return handle;
    }

    public void setNeedToShow(boolean var) {
        this.needToShow = var;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public void setLastShoot(float lastShoot) {
        this.lastShoot = lastShoot;
    }

    public void incLastShoot(float dt) {
        this.lastShoot += dt;
    }

    public float getLastShoot() {
        return lastShoot;
    }
}
