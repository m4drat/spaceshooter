package com.madrat.spaceshooter.gameobjects.poolobjects;

import com.badlogic.gdx.utils.Pool;

public class ExplosionPool extends Pool<Explosion> {

    private float frameLength;
    private int tileSize;
    private String explosionTexturePath;

    public ExplosionPool(int init, int max) {
        super(init, max);
    }

    // make pool with default 16 initial objects and no max
    public ExplosionPool(float frameLength, int tileSize, String explosionTexturePath) {
        super();

        this.frameLength = frameLength;
        this.tileSize = tileSize;
        this.explosionTexturePath = explosionTexturePath;
    }

    // method to create a single object
    @Override
    protected Explosion newObject() {
        return new Explosion(frameLength, tileSize, explosionTexturePath);
    }

}
