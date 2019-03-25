package com.madrat.spaceshooter.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.madrat.spaceshooter.gameobjects.poolobjects.Asteroid;
import com.madrat.spaceshooter.gameobjects.poolobjects.AsteroidPool;
import com.madrat.spaceshooter.gameobjects.poolobjects.Explosion;
import com.madrat.spaceshooter.gameobjects.poolobjects.ExplosionPool;
import com.madrat.spaceshooter.utils.Assets;

import java.util.Random;

import static com.madrat.spaceshooter.MainGame.SCALE_FACTOR;

public class Spawner {

    private ExplosionPool enemyExplosionPool = new ExplosionPool(0.11f, 96, Assets.explosion2);
    private ExplosionPool playerExplosionPool = new ExplosionPool(0.1f, 128, Assets.explosion3);
    private Array<Explosion> activeExplosions;

    private AsteroidPool asteroidPool = new AsteroidPool(0.07f, 32, 64, 64);
    private Array<Asteroid> activeAsteroids;
    private float asteroidSpawnTimer;

    private Random random;

    public Spawner() {
        this.random = new Random();
    }

    public void initAsteroids() {
        // Create activeAsteroids Array
        this.activeAsteroids = new Array<Asteroid>();

        // Create first spawnTimer value
        this.asteroidSpawnTimer = random.nextFloat() * (Asteroid.MAX_ASTEROID_SPAWN_TIME - Asteroid.MIN_ASTEROID_SPAWN_TIME) + Asteroid.MIN_ASTEROID_SPAWN_TIME;
    }

    public void spawnAsteroid() {

        // Generate new Timer value
        asteroidSpawnTimer = random.nextFloat() * (Asteroid.MAX_ASTEROID_SPAWN_TIME - Asteroid.MIN_ASTEROID_SPAWN_TIME) + Asteroid.MIN_ASTEROID_SPAWN_TIME;

        // Create Asteroid object from pool
        Asteroid newAsteroid = asteroidPool.obtain();

        // Set up object
        newAsteroid.setUpAsteroid(120 * SCALE_FACTOR, random.nextInt(Gdx.graphics.getWidth() - (int) (64 * SCALE_FACTOR)));
        activeAsteroids.add(newAsteroid);
        // System.out.println("[+] Objects in asteroidPool: " + asteroidPool.getFree());
    }

    public void updateAsteroids(float delta) {
        // Decrease SpawnTimer by delta
        asteroidSpawnTimer -= delta;

        // SpawnAsteroid
        if (asteroidSpawnTimer <= 0)
            spawnAsteroid();

        // Update asteroids (delete old)
        for (Asteroid asteroid : activeAsteroids) {
            asteroid.update(delta);
            if (asteroid.remove) {
                asteroidPool.free(asteroid);
                activeAsteroids.removeValue(asteroid, true);
            }
        }
    }

    public void renderAsteroids(SpriteBatch batch) {
        for (Asteroid asteroid : activeAsteroids) {
            asteroid.render(batch);
            asteroid.getCollisionCirlce().drawCollider(batch);
        }
    }

    public void initExplosions() {
        this.activeExplosions = new Array<Explosion>();
    }

    public void spawnEnemyExplosion(float x, float y, int preferredWidth, int preferredHeight) {
        Explosion newExplosion = enemyExplosionPool.obtain();
        newExplosion.setUpExplosion(x, y, preferredWidth, preferredHeight, false);
        activeExplosions.add(newExplosion); // add explosion to shared list

        System.out.println("[+] Objects in enemyExplosionPool: " + enemyExplosionPool.getFree());
    }

    public void spawnPlayerExplosion(float x, float y, int preferredWidth, int preferredHeight) {
        Explosion newExplosion = playerExplosionPool.obtain();
        newExplosion.setUpExplosion(x, y, preferredWidth, preferredHeight, true);
        activeExplosions.add(newExplosion); // add explosion to shared list

        System.out.println("[+] Objects in playerExplosionPool: " + playerExplosionPool.getFree());
    }

    public void updateExplosions(float delta) {
        for (Explosion explosion : activeExplosions) {
            explosion.update(delta);
            if (explosion.remove) {
                if (explosion.isByPlayer()) {
                    playerExplosionPool.free(explosion);
                    activeExplosions.removeValue(explosion, true);
                } else {
                    enemyExplosionPool.free(explosion);
                    activeExplosions.removeValue(explosion, true);
                }
            }
        }
    }

    public void renderExplosion(SpriteBatch batch) {
        for (Explosion explosion : activeExplosions) {
            explosion.render(batch);
        }
    }

    public float getAsteroidSpawnTimer() {
        return asteroidSpawnTimer;
    }

    public void setAsteroidSpawnTimer(float asteroidSpawnTimer) {
        this.asteroidSpawnTimer = asteroidSpawnTimer;
    }

    public Array<Asteroid> getActiveAsteroids() {
        return activeAsteroids;
    }

    public void setActiveAsteroids(Array<Asteroid> activeAsteroids) {
        this.activeAsteroids = activeAsteroids;
    }

    public AsteroidPool getAsteroidPool() {
        return asteroidPool;
    }

    public ExplosionPool getEnemyExplosionPool() {
        return enemyExplosionPool;
    }

    public ExplosionPool getPlayerExplosionPool() {
        return playerExplosionPool;
    }

    public Array<Explosion> getActiveExplosions() {
        return activeExplosions;
    }
}
