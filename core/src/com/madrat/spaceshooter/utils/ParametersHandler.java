package com.madrat.spaceshooter.utils;

import java.io.Serializable;

public class ParametersHandler implements Serializable {

    // All px values relevant to base resolution 720x480

    public String animationTexture; // path to animation spriteSheet
    public float frameLength; // frame duration

    public int realShipWidth; // real (px) ship width
    public int realShipHeight; // real (px) ship height
    public int preferredShipWidth; // preferred ship width (px)
    public int preferredShipHeight; // preferred ship height (px)
    public int colliderWidth; // collider width (px)
    public int colliderHeight; // collider height (px)
    public int colliderXcoordOffset; // collider offset (if the image was cropped incorrectly you can still move the collider) (X-axis)
    public int colliderYcoordOffset; // same as previous but for Y-axis

    public int enemyReward; // Reward for killing enemy ship
    public int shipPrice; // ship price in shop
    public boolean isBought; // is this power up active?
    public float shipSpeed; // ship speed
    public String handler; // handler aka ship name
    public String desc; // ship description

    public float maxHealth; // max ship health
    public float maxHealing; // max healing using medkit

    public int preferredBulletHeight; // preferred bullet height (px)
    public int preferredBulletWidth; // preferred bullet width (px)
    public String bulletTexture; // path to bullet texture
    public float bulletSpeed; // speed of each bullet
    public float damage; // damage of each bullet
    public float delayBetweenShootsBullets; // delay between shoots (how often will shoot)

    public String rocketTexturePath; // path to rocket animation spriteSheet
    public int preferredRocketHeight; // preferred rocket height (px)
    public int preferredRocketWidth; // preferred rocket width (px)
    public float rocketSpeed; // speed of each rocket
    public float lastRocketShoot; // just timer to use with "delayBetweenShootsRockets"
    public float delayBetweenShootsRockets; // delay between shoots (how often will shoot)
    public int maxRockets; // max rockets amount
    public int currentRockets; // current amount of rockets
    public boolean isAmmoActive; // is this power up active?

    public float shieldLifeTime; // shield (without damage) lifetime
    public float shieldHealthMax; // maximum absorbed damage
    public float currentShieldHealth; // current shield health
    public boolean isShieldActive; // is this power up active?

    public ParametersHandler() {
    }

    public void setUpDefaultShip() {

        // Animation
        this.animationTexture = Assets.ship0Animation;
        this.frameLength = 0.14f;

        // Sizes
        this.realShipWidth = 24;
        this.realShipHeight = 24;
        this.preferredShipWidth = 55;
        this.preferredShipHeight = 45;
        this.colliderWidth = 55;
        this.colliderHeight = 45;
        this.colliderXcoordOffset = 0;
        this.colliderYcoordOffset = 0;

        // Additional info
        this.enemyReward = 100;
        this.shipPrice = 0;
        this.isBought = true;
        this.shipSpeed = 360f;
        this.handler = Strings.zapperHandler;
        this.desc = Strings.zapperDesc;

        // Health
        this.maxHealth = 1.6f;
        this.maxHealing = 0.2f;

        // Bullets | shooting
        this.preferredBulletHeight = 10;
        this.preferredBulletWidth = 4;
        this.bulletTexture = Assets.bullet1;
        this.bulletSpeed = 600f;
        this.damage = 0.2f;
        this.delayBetweenShootsBullets = 0.3f;

        // Rockets | shooting
        this.rocketTexturePath = Assets.rocket1;
        this.preferredRocketHeight = 20;
        this.preferredRocketWidth = 20;
        this.rocketSpeed = 500;
        this.lastRocketShoot = 0;
        this.delayBetweenShootsRockets = 0.55f;
        this.maxRockets = 40;
        this.currentRockets = 0;
        this.isAmmoActive = false;

        // Shield
        this.shieldLifeTime = 20f;
        this.shieldHealthMax = 1f;
        this.currentShieldHealth = 0f;
        this.isShieldActive = false;
    }

