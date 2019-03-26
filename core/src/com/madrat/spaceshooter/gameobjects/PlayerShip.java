package com.madrat.spaceshooter.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.madrat.spaceshooter.MainGame;
import com.madrat.spaceshooter.gameobjects.poolobjects.Bullet;
import com.madrat.spaceshooter.gameobjects.poolobjects.BulletPool;
import com.madrat.spaceshooter.physics2d.CollisionRect;
import com.madrat.spaceshooter.utils.Assets;

import static com.madrat.spaceshooter.MainGame.SCALE_FACTOR;

public class PlayerShip extends SpaceShip {

    private static final int healthBarHeight = 5;

    // 0 - default ship State
    // 1 - ship damage
    // 2 - shield under attack
    // 3 - shield Activated
    // 4 - shield destroyed
    public enum animationState {
        defaultFlyAnimation,
        shipUnderAttackAnimation,
        shieldDefaultAnimation,
        shieldJustActivatedAnimation,
        shieldDestroyedAnimation
    }

    private Animation<TextureRegion>[] shipAnimations;
    private animationState currentAnimation;
    private float stateTime;

    private int score;

    private String bulletTexturePath;
    private int preferredBulletHeight, preferredBulletWidth;
    private final BulletPool bulletPool = new BulletPool();
    private Array<Bullet> activeBullets;

    private Color lightBlue;
    private Texture healthBar;
    private float newX, newY;

    private int colliderXcoordOffset, colliderYcoordOffset;
    private int colliderWidth, colliderHeight;

    private Texture animationSheet;

    private boolean isShieldActive, isAmmoActive;
    private float shieldHealthMax, currentShieldHealth, shieldLifeTime, shieldStateTime;

