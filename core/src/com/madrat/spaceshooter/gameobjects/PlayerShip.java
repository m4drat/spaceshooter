package com.madrat.spaceshooter.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.madrat.spaceshooter.MainGame;
import com.madrat.spaceshooter.utils.Assets;

import java.util.ArrayList;

import static com.madrat.spaceshooter.MainGame.SCALE_FACTOR;

public class PlayerShip extends SpaceShip {

    private static final int healthBarHeight = 5;

    private Animation<TextureRegion> shipAnimation;
    private float stateTime;
    private int score;

    private final BulletPool bulletPool = new BulletPool();
    private Array<Bullet> activeBullets;
    private Texture healthBar;
    private float newX, newY;

    private boolean isUnderAmmoPowerUp, isUnderShieldPowerUp;

    private String bulletTexturePath;
    private int preferredBulletHeight, preferredBulletWidth;
    private int colliderWidth, colliderHeight;

    private Texture animationSheet;

    // Constructor to generate player ship using only file data
    public PlayerShip() {
        // playerShip = new PlayerShip(new Texture(Assets.ship1Animation), 0.14f,1f, 2f, 1, 0.3f, 600f, 300f, "Zapper", 24, 23, 60, 50)
        super(Gdx.app.getPreferences("spacegame").getFloat("maxHealth"), Gdx.app.getPreferences("spacegame").getFloat("maxHealth"), Gdx.app.getPreferences("spacegame").getFloat("damage"), Gdx.app.getPreferences("spacegame").getFloat("delayBetweenShoots"), Gdx.app.getPreferences("spacegame").getFloat("bulletsSpeed"), Gdx.app.getPreferences("spacegame").getFloat("speed"), Gdx.app.getPreferences("spacegame").getString("handle"), Gdx.app.getPreferences("spacegame").getInteger("realShipWidth"), Gdx.app.getPreferences("spacegame").getInteger("realShipHeight"), Gdx.app.getPreferences("spacegame").getInteger("preferredShipWidth"), Gdx.app.getPreferences("spacegame").getInteger("preferredShipHeight"));

        Preferences data = Gdx.app.getPreferences("spacegame");
        this.bulletTexturePath = data.getString("bulletTexture");
        this.preferredBulletHeight = data.getInteger("preferredBulletHeight");
        this.preferredBulletWidth = data.getInteger("preferredBulletWidth");

        this.maxHealing = data.getFloat("maxHealing");

        this.x = Gdx.graphics.getWidth() / 2 - this.preferredShipWidth / 2;
        this.y = 25;

        this.colliderWidth = (int) (data.getInteger("colliderWidth") * SCALE_FACTOR);
        this.colliderHeight = (int) (data.getInteger("colliderHeight") * SCALE_FACTOR);

        this.shipCollisionRect = new CollisionRect(this.x + ((this.preferredShipWidth - this.colliderWidth) * SCALE_FACTOR), this.y + ((this.preferredBulletHeight - this.colliderHeight) * SCALE_FACTOR), this.colliderWidth, this.colliderHeight, "player");
        this.animationSheet = Assets.manager.get(data.getString("animationTexture"), Texture.class);
        this.shipAnimation = new Animation<TextureRegion>(data.getFloat("frameLength", 0.14f), TextureRegion.split(this.animationSheet, this.realShipWidth, this.realShipHeight)[0]);
        setup();
    }

    public PlayerShip(Texture shipAnimations, float frameLength, float currentHealth, float maxHealth, float damage, float delayBetweenShoots, float bulletsSpeed, float speed, String handle, int realShipWidth, int realShipHeight, int prefferedShipWidth, int prefferedShipHeight) {
        super(currentHealth, maxHealth, damage, delayBetweenShoots, bulletsSpeed, speed, handle, realShipWidth, realShipHeight, prefferedShipWidth, prefferedShipHeight);

        // Place ship in center
        this.x = Gdx.graphics.getWidth() / 2 - this.preferredShipWidth / 2;
        this.y = 25;

        // Create collision rect
        this.shipCollisionRect = new CollisionRect(this.x, this.y, this.preferredShipWidth, this.preferredShipHeight, "player");

        // Initialize animation array
        this.shipAnimation = new Animation<TextureRegion>(frameLength, TextureRegion.split(shipAnimations, this.realShipWidth, this.realShipHeight)[0]);
        setup();
    }

    private void setup() {
        // Initialize activeBullets list
        this.activeBullets = new Array<Bullet>();

        // Initialize score value
        this.score = 0;

        // Init blank texture to draw
        this.healthBar = Assets.manager.get(Assets.blank, Texture.class);
    }

