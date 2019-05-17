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
import com.madrat.spaceshooter.gameobjects.poolobjects.Asteroid;
import com.madrat.spaceshooter.gameobjects.poolobjects.Bullet;
import com.madrat.spaceshooter.gameobjects.PlayerShip;
import com.madrat.spaceshooter.gameobjects.poolobjects.Enemy;
import com.madrat.spaceshooter.gameobjects.poolobjects.PowerUp;
import com.madrat.spaceshooter.gameobjects.Spawner;
import com.madrat.spaceshooter.physics2d.CollisionRect;
import com.madrat.spaceshooter.utils.Assets;
import com.madrat.spaceshooter.utils.BuildConfig;
import com.madrat.spaceshooter.utils.Strings;
import com.madrat.spaceshooter.utils.uiutils.DialogAlert;
import com.madrat.spaceshooter.utils.ObjectHandler;
import com.madrat.spaceshooter.utils.ScrollingBackground;
import com.madrat.spaceshooter.utils.Stats;

import java.util.ArrayList;

import static com.madrat.spaceshooter.MainGame.SCALE_FACTOR;
import static com.madrat.spaceshooter.MainGame.debugUtils;
import static com.madrat.spaceshooter.gameobjects.PlayerShip.animationState;

/**
 * Main game class with game loop
 * All inGame logic is here
 */

public class MainGameScreen implements Screen {

    MainGame game;
    private PlayerShip playerShip;
    private Spawner spawner;

    private boolean isPaused;
    private Sprite background;
    private SpriteBatch batch;
    private ScrollingBackground scrollingBackground;
    private ArrayList<ObjectHandler> sprites;

    private BitmapFont scoreFont;
    private GlyphLayout scoreLayout, ammoLayout;
    private Color ammoLayoutColor = Assets.lightAquamarine;

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

    private Stats stats;
    // private int killedEnemies = 0, healPickedUp = 0, ammoPickedUp = 0, shieldPickedUp = 0, destroyedAsteroids = 0;

