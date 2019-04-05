package com.madrat.spaceshooter.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.madrat.spaceshooter.gameobjects.poolobjects.Asteroid;
import com.madrat.spaceshooter.gameobjects.poolobjects.AsteroidPool;
import com.madrat.spaceshooter.gameobjects.poolobjects.Enemy;
import com.madrat.spaceshooter.gameobjects.poolobjects.EnemyPool;
import com.madrat.spaceshooter.gameobjects.poolobjects.Explosion;
import com.madrat.spaceshooter.gameobjects.poolobjects.ExplosionPool;
import com.madrat.spaceshooter.gameobjects.poolobjects.PowerUp;
import com.madrat.spaceshooter.gameobjects.poolobjects.PowerUpPool;
import com.madrat.spaceshooter.utils.Assets;

import java.util.Random;

import static com.madrat.spaceshooter.MainGame.SCALE_FACTOR;

public class Spawner {

    // TODO enemies pools
    // New unique pool for every enemy type
    private EnemyPool pinkyPool = new EnemyPool(32, 32, 64, 64, 54, 56, 0, 0, 1f, 0.05f, 0.6f, 700, 80, "pinky", Assets.ship10Animation);
    // private EnemyPool turtlePool = new EnemyPool();
    // private EnemyPool pinkyPool = new EnemyPool();
    private Array<Enemy> activeEnemies;
    private float enemySpawnTimer;

    // All powerUps
    private PowerUpPool healPowerUpPool = new PowerUpPool(0.2f, 30, 25, "healPowerUp", Assets.healPowerUp);
    private PowerUpPool ammoPowerUpPool = new PowerUpPool(0.2f, 58, 53, "ammoPowerUp", Assets.ammoPowerUp);
    private PowerUpPool shieldPowerUpPool = new PowerUpPool(0.2f, 32, 32, "shieldPowerUp", Assets.shieldPowerUp);
    private Array<PowerUp> activePowerUps;

    // All explosions
    private ExplosionPool enemyExplosionPool = new ExplosionPool(0.11f, 96, Assets.explosion2);
    private ExplosionPool playerExplosionPool = new ExplosionPool(0.1f, 128, Assets.explosion3);
    private Array<Explosion> activeExplosions;

    // All Asteroids (its different from Enemy.class)
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
        if (random.nextInt(22) == 7) {
            PowerUp newPowerUp = healPowerUpPool.obtain();
            newPowerUp.setupPowerUp(x, y, 30, 25, 10f);
            activePowerUps.add(newPowerUp);
            // System.out.println("[+] Objects in healPowerUpPool: " + healPowerUpPool.getFree());

            // Ammo powerUp
        } else if (random.nextInt(22) == 14) {
            PowerUp newPowerUp = ammoPowerUpPool.obtain();
            newPowerUp.setupPowerUp(x, y, 28, 26, 10f);
            activePowerUps.add(newPowerUp);
            // System.out.println("[+] Objects in ammoPowerUpPool: " + ammoPowerUpPool.getFree());

            // Shield powerUp
        } else if (true) {
            PowerUp newPowerUp = shieldPowerUpPool.obtain();
            newPowerUp.setupPowerUp(x, y, 28, 28, 10f);
            activePowerUps.add(newPowerUp);
            // System.out.println("[+] Objects in shieldPowerUpPool: " + shieldPowerUpPool.getFree());
        }
    }

    // Update all possible powerups
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

    // Draw powerups on screen
    public void renderPowerUps(SpriteBatch batch) {
        for (PowerUp powerUp : activePowerUps) {
            powerUp.render(batch);
            powerUp.getPowerUpCollisionRect().drawCollider(batch);
        }
    }

    public void initEnemies() {
        this.activeEnemies = new Array<Enemy>();
        this.enemySpawnTimer = random.nextFloat() * (Asteroid.MAX_ASTEROID_SPAWN_TIME - Asteroid.MIN_ASTEROID_SPAWN_TIME) + Asteroid.MIN_ASTEROID_SPAWN_TIME;
    }

    public void spawnEnemy() {
        enemySpawnTimer = random.nextFloat() * (Asteroid.MAX_ASTEROID_SPAWN_TIME - Asteroid.MIN_ASTEROID_SPAWN_TIME) + Asteroid.MIN_ASTEROID_SPAWN_TIME;

        Enemy newEnemy = pinkyPool.obtain();
        newEnemy.enemySetUp(random.nextInt(Gdx.graphics.getWidth() - (int) (64 * SCALE_FACTOR)), Gdx.graphics.getHeight() + 64 * SCALE_FACTOR, 0.2f, 150);
        activeEnemies.add(newEnemy);
    }

    public void updateEnemies(float delta) {
        enemySpawnTimer -= delta;

        if (enemySpawnTimer <= 0)
            spawnEnemy();

        for (Enemy enemy : activeEnemies) {
            enemy.update(delta);
            if (enemy.remove) {
                if (enemy.getHandle() == "pinky") {
                    pinkyPool.free(enemy);
                    // System.out.println("[+] Removed pinky from game\n[+] pinkyPool size: " + pinkyPool.getFree());
                }
                activeEnemies.removeValue(enemy, true);
            }
        }
    }

    public void renderEnemies(SpriteBatch batch) {
        for (Enemy enemy : activeEnemies) {
            enemy.render(batch);
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

    public EnemyPool getPinkyPool() {
        return pinkyPool;
    }

    public Array<Enemy> getActiveEnemies() {
        return activeEnemies;
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
