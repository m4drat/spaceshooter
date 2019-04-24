package com.madrat.spaceshooter.gameobjects.poolobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;
import com.madrat.spaceshooter.physics2d.CollisionCircle;
import com.madrat.spaceshooter.physics2d.CollisionRect;
import com.madrat.spaceshooter.utils.Assets;

import static com.madrat.spaceshooter.MainGame.SCALE_FACTOR;

public class Asteroid implements Pool.Poolable {
    public static final float MIN_ASTEROID_SPAWN_TIME = 0.08f;
    public static final float MAX_ASTEROID_SPAWN_TIME = 3f;

    public static final int REWARD = 50;
    public static final float DAMAGE = 0.2f;

    private float asteroidSpeed;

    private Texture asteroidTextureAnimations;
    private CollisionCircle collisionCircle;

    private float x, y;
    public boolean remove = false;

    private Animation<TextureRegion> asteroidAnimation;
    private float stateTime;
    private int radius;

    @Override
    public void reset() {
        // Called when asteroid is freed
        this.remove = false;
    }

    public Asteroid(float animationSpeed, int radius, int realWidth, int realHeight) {
        this.radius = (int) (radius * SCALE_FACTOR);

        this.collisionCircle = new CollisionCircle(this.x, this.y, (int) (this.radius - 5 * SCALE_FACTOR), CollisionRect.colliderTag.enemy);

        asteroidTextureAnimations = Assets.manager.get(Assets.asteroid2Animation, Texture.class);
        asteroidAnimation = new Animation(animationSpeed, TextureRegion.split(asteroidTextureAnimations, realWidth, realHeight)[0]);
    }

    public void setUpAsteroid(float asteroidSpeed, float x) {
        this.asteroidSpeed = asteroidSpeed;

        this.x = x;
        this.y = Gdx.graphics.getHeight();
    }

    public void update(float deltaTime) {
        stateTime += deltaTime;
        y -= asteroidSpeed * deltaTime;

        if (y < -(asteroidTextureAnimations.getHeight() / 2))
            remove = true;

        collisionCircle.move(x + this.radius, y + this.radius);
    }

    public void render(SpriteBatch batch) {
        batch.draw(asteroidAnimation.getKeyFrame(stateTime, true), this.x, this.y, radius * 2, radius * 2);
    }

    public CollisionCircle getCollisionCirlce() {
        return collisionCircle;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getRadius() {
        return this.radius;
    }

    public void dispose() {
        asteroidTextureAnimations.dispose();
        for (TextureRegion region : asteroidAnimation.getKeyFrames()) {
            region.getTexture().dispose();
        }
    }
}