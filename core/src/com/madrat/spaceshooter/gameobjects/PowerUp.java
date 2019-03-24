package com.madrat.spaceshooter.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static com.madrat.spaceshooter.MainGame.SCALE_X;
import static com.madrat.spaceshooter.MainGame.SCALE_Y;

public class PowerUp {

    private Animation<TextureRegion> animation;
    private float x, y;
    private float stateTime;

    public boolean remove = false;

    private int preferredWidth, preferredHeight;

    public PowerUp(float x, float y, float frameLength, int tileSize, int preferredWidth, int preferredHeight, Texture explosion) {
        this.x = x;
        this.y = y;

        this.preferredWidth = (int) (preferredWidth * SCALE_X);
        this.preferredHeight = (int) (preferredHeight * SCALE_Y);

        this.stateTime = 0;

        this.animation = new Animation<TextureRegion>(frameLength, TextureRegion.split(explosion, tileSize, tileSize)[0]);
    }

    public void update(float deltaTime) {
        stateTime += deltaTime;
        if (animation.isAnimationFinished(stateTime))
            remove = true;
    }

    public void render(SpriteBatch batch) {
        batch.draw(animation.getKeyFrame(stateTime), x, y, preferredWidth, preferredHeight);
    }
}
