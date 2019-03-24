package com.madrat.spaceshooter.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.madrat.spaceshooter.utils.Assets;

import static com.madrat.spaceshooter.MainGame.SCALE_FACTOR;
import static com.madrat.spaceshooter.MainGame.SCALE_X;
import static com.madrat.spaceshooter.MainGame.SCALE_Y;

public class Asteroid {
    public static final float MIN_ASTEROID_SPAWN_TIME = 2f;
    public static final float MAX_ASTEROID_SPAWN_TIME = 6f;

    public static final int REWARD = 50;
    public static final float DAMAGE = 0.2f;

    private float asteroidSpeed;

    private static Texture asteroidTextureAnimations;
    private CollisionRect rect;

    private float x, y;
    public boolean remove = false;

    private Animation<TextureRegion> asteroidAnimation;
    private float stateTime;
    private int preferredWidth, preferredHeight;

    public Asteroid(float asteroidSpeed, float x, float animationSpeed, int preferredWidth, int preferredHeight, int realWidth, int realHeight) {
        this.asteroidSpeed = asteroidSpeed;
        this.x = x;
        this.y = Gdx.graphics.getHeight();

        this.preferredWidth = (int) (preferredWidth * SCALE_FACTOR);
        this.preferredHeight = (int) (preferredHeight * SCALE_FACTOR);

        this.rect = new CollisionRect(x, y, this.preferredWidth, this.preferredHeight, "enemy");

        asteroidTextureAnimations = new Texture(Assets.asteroid2Animation);
        asteroidAnimation = new Animation(animationSpeed, TextureRegion.split(asteroidTextureAnimations, realWidth, realHeight)[0]);
    }

    public void update(float deltaTime) {
        stateTime += deltaTime;
        y -= asteroidSpeed * deltaTime;

        if (y < -(asteroidTextureAnimations.getHeight() / 2))
            remove = true;

        rect.move(x, y);
    }

    public void render(SpriteBatch batch) {
        batch.draw(asteroidAnimation.getKeyFrame(stateTime, true), this.x, this.y, this.preferredWidth, this.preferredHeight);
    }

    public CollisionRect getCollisionRect() {
        return rect;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getPreferredWidth() {
        return preferredWidth;
    }

    public int getPreferredHeight() {
        return preferredHeight;
    }
}