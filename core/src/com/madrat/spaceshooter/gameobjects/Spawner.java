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
import com.madrat.spaceshooter.physics2d.CollisionRect;
import com.madrat.spaceshooter.utils.Assets;
import com.madrat.spaceshooter.utils.BuildConfig;

import java.util.Random;

import static com.madrat.spaceshooter.MainGame.SCALE_FACTOR;

public class Spawner {

    // All enemy types
    private EnemyPool zapperPool;
    private EnemyPool destroyerPool;
    private EnemyPool ignitorPool;
    private EnemyPool turtlePool;
    private EnemyPool ufoPool;
    private EnemyPool starPool;
    private EnemyPool pinkyPool;

    private Array<Enemy> activeEnemies;
    private Array<Enemy> enemiesToDelete;
    private float enemyWaveSpawnTimer;

    // All powerUps
    private PowerUpPool healPowerUpPool;
    private PowerUpPool ammoPowerUpPool;
    private PowerUpPool shieldPowerUpPool;
    private Array<PowerUp> activePowerUps;
    private Array<PowerUp> powerUpsToDelete;

    // All explosions
    private ExplosionPool enemyExplosionPool;
    private ExplosionPool playerExplosionPool;
    private Array<Explosion> activeExplosions;
    private Array<Explosion> explosionsToDelete;

    // All Asteroids (its different from Enemy.class)
    private AsteroidPool asteroidPool;
    private Array<Asteroid> activeAsteroids;
    private Array<Asteroid> asteroidsToDelete;
    private float asteroidSpawnTimer;

    // Random object
    private Random random;

    public Spawner() {
        this.random = new Random();
    }

    // Simple initializer function
    public void initAsteroids() {
        // Initialize Asteroid Pool
        this.asteroidPool = new AsteroidPool(0.07f, 34, 64, 64);

        // Create activeAsteroids Array
        this.activeAsteroids = new Array<Asteroid>();
        this.asteroidsToDelete = new Array<Asteroid>();

        // Create first spawnTimer value
        this.asteroidSpawnTimer = random.nextFloat() * (Asteroid.MAX_ASTEROID_SPAWN_TIME - Asteroid.MIN_ASTEROID_SPAWN_TIME) + Asteroid.MIN_ASTEROID_SPAWN_TIME;
    }

    // Spawn asteroid at random x which belongs to this range: [0, screen.width - asteroid.width]
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

        // Iterate through all active Asteroids, find which must be removed, add them to array, and after loop delete them from active array
        for (Asteroid asteroid : activeAsteroids) {
            asteroid.update(delta);
            if (asteroid.remove) {
                asteroidsToDelete.add(asteroid);
                asteroidPool.free(asteroid);
            }
        }

