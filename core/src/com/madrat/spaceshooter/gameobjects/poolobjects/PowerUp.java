package com.madrat.spaceshooter.gameobjects.poolobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;
import com.madrat.spaceshooter.physics2d.CollisionRect;
import com.madrat.spaceshooter.utils.Assets;

import static com.madrat.spaceshooter.MainGame.SCALE_FACTOR;

public class PowerUp implements Pool.Poolable {

    private static final float SPEED = 1.5f;

    private Animation<TextureRegion>[] animations;
    private float x, y;
    private float stateTime;
    private float timeToLive;

    public boolean remove = false;

    private int preferredWidth, preferredHeight;
    private CollisionRect powerUpCollisionRect;

    private int currentAnimation;

    @Override
    public void reset() {
        this.currentAnimation = 0;
        this.stateTime = 0f;
        this.remove = false;
    }

    public PowerUp(float frameLength, int tileWidth, int tileHeight, CollisionRect.colliderTag tag, String pathToAnimationSheet) {
        // Create collision rect
        this.powerUpCollisionRect = new CollisionRect(0, 0, 0, 0, tag);

        // Animation default state time
        this.stateTime = 0;
        this.currentAnimation = 0;

        // Initialize animation array
        this.animations = new Animation[2];
        TextureRegion[][] statesSpriteSheet = TextureRegion.split(Assets.manager.get(pathToAnimationSheet, Texture.class), tileWidth, tileHeight);
        this.animations[0] = new Animation<TextureRegion>(frameLength, statesSpriteSheet[0]); // Default animation
        this.animations[1] = new Animation<TextureRegion>(frameLength, statesSpriteSheet[1]); // Blinking animation
    }

    public void setupPowerUp(float x, float y, int preferredWidth, int preferredHeight, float timeToLive) {
        // setUp width/height
        this.preferredWidth = (int) (preferredWidth * SCALE_FACTOR);
        this.preferredHeight = (int) (preferredHeight * SCALE_FACTOR);

        // setUp collision rect
        powerUpCollisionRect.move(x, y);
        powerUpCollisionRect.resize(this.preferredWidth, this.preferredHeight);

        this.x = x;
        this.y = y;

        this.timeToLive = timeToLive;
    }

    public void update(float deltaTime) {

        this.y -= (int) (SPEED * SCALE_FACTOR);
        this.powerUpCollisionRect.move(this.x, this.y);

        stateTime += deltaTime;
        if (timeToLive < stateTime)
            remove = true;
        else if (timeToLive / 2 < stateTime)
            this.currentAnimation = 1;

        if (this.y + this.preferredHeight < 0)
            remove = true;
    }

    public void render(SpriteBatch batch) {
        batch.draw(animations[currentAnimation].getKeyFrame(stateTime, true), x, y, preferredWidth, preferredHeight);
    }

    public CollisionRect getPowerUpCollisionRect() {
        return powerUpCollisionRect;
    }
}
