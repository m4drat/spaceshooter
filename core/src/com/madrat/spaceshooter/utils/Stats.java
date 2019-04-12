package com.madrat.spaceshooter.utils;

public class Stats {

    private int killedEnemies = 0, healPickedUp = 0, ammoPickedUp = 0, shieldPickedUp = 0, destroyedAsteroids = 0, score = 0;

    /**
     * Enemies Stats
     */

    public int getKilledEnemies() {
        return killedEnemies;
    }

    public void setKilledEnemies(int killedEnemies) {
        this.killedEnemies = killedEnemies;
    }

    public void incKilledEnemies(int inc) {
        this.killedEnemies += inc;
    }

    /**
     * Heal Stats
     */

    public int getHealPickedUp() {
        return healPickedUp;
    }

    public void setHealPickedUp(int healPickedUp) {
        this.healPickedUp = healPickedUp;
    }

    public void incHealPickedUp(int inc) {
        this.healPickedUp += inc;
    }

    /**
     * Ammo Stats
     */

    public int getAmmoPickedUp() {
        return ammoPickedUp;
    }

    public void setAmmoPickedUp(int ammoPickedUp) {
        this.ammoPickedUp = ammoPickedUp;
    }

    public void incAmmoPickedUp(int inc) {
        this.ammoPickedUp += inc;
    }

    /**
     * Shield Stats
     */

    public int getShieldPickedUp() {
        return shieldPickedUp;
    }

    public void setShieldPickedUp(int shieldPickedUp) {
        this.shieldPickedUp = shieldPickedUp;
    }

    public void incShieldPickedUp(int inc) {
        this.shieldPickedUp += inc;
    }

    /**
     * Asteroids Stats
     */

    public int getDestroyedAsteroids() {
        return destroyedAsteroids;
    }

    public void setDestroyedAsteroids(int destroyedAsteroids) {
        this.destroyedAsteroids = destroyedAsteroids;
    }

    public void incDestroyedAsteroids(int inc) {
        this.destroyedAsteroids += inc;
    }

    /**
     * Score Stats
     */
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void incScore(int inc) {
        this.score += inc;
    }
}