        // Deleting 'old' objects, and clearing array with them
        activeAsteroids.removeAll(asteroidsToDelete, true);
        asteroidsToDelete.clear();
    }

    public void renderAsteroids(SpriteBatch batch) {

        // Render every asteroid in activeAsteroids array
        for (Asteroid asteroid : activeAsteroids) {

            // Draw actual asteroid
            asteroid.render(batch);

            // Draw collider rect onto screen
            if (BuildConfig.DEBUG)
                asteroid.getCollisionCirlce().drawCollider(batch);
        }
    }

    // Explosions initializer
    public void initExplosions() {
        this.enemyExplosionPool = new ExplosionPool(0.11f, 96, Assets.explosion2);
        this.playerExplosionPool = new ExplosionPool(0.1f, 128, Assets.explosion3);

        this.activeExplosions = new Array<Explosion>();
        this.explosionsToDelete = new Array<Explosion>();
    }

    // Create 'enemy' explosion at gained x, y and with sizes preferredWidth, preferredHeight
    public void spawnEnemyExplosion(float x, float y, int preferredWidth, int preferredHeight) {
        Explosion newExplosion = enemyExplosionPool.obtain();
        newExplosion.setUpExplosion(x, y, preferredWidth, preferredHeight, false);
        activeExplosions.add(newExplosion); // add explosion to shared list

        // System.out.println("[+] Objects in enemyExplosionPool: " + enemyExplosionPool.getFree());
    }

    // Create 'player' explosion at gained x, y and with sizes preferredWidth, preferredHeight
    public void spawnPlayerExplosion(float x, float y, int preferredWidth, int preferredHeight) {
        Explosion newExplosion = playerExplosionPool.obtain();
        newExplosion.setUpExplosion(x, y, preferredWidth, preferredHeight, true);
        activeExplosions.add(newExplosion); // add explosion to shared list

        // System.out.println("[+] Objects in playerExplosionPool: " + playerExplosionPool.getFree());
    }

    public void updateExplosions(float delta) {

        // Iterate through all active Explosions, find which must be removed, add them to array, and after loop delete them from active array
        for (Explosion explosion : activeExplosions) {
            explosion.update(delta);
            if (explosion.remove) {
                if (explosion.isByPlayer()) {
                    explosionsToDelete.add(explosion);
                    playerExplosionPool.free(explosion);
                } else {
                    explosionsToDelete.add(explosion);
                    enemyExplosionPool.free(explosion);
                }
            }
        }

        // Deleting 'old' objects, and clearing array with them
        activeExplosions.removeAll(explosionsToDelete, true);
        explosionsToDelete.clear();
    }

    // Draw explosions from activeExplosions array
    public void renderExplosion(SpriteBatch batch) {
        for (Explosion explosion : activeExplosions) {
            explosion.render(batch);
        }
    }

    // Simple init function
    public void initPowerUps() {
        this.healPowerUpPool = new PowerUpPool(0.2f, 30, 25, CollisionRect.colliderTag.healPowerUp, Assets.healPowerUp);
        this.ammoPowerUpPool = new PowerUpPool(0.2f, 58, 53, CollisionRect.colliderTag.ammoPowerUp, Assets.ammoPowerUp);
        this.shieldPowerUpPool = new PowerUpPool(0.2f, 32, 32, CollisionRect.colliderTag.shieldPowerUp, Assets.shieldPowerUp);

        this.activePowerUps = new Array<PowerUp>();
        this.powerUpsToDelete = new Array<PowerUp>();
    }

    // Spawn random power up at gained x an y
    public void randomPowerUp(float x, float y) {
        switch (random.nextInt(10)) {
            // Heal powerUp
            case 3: {
                PowerUp newPowerUp = healPowerUpPool.obtain();
                newPowerUp.setupPowerUp(x, y, 27, 23, 10f);
                activePowerUps.add(newPowerUp);
                // System.out.println("[+] Objects in healPowerUpPool: " + healPowerUpPool.getFree());
                break;
            }
            // Ammo powerUp
            case 6: {
                PowerUp newPowerUp = ammoPowerUpPool.obtain();
                newPowerUp.setupPowerUp(x, y, 25, 23, 10f);
                activePowerUps.add(newPowerUp);
                // System.out.println("[+] Objects in ammoPowerUpPool: " + ammoPowerUpPool.getFree());
                break;
            }
            // Shield powerUp
            case 9: {
                PowerUp newPowerUp = shieldPowerUpPool.obtain();
                newPowerUp.setupPowerUp(x, y, 25, 25, 10f);
                activePowerUps.add(newPowerUp);
                // System.out.println("[+] Objects in shieldPowerUpPool: " + shieldPowerUpPool.getFree());
                break;
            }
            default: {
                break;
            }
        }
    }

    // Update all possible powerups
    public void updatePowerUps(float delta) {

        // Iterate through all active powerUps, find which must be removed, add them to array, and after loop delete them from active array
        for (PowerUp powerUp : activePowerUps) {
            powerUp.update(delta);
            if (powerUp.remove) {
                if (powerUp.getPowerUpCollisionRect().getTag() == CollisionRect.colliderTag.healPowerUp) {
                    powerUpsToDelete.add(powerUp);
                    healPowerUpPool.free(powerUp);
                } else if (powerUp.getPowerUpCollisionRect().getTag() == CollisionRect.colliderTag.ammoPowerUp) {
                    powerUpsToDelete.add(powerUp);
                    ammoPowerUpPool.free(powerUp);
                } else if (powerUp.getPowerUpCollisionRect().getTag() == CollisionRect.colliderTag.shieldPowerUp) {
                    powerUpsToDelete.add(powerUp);
                    shieldPowerUpPool.free(powerUp);
                }
            }
        }

        // Deleting 'old' objects, and clearing array with them
        activePowerUps.removeAll(powerUpsToDelete, true);
        powerUpsToDelete.clear();
    }

    // Draw powerups on screen
    public void renderPowerUps(SpriteBatch batch) {
        for (PowerUp powerUp : activePowerUps) {

            // Draw actual powerUp texture
            powerUp.render(batch);

            // Draw collider
            if (BuildConfig.DEBUG)
                powerUp.getPowerUpCollisionRect().drawCollider(batch);
        }
    }

    // Simple initializer for enemies
    public void initEnemies() {

        // This objects initialized using constants because they're immutable (unlike player ship)
        this.zapperPool = new EnemyPool(24, 24, 60, 49, 55, 45, 0, 0, 1, 0.085f, 0.3f, 600, 150, 50, 1, SpaceShip.shipHandler.zapper, Assets.ship1Animation);
        this.destroyerPool = new EnemyPool(32, 32, 64, 64, 56, 48, 0, -2, 1.4f, 0.12f, 0.4f, 600, 200, 100, 2, SpaceShip.shipHandler.destroyer, Assets.ship3Animation);
        this.ignitorPool = new EnemyPool(32, 32, 56, 56, 56, 56, 0, 0, 1, 0.1f, 0.28f, 600, 250, 140, 2, SpaceShip.shipHandler.ignitor, Assets.ship5Animation);
        this.turtlePool = new EnemyPool(32, 32, 64, 64, 46, 64, 0, 0, 3, 0.18f, 0.4f, 500, 90, 200, 3, SpaceShip.shipHandler.turtle, Assets.ship7Animation);
        this.ufoPool = new EnemyPool(32, 32, 64, 64, 52, 52, 0, 0, 1.2f, 0.21f, 0.3f, 700, 200, 220, 2, SpaceShip.shipHandler.ufo, Assets.ship13Animation);
        this.starPool = new EnemyPool(32, 32, 64, 64, 56, 56, 0, 0, 0.75f, 0.25f, 0.42f, 700, 300, 260, 3, SpaceShip.shipHandler.star, Assets.ship9Animation);
        this.pinkyPool = new EnemyPool(32, 32, 64, 64, 50, 56, 0, 0, 1f, 0.12f, 0.28f, 700, 80, 250, 2, SpaceShip.shipHandler.pinky, Assets.ship11Animation);

        this.activeEnemies = new Array<Enemy>();
        this.enemiesToDelete = new Array<Enemy>();
        this.enemyWaveSpawnTimer = random.nextFloat() * (Enemy.MAX_ENEMY_WAVE_SPAWN_TIME - Enemy.MIN_ENEMY_WAVE_SPAWN_TIME) + Enemy.MIN_ENEMY_WAVE_SPAWN_TIME;
    }

    public Enemy createRandomEnemy(SpaceShip.shipHandler shipType) {
        Enemy newEnemy;

        switch (shipType) {
            case zapper:
                newEnemy = zapperPool.obtain();
                break;
            case destroyer:
                newEnemy = destroyerPool.obtain();
                break;
            case ignitor:
                newEnemy = ignitorPool.obtain();
                break;
            case turtle:
                newEnemy = turtlePool.obtain();
                break;
            case ufo:
                newEnemy = ufoPool.obtain();
                break;
            case star:
                newEnemy = starPool.obtain();
                break;
            case pinky:
                newEnemy = pinkyPool.obtain();
                break;
            default:
                newEnemy = zapperPool.obtain();
                break;
        }
        return newEnemy;
    }

    // Spawn enemy at random x which belongs to this range: [0, screen.width - enemy.width]
    public void spawnEnemyWave(int currentScore) {
        enemyWaveSpawnTimer = random.nextFloat() * (Enemy.MAX_ENEMY_WAVE_SPAWN_TIME - Enemy.MIN_ENEMY_WAVE_SPAWN_TIME) + Enemy.MIN_ENEMY_WAVE_SPAWN_TIME;
        Enemy newEnemy;

        // Enemies per wave difficulty
        if (currentScore <= 2500) { // max difficultyLevel = 4
            newEnemy = createRandomEnemy(SpaceShip.shipHandler.getRandomShip());
            newEnemy.enemySetUp(random.nextInt(Gdx.graphics.getWidth() - (int) (newEnemy.getPreferredShipWidth() * SCALE_FACTOR)), Gdx.graphics.getHeight() + (newEnemy.getPreferredShipHeight() * SCALE_FACTOR), random.nextFloat() * (1.15f - 0.9f) + 0.9f);
            activeEnemies.add(newEnemy);
        } else if (currentScore > 2500 && currentScore <= 7500) {
            for (int i = 0; i < 2; ++i) {
                newEnemy = createRandomEnemy(SpaceShip.shipHandler.getRandomShip());
                newEnemy.enemySetUp(random.nextInt(Gdx.graphics.getWidth() - (int) (newEnemy.getPreferredShipWidth() * SCALE_FACTOR)), Gdx.graphics.getHeight() + (newEnemy.getPreferredShipHeight() * SCALE_FACTOR), random.nextFloat() * (1.15f - 0.9f) + 0.9f);
                activeEnemies.add(newEnemy);
            }
        } else if (currentScore > 7500 && currentScore <= 11500) {
            for (int i = 0; i < 3; ++i) {
                newEnemy = createRandomEnemy(SpaceShip.shipHandler.getRandomShip());
                newEnemy.enemySetUp(random.nextInt(Gdx.graphics.getWidth() - (int) (newEnemy.getPreferredShipWidth() * SCALE_FACTOR)), Gdx.graphics.getHeight() + (newEnemy.getPreferredShipHeight() * SCALE_FACTOR), random.nextFloat() * (1.15f - 0.9f) + 0.9f);
                activeEnemies.add(newEnemy);
            }
        } else if (currentScore > 11500) {
            for (int i = 0; i < 4; ++i) {
                newEnemy = createRandomEnemy(SpaceShip.shipHandler.getRandomShip());
                newEnemy.enemySetUp(random.nextInt(Gdx.graphics.getWidth() - (int) (newEnemy.getPreferredShipWidth() * SCALE_FACTOR)), Gdx.graphics.getHeight() + (newEnemy.getPreferredShipHeight() * SCALE_FACTOR), random.nextFloat() * (1.15f - 0.9f) + 0.9f);
                activeEnemies.add(newEnemy);
            }
        } else {
            throw new java.lang.Error("[-] Impossible score state!");
        }
    }

    // update each enemy state
    public void updateEnemies(float delta, int currentScore) {
        enemyWaveSpawnTimer -= delta;

        if (enemyWaveSpawnTimer <= 0)
            spawnEnemyWave(currentScore);

        // Iterate through all active enemies, find which must be removed, add them to array, and after loop delete them from active array
        for (Enemy enemy : activeEnemies) {
            enemy.update(delta);
            if (enemy.remove && enemy.canBeFullyRemoved) {
                if (enemy.getHandle() == SpaceShip.shipHandler.zapper) {
                    enemiesToDelete.add(enemy);
                    zapperPool.free(enemy);
                } else if (enemy.getHandle() == SpaceShip.shipHandler.destroyer) {
                    enemiesToDelete.add(enemy);
                    destroyerPool.free(enemy);
                } else if (enemy.getHandle() == SpaceShip.shipHandler.ignitor) {
                    enemiesToDelete.add(enemy);
                    ignitorPool.free(enemy);
                } else if (enemy.getHandle() == SpaceShip.shipHandler.turtle) {
                    enemiesToDelete.add(enemy);
                    turtlePool.free(enemy);
                } else if (enemy.getHandle() == SpaceShip.shipHandler.ufo) {
                    enemiesToDelete.add(enemy);
                    ufoPool.free(enemy);
                } else if (enemy.getHandle() == SpaceShip.shipHandler.star) {
                    enemiesToDelete.add(enemy);
                    starPool.free(enemy);
                } else if (enemy.getHandle() == SpaceShip.shipHandler.pinky) {
                    enemiesToDelete.add(enemy);
                    pinkyPool.free(enemy);
                }
            }
        }

        // Clear enemy array
        activeEnemies.removeAll(enemiesToDelete, true);
        // Clear array
        enemiesToDelete.clear();
    }

    // Draw all enemies on screen
    public void renderEnemies(SpriteBatch batch) {
        for (Enemy enemy : activeEnemies) {

            // Call render method for enemy
            enemy.render(batch);

            // Draw collider
            if (BuildConfig.DEBUG)
                enemy.getShipCollisionRect().drawCollider(batch);
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

    public void checkAsteroidPool() {
        System.out.println("[+](Asteroids Pool) active: " + activeAsteroids.size + " | free: " + asteroidPool.getFree() + "/" + asteroidPool.max + " | record: " + asteroidPool.peak);
    }

    public void checkEnemyPool() {
        System.out.println("[+](Enemies) active: " + activeEnemies.size);
        System.out.println("\t[Pinky] free: " + pinkyPool.getFree() + "/" + pinkyPool.max + " | record: " + pinkyPool.peak);
    }

    public void checkExplosionPool() {
        System.out.println("[+](Explosion) active: " + activeExplosions.size);
        System.out.println("\t[Enemy explosion] free: " + enemyExplosionPool.getFree() + "/" + enemyExplosionPool.max + " | record: " + enemyExplosionPool.peak);
        System.out.println("\t[Player explosion] free: " + playerExplosionPool.getFree() + "/" + playerExplosionPool.max + " | record: " + playerExplosionPool.peak);
    }

    public void checkPowerUpPool() {
        System.out.println("[+](PowerUP) active: " + activePowerUps.size);
        System.out.println("\t[Heal PowerUP] free: " + healPowerUpPool.getFree() + "/" + healPowerUpPool.max + " | record: " + healPowerUpPool.peak);
        System.out.println("\t[Rocket PowerUp] free: " + ammoPowerUpPool.getFree() + "/" + ammoPowerUpPool.max + " | record: " + ammoPowerUpPool.peak);
        System.out.println("\t[Shield PowerUp] free: " + shieldPowerUpPool.getFree() + "/" + shieldPowerUpPool.max + " | record: " + shieldPowerUpPool.peak);
    }
}
