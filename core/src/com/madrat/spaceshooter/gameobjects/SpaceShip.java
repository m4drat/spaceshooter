package com.madrat.spaceshooter.gameobjects;

import com.madrat.spaceshooter.physics2d.CollisionRect;

import java.util.Random;

import static com.madrat.spaceshooter.MainGame.SCALE_FACTOR;

public class SpaceShip {

    public enum shipHandler {
        zapper,
        destroyer,
        ignitor,
        turtle,
        ufo,
        star,
        pinky;

        public static shipHandler getRandomShip() {
            if (random == null) {
                random = new Random();
            }
            return values()[random.nextInt(values().length)];
        }
    }

    private static Random random;

    protected int realShipWidth;
    protected int realShipHeight;

    protected int preferredShipWidth;
    protected int preferredShipHeight;

    protected float x, y;
    protected float speed;
    protected float delayBetweenShootsBullets, bulletsSpeed, lastBulletShoot;

    protected float currentHealth, maxHealth, maxHealing;
    protected float damage;

    protected shipHandler handle; // aka ship type

    protected CollisionRect shipCollisionRect;

    public boolean needToShow, isAlive;

    public SpaceShip() {

    }

    public SpaceShip(float maxHealth, float damage, float delayBetweenShootsBullets, float bulletsSpeed, float speed, shipHandler handle, int realShipWidth, int realShipHeight, int preferredShipWidth, int preferredShipHeight) {
        this.realShipHeight = realShipHeight;
        this.realShipWidth = realShipWidth;

        this.preferredShipHeight = (int) (preferredShipHeight * SCALE_FACTOR);
        this.preferredShipWidth = (int) (preferredShipWidth * SCALE_FACTOR);

        this.currentHealth = maxHealth;
        this.maxHealth = maxHealth;
        this.delayBetweenShootsBullets = delayBetweenShootsBullets;
        this.bulletsSpeed = bulletsSpeed * SCALE_FACTOR;
        this.lastBulletShoot = 0;
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

    public float getCurrentHealth() {
        return currentHealth;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public float getDamage() {
        return damage;
    }

    public shipHandler getHandle() {
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
