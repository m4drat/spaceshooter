package com.madrat.spaceshooter.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static com.madrat.spaceshooter.MainGame.SCALE_FACTOR;

public class Explosion {

    private Texture explosion;
    private Animation<TextureRegion> animation;
    private float x, y;
    private float stateTime;

    // private int tileSize; // 96 for explosion2.png and 64 for explosion1.png
    // private float scale; // 2f for explosion1.png and 0.9f for explosion2.png

    private int preferredWidth, preferredHeight;

    public boolean remove = false;

    public Explosion(float x, float y, float frameLength, int tileSize, int preferredWidth, int preferredHeight, Texture explosion) {
        this.x = x;
        this.y = y;

        this.explosion = explosion;

        this.preferredWidth = (int) (preferredWidth * SCALE_FACTOR);
        this.preferredHeight = (int) (preferredHeight * SCALE_FACTOR);

        this.stateTime = 0;

        this.animation = new Animation<TextureRegion>(frameLength, TextureRegion.split(this.explosion, tileSize, tileSize)[0]);
    }

    public void update(float deltaTime) {
        stateTime += deltaTime;
        if (animation.isAnimationFinished(stateTime))
            remove = true;
    }

    public void render(SpriteBatch batch) {
        batch.draw(animation.getKeyFrame(stateTime), x, y, this.preferredWidth, this.preferredHeight);
    }

    public void dispose() {
        this.explosion.dispose();
    }
}