    public MainGameScreen(MainGame newGame) {
        this.game = newGame;
        this.batch = new SpriteBatch();
        this.gameScreen = this;

        stats = new Stats();

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
        continueButton = new TextButton(Strings.continueTxt, skin);
        continueButton.getLabel().setFontScale(1f * SCALE_FACTOR);
        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                closePauseMenu();
            }
        });

        // Restart game button
        restartButton = new TextButton(Strings.restartTxt, skin);
        restartButton.getLabel().setFontScale(1f * SCALE_FACTOR);
        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                confirm = new DialogAlert("", skin, stage);
                confirm.text(Strings.restartConfirmTxt);
                confirm.yesButton(Strings.yesTxt, new InputListener() {
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        batch.dispose();
                        game.setScreen(new MainGameScreen(game));
                        return true;
                    }
                }).noButton(Strings.noTxt, new InputListener() {
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        confirm.hide();
                        return true;
                    }
                });
                confirm.buttonYes.getLabel().setColor(Assets.lightPinky);
                confirm.buttonYes.getLabel().setFontScale(1f * SCALE_FACTOR);
                confirm.buttonNo.getLabel().setColor(Assets.lightGreen_2);
                confirm.buttonNo.getLabel().setFontScale(1f * SCALE_FACTOR);
                confirm.show(stage);
            }
        });

        // Back to main menu button
        backButton = new TextButton(Strings.backTxt, skin);
        backButton.getLabel().setFontScale(1f * SCALE_FACTOR);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                confirm = new DialogAlert("", skin, stage);
                confirm.text(Strings.confirmLeave);
                confirm.yesButton(Strings.yesTxt, new InputListener() {
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        batch.dispose();
                        game.setScreen(new MainMenuScreen(game));
                        return true;
                    }
                }).noButton(Strings.noTxt, new InputListener() {
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        confirm.hide();
                        return true;
                    }
                });
                confirm.buttonYes.getLabel().setColor(Assets.lightPinky);
                confirm.buttonYes.getLabel().setFontScale(1f * SCALE_FACTOR);
                confirm.buttonNo.getLabel().setColor(Assets.lightGreen_2);
                confirm.buttonNo.getLabel().setFontScale(1f * SCALE_FACTOR);
                confirm.show(stage);
            }
        });

        // Close game button
        exitButton = new TextButton(Strings.exitTxt, skin);
        exitButton.getLabel().setFontScale(1f * SCALE_FACTOR);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                confirm = new DialogAlert("", skin, stage);
                confirm.text(Strings.confirmExitTxt);
                confirm.yesButton(Strings.yesTxt, new InputListener() {
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        Assets.manager.dispose();
                        Gdx.app.exit();
                        return true;
                    }
                }).noButton(Strings.noTxt, new InputListener() {
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        confirm.hide();
                        return true;
                    }
                });
                confirm.buttonYes.getLabel().setColor(Assets.lightPinky);
                confirm.buttonYes.getLabel().setFontScale(1f * SCALE_FACTOR);
                confirm.buttonNo.getLabel().setColor(Assets.lightGreen_2);
                confirm.buttonNo.getLabel().setFontScale(1f * SCALE_FACTOR);
                // confirm.scaleBy(1f * SCALE_FACTOR);
                confirm.show(stage);
            }
        });

        // Pause menu table (with all buttons)
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

        // Score BitmapFont + score GlyphLayout
        scoreFont = Assets.manager.get(Assets.emulogicfnt, BitmapFont.class);
        scoreFont.setColor(new Color(0x7a9af1));
        scoreFont.getData().setScale(SCALE_FACTOR);
        scoreLayout = new GlyphLayout(scoreFont, "0");

        // Layout to show ammo stats
        ammoLayout = new GlyphLayout(scoreFont, "0");

        // Spawn player ship
        playerShip = new PlayerShip();

        // Set new input processor
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
                openPauseMenu();
            }
        });

        // Create spawner object + initialize asteroids
        spawner = new Spawner();
        spawner.initAsteroids();

        // Initialize explosions
        spawner.initExplosions();

        // Initialize powerUps
        spawner.initPowerUps();

        // Initialize enemies
        spawner.initEnemies();

        // Add actor to stage (pause Button + pause menu)

        if (BuildConfig.UIDEBUG) {
            pauseTable.debug();
            PauseMenuTable.debug();
        }

        stage.addActor(pauseTable);
        stage.addActor(PauseMenuTable);

        // Back Key listener
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if ((keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) && !isPaused) {
                    openPauseMenu();
                    return true;
                } else if (((keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) && isPaused)) {
                    closePauseMenu();
                    return true;
                }
                return false;
            }
        });
    }

    private void openPauseMenu() {
        isPaused = true;
        // scrollingBackground.pause();
        PauseMenuTable.setVisible(true);
    }

    private void closePauseMenu() {
        isPaused = false;
        // scrollingBackground._continue();
        PauseMenuTable.setVisible(false);
    }

    @Override
    public void show() { // Screen first appears
    }

    @Override
    public void render(float delta) {
        if (!isPaused) {

            // Update and spawn asteroids
            spawner.updateAsteroids(delta);

            // Update Explosion (delete old)
            spawner.updateExplosions(delta);

            // Update powerUps
            spawner.updatePowerUps(delta);

            // Update enemies + spawn enemy waves based on score value
            spawner.updateEnemies(delta, playerShip.getScore());

            // Player ship moving
            if (MainGame.applicationType == Application.ApplicationType.Android) {
                playerShip.performInputMobile(delta);
            } else {
                playerShip.performInputPC(delta);
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

        // Shooting
        if (!isPaused) {
            playerShip.shoot(delta);
            // Update player Bullets (delete old + move them)
            playerShip.updateBullets(delta);
        }

        // Render player bullets
        playerShip.renderBullets(batch);

        // Render asteroids
        spawner.renderAsteroids(batch);

        if (!isPaused) {

            // Collision detection (only player bullets/rockets - enemyShips)
            for (Bullet bullet : playerShip.getActiveBullets()) {
                for (Enemy enemy : spawner.getActiveEnemies()) {
                    if (bullet.getCollisionRect().collidesWith(enemy.getShipCollisionRect())) {
                        if (bullet.getType() == Bullet.bulletType.rocket) {
                            spawner.spawnPlayerExplosion(bullet.getX() - bullet.getPreferredWidth() / 2, bullet.getY() - bullet.getPreferredHeight() / 2, 40, 40);
                        }
                        bullet.remove = true;

                        enemy.setCurrentHealth(enemy.getCurrentHealth() - playerShip.getDamage());
                        if (enemy.getCurrentAnimation() != animationState.shipUnderAttackAnimation)
                            enemy.setCurrentAnimation(animationState.shipUnderAttackAnimation);

                        if (enemy.getCurrentHealth() <= 0) {
                            stats.incKilledEnemies(1);

                            enemy.die();
                            spawner.spawnEnemyExplosion(enemy.getX() - enemy.getPreferredShipWidth() / 2, enemy.getY() - enemy.getPreferredShipHeight() / 2, 86, 86);

                            // Increase score value
                            playerShip.setScore(playerShip.getScore() + enemy.getReward());

                            // Update Score value
                            scoreLayout.setText(scoreFont, "" + playerShip.getScore());

                            // Spawn Random PowerUp
                            spawner.randomPowerUp(enemy.getX(), enemy.getY());
                        }
                    }
                }
            }

            // Enemy bullets - player collisions
            for (Enemy enemy : spawner.getActiveEnemies()) {
                for (Bullet enemyBullet : enemy.getActiveBullets()) {
                    if (enemyBullet.getCollisionRect().collidesWith(playerShip.getShipCollisionRect())) {
                        enemyBullet.remove = true;
                        // Update player health
                        playerShip.updateHealth(enemy.getDamage());
                    }
                }
            }

            // enemy ships - player ship collision
            for (Enemy enemy : spawner.getActiveEnemies()) {
                if (enemy.getShipCollisionRect().collidesWith(playerShip.getShipCollisionRect())) {
                    if (enemy.isAlive) {
                        stats.incKilledEnemies(1);

                        enemy.die();
                        spawner.spawnEnemyExplosion(enemy.getX() - enemy.getPreferredShipWidth() / 2, enemy.getY() - enemy.getPreferredShipHeight() / 2, 86, 86);
                        playerShip.setScore(playerShip.getScore() + enemy.getReward());
                        // Update Score value
                        scoreLayout.setText(scoreFont, "" + playerShip.getScore());
                        playerShip.updateHealth(enemy.getCollisionDamage());
                    }
                }
            }

            // Collision detecting (only player bullets - asteroids)
            for (Bullet bullet : playerShip.getActiveBullets()) {
                for (Asteroid asteroid : spawner.getActiveAsteroids()) {
                    // Collision between asteroid and bullet
                    if (bullet.getCollisionRect().collidesWith(asteroid.getCollisionCirlce())) {

                        // If bullet collides with asteroid we need to delete them too
                        if (bullet.getType() == Bullet.bulletType.rocket) {
                            spawner.spawnPlayerExplosion(bullet.getX() - bullet.getPreferredWidth() / 2, bullet.getY() - bullet.getPreferredHeight() / 2, 40, 40);
                        }
                        bullet.remove = true;

                        stats.incDestroyedAsteroids(1);

                        /*spawner.getAsteroidPool().free(asteroid);
                        spawner.getActiveAsteroids().removeValue(asteroid, true);*/

                        // Spawn Enemy explosion
                        spawner.spawnPlayerExplosion(asteroid.getX() - asteroid.getRadius(), asteroid.getY() - asteroid.getRadius(), 128, 128);
                        // spawner.spawnEnemyExplosion(asteroid.getX(), asteroid.getY() - asteroid.getRadius(), 96, 96);
                        // explosions.add(new Explosion(asteroid.getX(), asteroid.getY() - asteroid.getRadius(), 0.11f, 96, 96, 96, Assets.manager.get(Assets.explosion2, Texture.class)));

                        // Increase score value
                        playerShip.setScore(playerShip.getScore() + Asteroid.REWARD);
                        // Update Score value
                        scoreLayout.setText(scoreFont, "" + playerShip.getScore());

                        // Spawn Random PowerUp
                        if (!asteroid.remove)
                            spawner.randomPowerUp(asteroid.getX(), asteroid.getY());

                        asteroid.remove = true;
                    }
                }
            }

            // Check for collisions between player and powerUp
            for (PowerUp powerUp : spawner.getActivePowerUps()) {

                // User pickUps powerUp
                if (powerUp.getPowerUpCollisionRect().collidesWith(playerShip.getShipCollisionRect())) {
                    powerUp.remove = true;

                    // Heal powerUp
                    if (powerUp.getPowerUpCollisionRect().getTag() == CollisionRect.colliderTag.healPowerUp && !playerShip.isGoingToDie() && !playerShip.isDestroyed()) {
                        stats.incHealPickedUp(1);
                        playerShip.setHealPowerUpActive();
                        // Rockets PowerUp
                    } else if (powerUp.getPowerUpCollisionRect().getTag() == CollisionRect.colliderTag.ammoPowerUp) {
                        stats.incAmmoPickedUp(1);
                        playerShip.setAmmoPowerUpActive(true, 40);
                        // Shield PowerUp
                    } else if (powerUp.getPowerUpCollisionRect().getTag() == CollisionRect.colliderTag.shieldPowerUp && !playerShip.isGoingToDie() && !playerShip.isDestroyed()) {
                        stats.incShieldPickedUp(1);
                        playerShip.activateShield();
                    }
                }
            }

            // Check for collisions between player and asteroids
            for (Asteroid asteroid : spawner.getActiveAsteroids()) {
                if (asteroid.getCollisionCirlce().collidesWith(playerShip.getShipCollisionRect())) {
                    stats.incDestroyedAsteroids(1);
                    // delete asteroid
                    spawner.getAsteroidPool().free(asteroid);
                    spawner.getActiveAsteroids().removeValue(asteroid, true);

                    // Spawn Player Explosion
                    spawner.spawnPlayerExplosion(asteroid.getX() - asteroid.getRadius(), asteroid.getY() - asteroid.getRadius(), 128, 128);

                    // Update player health
                    playerShip.updateHealth(asteroid.DAMAGE);

                    // Increase score value
                    playerShip.setScore(playerShip.getScore() + Asteroid.REWARD);
                    // Update Score value
                    scoreLayout.setText(scoreFont, "" + playerShip.getScore());
                }
            }
        }

        // Shield over
        if (playerShip.getCurrentShieldHealth() <= 0) {
            playerShip.setShieldActive(false);
        }

        // Game Over
        if (playerShip.getCurrentHealth() <= 0) {
            if (playerShip.isDestroyed()) {
                stats.setScore(playerShip.getScore());
                game.setScreen(new GameOverScreen(game, scrollingBackground, stats));
            }
            if (!playerShip.isGoingToDie()) {
                playerShip.setGoingToDie(true);
                playerShip.setCurrentAnimation(animationState.shipDestroyedAnimation);
            }
        }

        // Render enemies
        spawner.renderEnemies(batch);

        // Render powerUps
        spawner.renderPowerUps(batch);

        // Draw ship with animations and collisionRectangle
        playerShip.draw(batch, delta);
        if (BuildConfig.DEBUG)
            playerShip.getShipCollisionRect().drawCollider(batch);
        if (!isPaused)
            playerShip.updateShieldState(delta);

        // Render all possible explosions
        spawner.renderExplosion(batch);

        if (!isPaused) {
            if (playerShip.isAmmoActive()) {
                scoreFont.setColor(Color.WHITE);
                scoreFont.getData().setScale(SCALE_FACTOR / 2);
                ammoLayout.setText(scoreFont, "" + playerShip.getCurrentRockets());
                scoreFont.draw(batch, ammoLayout, Gdx.graphics.getWidth() / 2 - ammoLayout.width / 2, Gdx.graphics.getHeight() - scoreLayout.height * 2 - 20 * SCALE_FACTOR);
                scoreFont.setColor(ammoLayoutColor);
                scoreFont.getData().setScale(SCALE_FACTOR);
            }
            // Draw score
            scoreFont.draw(batch, scoreLayout, Gdx.graphics.getWidth() / 2 - scoreLayout.width / 2, Gdx.graphics.getHeight() - scoreLayout.height - 5);
        }

        batch.end();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        if (BuildConfig.DEBUG) {
            debugUtils.update();
            debugUtils.render();

            /*
            if (!isPaused)
                for (Bullet bullet : playerShip.getActiveBullets()) {
                    System.out.println("Bullet  y: " + bullet.getY());
                }
            System.out.print("\r[!] Deltatime: " + delta);
            spawner.checkAsteroidPool();
            spawner.checkEnemyPool();
            spawner.checkExplosionPool();
            spawner.checkPowerUpPool();
            */
        }
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
        Assets.unloadPowerUps();
        Assets.unloadShips();
        Assets.unloadBullets();
    }
}