    // Destroyer SpaceShip
    public void setUpDestroyer() {
        // Animation
        this.animationTexture = Assets.ship2Animation;
        this.frameLength = 0.14f;

        // Sizes
        this.realShipWidth = 32;
        this.realShipHeight = 32;
        this.preferredShipWidth = 56;
        this.preferredShipHeight = 56;
        this.colliderWidth = 56;
        this.colliderHeight = 48;
        this.colliderXcoordOffset = 0;
        this.colliderYcoordOffset = 2;

        // Additional info
        this.enemyReward = 120;
        this.shipPrice = 1150;
        this.isBought = false;
        this.shipSpeed = 400f;
        this.handler = Strings.destroyerHandler;
        this.desc = Strings.destroyerDesc;

        // Health
        this.maxHealth = 2f;
        this.maxHealing = 0.4f;

        // Bullets | shooting
        this.preferredBulletHeight = 10;
        this.preferredBulletWidth = 4;
        this.bulletTexture = Assets.bullet1;
        this.bulletSpeed = 600f;
        this.damage = 0.35f;
        this.delayBetweenShootsBullets = 0.4f;

        // Rockets | shooting
        this.rocketTexturePath = Assets.rocket1;
        this.preferredRocketHeight = 20;
        this.preferredRocketWidth = 20;
        this.rocketSpeed = 500;
        this.lastRocketShoot = 0;
        this.delayBetweenShootsRockets = 0.55f;
        this.maxRockets = 40;
        this.currentRockets = 0;
        this.isAmmoActive = false;

        // Shield
        this.shieldLifeTime = 20f;
        this.shieldHealthMax = 1f;
        this.currentShieldHealth = 0f;
        this.isShieldActive = false;
    }

    // Ignitor spaceship
    public void setUpIgnitor() {
        // Animation
        this.animationTexture = Assets.ship4Animation;
        this.frameLength = 0.14f;

        // Sizes
        this.realShipWidth = 32;
        this.realShipHeight = 32;
        this.preferredShipWidth = 52;
        this.preferredShipHeight = 52;
        this.colliderWidth = 52;
        this.colliderHeight = 52;
        this.colliderXcoordOffset = 0;
        this.colliderYcoordOffset = 0;

        // Additional info
        this.enemyReward = 140;
        this.shipPrice = 1380;
        this.isBought = false;
        this.shipSpeed = 500f;
        this.handler = Strings.ignitorHandler;
        this.desc = Strings.ignitorDesc;

        // Health
        this.maxHealth = 2.2f;
        this.maxHealing = 0.2f;

        // Bullets | shooting
        this.preferredBulletHeight = 10;
        this.preferredBulletWidth = 4;
        this.bulletTexture = Assets.bullet1;
        this.bulletSpeed = 700f;
        this.damage = 0.09f;
        this.delayBetweenShootsBullets = 0.2f;

        // Rockets | shooting
        this.rocketTexturePath = Assets.rocket1;
        this.preferredRocketHeight = 20;
        this.preferredRocketWidth = 20;
        this.rocketSpeed = 500;
        this.lastRocketShoot = 0;
        this.delayBetweenShootsRockets = 0.55f;
        this.maxRockets = 40;
        this.currentRockets = 0;
        this.isAmmoActive = false;

        // Shield
        this.shieldLifeTime = 20f;
        this.shieldHealthMax = 1f;
        this.currentShieldHealth = 0f;
        this.isShieldActive = false;
    }

