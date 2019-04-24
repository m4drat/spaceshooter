package com.madrat.spaceshooter.gameobjects;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.madrat.spaceshooter.MainGame;
import com.madrat.spaceshooter.gameobjects.poolobjects.Bullet;
import com.madrat.spaceshooter.gameobjects.poolobjects.BulletPool;
import com.madrat.spaceshooter.physics2d.CollisionRect;
import com.madrat.spaceshooter.utils.Assets;
import com.madrat.spaceshooter.utils.BuildConfig;


import static com.madrat.spaceshooter.MainGame.SCALE_FACTOR;
import static com.madrat.spaceshooter.gameobjects.PlayerShip.animationState.shieldAttackedAnimation;
import static com.madrat.spaceshooter.gameobjects.PlayerShip.animationState.shipUnderAttackAnimation;

public class PlayerShip extends SpaceShip {

    private static final int healthBarHeight = 5;

    // 0 - default ship State
    // 1 - ship damage
    // 2 - shield default animation
    // 3 - shield Activated
    // 4 - shield destroyed
    // 5 - shield under attack
    // 6 - shield Going to disappear (4 seconds left)
    // 7 - ship destroyed (gameOver)
    public enum animationState {
        defaultFlyAnimation,
        shipUnderAttackAnimation,
        shieldDefaultAnimation,
        shieldJustActivatedAnimation,
        shieldDestroyedAnimation,
        shieldAttackedAnimation,
        shipDestroyedAnimation
    }

    private Animation<TextureRegion>[] shipAnimations;
    private animationState currentAnimation;
    private Texture animationSheet;
    private float stateTime;

    private int score;

    private String bulletTexturePath, rocketTexturePath;
    private int preferredBulletHeight, preferredBulletWidth, preferredRocketHeight, preferredRocketWidth;
    private BulletPool bulletPool;
    private BulletPool rocketPool;
    private float rocketSpeed, lastRocketShoot, delayBetweenShootsRockets;
    private int maxRockets, currentRockets;
    private Array<Bullet> activeBullets;
    private Array<Bullet> bulletsToRemove = new Array<Bullet>();

    private Color lightBlue;
    private Texture healthBar;
    private float newX, newY;

    private int colliderXcoordOffset, colliderYcoordOffset;
    private int colliderWidth, colliderHeight;

    private boolean isShieldActive, isAmmoActive, isDestroyed, goingToDie;
    private float shieldHealthMax, currentShieldHealth, shieldLifeTime, shieldStateTime;

    // Constructor to generate player ship using only file data
    public PlayerShip() {
        super();
        // super(Gdx.app.getPreferences("spacegame").getFloat("maxHealth"), Gdx.app.getPreferences("spacegame").getFloat("damage"), Gdx.app.getPreferences("spacegame").getFloat("delayBetweenShootsBullets"), Gdx.app.getPreferences("spacegame").getFloat("bulletsSpeed"), Gdx.app.getPreferences("spacegame").getFloat("speed"), Gdx.app.getPreferences("spacegame").getString("handle"), Gdx.app.getPreferences("spacegame").getInteger("realShipWidth"), Gdx.app.getPreferences("spacegame").getInteger("realShipHeight"), Gdx.app.getPreferences("spacegame").getInteger("preferredShipWidth"), Gdx.app.getPreferences("spacegame").getInteger("preferredShipHeight"));

        initFromFile(MainGame.pathToCurrentState);
        setup();
    }

