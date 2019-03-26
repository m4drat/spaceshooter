package com.madrat.spaceshooter.gameobjects.poolobjects.poweruppools;

import com.badlogic.gdx.utils.Pool;

public class PowerUpPool extends Pool<PowerUp> {

    private float frameLength, timeToLive;
    private int tileWidth, tileHeight, preferredWidth, preferredHeight;
    private String colliderTag, pathToAnimationSheet;

    public PowerUpPool(float frameLength, int tileWidth, int tileHeight, int preferredWidth, int preferredHeight, String colliderTag, String pathToAnimationSheet) {
        super();

        this.frameLength = frameLength;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.preferredWidth = preferredWidth;
        this.preferredHeight = preferredHeight;
        this.timeToLive = timeToLive;
        this.colliderTag = colliderTag;
        this.pathToAnimationSheet = pathToAnimationSheet;
    }

    @Override
    protected PowerUp newObject() {
        System.out.println("[+] Creating new powerUp");
        return new PowerUp(frameLength, tileWidth, tileHeight, colliderTag, pathToAnimationSheet);
    }
}