    // Turtle space ship
    public void setUpTurtle() {
        // Animation
        this.animationTexture = Assets.ship6Animation;
        this.frameLength = 0.14f;

        // Sizes
        this.realShipWidth = 32;
        this.realShipHeight = 32;
        this.preferredShipWidth = 64;
        this.preferredShipHeight = 64;
        this.colliderWidth = 46;
        this.colliderHeight = 64;
        this.colliderXcoordOffset = 0;
        this.colliderYcoordOffset = 0;

        // Additional info
        this.enemyReward = 180;
        this.shipPrice = 2200;
        this.isBought = false;
        this.shipSpeed = 300f;
        this.handler = Strings.turtleHandler;
        this.desc = Strings.turtleDesc;

        // Health
        this.maxHealth = 5f;
        this.maxHealing = 0.8f;

        // Bullets | shooting
        this.preferredBulletHeight = 10;
        this.preferredBulletWidth = 4;
        this.bulletTexture = Assets.bullet1;
        this.bulletSpeed = 600f;
        this.damage = 0.15f;
        this.delayBetweenShootsBullets = 0.365f;

        // Rockets | shooting
        this.rocketTexturePath = Assets.rocket1;
        this.preferredRocketHeight = 20;
        this.preferredRocketWidth = 20;
        this.rocketSpeed = 500;
        this.lastRocketShoot = 0;
        this.delayBetweenShootsRockets = 0.55f;
        this.maxRockets = 40;
        this.currentRockets = 0;
        this.isAmmoActive = false;

        // Shield
        this.shieldLifeTime = 20f;
        this.shieldHealthMax = 1f;
        this.currentShieldHealth = 0f;
        this.isShieldActive = false;
    }

    // Ufo space ship
    public void setUpUfo() {

        // Animation
        this.animationTexture = Assets.ship12Animation;
        this.frameLength = 0.14f;

        // Sizes
        this.realShipWidth = 32;
        this.realShipHeight = 32;
        this.preferredShipWidth = 64;
        this.preferredShipHeight = 64;
        this.colliderWidth = 52;
        this.colliderHeight = 52;
        this.colliderXcoordOffset = 0;
        this.colliderYcoordOffset = 0;

        // Additional info
        this.enemyReward = 200;
        this.shipPrice = 1800;
        this.isBought = false;
        this.shipSpeed = 480f;
        this.handler = Strings.ufoHandler;
        this.desc = Strings.ufoDesc;

        // Health
        this.maxHealth = 2.5f;
        this.maxHealing = 0.4f;

        // Bullets | shooting
        this.preferredBulletHeight = 10;
        this.preferredBulletWidth = 4;
        this.bulletTexture = Assets.bullet1;
        this.bulletSpeed = 700f;
        this.damage = 0.25f;
        this.delayBetweenShootsBullets = 0.3f;

        // Rockets | shooting
        this.rocketTexturePath = Assets.rocket1;
        this.preferredRocketHeight = 20;
        this.preferredRocketWidth = 20;
        this.rocketSpeed = 500;
        this.lastRocketShoot = 0;
        this.delayBetweenShootsRockets = 0.55f;
        this.maxRockets = 40;
        this.currentRockets = 0;
        this.isAmmoActive = false;

        // Shield
        this.shieldLifeTime = 20f;
        this.shieldHealthMax = 1f;
        this.currentShieldHealth = 0f;
        this.isShieldActive = false;
    }

    // Star space ship
    public void setUpStar() {
        // Animation
        this.animationTexture = Assets.ship8Animation;
        this.frameLength = 0.18f;

        // Sizes
        this.realShipWidth = 32;
        this.realShipHeight = 32;
        this.preferredShipWidth = 56;
        this.preferredShipHeight = 56;
        this.colliderWidth = 56;
        this.colliderHeight = 56;
        this.colliderXcoordOffset = 0;
        this.colliderYcoordOffset = 0;

        // Additional info
        this.enemyReward = 120;
        this.shipPrice = 1550;
        this.isBought = false;
        this.shipSpeed = 700f;
        this.handler = Strings.starHandler;
        this.desc = Strings.starDesc;

        // Health
        this.maxHealth = 1.25f;
        this.maxHealing = 0.2f;

        // Bullets | shooting
        this.preferredBulletHeight = 10;
        this.preferredBulletWidth = 4;
        this.bulletTexture = Assets.bullet1;
        this.bulletSpeed = 700f;
        this.damage = 0.4f;
        this.delayBetweenShootsBullets = 0.3f;

        // Rockets | shooting
        this.rocketTexturePath = Assets.rocket1;
        this.preferredRocketHeight = 20;
        this.preferredRocketWidth = 20;
        this.rocketSpeed = 500;
        this.lastRocketShoot = 0;
        this.delayBetweenShootsRockets = 0.55f;
        this.maxRockets = 40;
        this.currentRockets = 0;
        this.isAmmoActive = false;

        // Shield
        this.shieldLifeTime = 20f;
        this.shieldHealthMax = 1f;
        this.currentShieldHealth = 0f;
        this.isShieldActive = false;
    }