    private void initFromFile(String path) {
        FileHandle currentFileHandle;
        JsonObject shipData;
        JsonObject powerUpsState;

        JsonParser parser = new JsonParser();

        // Set appropriate path to file
        if (MainGame.applicationType == Application.ApplicationType.Android) {
            currentFileHandle = Gdx.files.local(path);
        } else if (MainGame.applicationType == Application.ApplicationType.Desktop) {
            currentFileHandle = Gdx.files.absolute(path);
        } else {
            currentFileHandle = Gdx.files.local(path);
        }

        try {
            // Init health, real sizes, speed
            shipData = parser.parse(MainGame.cryptor.decrypt(currentFileHandle.readString())).getAsJsonObject().getAsJsonObject("currentShip");
            powerUpsState = parser.parse(MainGame.cryptor.decrypt(currentFileHandle.readString())).getAsJsonObject().getAsJsonObject("powerUpUpgradesState");

            // System.out.println(powerUpsState);

            this.maxHealth = shipData.get("maxHealth").getAsFloat();
            this.currentHealth = maxHealth;
            this.damage = shipData.get("damage").getAsFloat();
            this.delayBetweenShootsBullets = shipData.get("delayBetweenShootsBullets").getAsFloat();
            this.bulletsSpeed = (shipData.get("bulletSpeed").getAsFloat()) * SCALE_FACTOR;
            this.speed = (shipData.get("shipSpeed").getAsFloat()) * SCALE_FACTOR;
            this.handle = SpaceShip.shipHandler.valueOf(shipData.get("handler").getAsString());

            this.realShipWidth = shipData.get("realShipWidth").getAsInt();
            this.realShipHeight = shipData.get("realShipHeight").getAsInt();
            this.preferredShipWidth = (int) (shipData.get("preferredShipWidth").getAsInt() * SCALE_FACTOR);
            this.preferredShipHeight = (int) (shipData.get("preferredShipHeight").getAsInt() * SCALE_FACTOR);
            // set collider width
            this.colliderWidth = (int) (shipData.get("colliderWidth").getAsInt() * SCALE_FACTOR);
            this.colliderHeight = (int) (shipData.get("colliderHeight").getAsInt() * SCALE_FACTOR);
            // set offsets for the collider
            this.colliderXcoordOffset = (int) (shipData.get("colliderXcoordOffset").getAsInt() * SCALE_FACTOR);
            this.colliderYcoordOffset = (int) (shipData.get("colliderYcoordOffset").getAsInt() * SCALE_FACTOR);

            // Init bullets
            this.bulletTexturePath = shipData.get("bulletTexture").getAsString();
            this.preferredBulletHeight = shipData.get("preferredBulletHeight").getAsInt();
            this.preferredBulletWidth = shipData.get("preferredBulletWidth").getAsInt();
            this.lastBulletShoot = 0;

            // Init rockets
            this.rocketTexturePath = shipData.get("rocketTexturePath").getAsString();
            this.preferredRocketHeight = shipData.get("preferredRocketHeight").getAsInt();
            this.preferredRocketWidth = shipData.get("preferredRocketWidth").getAsInt();
            this.rocketSpeed = (shipData.get("rocketSpeed").getAsFloat()) * SCALE_FACTOR;
            this.maxRockets = shipData.get("maxRockets").getAsInt();
            this.currentRockets = shipData.get("currentRockets").getAsInt();
            this.lastRocketShoot = shipData.get("lastRocketShoot").getAsFloat();
            this.delayBetweenShootsRockets = shipData.get("delayBetweenShootsRockets").getAsFloat();
            this.isAmmoActive = shipData.get("isAmmoActive").getAsBoolean();

            this.isAlive = true;
            this.needToShow = true;
            this.isDestroyed = false;
            this.goingToDie = false;

            // Max healing upgrade
            if (!powerUpsState.getAsJsonObject("heal").getAsJsonObject("0").get("isBought").getAsBoolean()) {
                this.maxHealing = shipData.get("maxHealing").getAsFloat();
            } else {
                this.maxHealing = shipData.get("maxHealing").getAsFloat() * 1.25f;
            }

            // + health
            if (!powerUpsState.getAsJsonObject("shield").getAsJsonObject("0").get("isBought").getAsBoolean()) {
                this.shieldHealthMax = (this.maxHealth / 2);
            } else {
                this.shieldHealthMax = (this.maxHealth / 2) * 1.25f;
            }

            // + lifetime
            if (!powerUpsState.getAsJsonObject("shield").getAsJsonObject("1").get("isBought").getAsBoolean()) {
                this.shieldLifeTime = shipData.get("shieldLifeTime").getAsFloat();
            } else {
                this.shieldLifeTime = shipData.get("shieldLifeTime").getAsFloat() * 1.5f;
            }

            if (BuildConfig.DEBUG) {
                System.out.println("[+] Shield healthMax default health: " + this.maxHealth / 2);
                System.out.println("[+] Shield healthMax current health: " + this.shieldHealthMax);
                System.out.println("[+] Default healMax: " + shipData.get("maxHealing").getAsFloat());
                System.out.println("[+] Current healMax: " + this.maxHealing);
                System.out.println("[+] Default shield lifeTime: " + shipData.get("shieldLifeTime").getAsFloat());
                System.out.println("[+] Current shield lifeTime: " + this.shieldLifeTime);
            }

            this.currentShieldHealth = shipData.get("currentShieldHealth").getAsFloat();
            this.isShieldActive = shipData.get("isShieldActive").getAsBoolean();
            this.lightBlue = Assets.lightBlue_2;

            this.x = Gdx.graphics.getWidth() / 2 - this.preferredShipWidth / 2;
            this.y = 25;

            // create collision rect
            this.shipCollisionRect = new CollisionRect(this.x + ((this.preferredShipWidth - this.colliderWidth) * SCALE_FACTOR) + this.colliderXcoordOffset, this.y + ((this.preferredBulletHeight - this.colliderHeight) * SCALE_FACTOR) + this.colliderYcoordOffset, this.colliderWidth, this.colliderHeight, CollisionRect.colliderTag.player);

            this.currentAnimation = animationState.defaultFlyAnimation;
            this.shipAnimations = new Animation[7];
            this.animationSheet = Assets.manager.get(shipData.get("animationTexture").getAsString(), Texture.class);
            TextureRegion[][] statesSpriteSheet = TextureRegion.split(this.animationSheet, this.realShipWidth, this.realShipHeight);
            // Default animation
            this.shipAnimations[0] = new Animation<TextureRegion>(shipData.get("frameLength").getAsFloat(), statesSpriteSheet[0]);
            // Damage animation
            this.shipAnimations[1] = new Animation<TextureRegion>(shipData.get("frameLength").getAsFloat(), statesSpriteSheet[1]);
            // shield default animation
            this.shipAnimations[2] = new Animation<TextureRegion>(shipData.get("frameLength").getAsFloat(), statesSpriteSheet[2]);
            // shield just activated animation
            this.shipAnimations[3] = new Animation<TextureRegion>(shipData.get("frameLength").getAsFloat(), statesSpriteSheet[3]);
            // shield destroyed animation
            this.shipAnimations[4] = new Animation<TextureRegion>(shipData.get("frameLength").getAsFloat(), statesSpriteSheet[4]);
            // Shield under attack animation
            this.shipAnimations[5] = new Animation<TextureRegion>(shipData.get("frameLength").getAsFloat(), statesSpriteSheet[5]);
            // ship destroyed animation
            this.shipAnimations[6] = new Animation<TextureRegion>(shipData.get("frameLength").getAsFloat(), statesSpriteSheet[6]);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setup() {

        // Initialize bullet/rocket pool
        this.bulletPool = new BulletPool(bulletTexturePath, 1f, 3, 8, Bullet.bulletType.bullet);
        this.rocketPool = new BulletPool(rocketTexturePath, 0.1f, 16, 16, Bullet.bulletType.rocket);

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
        if (this.x > MainGame.GENERAL_WIDTH - this.preferredShipWidth) {
            this.x = MainGame.GENERAL_WIDTH - this.preferredShipWidth;
        }

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
                case shieldAttackedAnimation:
                    batch.draw(shipAnimations[5].getKeyFrame(stateTime, false), this.x, this.y, this.preferredShipWidth, this.preferredShipHeight);
                    if (shipAnimations[5].isAnimationFinished(stateTime))
                        setCurrentAnimation(animationState.shieldDefaultAnimation);
                    break;
                case shipDestroyedAnimation:
                    batch.draw(shipAnimations[6].getKeyFrame(stateTime, false), this.x, this.y, this.preferredShipWidth, this.preferredShipHeight);
                    if (shipAnimations[6].isAnimationFinished(stateTime))
                        this.isDestroyed = true;
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
        if (isAlive && !goingToDie) {
            if (this.lastBulletShoot > delayBetweenShootsBullets) {
                // set Shoot timer to 0
                this.lastBulletShoot = 0;

                // Add first activeBullet
                Bullet newBullet1 = bulletPool.obtain();
                newBullet1.setupBullet(this.bulletsSpeed, getShipCollisionRect().getX() + 8 * SCALE_FACTOR, this.y + this.preferredShipHeight / 2, preferredBulletWidth, preferredBulletHeight, CollisionRect.colliderTag.player);

                // Add second activeBullet
                Bullet newBullet2 = bulletPool.obtain();
                newBullet2.setupBullet(this.bulletsSpeed, getShipCollisionRect().getX() + colliderWidth - (16 - preferredBulletWidth) * SCALE_FACTOR, this.y + this.preferredShipHeight / 2, preferredBulletWidth, preferredBulletHeight, CollisionRect.colliderTag.player);

                activeBullets.add(newBullet1, newBullet2);
                // System.out.println("[+] Objects in bulletPool: " + bulletPool.getFree());
            } else {
                lastBulletShoot += delta;
            }

            // Spawn rockets if need
            if (this.lastRocketShoot > delayBetweenShootsRockets) {
                this.lastRocketShoot = 0;
                if (currentRockets > 0 && isAmmoActive) {
                    Bullet newRocket1 = rocketPool.obtain();
                    newRocket1.setupBullet(this.rocketSpeed, getShipCollisionRect().getX() + 8 * SCALE_FACTOR, this.y + this.preferredShipHeight / 2, preferredRocketWidth, preferredRocketHeight, CollisionRect.colliderTag.player);
                    Bullet newRocket2 = rocketPool.obtain();
                    newRocket2.setupBullet(this.rocketSpeed, getShipCollisionRect().getX() + colliderWidth - (48 - preferredRocketWidth) * SCALE_FACTOR, this.y + this.preferredShipHeight / 2, preferredRocketWidth, preferredRocketHeight, CollisionRect.colliderTag.player);

                    activeBullets.add(newRocket1, newRocket2);

                    currentRockets -= 2;
                } else {
                    isAmmoActive = false;
                }
            } else {
                lastRocketShoot += delta;
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
                if (bullet.getType() == Bullet.bulletType.rocket) {
                    bulletsToRemove.add(bullet);
                    rocketPool.free(bullet);
                } else if (bullet.getType() == Bullet.bulletType.bullet) {
                    bulletsToRemove.add(bullet);
                    bulletPool.free(bullet);
                }
            }
        }

        activeBullets.removeAll(bulletsToRemove, true);
        bulletsToRemove.clear();
    }

    public void renderBullets(SpriteBatch batch) {
        for (Bullet bullet : this.activeBullets) {
            bullet.render(batch);
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

    public void updateHealth(float damage) {

        // decrease player health or shield if its active
        if (isShieldActive) {
            if (currentAnimation != shieldAttackedAnimation)
                this.setCurrentAnimation(shieldAttackedAnimation);
            currentShieldHealth -= damage;
        } else {
            currentHealth -= damage;
        }

        // Set damage animation if shield (and other states doesn't set) isn't active
        if (currentAnimation != animationState.shieldJustActivatedAnimation
                && currentAnimation != animationState.shieldDestroyedAnimation
                && currentAnimation != animationState.shieldDefaultAnimation
                && currentAnimation != animationState.shipDestroyedAnimation
                && currentAnimation != animationState.shieldAttackedAnimation) // && currentAnimation != animationState.shipUnderAttackAnimation)
            this.setCurrentAnimation(shipUnderAttackAnimation);
    }

    // PowerUps Section
    // Heal
    public void setHealPowerUpActive() {
        if (currentAnimation != animationState.shipDestroyedAnimation) {
            if (this.currentHealth + this.maxHealing > this.maxHealth) {
                this.currentHealth = this.maxHealth;
            } else {
                this.currentHealth += this.maxHealing;
            }
        }
    }

    // Rockets
    public void setAmmoPowerUpActive(boolean ammoActive, int currentRockets) {
        this.currentRockets = currentRockets;
        this.isAmmoActive = ammoActive;
    }

    // Shield
    public void setShieldActive(boolean shieldActive) {
        shieldStateTime = 0;
        isShieldActive = shieldActive;
    }

    public void activateShield() {
        setShieldActive(true);

        // Set new animation
        setCurrentAnimation(animationState.shieldJustActivatedAnimation);

        // Set new healt
        setCurrentShieldHealth(shieldHealthMax);
    }

    public boolean isAmmoActive() {
        return isAmmoActive;
    }

    public int getCurrentRockets() {
        return currentRockets;
    }

    public BulletPool getRocketPool() {
        return rocketPool;
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

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public boolean isGoingToDie() {
        return goingToDie;
    }

    public void setGoingToDie(boolean goingToDie) {
        this.goingToDie = goingToDie;
    }

    public void dispose() {
        // Dispose health bar
        this.animationSheet.dispose();
        this.healthBar.dispose();
    }
}