    public void correctBounds() {
        // Left bounds
        if (this.x < 0)
            this.x = 0;

        // Right bounds
        if (this.x > MainGame.GENERAL_WIDTH - this.preferredShipWidth)
            this.x = MainGame.GENERAL_WIDTH - this.preferredShipWidth;

        // Bottom bounds
        if (this.y < 0)
            this.y = 0;

        // Top bounds
        if (this.y > MainGame.GENERAL_HEIGHT - this.preferredShipHeight)
            this.y = MainGame.GENERAL_HEIGHT - this.preferredShipHeight;
    }

    public void draw(SpriteBatch batch, float delta) {
        if (needToShow && isAlive) {
            stateTime += delta;
            // rendering ship
            batch.draw(shipAnimation.getKeyFrame(stateTime, true), this.x, this.y, this.preferredShipWidth, this.preferredShipHeight);

            // Draw health bar
            if (this.currentHealth > 0.7f)
                batch.setColor(Color.GREEN);
            else if (this.currentHealth <= 0.7f && this.currentHealth > 0.3)
                batch.setColor(Color.ORANGE);
            else if (this.currentHealth <= 0.3f)
                batch.setColor(Color.RED);
            batch.draw(healthBar, 0, 0, Gdx.graphics.getWidth() * this.currentHealth / this.maxHealth, healthBarHeight * SCALE_FACTOR);
            batch.setColor(Color.WHITE);
        }
    }


    public void shoot(float delta) {
        if (isAlive) {
            if (this.lastShoot > delayBetweenShoots) {

                // Add first activeBullet
                Bullet newBullet1 = bulletPool.obtain();
                newBullet1.setupBullet(this.bulletsSpeed, this.x + this.preferredShipWidth - this.preferredShipWidth / 5, this.y + this.preferredShipHeight / 2, preferredBulletWidth, preferredBulletHeight, "player");
                activeBullets.add(newBullet1);

                // Add second activeBullet
                Bullet newBullet2 = bulletPool.obtain();
                newBullet2.setupBullet(this.bulletsSpeed, this.x + this.preferredShipWidth / 5 - 3, this.y + this.preferredShipHeight / 2, preferredBulletWidth, preferredBulletHeight, "player");
                activeBullets.add(newBullet2);

                System.out.println("[+] Objects in bulletPool: " + bulletPool.getFree());

                // set Shoot timer to 0
                this.lastShoot = 0;
            } else {
                lastShoot += delta;
            }
        }
    }

    // y = mx + b
    // x = (-y + b) / -m

    public void updateCollisionRect() {
        this.shipCollisionRect.move(this.x + ((this.preferredShipWidth - this.colliderWidth)) / 2, this.y + ((this.preferredShipHeight - this.colliderHeight)) / 2);
    }

    public void performInput(float deltaTime) {
        if (Gdx.input.isTouched()) {
            newX = Gdx.input.getX();
            newY = Gdx.input.getY();

            float xToGo, yToGo;

            xToGo = MoveTowards(this.x, newX - this.preferredShipWidth / 2, deltaTime * this.speed);
            yToGo = MoveTowards(this.y, -newY + Gdx.graphics.getHeight() - this.preferredShipHeight / 2, deltaTime * this.speed);

            this.x = xToGo;
            this.y = yToGo;
            // float m = newY - this.y / newX - this.x; // calculate m
            // float b = -(m * newX) + newY; // calculate b
            // this.x = (newX - this.preferredShipWidth / 2); // (newX - this.preferredShipWidth / 2)
            // this.y = (-newY + Gdx.graphics.getHeight() - this.preferredShipHeight / 2); // (-newY + Gdx.graphics.getHeight() - this.preferredShipHeight / 2)

            // check for bounds
            this.correctBounds();
        }
    }

    public float MoveTowards(float current, float target, float maxDelta) {
        if (Math.abs(target - current) <= maxDelta) {
            return target;
        }
        return current + Math.signum(target - current) * maxDelta;
    }


    public Array<Bullet> getActiveBullets() {
        return activeBullets;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int newScore) {
        score = newScore;
    }

    public boolean isUnderAmmoPowerUp() {
        return isUnderAmmoPowerUp;
    }

    public void setUnderAmmoPowerUp(boolean underAmmoPowerUp) {
        isUnderAmmoPowerUp = underAmmoPowerUp;
    }

    public boolean isUnderShieldPowerUp() {
        return isUnderShieldPowerUp;
    }

    public void setUnderShieldPowerUp(boolean underShieldPowerUp) {
        isUnderShieldPowerUp = underShieldPowerUp;
    }

    public void setActiveBullets(Array<Bullet> activeBullets) {
        this.activeBullets = activeBullets;
    }

    public BulletPool getBulletPool() {
        return bulletPool;
    }

    public void dispose() {
        // Dispose health bar
        this.animationSheet.dispose();
        this.healthBar.dispose();
    }
}
