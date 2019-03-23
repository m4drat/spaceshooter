package com.madrat.spaceshooter.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.madrat.spaceshooter.MainGame;
import com.madrat.spaceshooter.utils.Assets;

import java.util.ArrayList;

public class PlayerShip extends SpaceShip {

    private static final int healthBarHeight = 5;
    private static final int animationsCount = 2;// 0 - alive; 1 - died

    private Animation<TextureRegion>[] animationsArray;
    private float stateTime;
    private int currentAnimation, score;
    private float animationSpeed;

    private ArrayList<Bullet> bullets;

    private Texture healthBar;

    // Constructor to generate player ship using only file data
    public PlayerShip() {
        // playerShip = new PlayerShip(new Texture(Assets.ship1Animation), 0.14f,1f, 2f, 1, 0.3f, 600f, 300f, "Zapper", 24, 23, 60, 50)
        super(Gdx.app.getPreferences("spacegame").getFloat("maxHealth"), Gdx.app.getPreferences("spacegame").getFloat("maxHealth"), Gdx.app.getPreferences("spacegame").getFloat("damage"), Gdx.app.getPreferences("spacegame").getFloat("delayBetweenShoots"), Gdx.app.getPreferences("spacegame").getFloat("bulletsSpeed"), Gdx.app.getPreferences("spacegame").getFloat("speed"), Gdx.app.getPreferences("spacegame").getString("handle"), Gdx.app.getPreferences("spacegame").getInteger("realShipWidth"), Gdx.app.getPreferences("spacegame").getInteger("realShipHeight"), Gdx.app.getPreferences("spacegame").getInteger("prefferedShipWidth"), Gdx.app.getPreferences("spacegame").getInteger("prefferedShipHeight"));

        Preferences data = Gdx.app.getPreferences("spacegame");

        this.x = Gdx.graphics.getWidth() / 2 - this.prefferedShipWidth / 2;
        this.y = 25;

        this.shipCollisionRect = new CollisionRect(this.x, this.y, this.prefferedShipWidth - 5, this.prefferedShipHeight - 5, "player");
        this.animationSpeed = data.getFloat("animationSpeed");

        this.animationsArray = new Animation[animationsCount];

        // Set first "default" animation
        this.currentAnimation = 0;
        TextureRegion[][] animationSpriteSheet = TextureRegion.split(new Texture(data.getString("animationTexture")), this.realShipWidth, this.realShipHeight);

        // Default fly animation
        this.animationsArray[currentAnimation] = new Animation(animationSpeed, animationSpriteSheet[0]);

        // Explosion animation
        // this.animationsArray[currentAnimation + 1] = new Animation(animationSpeed, animationSpriteSheet[1]);

        // Initialize bullets list
        this.bullets = new ArrayList<Bullet>();

        // Initialize score value
        this.score = 0;

        // Init blank texture to draw
        this.healthBar = new Texture(Assets.blank);
    }

    public PlayerShip(Texture shipAnimations, float animationSpeed, float currentHealth, float maxHealth, float damage, float delayBetweenShoots, float bulletsSpeed, float speed, String handle, int realShipWidth, int realShipHeight, int prefferedShipWidth, int prefferedShipHeight) {
        super(currentHealth, maxHealth, damage, delayBetweenShoots, bulletsSpeed, speed, handle, realShipWidth, realShipHeight, prefferedShipWidth, prefferedShipHeight);

        // Place ship in center
        this.x = Gdx.graphics.getWidth() / 2 - this.prefferedShipWidth / 2;
        this.y = 25;

        // Create collision rect
        this.shipCollisionRect = new CollisionRect(this.x, this.y, this.prefferedShipWidth, this.prefferedShipHeight, "player");

        this.animationSpeed = animationSpeed;

        // Initialize animation array
        this.animationsArray = new Animation[animationsCount];

        // Set first "default" animation
        this.currentAnimation = 0;
        TextureRegion[][] animationSpriteSheet = TextureRegion.split(shipAnimations, this.realShipWidth, this.realShipHeight);

        // Default fly animation
        this.animationsArray[currentAnimation] = new Animation(animationSpeed, animationSpriteSheet[0]);

        // Explosion animation
        // this.animationsArray[currentAnimation + 1] = new Animation(animationSpeed, animationSpriteSheet[1]);

        // Initialize bullets list
        this.bullets = new ArrayList<Bullet>();

        // Initialize score value
        this.score = 0;

        // Init blank texture to draw
        this.healthBar = new Texture(Assets.blank);
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
        if (needToShow && isAlive) {
            stateTime += delta;
            // rendering ship
            batch.draw(this.getAnimationsArray()[this.getCurrentAnimation()].getKeyFrame(this.getStateTime(), true), this.getX(), this.getY(), this.prefferedShipWidth, this.prefferedShipHeight);

            // Draw health bar
            if (this.currentHealth > 0.7f)
                batch.setColor(Color.GREEN);
            else if (this.currentHealth <= 0.7f && this.currentHealth > 0.3)
                batch.setColor(Color.ORANGE);
            else if (this.currentHealth <= 0.3f)
                batch.setColor(Color.RED);
            batch.draw(healthBar, 0, 0, Gdx.graphics.getWidth() * this.currentHealth / this.maxHealth, healthBarHeight);
            batch.setColor(Color.WHITE);
        }
    }

    public void shoot(float delta) {
        if (isAlive) {
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
        this.healthBar.dispose();

        // Dispose bullets textures
        for (Bullet bullet : this.bullets) {
            bullet.getBulletTexture().dispose();
        }
    }
}
