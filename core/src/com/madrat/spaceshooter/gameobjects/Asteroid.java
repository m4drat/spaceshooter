package com.madrat.spaceshooter.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.madrat.spaceshooter.utils.Assets;

public class Asteroid {
    public static final int REWARD = 50;
    public static final float DAMAGE = 0.2f;
    public static final int WIDTH = 64, HEIGHT = 64;

    private float asteroidSpeed;
    private static Texture asteroidTextureAnimations;
    private CollisionRect rect;

    private float x, y;
    public boolean remove = false;

    private static final float animationSpeed = 0.07f;
    private static final int animationsCount = 2;// 1 - default; 2 - explosion

    private Animation<TextureRegion>[] animationsArray;
    private float stateTime;
    private int currentAnimation;

    public Asteroid(float asteroidSpeed, float x) {
        this.asteroidSpeed = asteroidSpeed;
        this.x = x;
        this.y = Gdx.graphics.getHeight();

        this.rect = new CollisionRect(x, y, WIDTH - 12, HEIGHT - 12, "enemy");

        asteroidTextureAnimations = new Texture(Assets.asteroid2Animation);
        animationsArray = new Animation[animationsCount];
        currentAnimation = 0;
        TextureRegion[][] animationSpriteSheet = TextureRegion.split(asteroidTextureAnimations, WIDTH, HEIGHT);
        animationsArray[currentAnimation] = new Animation(animationSpeed, animationSpriteSheet[0]);
        animationsArray[currentAnimation + 1] = new Animation(animationSpeed, animationSpriteSheet[1]);
    }

    public Animation<TextureRegion>[] getAnimationsArray() {
        return animationsArray;
    }

    public int getCurrentAnimation() {
        return currentAnimation;
    }

    public void update(float deltaTime) {
        stateTime += deltaTime;
        y -= asteroidSpeed * deltaTime;

        if (y < -(asteroidTextureAnimations.getHeight() / 2))
            remove = true;

        rect.move(x, y);
    }

    public void render(SpriteBatch batch) {
        batch.draw(this.getAnimationsArray()[this.getCurrentAnimation()].getKeyFrame(stateTime, true), this.x, this.y, WIDTH, HEIGHT);
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
}