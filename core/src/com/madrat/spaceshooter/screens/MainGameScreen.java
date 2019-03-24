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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.madrat.spaceshooter.MainGame;
import com.madrat.spaceshooter.gameobjects.Asteroid;
import com.madrat.spaceshooter.gameobjects.Bullet;
import com.madrat.spaceshooter.gameobjects.Explosion;
import com.madrat.spaceshooter.gameobjects.PlayerShip;
import com.madrat.spaceshooter.utils.Assets;
import com.madrat.spaceshooter.utils.DialogAlert;
import com.madrat.spaceshooter.utils.ObjectHandler;
import com.madrat.spaceshooter.utils.ScrollingBackground;

import java.util.ArrayList;
import java.util.Random;

import static com.madrat.spaceshooter.MainGame.SCALE_FACTOR;

public class MainGameScreen implements Screen {

    private ArrayList<Asteroid> asteroids;
    private ArrayList<Explosion> explosions;

    private Random random;

    MainGame game;
    private PlayerShip playerShip;
    private float asteroidSpawnTimer;

    private boolean isPaused;
    private Sprite background;
    private SpriteBatch batch;
    private ScrollingBackground scrollingBackground;
    private ArrayList<ObjectHandler> sprites;

    private BitmapFont scoreFont;
    private GlyphLayout scoreLayout;

    private ArrayList<Asteroid> asteroidsToRemove;
    private ArrayList<Bullet> bulletsToRemove;
    private ArrayList<Explosion> explosionToRemove;

    private Stage stage;
    private Skin skin;
    private Table pauseTable, PauseMenuTable;
    private ImageButton pauseBtn;

    private TextButton continueButton;
    private TextButton restartButton;
    private TextButton backButton;
    private TextButton exitButton;

    private DialogAlert confirm;

    private MainGameScreen gameScreen;

