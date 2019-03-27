package com.madrat.spaceshooter.gameobjects.poolobjects;

import com.badlogic.gdx.utils.Pool;

public class BulletPool extends Pool<Bullet> {

    private String bulletTexturePath;
    private float animationSpeed;
    private int realWidth, realHeight;
    String bulletType;

    public BulletPool(String bulletTexturePath, float animationSpeed, int realWidth, int realHeight, String bulletType) {
        super();

        this.bulletTexturePath = bulletTexturePath;
        this.animationSpeed = animationSpeed;
        this.realWidth = realWidth;
        this.realHeight = realHeight;

        this.bulletType = bulletType;
    }

    // method to create a single object
    @Override
    protected Bullet newObject() {
        // System.out.println("[+] Creating new bullet");
        return new Bullet(this.bulletTexturePath, animationSpeed, realWidth, realHeight, bulletType);
    }
}