    // Pinky space ship
    public void setUpPinky() {
        // Animation
        this.animationTexture = Assets.ship10Animation;
        this.frameLength = 0.14f;

        // Sizes
        this.realShipWidth = 32;
        this.realShipHeight = 32;
        this.preferredShipWidth = 64;
        this.preferredShipHeight = 64;
        this.colliderWidth = 50;
        this.colliderHeight = 64;
        this.colliderXcoordOffset = 0;
        this.colliderYcoordOffset = 0;

        // Additional info
        this.enemyReward = 180;
        this.shipPrice = 1650;
        this.isBought = false;
        this.shipSpeed = 380f;
        this.handler = Strings.pinkyHandler;
        this.desc = Strings.pinkyDesc;

        // Health
        this.maxHealth = 2f;
        this.maxHealing = 0.5f;

        // Bullets | shooting
        this.preferredBulletHeight = 10;
        this.preferredBulletWidth = 4;
        this.bulletTexture = Assets.bullet1;
        this.bulletSpeed = 600f;
        this.damage = 0.12f;
        this.delayBetweenShootsBullets = 0.2f;

        // Rockets | shooting
        this.rocketTexturePath = Assets.rocket1;
        this.preferredRocketHeight = 20;
        this.preferredRocketWidth = 20;
        this.rocketSpeed = 500;
        this.lastRocketShoot = 0;
        this.delayBetweenShootsRockets = 0.55f;
        this.maxRockets = 40;
        this.currentRockets = 0;
        this.isAmmoActive = false;

        // Shield
        this.shieldLifeTime = 20f;
        this.shieldHealthMax = 1f;
        this.currentShieldHealth = 0f;
        this.isShieldActive = false;
    }

    public String getAnimationTexture() {
        return animationTexture;
    }

    public void setAnimationTexture(String animationTexture) {
        this.animationTexture = animationTexture;
    }

    public float getFrameLength() {
        return frameLength;
    }

    public void setFrameLength(float frameLength) {
        this.frameLength = frameLength;
    }

    public int getRealShipWidth() {
        return realShipWidth;
    }

    public void setRealShipWidth(int realShipWidth) {
        this.realShipWidth = realShipWidth;
    }

    public int getRealShipHeight() {
        return realShipHeight;
    }

    public void setRealShipHeight(int realShipHeight) {
        this.realShipHeight = realShipHeight;
    }

    public int getPreferredShipWidth() {
        return preferredShipWidth;
    }

    public void setPreferredShipWidth(int preferredShipWidth) {
        this.preferredShipWidth = preferredShipWidth;
    }

    public int getPreferredShipHeight() {
        return preferredShipHeight;
    }

    public void setPreferredShipHeight(int preferredShipHeight) {
        this.preferredShipHeight = preferredShipHeight;
    }

    public int getColliderWidth() {
        return colliderWidth;
    }

    public void setColliderWidth(int colliderWidth) {
        this.colliderWidth = colliderWidth;
    }

    public int getColliderHeight() {
        return colliderHeight;
    }

    public void setColliderHeight(int colliderHeight) {
        this.colliderHeight = colliderHeight;
    }

    public int getColliderXcoordOffset() {
        return colliderXcoordOffset;
    }

    public void setColliderXcoordOffset(int colliderXcoordOffset) {
        this.colliderXcoordOffset = colliderXcoordOffset;
    }

    public int getColliderYcoordOffset() {
        return colliderYcoordOffset;
    }

    public void setColliderYcoordOffset(int colliderYcoordOffset) {
        this.colliderYcoordOffset = colliderYcoordOffset;
    }

    public int getShipPrice() {
        return shipPrice;
    }

    public void setShipPrice(int shipPrice) {
        this.shipPrice = shipPrice;
    }

    public float getShipSpeed() {
        return shipSpeed;
    }

