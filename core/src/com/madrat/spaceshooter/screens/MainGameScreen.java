package com.madrat.spaceshooter.screens;

import com.badlogic.gdx.Application;
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
import com.madrat.spaceshooter.gameobjects.PowerUp;
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
    private ArrayList<PowerUp> powerUps;

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
    private ArrayList<Explosion> explosionToRemove;
    private ArrayList<PowerUp> powerUpsToRemove;

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

    public MainGameScreen(MainGame newGame) {

        // Load Assets
        Assets.loadExplosions();
        Assets.loadAsteroids();
        Assets.loadUiButtons();
        Assets.loadPowerups();
        Assets.loadShips();
        Assets.loadBullets();
        Assets.manager.finishLoading();

        this.game = newGame;
        this.batch = new SpriteBatch();
        this.gameScreen = this;

        stage = new Stage(new ScreenViewport());
        skin = Assets.manager.get(Assets.uiskin, Skin.class);
        pauseBtn = new ImageButton(new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.pauseBtnUp, Texture.class))), new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.pauseBtnDown, Texture.class))));
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
                        dispose();
                        batch.dispose();
                        unload();
                        game.setScreen(new MainGameScreen(game));
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
                        dispose();
                        batch.dispose();
                        unload();
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
        scoreFont = Assets.manager.get(Assets.emulogicfnt, BitmapFont.class);
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
        // background = new Sprite(new Texture(Gdx.files.internal(Assets.backgroundSpace)));
        background = new Sprite(Assets.manager.get(Assets.backgroundSpace, Texture.class));
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

        powerUps = new ArrayList<PowerUp>();

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

                // FIXME memoryLeak
                // asteroids.add(new Asteroid(120 * SCALE_FACTOR, random.nextInt(Gdx.graphics.getWidth() - (int) (64 * SCALE_FACTOR)), 0.07f, 32,64, 64));
            }

            // Update asteroids (delete old)
            asteroidsToRemove = new ArrayList<Asteroid>();
            for (Asteroid asteroid : asteroids) {
                asteroid.update(delta);
                if (asteroid.remove) {
                    asteroidsToRemove.add(asteroid);
                    // asteroid.dispose();
                }
            }

            // Update player Bullets (delete old)
            for (Bullet bullet : playerShip.getActiveBullets()) {
                bullet.update(delta);
                if (bullet.remove) {
                    playerShip.getBulletPool().free(bullet);
                    playerShip.getActiveBullets().removeValue(bullet, true);
                }
            }

            // Update Explosion (delete old)
            explosionToRemove = new ArrayList<Explosion>();
            for (Explosion explosion : explosions) {
                explosion.update(delta);
                if (explosion.remove) {
                    explosionToRemove.add(explosion);
                    // explosion.dispose();
                }
            }
            // Deleting Explosion
            explosions.removeAll(explosionToRemove);

            // Update powerUps
            powerUpsToRemove = new ArrayList<PowerUp>();
            for (PowerUp powerUp : powerUps) {
                powerUp.update(delta);
                if (powerUp.remove) {
                    powerUpsToRemove.add(powerUp);
                    // powerUp.dispose();
                }
            }
            // Deleting powerUps
            powerUps.removeAll(powerUpsToRemove);

            // Player ship moving
            if (MainGame.applicationType == Application.ApplicationType.Android) {
                playerShip.performInput(delta);
            } else {
                if (Gdx.input.isKeyPressed(Input.Keys.UP))
                    playerShip.setY(playerShip.getY() + playerShip.getSpeed() * Gdx.graphics.getDeltaTime());
                if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
                    playerShip.setY(playerShip.getY() - playerShip.getSpeed() * Gdx.graphics.getDeltaTime());
                if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
                    playerShip.setX(playerShip.getX() - playerShip.getSpeed() * Gdx.graphics.getDeltaTime());
                if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
                    playerShip.setX(playerShip.getX() + playerShip.getSpeed() * Gdx.graphics.getDeltaTime());

                playerShip.correctBounds();
            }
            // Update player collision rect
            playerShip.updateCollisionRect();
        }

        // GL important thing
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Begin SpriteBatch
        batch.begin();

        // Draw background
        scrollingBackground.draw(batch);

        // FIXME memoryLeak (in bullets creation)
        // Shooting
        if (!isPaused)
            playerShip.shoot(delta);

        // Render player bullets
        if (playerShip.getActiveBullets().size > 0) {
            for (Bullet bullet : playerShip.getActiveBullets()) {
                bullet.render(batch);
                // bullet.getCollisionRect().drawCollider(batch);
            }
        }

        // Render asteroids
        for (Asteroid asteroid : asteroids) {
            asteroid.render(batch);
            asteroid.getCollisionCirlce().drawCollider(batch);
        }

        if (!isPaused) {
            // Collision detecting (only player bullets - asteroids)
            for (Bullet bullet : playerShip.getActiveBullets()) {
                for (Asteroid asteroid : asteroids) {
                    // Collision between asteroid and bullet
                    if (bullet.getCollisionRect().collidesWith(asteroid.getCollisionCirlce())) {

                        // If bullet collides with asteroid we need to delete them too
                        playerShip.getBulletPool().free(bullet);
                        playerShip.getActiveBullets().removeValue(bullet, true);
                        asteroidsToRemove.add(asteroid);

                        // FIXME memoryLeak
                        // Spawn explosion
                        explosions.add(new Explosion(asteroid.getX(), asteroid.getY() - asteroid.getRadius(), 0.11f, 96, 96, 96, Assets.manager.get(Assets.explosion2, Texture.class)));

                        // Increase score value
                        playerShip.setScore(playerShip.getScore() + Asteroid.REWARD);

                        // FIXME memoryLeak
                        // Spawn PowerUp
                        if (random.nextInt(7) == 5)
                            powerUps.add(new PowerUp(asteroid.getX(), asteroid.getY(), 0.2f, 30, 25, 30, 25, 10f, "healPowerUp", Assets.manager.get(Assets.healPowerUp, Texture.class)));
                    }
                }
            }

            // Check for collisions between player and powerUp
            for (PowerUp powerUp : powerUps) {

                // User pickUps powerUp
                if (powerUp.getPowerUpCollisionRect().collidesWith(playerShip.getShipCollisionRect())) {
                    powerUp.remove = true;

                    // Heal powerUp
                    if (powerUp.getPowerUpCollisionRect().getColliderTag() == "healPowerUp") {
                        if (playerShip.getCurrentHealth() + playerShip.getMaxHealing() > playerShip.getMaxHealth()) {
                            playerShip.setCurrentHealth(playerShip.getMaxHealth());
                        } else {
                            playerShip.setCurrentHealth(playerShip.getCurrentHealth() + playerShip.getMaxHealing());
                        }
                    } else if (powerUp.getPowerUpCollisionRect().getColliderTag() == "ammoPowerUp") {

                    }
                }
            }

            // Check for collisions between player and asteroids
            for (Asteroid asteroid : asteroids) {
                if (asteroid.getCollisionCirlce().collidesWith(playerShip.getShipCollisionRect())) {
                    // delete asteroid
                    asteroidsToRemove.add(asteroid);

                    // FIXME memoryLeak
                    // Spawn Explosion
                    explosions.add(new Explosion(asteroid.getX() - asteroid.getRadius(), asteroid.getY() - asteroid.getRadius(), 0.1f, 128, 128, 128, Assets.manager.get(Assets.explosion3, Texture.class)));

                    // decrease player health
                    playerShip.setCurrentHealth(playerShip.getCurrentHealth() - asteroid.DAMAGE);

                    // Increase score value
                    playerShip.setScore(playerShip.getScore() + Asteroid.REWARD);

                    // Game Over
                    if (playerShip.getCurrentHealth() <= 0) {
                        dispose();
                        batch.dispose();
                        this.unload();
                        game.setScreen(new GameOverScreen(game, scrollingBackground, playerShip.getScore()));
                    }
                }
            }

            // After all possible collisions delete asteroids
            asteroids.removeAll(asteroidsToRemove);
        }

        // Render powerUps
        for (PowerUp powerUp : powerUps) {
            powerUp.render(batch);
            powerUp.getPowerUpCollisionRect().drawCollider(batch);
        }

        // Draw ship with animations, bullets, etc + collisionRectangle
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
        // game.dispose();
        // scrollingBackground.dispose();
        playerShip.dispose();
    }

    public void unload() {
        // Unload Assets
        Assets.unloadExplosions();
        Assets.unloadAsteroids();
        Assets.unloadUiButtons();
        Assets.unloadPowerups();
        Assets.unloadShips();
        Assets.unloadBullets();
    }
}
