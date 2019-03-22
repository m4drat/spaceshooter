package com.madrat.spaceshooter.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.madrat.spaceshooter.MainGame;

import java.util.ArrayList;

public class PlayerShip extends SpaceShip {

    private static final float animationSpeed = 0.2f;
    private static final int animationsCount = 2;// 0 - alive; 1 - died

    private Animation<TextureRegion>[] animationsArray;
    private float stateTime;
    private int currentAnimation, score;

    private ArrayList<Bullet> bullets;

    public PlayerShip(Texture shipAnimations, int lives, int maxLives, int damage, float delayBetweenShoots, float bulletsSpeed, float speed, String handle) {
        super(lives, maxLives, damage, delayBetweenShoots, bulletsSpeed, speed, handle);

        // Place ship in center
        this.x = Gdx.graphics.getWidth() / 2 - preferred_SHIP_WIDTH / 2;
        this.y = 25;

        // Initialize animation array
        animationsArray = new Animation[animationsCount];

        // Set first "default" animation
        currentAnimation = 0;
        TextureRegion[][] animationSpriteSheet = TextureRegion.split(shipAnimations, SHIP_WIDTH, SHIP_HEIGHT);

        // Default fly animation
        animationsArray[currentAnimation] = new Animation(animationSpeed, animationSpriteSheet[0]);

        //Explosion animation
        animationsArray[currentAnimation + 1] = new Animation(animationSpeed, animationSpriteSheet[1]);

        // Initialize bullets list
        bullets = new ArrayList<Bullet>();

        // Initialize score value
        this.score = 0;
    }

    public Animation<TextureRegion>[] getAnimationsArray() {
        return animationsArray;
    }

    public int getCurrentAnimation() {
        return currentAnimation;
    }

    public float getStateTime() {
        return stateTime;
    }

    public void incStateTime(float dt) {
        this.stateTime += dt;
    }

    public void correctBounds() {
        // Left bounds
        if (this.x < 0)
            this.x = 0;

        // Right bounds
        if (this.x > MainGame.GENERAL_WIDTH - preferred_SHIP_WIDTH)
            this.x = MainGame.GENERAL_WIDTH - preferred_SHIP_WIDTH;

        // Bottom bounds
        if (this.y < 0)
            this.y = 0;

        // Top bounds
        if (this.y > MainGame.GENERAL_HEIGHT - preferred_SHIP_HEIGHT)
            this.y = MainGame.GENERAL_HEIGHT - preferred_SHIP_HEIGHT;
    }

    public void draw(SpriteBatch batch, float delta) {
        if (needToShow) {
            // rendering ship
            batch.draw(this.getAnimationsArray()[this.getCurrentAnimation()].getKeyFrame(this.getStateTime(), true), this.getX(), this.getY(), this.preferred_SHIP_WIDTH, this.preferred_SHIP_HEIGHT);
        }
    }

    public void shoot() {
        bullets.add(new Bullet(this.bulletsSpeed, this.x + preferred_SHIP_WIDTH - preferred_SHIP_WIDTH / 5, this.y + preferred_SHIP_HEIGHT / 2, "player"));
        bullets.add(new Bullet(this.bulletsSpeed, this.x + preferred_SHIP_WIDTH / 5 - 3, this.y + preferred_SHIP_HEIGHT / 2, "player"));
    }

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int newScore) {
        score = newScore;
    }
}
