package com.madrat.spaceshooter.gameobjects;

import static com.madrat.spaceshooter.MainGame.SCALE_FACTOR;

public class SpaceShip {
    protected int realShipWidth;
    protected int realShipHeight;

    protected int preferredShipWidth;
    protected int preferredShipHeight;

    protected float x, y;
    protected float speed;
    protected float delayBetweenShoots;
    protected float bulletsSpeed;

    protected float lastShoot;

    protected float currentHealth, maxHealth, maxHealing;
    protected float damage;

    protected String handle; // aka ship name

    protected CollisionRect shipCollisionRect;

    public boolean needToShow, isAlive;

    public SpaceShip(float currentHealth, float maxHealth, float damage, float delayBetweenShoots, float bulletsSpeed, float speed, String handle, int realShipWidth, int realShipHeight, int preferredShipWidth, int preferredShipHeight) {
        this.realShipHeight = realShipHeight;
        this.realShipWidth = realShipWidth;

        this.preferredShipHeight = (int) (preferredShipHeight * SCALE_FACTOR);
        this.preferredShipWidth = (int) (preferredShipWidth * SCALE_FACTOR);

        this.currentHealth = currentHealth;
        this.maxHealth = maxHealth;
        this.delayBetweenShoots = delayBetweenShoots;
        this.bulletsSpeed = bulletsSpeed * SCALE_FACTOR;
        this.lastShoot = 0;
        this.damage = damage;
        this.speed = speed * SCALE_FACTOR;
        this.handle = handle;
        this.needToShow = true;
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

    public float getCurrentHealth() {
        return currentHealth;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public float getDamage() {
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

    public int getPreferredShipWidth() {
        return preferredShipWidth;
    }

    public int getPreferredShipHeight() {
        return preferredShipHeight;
    }

    public CollisionRect getShipCollisionRect() {
        return shipCollisionRect;
    }

    public void setCurrentHealth(float currentHealth) {
        this.currentHealth = currentHealth;
    }

    public float getMaxHealing() {
        return maxHealing;
    }

    public void setMaxHealing(float maxHealing) {
        this.maxHealing = maxHealing;
    }
}
