package com.madrat.spaceshooter.gameobjects.poolobjects;

import com.badlogic.gdx.utils.Pool;

public class BulletPool extends Pool<Bullet> {

    private String bulletTexturePath;
    private float animationSpeed;
    private int realWidth, realHeight;
    private Bullet.bulletType type;

    public BulletPool(String bulletTexturePath, float animationSpeed, int realWidth, int realHeight, Bullet.bulletType type) {
        super();

        this.bulletTexturePath = bulletTexturePath;
        this.animationSpeed = animationSpeed;
        this.realWidth = realWidth;
        this.realHeight = realHeight;

        this.type = type;
    }

    // method to create a single object
    @Override
    protected Bullet newObject() {
        return new Bullet(this.bulletTexturePath, animationSpeed, realWidth, realHeight, type);
    }
}
