package com.madrat.spaceshooter.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.madrat.spaceshooter.utils.Assets;

public class Explosion {

    public static final float SCALE = 0.9f; // 2f for explosion1.png
    public static final float FRAME_LENGTH = 0.12f;
    public static final int OFFSET = 8;
    public static final int SIZE = 96; // 96 for explosion2.png and 64 for explosion1.png

    private static Animation<TextureRegion> animation;
    private float x, y;
    private float stateTime;

    public boolean remove = false;

    public Explosion(float x, float y) {
        this.x = x;
        this.y = y;

        stateTime = 0;
        if (animation == null)
            animation = new Animation<TextureRegion>(FRAME_LENGTH, TextureRegion.split(new Texture(Assets.explosion2), SIZE, SIZE)[0]);
    }

    public void update(float deltaTime) {
        stateTime += deltaTime;
        if (animation.isAnimationFinished(stateTime))
            remove = true;
    }

    public void render(SpriteBatch batch) {
        batch.draw(animation.getKeyFrame(stateTime), x, y, (int) (SIZE) * SCALE, (int) (SIZE) * SCALE);
    }

}