    // Constructor to generate player ship using only file data
    public PlayerShip() {
        // playerShip = new PlayerShip(new Texture(Assets.ship1Animation), 0.14f,1f, 2f, 1, 0.3f, 600f, 300f, "Zapper", 24, 23, 60, 50)
        super(Gdx.app.getPreferences("spacegame").getFloat("maxHealth"), Gdx.app.getPreferences("spacegame").getFloat("maxHealth"), Gdx.app.getPreferences("spacegame").getFloat("damage"), Gdx.app.getPreferences("spacegame").getFloat("delayBetweenShootsBullets"), Gdx.app.getPreferences("spacegame").getFloat("bulletsSpeed"), Gdx.app.getPreferences("spacegame").getFloat("speed"), Gdx.app.getPreferences("spacegame").getString("handle"), Gdx.app.getPreferences("spacegame").getInteger("realShipWidth"), Gdx.app.getPreferences("spacegame").getInteger("realShipHeight"), Gdx.app.getPreferences("spacegame").getInteger("preferredShipWidth"), Gdx.app.getPreferences("spacegame").getInteger("preferredShipHeight"));

        Preferences data = Gdx.app.getPreferences("spacegame");
        this.bulletTexturePath = data.getString("bulletTexture");
        this.preferredBulletHeight = data.getInteger("preferredBulletHeight");
        this.preferredBulletWidth = data.getInteger("preferredBulletWidth");

        this.maxHealing = data.getFloat("maxHealing");

        this.shieldLifeTime = 15f;
        this.shieldHealthMax = this.maxHealth / 2;
        this.currentShieldHealth = 0;
        this.isShieldActive = false;
        this.isAmmoActive = false;
        this.lightBlue = new Color(0x1f8be2ff);

        this.x = Gdx.graphics.getWidth() / 2 - this.preferredShipWidth / 2;
        this.y = 25;

        this.colliderWidth = (int) (data.getInteger("colliderWidth") * SCALE_FACTOR);
        this.colliderHeight = (int) (data.getInteger("colliderHeight") * SCALE_FACTOR);

        this.colliderXcoordOffset = (int) (data.getInteger("colliderXcoordOffset", 0) * SCALE_FACTOR);
        this.colliderYcoordOffset = (int) (data.getInteger("colliderYcoordOffset", 0) * SCALE_FACTOR);

        this.shipCollisionRect = new CollisionRect(this.x + ((this.preferredShipWidth - this.colliderWidth) * SCALE_FACTOR) + this.colliderXcoordOffset, this.y + ((this.preferredBulletHeight - this.colliderHeight) * SCALE_FACTOR) + this.colliderYcoordOffset, this.colliderWidth, this.colliderHeight, "player");

        this.currentAnimation = animationState.defaultFlyAnimation;
        this.shipAnimations = new Animation[5];
        this.animationSheet = Assets.manager.get(data.getString("animationTexture"), Texture.class);
        TextureRegion[][] statesSpriteSheet = TextureRegion.split(this.animationSheet, this.realShipWidth, this.realShipHeight);
        // Default animation
        this.shipAnimations[0] = new Animation<TextureRegion>(data.getFloat("frameLength", 0.14f), statesSpriteSheet[0]);
        // Damage animation
        this.shipAnimations[1] = new Animation<TextureRegion>(data.getFloat("frameLength", 0.1f), statesSpriteSheet[1]);
        // shield default animation
        this.shipAnimations[2] = new Animation<TextureRegion>(data.getFloat("frameLength", 0.14f), statesSpriteSheet[2]);
        // shield just activated animation
        this.shipAnimations[3] = new Animation<TextureRegion>(data.getFloat("frameLength", 0.14f), statesSpriteSheet[3]);
        // shield destroyed animation
        this.shipAnimations[4] = new Animation<TextureRegion>(data.getFloat("frameLength", 0.14f), statesSpriteSheet[4]);
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

            // render ship in appropriate state
            switch (this.currentAnimation) {
                case defaultFlyAnimation:
                    batch.draw(shipAnimations[0].getKeyFrame(stateTime, true), this.x, this.y, this.preferredShipWidth, this.preferredShipHeight);
                    break;
                case shipUnderAttackAnimation:
                    batch.draw(shipAnimations[1].getKeyFrame(stateTime, false), this.x, this.y, this.preferredShipWidth, this.preferredShipHeight);
                    if (shipAnimations[1].isAnimationFinished(stateTime))
                        setCurrentAnimation(animationState.defaultFlyAnimation);
                    break;
                case shieldDefaultAnimation:
                    batch.draw(shipAnimations[2].getKeyFrame(stateTime, true), this.x, this.y, this.preferredShipWidth, this.preferredShipHeight);
                    if (!isShieldActive)
                        setCurrentAnimation(animationState.shieldDestroyedAnimation);
                    break;
                case shieldJustActivatedAnimation:
                    batch.draw(shipAnimations[3].getKeyFrame(stateTime, false), this.x, this.y, this.preferredShipWidth, this.preferredShipHeight);
                    if (shipAnimations[3].isAnimationFinished(stateTime))
                        setCurrentAnimation(animationState.shieldDefaultAnimation);
                    break;
                case shieldDestroyedAnimation:
                    batch.draw(shipAnimations[4].getKeyFrame(stateTime, false), this.x, this.y, this.preferredShipWidth, this.preferredShipHeight);
                    if (shipAnimations[4].isAnimationFinished(stateTime))
                        setCurrentAnimation(animationState.defaultFlyAnimation);
                    break;
                default:
                    batch.draw(shipAnimations[0].getKeyFrame(stateTime, true), this.x, this.y, this.preferredShipWidth, this.preferredShipHeight);
                    break;
            }

            // Draw health bar
            if (this.currentHealth > 0.7f * maxHealth)
                batch.setColor(Color.GREEN);
            else if (this.currentHealth <= 0.7f * maxHealth && this.currentHealth > 0.3 * maxHealth)
                batch.setColor(Color.ORANGE);
            else if (this.currentHealth <= 0.3f * maxHealth)
                batch.setColor(Color.RED);
            batch.draw(healthBar, 0, 0, Gdx.graphics.getWidth() * this.currentHealth / this.maxHealth, healthBarHeight * SCALE_FACTOR);

            // Draw shield bar
            if (isShieldActive) {
                batch.setColor(lightBlue);
                batch.draw(healthBar, 0, healthBarHeight * SCALE_FACTOR, Gdx.graphics.getWidth() * this.currentShieldHealth / this.shieldHealthMax, healthBarHeight * SCALE_FACTOR);
            }

            // Set default color
            batch.setColor(Color.WHITE);
        }
    }


    public void shoot(float delta) {
        if (isAlive) {
            if (this.lastBulletShoot > delayBetweenShootsBullets) {

                // set Shoot timer to 0
                this.lastBulletShoot = 0;

                // Add first activeBullet
                Bullet newBullet1 = bulletPool.obtain();
                newBullet1.setupBullet(this.bulletsSpeed, this.x + this.preferredShipWidth - this.preferredShipWidth / 5, this.y + this.preferredShipHeight / 2, preferredBulletWidth, preferredBulletHeight, "player");
                activeBullets.add(newBullet1);

                // Add second activeBullet
                Bullet newBullet2 = bulletPool.obtain();
                newBullet2.setupBullet(this.bulletsSpeed, this.x + this.preferredShipWidth / 5 - 3, this.y + this.preferredShipHeight / 2, preferredBulletWidth, preferredBulletHeight, "player");
                activeBullets.add(newBullet2);
                // System.out.println("[+] Objects in bulletPool: " + bulletPool.getFree());
            } else {
                lastBulletShoot += delta;
            }

            if (this.isAmmoActive) {
                // TODO spawn rockets
            }
        }
    }

    public void updateCollisionRect() {
        this.shipCollisionRect.move(this.x + ((this.preferredShipWidth - this.colliderWidth)) / 2 + this.colliderXcoordOffset, this.y + ((this.preferredShipHeight - this.colliderHeight)) / 2 + this.colliderYcoordOffset);
    }

    public void updateBullets(float delta) {
        for (Bullet bullet : activeBullets) {
            bullet.update(delta);
            if (bullet.remove) {
                bulletPool.free(bullet);
                activeBullets.removeValue(bullet, true);
            }
        }
    }

    public void renderBullets(SpriteBatch batch) {
        for (Bullet bullet : this.activeBullets) {
            bullet.render(batch);
            // bullet.getCollisionRect().drawCollider(batch);
        }
    }

    public void performInputMobile(float deltaTime) {
        if (Gdx.input.isTouched()) {
            newX = Gdx.input.getX();
            newY = Gdx.input.getY();

            float xToGo, yToGo;

            xToGo = MoveTowards(this.x, newX - this.preferredShipWidth / 2, deltaTime * this.speed);
            yToGo = MoveTowards(this.y, -newY + Gdx.graphics.getHeight() - this.preferredShipHeight / 2, deltaTime * this.speed);

            this.x = xToGo;
            this.y = yToGo;

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

    public void performInputPC(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.UP))
            this.y = (this.y + this.speed * delta);
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
            this.y = (this.y - this.speed * delta);
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            this.x = (this.x - this.speed * delta);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            this.x = (this.x + this.speed * delta);

        this.correctBounds();
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

    public BulletPool getBulletPool() {
        return bulletPool;
    }

    public animationState getCurrentAnimation() {
        return currentAnimation;
    }

    public void setCurrentAnimation(animationState currentAnimation) {
        this.stateTime = 0;
        this.currentAnimation = currentAnimation;
    }

    public boolean isShieldActive() {
        return isShieldActive;
    }

    public void setShieldActive(boolean shieldActive) {
        shieldStateTime = 0;
        isShieldActive = shieldActive;
    }

    public void updateShieldState(float delta) {
        if (isShieldActive) {
            if (shieldStateTime > shieldLifeTime) {
                isShieldActive = false;
                shieldStateTime = 0;
            } else {
                shieldStateTime += delta;
            }
        }
    }

    public boolean isAmmoActive() {
        return isAmmoActive;
    }

    public void setAmmoActive(boolean ammoActive) {
        isAmmoActive = ammoActive;
    }

    public float getShieldHealthMax() {
        return shieldHealthMax;
    }

    public float getCurrentShieldHealth() {
        return currentShieldHealth;
    }

    public void setCurrentShieldHealth(float currentShieldHealth) {
        this.currentShieldHealth = currentShieldHealth;
    }

    public void dispose() {
        // Dispose health bar
        this.animationSheet.dispose();
        this.healthBar.dispose();
    }
}
