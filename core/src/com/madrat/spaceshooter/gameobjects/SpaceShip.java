package com.madrat.spaceshooter.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.madrat.spaceshooter.utils.Assets;

public class SpaceShip {
    public static final int SHIP_WIDTH = 24;
    public static final int SHIP_HEIGHT = 26;

    public static final int preffered_SHIP_WIDTH = 60;
    public static final int preffered_SHIP_HEIGHT = 50;

    private Texture shipTexture;
    private Animation<TextureRegion>[] engines;
    private static final float animationSpeed = 0.2f;
    private float stateTime;
    private int frame;

    private float x, y, speed, fireRate;
    private int currentLives, maxLives, damage;
    private String handle; // aka ship name

    public SpaceShip(Texture texture, int lives, int maxLives, int damage, float fireRate, float speed, String handle) {
        this.shipTexture = texture;

        // Place ship in center
        this.x = Gdx.graphics.getWidth() / 2 - preffered_SHIP_WIDTH / 2;
        this.y = 25;

        this.currentLives = lives;
        this.maxLives = maxLives;
        this.fireRate = fireRate;
        this.damage = damage;
        this.speed = speed;
        this.handle = handle;

        // Initialize animations
        frame = 0;
        engines = new Animation[8];
        TextureRegion[][] engineSpriteSheet = TextureRegion.split(new Texture(Assets.ship1Animation), SHIP_WIDTH, SHIP_HEIGHT);
        engines[frame] = new Animation(animationSpeed, engineSpriteSheet[0]);
    }

    public void die() {
        this.dispose();
    }

    public void dispose() {
        this.shipTexture.dispose();
    }

    public Texture getShipTexture() {
        return shipTexture;
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

    public float getFireRate() {
        return fireRate;
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

    public Animation<TextureRegion>[] getEngines() {
        return engines;
    }

    public int getFrame() {
        return frame;
    }

    public float getStateTime() {
        return stateTime;
    }

    public void setStateTime(float stateTime) {
        this.stateTime = stateTime;
    }

    public void incStateTime(float dt) {
        this.stateTime += dt;
    }
}
