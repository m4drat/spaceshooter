package com.madrat.spaceshooter.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Explosion {

    public static final int OFFSET = 8;

    private Animation<TextureRegion> animation;
    private float x, y;
    private float stateTime;

    private float frameLength;
    private int tileSize; // 96 for explosion2.png and 64 for explosion1.png
    private float scale; // 2f for explosion1.png and 0.9f for explosion2.png

    public boolean remove = false;

    public Explosion(float x, float y, float frameLength, int tileSize, float scale, Texture explosion) {
        this.x = x;
        this.y = y;

        this.frameLength = frameLength;
        this.tileSize = tileSize;
        this.scale = scale;

        this.stateTime = 0;

        this.animation = new Animation<TextureRegion>(frameLength, TextureRegion.split(explosion, tileSize, tileSize)[0]);
    }

    public void update(float deltaTime) {
        stateTime += deltaTime;
        if (animation.isAnimationFinished(stateTime))
            remove = true;
    }

    public void render(SpriteBatch batch) {
        batch.draw(animation.getKeyFrame(stateTime), x, y, (tileSize) * this.scale, (tileSize) * this.scale);
    }

}
