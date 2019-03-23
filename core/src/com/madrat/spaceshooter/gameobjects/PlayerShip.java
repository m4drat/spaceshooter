package com.madrat.spaceshooter.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.madrat.spaceshooter.MainGame;
import com.madrat.spaceshooter.utils.Assets;

import java.util.ArrayList;

public class PlayerShip extends SpaceShip {

    private static final int healthBarHeight = 5;
    private static final float animationSpeed = 0.2f;
    private static final int animationsCount = 2;// 0 - alive; 1 - died

    private Animation<TextureRegion>[] animationsArray;
    private float stateTime;
    private int currentAnimation, score;

    private ArrayList<Bullet> bullets;

    private Texture healthBar;

    public PlayerShip(Texture shipAnimations, float currentHealth, float maxHealth, int damage, float delayBetweenShoots, float bulletsSpeed, float speed, String handle, int realShipWidth, int realShipHeight, int prefferedShipWidth, int prefferedShipHeight) {
        super(currentHealth, maxHealth, damage, delayBetweenShoots, bulletsSpeed, speed, handle, realShipWidth, realShipHeight, prefferedShipWidth, prefferedShipHeight);

        // Place ship in center
        this.x = Gdx.graphics.getWidth() / 2 - this.prefferedShipWidth / 2;
        this.y = 25;

        // Create collision rect
        this.shipCollisionRect = new CollisionRect(this.x, this.y, this.prefferedShipWidth, this.prefferedShipHeight, "player");

        // Initialize animation array
        animationsArray = new Animation[animationsCount];

        // Set first "default" animation
        currentAnimation = 0;
        TextureRegion[][] animationSpriteSheet = TextureRegion.split(shipAnimations, this.realShipWidth, this.realShipHeight);

        // Default fly animation
        animationsArray[currentAnimation] = new Animation(animationSpeed, animationSpriteSheet[0]);

        //Explosion animation
        animationsArray[currentAnimation + 1] = new Animation(animationSpeed, animationSpriteSheet[1]);

        // Initialize bullets list
        bullets = new ArrayList<Bullet>();

        // Initialize score value
        this.score = 0;

        // Init blank texture to draw
        healthBar = new Texture(Assets.blank);
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

    public void correctBounds() {
        // Left bounds
        if (this.x < 0)
            this.x = 0;

        // Right bounds
        if (this.x > MainGame.GENERAL_WIDTH - this.prefferedShipWidth)
            this.x = MainGame.GENERAL_WIDTH - this.prefferedShipWidth;

        // Bottom bounds
        if (this.y < 0)
            this.y = 0;

        // Top bounds
        if (this.y > MainGame.GENERAL_HEIGHT - this.prefferedShipHeight)
            this.y = MainGame.GENERAL_HEIGHT - this.prefferedShipHeight;
    }

    public void draw(SpriteBatch batch, float delta) {
        if (needToShow) {
            stateTime += delta;
            // rendering ship
            batch.draw(this.getAnimationsArray()[this.getCurrentAnimation()].getKeyFrame(this.getStateTime(), true), this.getX(), this.getY(), this.prefferedShipWidth, this.prefferedShipHeight);

            // Draw health bar
            batch.draw(healthBar, 0, 0, Gdx.graphics.getWidth() * this.currentHealth, healthBarHeight);
        }
    }

    public void shoot(float delta) {
        if (this.lastShoot > delayBetweenShoots) {
            // Add two bullets
            bullets.add(new Bullet(this.bulletsSpeed, this.x + this.prefferedShipWidth - this.prefferedShipWidth / 5, this.y + this.prefferedShipHeight / 2, "player"));
            bullets.add(new Bullet(this.bulletsSpeed, this.x + this.prefferedShipWidth / 5 - 3, this.y + this.prefferedShipHeight / 2, "player"));

            // set Shoot timer to 0
            this.lastShoot = 0;
        } else {
            lastShoot += delta;
        }
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

    public Texture getHealthBar() {
        return healthBar;
    }

    public void dispose() {

        // Dispose health bar
        healthBar.dispose();

        // Dispose animation
        for (Animation<TextureRegion> region : animationsArray) {
            for (TextureRegion texture : region.getKeyFrames()) {
                texture.getTexture().dispose();
            }
        }

        // Dispose bullets textures
        for (Bullet bullet : bullets) {
            bullet.getBulletTexture().dispose();
        }
    }
}
