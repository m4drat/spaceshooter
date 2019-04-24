package com.madrat.spaceshooter.gameobjects.poolobjects;

import com.badlogic.gdx.utils.Pool;
import com.madrat.spaceshooter.physics2d.CollisionRect;

public class PowerUpPool extends Pool<PowerUp> {

    private float frameLength;
    private int tileWidth, tileHeight;
    private String pathToAnimationSheet;

    private CollisionRect.colliderTag tag;

    public PowerUpPool(float frameLength, int tileWidth, int tileHeight, CollisionRect.colliderTag tag, String pathToAnimationSheet) {
        super();

        this.frameLength = frameLength;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.tag = tag;
        this.pathToAnimationSheet = pathToAnimationSheet;
    }

    @Override
    protected PowerUp newObject() {
        return new PowerUp(frameLength, tileWidth, tileHeight, tag, pathToAnimationSheet);
    }
}
