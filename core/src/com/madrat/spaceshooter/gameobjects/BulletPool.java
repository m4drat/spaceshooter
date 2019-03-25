package com.madrat.spaceshooter.gameobjects;

import com.badlogic.gdx.utils.Pool;

public class BulletPool extends Pool<Bullet> {

    // constructor with initial object count and max object count
    // max is the maximum of object held in the pool and not the
    // maximum amount of objects that can be created by the pool
    public BulletPool(int init, int max) {
        super(init, max);
    }

    // make pool with default 16 initial objects and no max
    public BulletPool() {
        super();
    }

    // method to create a single object
    @Override
    protected Bullet newObject() {
        System.out.println("[+] Creating new bullet");
        return new Bullet();
    }
}
