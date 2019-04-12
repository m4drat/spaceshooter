package com.madrat.spaceshooter.gameobjects.poolobjects;

import com.badlogic.gdx.utils.Pool;
import com.madrat.spaceshooter.gameobjects.SpaceShip;

public class EnemyPool extends Pool<Enemy> {

    private int realShipWidth, realShipHeight, preferredShipWidth, preferredShipHeight;
    private int colliderWidth, colliderHeight, colliderXcoordOffset, colliderYcoordOffset, reward, difficultyLevel;
    private float maxHealth, damage, delayBetweenShootsBullets, bulletsSpeed, speed, collisionDamage;

    private String enemyAnimationSheetPath;
    private SpaceShip.shipHandler handle;

    public EnemyPool(int realShipWidth, int realShipHeight, int preferredShipWidth, int preferredShipHeight, int colliderWidth, int colliderHeight, int colliderXcoordOffset, int colliderYcoordOffset, float maxHealth, float damage, float delayBetweenShootsBullets, float bulletsSpeed, float speed, int reward, int difficultyLevel, SpaceShip.shipHandler handle, String enemyAnimationSheetPath) {
        super();

        this.realShipWidth = realShipWidth;
        this.realShipHeight = realShipHeight;
        this.preferredShipWidth = preferredShipWidth;
        this.preferredShipHeight = preferredShipHeight;
        this.colliderWidth = colliderWidth;
        this.colliderHeight = colliderHeight;
        this.colliderXcoordOffset = colliderXcoordOffset;
        this.colliderYcoordOffset = colliderYcoordOffset;
        this.maxHealth = maxHealth;
        this.damage = damage;
        this.delayBetweenShootsBullets = delayBetweenShootsBullets;
        this.bulletsSpeed = bulletsSpeed;
        this.speed = speed;
        this.reward = reward;
        this.difficultyLevel = difficultyLevel;

        this.collisionDamage = this.damage * 2;

        this.handle = handle;
        this.enemyAnimationSheetPath = enemyAnimationSheetPath;
    }

    @Override
    protected Enemy newObject() {
        // System.out.println("[+] Creating new Enemy");
        return new Enemy(colliderWidth, colliderHeight, colliderXcoordOffset, colliderYcoordOffset, maxHealth, damage, delayBetweenShootsBullets, bulletsSpeed, speed, handle, realShipWidth, realShipHeight, preferredShipWidth, preferredShipHeight, reward, collisionDamage, difficultyLevel, enemyAnimationSheetPath);
    }
}
