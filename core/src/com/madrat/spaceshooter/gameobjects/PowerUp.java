package com.madrat.spaceshooter.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static com.madrat.spaceshooter.MainGame.SCALE_FACTOR;

public class PowerUp {

    private static final int SPEED = 1;

    private Animation<TextureRegion>[] animations;
    private float x, y;
    private float stateTime;
    private float timeToLive;

    public boolean remove = false;

    private int preferredWidth, preferredHeight;
    private CollisionRect powerUpCollisionRect;

    private Texture animationSheet;
    private int currentAnimation;

    public PowerUp(float x, float y, float frameLength, int tileWidth, int tileHeight, int preferredWidth, int preferredHeight, float timeToLive, String colliderTag, Texture animationSheet) {
        this.x = x;
        this.y = y;

        this.timeToLive = timeToLive;

        this.preferredWidth = (int) (preferredWidth * SCALE_FACTOR);
        this.preferredHeight = (int) (preferredHeight * SCALE_FACTOR);

        this.animationSheet = animationSheet;
        this.stateTime = 0;

        // Create collision rect
        this.powerUpCollisionRect = new CollisionRect(this.x, this.y, this.preferredWidth, this.preferredHeight, colliderTag);

        // Initialize animation array
        this.animations = new Animation[2];
        TextureRegion[][] statesSpriteSheet = TextureRegion.split(animationSheet, tileWidth, tileHeight);
        this.animations[0] = new Animation<TextureRegion>(frameLength, statesSpriteSheet[0]); // Standart animation
        this.animations[1] = new Animation<TextureRegion>(frameLength, statesSpriteSheet[1]); // Blinking animation

        this.currentAnimation = 0;
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

    public void dispose() {
        this.animationSheet.dispose();
    }
}