    public void setShipSpeed(float shipSpeed) {
        this.shipSpeed = shipSpeed;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(float maxHealth) {
        this.maxHealth = maxHealth;
    }

    public float getMaxHealing() {
        return maxHealing;
    }

    public void setMaxHealing(float maxHealing) {
        this.maxHealing = maxHealing;
    }

    public int getPreferredBulletHeight() {
        return preferredBulletHeight;
    }

    public void setPreferredBulletHeight(int preferredBulletHeight) {
        this.preferredBulletHeight = preferredBulletHeight;
    }

    public int getPreferredBulletWidth() {
        return preferredBulletWidth;
    }

    public void setPreferredBulletWidth(int preferredBulletWidth) {
        this.preferredBulletWidth = preferredBulletWidth;
    }

    public String getBulletTexture() {
        return bulletTexture;
    }

    public void setBulletTexture(String bulletTexture) {
        this.bulletTexture = bulletTexture;
    }

    public float getBulletSpeed() {
        return bulletSpeed;
    }

    public void setBulletSpeed(float bulletSpeed) {
        this.bulletSpeed = bulletSpeed;
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getDelayBetweenShootsBullets() {
        return delayBetweenShootsBullets;
    }

    public void setDelayBetweenShootsBullets(float delayBetweenShootsBullets) {
        this.delayBetweenShootsBullets = delayBetweenShootsBullets;
    }

    public String getRocketTexturePath() {
        return rocketTexturePath;
    }

    public void setRocketTexturePath(String rocketTexturePath) {
        this.rocketTexturePath = rocketTexturePath;
    }

    public int getPreferredRocketHeight() {
        return preferredRocketHeight;
    }

    public void setPreferredRocketHeight(int preferredRocketHeight) {
        this.preferredRocketHeight = preferredRocketHeight;
    }

    public int getPreferredRocketWidth() {
        return preferredRocketWidth;
    }

    public void setPreferredRocketWidth(int preferredRocketWidth) {
        this.preferredRocketWidth = preferredRocketWidth;
    }

    public float getRocketSpeed() {
        return rocketSpeed;
    }

    public void setRocketSpeed(float rocketSpeed) {
        this.rocketSpeed = rocketSpeed;
    }

    public float getLastRocketShoot() {
        return lastRocketShoot;
    }

    public void setLastRocketShoot(float lastRocketShoot) {
        this.lastRocketShoot = lastRocketShoot;
    }

    public float getDelayBetweenShootsRockets() {
        return delayBetweenShootsRockets;
    }

    public void setDelayBetweenShootsRockets(float delayBetweenShootsRockets) {
        this.delayBetweenShootsRockets = delayBetweenShootsRockets;
    }

    public int getMaxRockets() {
        return maxRockets;
    }

    public void setMaxRockets(int maxRockets) {
        this.maxRockets = maxRockets;
    }

    public int getCurrentRockets() {
        return currentRockets;
    }

    public void setCurrentRockets(int currentRockets) {
        this.currentRockets = currentRockets;
    }

    public boolean getIsAmmoActive() {
        return isAmmoActive;
    }

    public void setAmmoActive(boolean IsAmmoActive) {
        this.isAmmoActive = IsAmmoActive;
    }

    public float getShieldLifeTime() {
        return shieldLifeTime;
    }

    public void setShieldLifeTime(float shieldLifeTime) {
        this.shieldLifeTime = shieldLifeTime;
    }

    public float getShieldHealthMax() {
        return shieldHealthMax;
    }

    public void setShieldHealthMax(float shieldHealthMax) {
        this.shieldHealthMax = shieldHealthMax;
    }

    public float getCurrentShieldHealth() {
        return currentShieldHealth;
    }

    public void setCurrentShieldHealth(float currentShieldHealth) {
        this.currentShieldHealth = currentShieldHealth;
    }

    public boolean getIsShieldActive() {
        return isShieldActive;
    }

    public void setShieldActive(boolean IsShieldActive) {
        this.isShieldActive = IsShieldActive;
    }

    public boolean getIsBought() {
        return isBought;
    }

    public void setBought(boolean IsBought) {
        this.isBought = IsBought;
    }

    public int getEnemyReward() {
        return enemyReward;
    }

    public void setEnemyReward(int enemyReward) {
        this.enemyReward = enemyReward;
    }
}
