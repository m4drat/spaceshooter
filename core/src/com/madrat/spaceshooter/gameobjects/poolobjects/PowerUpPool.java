package com.madrat.spaceshooter.gameobjects.poolobjects;

import com.badlogic.gdx.utils.Pool;

public class PowerUpPool extends Pool<PowerUp> {

    private float frameLength;
    private int tileWidth, tileHeight;
    private String colliderTag, pathToAnimationSheet;

    public PowerUpPool(float frameLength, int tileWidth, int tileHeight, String colliderTag, String pathToAnimationSheet) {
        super();

        this.frameLength = frameLength;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.colliderTag = colliderTag;
        this.pathToAnimationSheet = pathToAnimationSheet;
    }

    @Override
    protected PowerUp newObject() {
        // System.out.println("[+] Creating new powerUp");
        return new PowerUp(frameLength, tileWidth, tileHeight, colliderTag, pathToAnimationSheet);
    }
}