    public MainGameScreen(MainGame newGame, SpriteBatch oldBatch) {

        this.game = newGame;
        this.batch = oldBatch;
        this.gameScreen = this;

        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal(Assets.uiskin));
        pauseBtn = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(Assets.pauseBtnUp)))), new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(Assets.pauseBtnDown)))));
        pauseBtn.getImage().setScale(SCALE_FACTOR);

        // Create Table for pause button
        pauseTable = new Table();
        pauseTable.setWidth(stage.getWidth());
        pauseTable.align(Align.right | Align.top);
        pauseTable.setPosition(0, MainGame.GENERAL_HEIGHT);
        pauseTable.padTop((15 * SCALE_FACTOR) * SCALE_FACTOR);
        pauseTable.padRight((15 * SCALE_FACTOR) * SCALE_FACTOR);
        pauseTable.add(pauseBtn);

        // Continue game button
        continueButton = new TextButton("continue", skin);
        continueButton.getLabel().setFontScale(1f * SCALE_FACTOR);
        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isPaused = false;
                // scrollingBackground._continue();
                PauseMenuTable.setVisible(false);
            }
        });

        // Restart game button
        restartButton = new TextButton("restart", skin);
        restartButton.getLabel().setFontScale(1f * SCALE_FACTOR);
        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                confirm = new DialogAlert("", skin);
                confirm.text("Do you really\nwant to restart?");
                confirm.yesButton("YES", new InputListener() {
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        gameScreen.dispose();
                        game.setScreen(new MainGameScreen(game, batch));
                        return true;
                    }
                }).noButton("NO", new InputListener() {
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        confirm.hide();
                        return true;
                    }
                });
                confirm.buttonYes.getLabel().setColor(new Color(0xe57575ff));
                confirm.buttonYes.getLabel().setFontScale(1f * SCALE_FACTOR);
                confirm.buttonNo.getLabel().setColor(new Color(0x94dd99ff));
                confirm.buttonNo.getLabel().setFontScale(1f * SCALE_FACTOR);
                confirm.show(stage);
            }
        });

        // Back to main menu button
        backButton = new TextButton("back", skin);
        backButton.getLabel().setFontScale(1f * SCALE_FACTOR);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                confirm = new DialogAlert("", skin);
                confirm.text("Do you really\nwant to leave?");
                confirm.yesButton("YES", new InputListener() {
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        gameScreen.dispose();
                        game.setScreen(new MainMenuScreen(game));
                        return true;
                    }
                }).noButton("NO", new InputListener() {
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        confirm.hide();
                        return true;
                    }
                });
                confirm.buttonYes.getLabel().setColor(new Color(0xe57575ff));
                confirm.buttonYes.getLabel().setFontScale(1f * SCALE_FACTOR);
                confirm.buttonNo.getLabel().setColor(new Color(0x94dd99ff));
                confirm.buttonNo.getLabel().setFontScale(1f * SCALE_FACTOR);
                // confirm.scaleBy(1f * SCALE_FACTOR);
                confirm.show(stage);
            }
        });

        // Close game button
        exitButton = new TextButton("exit", skin);
        exitButton.getLabel().setFontScale(1f * SCALE_FACTOR);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                confirm = new DialogAlert("", skin);
                confirm.text("Do you really\nwant to exit?");
                confirm.yesButton("YES", new InputListener() {
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        gameScreen.dispose();
                        Gdx.app.exit();
                        return true;
                    }
                }).noButton("NO", new InputListener() {
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        confirm.hide();
                        return true;
                    }
                });
                confirm.buttonYes.getLabel().setColor(new Color(0xe57575ff));
                confirm.buttonYes.getLabel().setFontScale(1f * SCALE_FACTOR);
                confirm.buttonNo.getLabel().setColor(new Color(0x94dd99ff));
                confirm.buttonNo.getLabel().setFontScale(1f * SCALE_FACTOR);
                // confirm.scaleBy(1f * SCALE_FACTOR);
                confirm.show(stage);
            }
        });

        // Pause menu table
        PauseMenuTable = new Table();
        PauseMenuTable.setWidth(stage.getWidth());
        PauseMenuTable.align(Align.center | Align.top);
        PauseMenuTable.setPosition(0, MainGame.GENERAL_HEIGHT);
        PauseMenuTable.padTop(120 * SCALE_FACTOR);
        PauseMenuTable.add(continueButton).padBottom(48 * SCALE_FACTOR);
        PauseMenuTable.row();
        PauseMenuTable.add(restartButton).padBottom(48 * SCALE_FACTOR);
        PauseMenuTable.row();
        PauseMenuTable.add(backButton).padBottom(48 * SCALE_FACTOR);
        PauseMenuTable.row();
        PauseMenuTable.add(exitButton);
        PauseMenuTable.setVisible(false);

        // Initialize explosions array
        explosions = new ArrayList<Explosion>();

        // Score BitmapFont + score GlyphLayout
        scoreFont = new BitmapFont(Gdx.files.internal(Assets.emulogicfnt));
        scoreFont.setColor(new Color(0x7a9af1));
        scoreFont.getData().setScale(SCALE_FACTOR);
        scoreLayout = new GlyphLayout(scoreFont, "0");

        // Asteroids
        random = new Random();
        asteroids = new ArrayList<Asteroid>();
        asteroidSpawnTimer = random.nextFloat() * (Asteroid.MAX_ASTEROID_SPAWN_TIME - Asteroid.MIN_ASTEROID_SPAWN_TIME) + Asteroid.MIN_ASTEROID_SPAWN_TIME;

        // Spawn player ship
        playerShip = new PlayerShip();

        // Create SpriteBatch to draw
        Gdx.input.setInputProcessor(stage);

        // Create background Sprite
        background = new Sprite(new Texture(Gdx.files.internal(Assets.backgroundSpace)));
        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Create scrolling background
        sprites = ScrollingBackground.initStarBackground();
        scrollingBackground = new ScrollingBackground(background, sprites);

        isPaused = false;
        playerShip.setNeedToShow(true);

        pauseBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isPaused = true;
                // scrollingBackground.pause();
                PauseMenuTable.setVisible(true);
            }
        });

        stage.addActor(pauseTable);
        stage.addActor(PauseMenuTable);
    }

    @Override
    public void show() { // Screen first appears
    }

    @Override
    public void render(float delta) {

        // Spawn asteroids
        if (!isPaused) {
            asteroidSpawnTimer -= delta;
            if (asteroidSpawnTimer <= 0) {
                asteroidSpawnTimer = random.nextFloat() * (Asteroid.MAX_ASTEROID_SPAWN_TIME - Asteroid.MIN_ASTEROID_SPAWN_TIME) + Asteroid.MIN_ASTEROID_SPAWN_TIME;
                asteroids.add(new Asteroid(120 * SCALE_FACTOR, random.nextInt(Gdx.graphics.getWidth() - (int) (64 * SCALE_FACTOR)), 0.07f, 64, 64, 64, 64));
            }

            // Update asteroids (delete old)
            asteroidsToRemove = new ArrayList<Asteroid>();
            for (Asteroid asteroid : asteroids) {
                asteroid.update(delta);
                if (asteroid.remove)
                    asteroidsToRemove.add(asteroid);
            }

            // Update player Bullets (delete old)
            bulletsToRemove = new ArrayList<Bullet>();
            for (Bullet bullet : playerShip.getBullets()) {
                bullet.update(delta);
                if (bullet.remove)
                    bulletsToRemove.add(bullet);
            }

            // Update Explosion (delete old)
            explosionToRemove = new ArrayList<Explosion>();
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
        }

        // GL important thing
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Begin SpriteBatch
        batch.begin();

        // Draw background
        scrollingBackground.draw(batch);

        // Shooting
        if (!isPaused)
            playerShip.shoot(delta);

        // Render player bullets
        if (playerShip.getBullets().size() > 0) {
            for (Bullet bullet : playerShip.getBullets()) {
                bullet.render(batch);
                bullet.getCollisionRect().drawCollider(batch);
            }
        }

        // Render asteroids
        for (Asteroid asteroid : asteroids) {
            asteroid.render(batch);
            asteroid.getCollisionRect().drawCollider(batch);
        }

        if (!isPaused) {
            // Collision detecting (only player bullets - asteroids)
            for (Bullet bullet : playerShip.getBullets()) {
                for (Asteroid asteroid : asteroids) {
                    // Collision between asteroid and bullet
                    if (bullet.getCollisionRect().collidesWith(asteroid.getCollisionRect())) {

                        // If bullet collides with asteroid we need to delete them too
                        bulletsToRemove.add(bullet);
                        asteroidsToRemove.add(asteroid);

                        // Spawn explosion
                        explosions.add(new Explosion(asteroid.getX(), asteroid.getY() - asteroid.getPreferredHeight() / 2, 0.11f, 96, 96, 96, new Texture(Assets.explosion2)));

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
                    explosions.add(new Explosion(asteroid.getX(), asteroid.getY() - asteroid.getPreferredHeight() / 2, 0.1f, 128, 128, 128, new Texture(Assets.explosion3)));

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
        }

        // Draw ship with animations bullets etc
        playerShip.draw(batch, delta);
        playerShip.getShipCollisionRect().drawCollider(batch);

        // Render explosions
        for (Explosion explosion : explosions) {
            explosion.render(batch);
        }

        if (!isPaused) {
            // Draw and update score
            scoreLayout.setText(scoreFont, "" + playerShip.getScore());
            scoreFont.draw(batch, scoreLayout, Gdx.graphics.getWidth() / 2 - scoreLayout.width / 2, Gdx.graphics.getHeight() - scoreLayout.height - 5);
        }

        batch.end();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
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
        scrollingBackground.dispose();
        playerShip.dispose();
    }
}
