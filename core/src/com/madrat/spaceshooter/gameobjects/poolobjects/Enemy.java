package com.madrat.spaceshooter.gameobjects.poolobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import com.madrat.spaceshooter.gameobjects.PlayerShip.animationState;
import com.madrat.spaceshooter.gameobjects.SpaceShip;
import com.madrat.spaceshooter.physics2d.CollisionRect;
import com.madrat.spaceshooter.utils.Assets;

import static com.madrat.spaceshooter.MainGame.SCALE_FACTOR;
import static com.madrat.spaceshooter.gameobjects.PlayerShip.animationState.defaultFlyAnimation;

public class Enemy extends SpaceShip implements Pool.Poolable {

    public static final float MIN_ENEMY_WAVE_SPAWN_TIME = 2f;
    public static final float MAX_ENEMY_WAVE_SPAWN_TIME = 5f;

    private Texture enemyAnimationSheet; // SpriteSheet
    private Animation<TextureRegion>[] shipAnimations; // Animations array
    private animationState currentAnimation; // Current Animation enum
    private float stateTime; // Animation State time

    private int preferredBulletHeight, preferredBulletWidth;
    private final BulletPool bulletPool = new BulletPool(Assets.bullet2, 1f, 3, 8, Bullet.bulletType.bullet);
    private Array<Bullet> activeBullets;
    private Array<Bullet> bulletsToRemove;

    private int colliderXcoordOffset, colliderYcoordOffset;
    private int colliderWidth, colliderHeight;
    private float collisionDamage;

    private int difficultyLevel;
    private int reward;
    public boolean remove, canBeFullyRemoved;

    @Override
    public void reset() {
        this.currentAnimation = defaultFlyAnimation;
        this.stateTime = 0f;
        this.remove = false;
        this.isAlive = true;
        this.currentHealth = 0f;
    }

    public Enemy(int colliderWidth, int colliderHeight, int colliderXcoordOffset, int colliderYcoordOffset, float maxHealth, float damage, float delayBetweenShootsBullets, float bulletsSpeed, float speed, SpaceShip.shipHandler handle, int realShipWidth, int realShipHeight, int preferredShipWidth, int preferredShipHeight, int reward, float collisionDamage, int difficultyLevel, String enemyAnimationSheetPath) {
        super(maxHealth, damage, delayBetweenShootsBullets, bulletsSpeed, speed, handle, realShipWidth, realShipHeight, preferredShipWidth, preferredShipHeight);

        this.preferredBulletHeight = 10;
        this.preferredBulletWidth = 4;

        this.activeBullets = new Array<Bullet>();
        this.bulletsToRemove = new Array<Bullet>();

        this.colliderWidth = (int) (colliderWidth * SCALE_FACTOR);
        this.colliderHeight = (int) (colliderHeight * SCALE_FACTOR);
        this.colliderXcoordOffset = (int) (colliderXcoordOffset * SCALE_FACTOR);
        this.colliderYcoordOffset = (int) (colliderYcoordOffset * SCALE_FACTOR);

        this.shipCollisionRect = new CollisionRect(0, 0, this.colliderWidth, this.colliderHeight, CollisionRect.colliderTag.enemy);

        this.remove = false;
        this.isAlive = true;
        this.canBeFullyRemoved = false;

        this.reward = reward;
        this.collisionDamage = collisionDamage;
        this.difficultyLevel = difficultyLevel;

        this.shipAnimations = new Animation[3];
        this.currentAnimation = defaultFlyAnimation;
        this.enemyAnimationSheet = Assets.manager.get(enemyAnimationSheetPath, Texture.class);
        TextureRegion[][] statesSpriteSheet = TextureRegion.split(this.enemyAnimationSheet, this.realShipWidth, this.realShipHeight);
        // Default animation
        this.shipAnimations[0] = new Animation<TextureRegion>(0.14f, statesSpriteSheet[0]);
        // Destroyed animation
        this.shipAnimations[1] = new Animation<TextureRegion>(0.14f, statesSpriteSheet[1]);
        // Under attack
        this.shipAnimations[2] = new Animation<TextureRegion>(0.14f, statesSpriteSheet[2]);
    }

