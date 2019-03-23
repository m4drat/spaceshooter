package com.madrat.spaceshooter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.madrat.spaceshooter.MainGame;
import com.madrat.spaceshooter.gameobjects.Asteroid;
import com.madrat.spaceshooter.gameobjects.Bullet;
import com.madrat.spaceshooter.gameobjects.Explosion;
import com.madrat.spaceshooter.gameobjects.PlayerShip;
import com.madrat.spaceshooter.utils.Assets;
import com.madrat.spaceshooter.utils.ObjectHandler;
import com.madrat.spaceshooter.utils.ScrollingBackground;

import java.util.ArrayList;
import java.util.Random;

public class MainGameScreen implements Screen {

    public static final float MIN_ASTEROID_SPAWN_TIME = 0.3f;
    public static final float MAX_ASTEROID_SPAWN_TIME = 0.6f;
    private float asteroidSpawnTimer;

    private ArrayList<Asteroid> asteroids;
    private ArrayList<Explosion> explosions;

    private Random random;

    MainGame game;
    private PlayerShip playerShip;

    private boolean isPaused;
    private Sprite background;
    private SpriteBatch batch;
    private ScrollingBackground scrollingBackground;
    private ArrayList<ObjectHandler> sprites;

    private BitmapFont scoreFont;
    private GlyphLayout scoreLayout;

    private Stage stage;
    private Skin skin;
    private Table menuTable;

    private TextButton pause;

    public MainGameScreen(MainGame newgame, SpriteBatch oldBatch) {

        this.game = newgame;

        stage = new Stage(new ScreenViewport());

        // Explosions
        explosions = new ArrayList<Explosion>();

        // Score BitmapFont + score GlyphLayout
        scoreFont = new BitmapFont(Gdx.files.internal(Assets.emulogicfnt));
        scoreFont.setColor(new Color(0x7a9af1));
        scoreLayout = new GlyphLayout(scoreFont, "0");

        // Asteroids
        random = new Random();
        asteroids = new ArrayList<Asteroid>();
        asteroidSpawnTimer = random.nextFloat() * (MAX_ASTEROID_SPAWN_TIME - MIN_ASTEROID_SPAWN_TIME) + MIN_ASTEROID_SPAWN_TIME;

        // Spawn player ship
        playerShip = new PlayerShip();

        // Create SpriteBatch to draw
        this.batch = oldBatch;
        Gdx.input.setInputProcessor(stage);

        // Create background Sprite
        background = new Sprite(new Texture(Gdx.files.internal(Assets.backgroundSpace)));
        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Create scrolling background
        sprites = ScrollingBackground.initStarBackground();
        scrollingBackground = new ScrollingBackground(background, sprites);

        isPaused = false;
        playerShip.setNeedToShow(true);


    }

    @Override
    public void show() { // Screen first appears
    }

