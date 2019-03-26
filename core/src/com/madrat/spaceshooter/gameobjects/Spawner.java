package com.madrat.spaceshooter.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.madrat.spaceshooter.gameobjects.poolobjects.Asteroid;
import com.madrat.spaceshooter.gameobjects.poolobjects.AsteroidPool;
import com.madrat.spaceshooter.gameobjects.poolobjects.Explosion;
import com.madrat.spaceshooter.gameobjects.poolobjects.ExplosionPool;
import com.madrat.spaceshooter.gameobjects.poolobjects.poweruppools.PowerUp;
import com.madrat.spaceshooter.gameobjects.poolobjects.poweruppools.PowerUpPool;
import com.madrat.spaceshooter.utils.Assets;

import java.util.ArrayList;
import java.util.Random;

import static com.madrat.spaceshooter.MainGame.SCALE_FACTOR;

public class Spawner {

    // TODO enemies pools
    // New unique pool for every enemy type
    // private ZapperPool zapperPool = new ZapperPool();
    // private TurtlePool turtlePool = new TurtlePool();
    // private PinkyPool pinkyPool = new PinkyPool();
    // private Array<Enemy> allEnemies;

    // All powerUps
    private PowerUpPool healPowerUpPool = new PowerUpPool(0.2f, 30, 25, 30, 25, "healPowerUp", Assets.healPowerUp);
    private PowerUpPool ammoPowerUpPool = new PowerUpPool(0.2f, 58, 53, 28, 26, "ammoPowerUp", Assets.ammoPowerUp);
    private PowerUpPool shieldPowerUpPool = new PowerUpPool(0.2f, 32, 32, 28, 28, "shieldPowerUp", Assets.shieldPowerUp);
    private Array<PowerUp> activePowerUps;

    // All explosions
    private ExplosionPool enemyExplosionPool = new ExplosionPool(0.11f, 96, Assets.explosion2);
    private ExplosionPool playerExplosionPool = new ExplosionPool(0.1f, 128, Assets.explosion3);
    private Array<Explosion> activeExplosions;

    // Asteroids
    private AsteroidPool asteroidPool = new AsteroidPool(0.07f, 32, 64, 64);
    private Array<Asteroid> activeAsteroids;
    private float asteroidSpawnTimer;

    // Holy Random
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

        // System.out.println("[+] Objects in enemyExplosionPool: " + enemyExplosionPool.getFree());
    }

    public void spawnPlayerExplosion(float x, float y, int preferredWidth, int preferredHeight) {
        Explosion newExplosion = playerExplosionPool.obtain();
        newExplosion.setUpExplosion(x, y, preferredWidth, preferredHeight, true);
        activeExplosions.add(newExplosion); // add explosion to shared list

        // System.out.println("[+] Objects in playerExplosionPool: " + playerExplosionPool.getFree());
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

    public void initPowerUps() {
        this.activePowerUps = new Array<PowerUp>();
    }

    public void randomPowerUp(float x, float y) {

        // Heal powerUp
        if (random.nextInt(3) == 2) {
            PowerUp newPowerUp = healPowerUpPool.obtain();
            newPowerUp.setupPowerUp(x, y, 30, 25, 10f);
            activePowerUps.add(newPowerUp);
            // System.out.println("[+] Objects in healPowerUpPool: " + healPowerUpPool.getFree());

            // Shield powerUp
        } else if (random.nextInt(3) == 1) {
            PowerUp newPowerUp = ammoPowerUpPool.obtain();
            newPowerUp.setupPowerUp(x, y, 28, 26, 10f);
            activePowerUps.add(newPowerUp);
            // System.out.println("[+] Objects in ammoPowerUpPool: " + ammoPowerUpPool.getFree());

            // Shield powerUp
        } else if (random.nextInt(3) == 0) {
            PowerUp newPowerUp = shieldPowerUpPool.obtain();
            newPowerUp.setupPowerUp(x, y, 28, 28, 10f);
            activePowerUps.add(newPowerUp);
            // System.out.println("[+] Objects in shieldPowerUpPool: " + shieldPowerUpPool.getFree());
        }
    }

    public void updatePowerUps(float delta) {
        for (PowerUp powerUp : activePowerUps) {
            powerUp.update(delta);
            if (powerUp.remove) {
                if (powerUp.getPowerUpCollisionRect().getColliderTag() == "healPowerUp")
                    healPowerUpPool.free(powerUp);
                else if (powerUp.getPowerUpCollisionRect().getColliderTag() == "ammoPowerUp")
                    ammoPowerUpPool.free(powerUp);
                else if (powerUp.getPowerUpCollisionRect().getColliderTag() == "shieldPowerUp")
                    shieldPowerUpPool.free(powerUp);
                activePowerUps.removeValue(powerUp, true);
            }
        }
    }

    public void renderPowerUps(SpriteBatch batch) {
        for (PowerUp powerUp : activePowerUps) {
            powerUp.render(batch);
            powerUp.getPowerUpCollisionRect().drawCollider(batch);
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

    public Array<PowerUp> getActivePowerUps() {
        return activePowerUps;
    }
}