    public void enemySetUp(float x, float y, float speedMultiplier) {
        this.x = x;
        this.y = y;

        this.currentHealth = maxHealth;
        this.speed = this.speed * speedMultiplier;

        this.shipCollisionRect.move(this.x + ((this.preferredShipWidth - this.colliderWidth) * SCALE_FACTOR) + this.colliderXcoordOffset, this.y + ((this.preferredShipHeight - this.colliderHeight) * SCALE_FACTOR) + this.colliderYcoordOffset);
    }

    public void update(float delta) {
        stateTime += delta;

        y -= speed * delta;

        if (y < -preferredShipHeight || !isAlive) {
            remove = true;
            if (activeBullets.size <= 0) {
                canBeFullyRemoved = true;
            }
        }

        // Move collision rect
        shipCollisionRect.move(x + ((this.preferredShipWidth - this.colliderWidth)) / 2 + this.colliderXcoordOffset, y + ((this.preferredShipHeight - this.colliderHeight)) / 2 + this.colliderYcoordOffset);

        // Update Bullets
        for (Bullet bullet : activeBullets) {
            bullet.update(delta);
            if (bullet.remove) {
                bulletsToRemove.add(bullet);
                bulletPool.free(bullet);
            }
        }
        activeBullets.removeAll(bulletsToRemove, true);
        bulletsToRemove.clear();

        if (isAlive && !remove && currentHealth > 0) {
            if (this.lastBulletShoot > delayBetweenShootsBullets) {
                this.lastBulletShoot = 0;

                // Add first activeBullet
                Bullet newBullet1 = bulletPool.obtain();
                newBullet1.setupBullet(-this.bulletsSpeed, getShipCollisionRect().getX() + 8 * SCALE_FACTOR, this.y + this.preferredShipHeight / 2, preferredBulletWidth, preferredBulletHeight, CollisionRect.colliderTag.enemyBullet);
                activeBullets.add(newBullet1);

                // Add second activeBullet
                Bullet newBullet2 = bulletPool.obtain();
                newBullet2.setupBullet(-this.bulletsSpeed, getShipCollisionRect().getX() + colliderWidth - (16 - preferredBulletWidth) * SCALE_FACTOR, this.y + this.preferredShipHeight / 2, preferredBulletWidth, preferredBulletHeight, CollisionRect.colliderTag.enemyBullet);
                activeBullets.add(newBullet2);
            } else {
                lastBulletShoot += delta;
            }
        }
    }

    public void die() {
        this.isAlive = false;
        this.speed = 0;
        this.shipCollisionRect.resize(0, 0);
/*        for (Bullet bullet : activeBullets) {
            activeBullets.removeValue(bullet, true);
            bulletPool.free(bullet);
        }*/
        setCurrentAnimation(animationState.shipDestroyedAnimation);
    }

    public void render(SpriteBatch batch) {

        // Draw enemy in appropriate state
        if (!remove) {
            switch (this.currentAnimation) {
                case defaultFlyAnimation:
                    batch.draw(shipAnimations[0].getKeyFrame(stateTime, true), this.x, this.y, preferredShipWidth, preferredShipHeight);
                    break;
                case shipDestroyedAnimation:
                    batch.draw(shipAnimations[1].getKeyFrame(stateTime, false), this.x, this.y, preferredShipWidth, preferredShipHeight);
                    if (shipAnimations[1].isAnimationFinished(stateTime)) {
                        remove = true;
                    }
                    break;
                case shipUnderAttackAnimation:
                    batch.draw(shipAnimations[2].getKeyFrame(stateTime, false), this.x, this.y, preferredShipWidth, preferredShipHeight);
                    if (shipAnimations[2].isAnimationFinished(stateTime)) {
                        setCurrentAnimation(animationState.defaultFlyAnimation);
                    }
                    break;
                default:
                    batch.draw(shipAnimations[0].getKeyFrame(stateTime, true), this.x, this.y, preferredShipWidth, preferredShipHeight);
                    break;
            }
        }

        // render enemy bullets
        for (Bullet bullet : this.activeBullets) {
            bullet.render(batch);
        }
    }

    public void setCurrentAnimation(animationState animation) {
        this.stateTime = 0;
        this.currentAnimation = animation;
    }

    public animationState getCurrentAnimation() {
        return currentAnimation;
    }

    public Array<Bullet> getActiveBullets() {
        return activeBullets;
    }

    public int getReward() {
        return reward;
    }

    public float getCollisionDamage() {
        return collisionDamage;
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }
}