    @Override
    public void render(float delta) {

        // Spawn asteroids
        asteroidSpawnTimer -= delta;
        if (asteroidSpawnTimer <= 0) {
            asteroidSpawnTimer = random.nextFloat() * (MAX_ASTEROID_SPAWN_TIME - MIN_ASTEROID_SPAWN_TIME) + MIN_ASTEROID_SPAWN_TIME;
            asteroids.add(new Asteroid(120, random.nextInt(Gdx.graphics.getWidth() - Asteroid.WIDTH)));
        }

        // Update asteroids (delete old)
        ArrayList<Asteroid> asteroidsToRemove = new ArrayList<Asteroid>();
        for (Asteroid asteroid : asteroids) {
            asteroid.update(delta);
            if (asteroid.remove)
                asteroidsToRemove.add(asteroid);
        }

        // Update player Bullets (delete old)
        ArrayList<Bullet> bulletsToRemove = new ArrayList<Bullet>();
        for (Bullet bullet : playerShip.getBullets()) {
            bullet.update(delta);
            if (bullet.remove)
                bulletsToRemove.add(bullet);
        }

        // Update Explosion (delete old)
        ArrayList<Explosion> explosionToRemove = new ArrayList<Explosion>();
        for (Explosion explosion : explosions) {
            explosion.update(delta);
            if (explosion.remove)
                explosionToRemove.add(explosion);
        }
        // Deleting Explosion
        explosions.removeAll(explosionToRemove);

        // Player ship moving
        if (Gdx.input.isKeyPressed(Input.Keys.UP))
            playerShip.setY(playerShip.getY() + playerShip.getSpeed() * Gdx.graphics.getDeltaTime());
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
            playerShip.setY(playerShip.getY() - playerShip.getSpeed() * Gdx.graphics.getDeltaTime());
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            playerShip.setX(playerShip.getX() - playerShip.getSpeed() * Gdx.graphics.getDeltaTime());
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            playerShip.setX(playerShip.getX() + playerShip.getSpeed() * Gdx.graphics.getDeltaTime());

        // Update player collision rect
        playerShip.getShipCollisionRect().move(playerShip.getX(), playerShip.getY());

        // GL important thing
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Begin SpriteBatch
        batch.begin();

        // Draw background
        scrollingBackground.draw(batch);

        // Player Handlers
            // Shooting
            playerShip.shoot(delta);

            // Render player bullets
            if (playerShip.getBullets().size() > 0) {
                for (Bullet bullet : playerShip.getBullets()) {
                    bullet.render(batch);
                }
            }

            // Render asteroids
            for (Asteroid asteroid : asteroids) {
                asteroid.render(batch);
            }

            // Collision detecting (only player bullets - asteroids)
            for (Bullet bullet : playerShip.getBullets()) {
                for (Asteroid asteroid : asteroids) {
                    // Collision between asteroid and bullet
                    if (bullet.getCollisionRect().collidesWith(asteroid.getCollisionRect())) {

                        // If bullet collides with asteroid we need to delete them too
                        bulletsToRemove.add(bullet);
                        asteroidsToRemove.add(asteroid);

                        // Spawn explosion
                        explosions.add(new Explosion(asteroid.getX(), asteroid.getY() - asteroid.HEIGHT / 2, 0.11f, 96, 0.9f, new Texture(Assets.explosion2)));

                        // Increase score value
                        playerShip.setScore(playerShip.getScore() + Asteroid.REWARD);
                    }
                }
            }
            // Remove all bullets which must be destroyed (collision results, y < 0, ...)
            playerShip.getBullets().removeAll(bulletsToRemove);

            // Check for collisions between player and asteroids
            for (Asteroid asteroid : asteroids) {
                if (asteroid.getCollisionRect().collidesWith(playerShip.getShipCollisionRect())) {
                    // delete asteroid
                    asteroidsToRemove.add(asteroid);

                    // Spawn Explosion
                    explosions.add(new Explosion(asteroid.getX(), asteroid.getY() - asteroid.HEIGHT / 2, 0.1f, 128, 0.8f, new Texture(Assets.explosion3)));

                    // decrease player health
                    playerShip.setCurrentHealth(playerShip.getCurrentHealth() - asteroid.DAMAGE);

                    // Increase score value
                    playerShip.setScore(playerShip.getScore() + Asteroid.REWARD);

                    // Game Over
                    if (playerShip.getCurrentHealth() <= 0) {
                        game.setScreen(new GameOverScreen(game, batch, scrollingBackground, playerShip.getScore()));
                    }
                }
            }

            // After all possible collisions delete asteroids
            asteroids.removeAll(asteroidsToRemove);

            // check for bounds
            playerShip.correctBounds();
            // Draw ship with animations bullets etc
            playerShip.draw(batch, delta);

        // Render explosions
        for (Explosion explosion : explosions) {
            explosion.render(batch);
        }

        // Draw and update score
        scoreLayout.setText(scoreFont, "" + playerShip.getScore());
        scoreFont.draw(batch, scoreLayout, Gdx.graphics.getWidth() / 2 - scoreLayout.width / 2, Gdx.graphics.getHeight() - scoreLayout.height - 5);

        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() { // get rid of the screen
        batch.dispose();
        background.getTexture().dispose();
        scrollingBackground.dispose();
        playerShip.dispose();
    }
}
