package com.madrat.spaceshooter.gameobjects;

public class SpaceShip {
    protected int realShipWidth;
    protected int realShipHeight;

    protected int prefferedShipWidth;
    protected int prefferedShipHeight;

    protected float x;
    protected float y;
    protected float speed;
    protected float delayBetweenShoots;
    protected float bulletsSpeed;

    protected float lastShoot;

    protected float currentHealth;
    protected float maxHealth;
    protected float damage;

    protected String handle; // aka ship name

    protected CollisionRect shipCollisionRect;

    public boolean needToShow, isAlive;

    public SpaceShip(float currentHealth, float maxHealth, int damage, float delayBetweenShoots, float bulletsSpeed, float speed, String handle, int realShipWidth, int realShipHeight, int prefferedShipWidth, int prefferedShipHeight) {
        this.realShipHeight = realShipHeight;
        this.realShipWidth = realShipWidth;

        this.prefferedShipHeight = prefferedShipHeight;
        this.prefferedShipWidth = prefferedShipWidth;

        this.currentHealth = currentHealth;
        this.maxHealth = maxHealth;
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

    public CollisionRect getShipCollisionRect() {
        return shipCollisionRect;
    }

    public void setCurrentHealth(float currentHealth) {
        this.currentHealth = currentHealth;
    }
}
