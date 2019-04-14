package com.madrat.spaceshooter.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.madrat.spaceshooter.MainGame;
import com.madrat.spaceshooter.utils.Assets;
import com.madrat.spaceshooter.utils.BuildConfig;
import com.madrat.spaceshooter.utils.uiutils.DialogAlert;
import com.madrat.spaceshooter.utils.ScrollingBackground;
import com.madrat.spaceshooter.utils.Stats;

import static com.madrat.spaceshooter.MainGame.SCALE_FACTOR;

public class GameOverScreen implements Screen {

    MainGame game;

    private Stage stage;
    private Skin skin;
    private Table menuTable;

    private SpriteBatch batch;
    private ScrollingBackground scrollingBackground;

    private BitmapFont highScoreFont, totalMoneyFont, scoreFont, coinsFont, gameOverFont;
    private GlyphLayout highScoreLayout, totalMoneyLayout, scoreLayout, coinsLayout, gameOverLayout;

    private TextButton restartBtn;
    private TextButton backBtn;
    private TextButton exitBtn;

    private DialogAlert exit;

    private int score, highScore, money, totalMoney;

    public GameOverScreen(MainGame newGame, ScrollingBackground scrBack, Stats stats) {
        this.game = newGame;
        this.batch = new SpriteBatch();
        this.scrollingBackground = scrBack;
        this.score = stats.getScore();
        this.money = (int) (((float) score / 100f) * 3.25f);

        Assets.unloadFont();
        Assets.loadFont();
        Assets.manager.finishLoading();

        skin = Assets.manager.get(Assets.uiskin, Skin.class);
        stage = new Stage(new ScreenViewport());

        // Get and check highscore from file + update money value
        updateValues(MainGame.pathToCurrentState, stats);

        // Create gameOver font and textLayout
        gameOverFont = Assets.manager.get(Assets.emulogicfnt, BitmapFont.class);
        gameOverFont.setColor(new Color(0x30db88ff));
        gameOverFont.getData().setScale(1.25f * SCALE_FACTOR);
        gameOverLayout = new GlyphLayout(gameOverFont, "GAME OVER");

        // Create Highscore font and textLayout
        highScoreFont = Assets.manager.get(Assets.emulogicfnt, BitmapFont.class);
        highScoreFont.setColor(new Color(0x7a9af1ff));
        highScoreFont.getData().setScale(0.7f * SCALE_FACTOR);
        highScoreLayout = new GlyphLayout(highScoreFont, "Highscore:" + this.highScore);

        // Create total money textLayout
        totalMoneyFont = Assets.manager.get(Assets.emulogicfnt, BitmapFont.class);
        totalMoneyFont.setColor(new Color(0x7a9af1ff));
        totalMoneyFont.getData().setScale(0.7f * SCALE_FACTOR);
        totalMoneyLayout = new GlyphLayout(highScoreFont, "Total Money:" + this.totalMoney);

        // Create score font and textLayout
        scoreFont = Assets.manager.get(Assets.emulogicfnt, BitmapFont.class);
        scoreFont.setColor(new Color(0xceb963ff));
        scoreFont.getData().setScale(0.7f * SCALE_FACTOR);
        scoreLayout = new GlyphLayout(scoreFont, "score:" + score);

        // Create coins font and textLayout
        coinsFont = Assets.manager.get(Assets.emulogicfnt, BitmapFont.class);
        coinsFont.setColor(new Color(0xceb963ff));
        coinsFont.getData().setScale(0.7f * SCALE_FACTOR);
        coinsLayout = new GlyphLayout(coinsFont, "money:" + money);


        // Create buttons
        restartBtn = new TextButton("restart", skin);
        restartBtn.getLabel().setFontScale(SCALE_FACTOR);

        // go back button
        backBtn = new TextButton("back", skin);
        backBtn.getLabel().setFontScale(SCALE_FACTOR);

        // exit button
        exitBtn = new TextButton("exit", skin);
        exitBtn.getLabel().setFontScale(SCALE_FACTOR);

        // restart game button listener
        restartBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                batch.dispose();
                game.setScreen(new MainGameScreen(game));
            }
        });
        // go back button listener
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setPreviousScreen();
            }
        });

        // exit confirm dialog
        exit = new DialogAlert("", skin, stage);
        exit.text("Do you really\nwant to exit?");
        exit.yesButton("YES", new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Assets.manager.dispose();
                Gdx.app.exit();
                return true;
            }
        }).noButton("NO", new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                exit.hide();
                return true;
            }
        });
        exit.buttonYes.getLabel().setColor(new Color(0xe57575ff));
        exit.buttonYes.getLabel().setFontScale(SCALE_FACTOR);

        exit.buttonNo.getLabel().setColor(new Color(0x94dd99ff));
        exit.buttonNo.getLabel().setFontScale(SCALE_FACTOR);

        exitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                exit.show(stage);
            }
        });

        menuTable = new Table();
        menuTable.setWidth(stage.getWidth());
        menuTable.align(Align.center | Align.top);
        menuTable.setPosition(0, MainGame.GENERAL_HEIGHT);

        // Setup all relative positions
        menuTable.padTop(405 * SCALE_FACTOR);
        menuTable.add(restartBtn).padBottom(48 * SCALE_FACTOR);
        menuTable.row();
        menuTable.add(backBtn).padBottom(48 * SCALE_FACTOR);
        menuTable.row();
        menuTable.add(exitBtn).padBottom(120 * SCALE_FACTOR);

        if (BuildConfig.UIDEBUG)
            menuTable.debug();

        // Add table to stage (buttons)
        stage.addActor(menuTable);

        // Back Key listener
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
                    setPreviousScreen();
                    return true;
                }
                return false;
            }
        });

        Gdx.input.setInputProcessor(stage);
    }

    private void setPreviousScreen() {
        batch.dispose();
        game.setScreen(new MainMenuScreen(game, scrollingBackground));
    }

    private void updateValues(String path, Stats stats) {
        FileHandle currentFileHandle;
        JsonObject currentState;

        JsonParser parser = new JsonParser();
        Gson builder = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

        // Set appropriate path to file
        if (MainGame.applicationType == Application.ApplicationType.Android) {
            currentFileHandle = Gdx.files.local(path);
        } else if (MainGame.applicationType == Application.ApplicationType.Desktop) {
            currentFileHandle = Gdx.files.absolute(path);
        } else {
            currentFileHandle = Gdx.files.local(path);
        }

        try {
            // Extract current state from "encrypted" json
            currentState = parser.parse(MainGame.cryptor.decrypt(currentFileHandle.readString())).getAsJsonObject();

            // Update highscore value
            this.highScore = currentState.getAsJsonObject("stats").get("highscore").getAsInt();
            if (highScore < stats.getScore()) {
                this.highScore = stats.getScore();
                currentState.getAsJsonObject("stats").addProperty("highscore", score);
                // currentFileHandle.writeString(builder.toJson(currentState), false);
            }

            // Update money value
            this.totalMoney = currentState.getAsJsonObject("stats").get("money").getAsInt() + money;
            currentState.getAsJsonObject("stats").addProperty("money", totalMoney);

            currentState.getAsJsonObject("stats").addProperty("totalKilledEnemies", currentState.getAsJsonObject("stats").get("totalKilledEnemies").getAsInt() + stats.getKilledEnemies());
            currentState.getAsJsonObject("stats").addProperty("DestroyedAsteroids", currentState.getAsJsonObject("stats").get("DestroyedAsteroids").getAsInt() + stats.getDestroyedAsteroids());
            currentState.getAsJsonObject("stats").addProperty("totalEarnedMoneys", currentState.getAsJsonObject("stats").get("totalEarnedMoneys").getAsInt() + money);
            currentState.getAsJsonObject("stats").addProperty("totalDeaths", currentState.getAsJsonObject("stats").get("totalDeaths").getAsInt() + 1);
            currentState.getAsJsonObject("stats").addProperty("healPickedUp", currentState.getAsJsonObject("stats").get("healPickedUp").getAsInt() + stats.getHealPickedUp());
            currentState.getAsJsonObject("stats").addProperty("ammoPickedUp", currentState.getAsJsonObject("stats").get("ammoPickedUp").getAsInt() + stats.getAmmoPickedUp());
            currentState.getAsJsonObject("stats").addProperty("shieldPickedUp", currentState.getAsJsonObject("stats").get("shieldPickedUp").getAsInt() + stats.getShieldPickedUp());

            if (BuildConfig.DEBUG) {
                System.out.println("GameOverDump:\n" + builder.toJson(currentState));
            }

            // Write everything to file + 'encrypt' it
            currentFileHandle.writeString(MainGame.cryptor.encrypt(builder.toJson(currentState)), false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        // Display background
        scrollingBackground.draw(batch);

        // Display highscore + current score
        gameOverFont.getData().setScale(1.35f * SCALE_FACTOR);
        gameOverFont.draw(batch, gameOverLayout, Gdx.graphics.getWidth() / 2 - gameOverLayout.width / 2, Gdx.graphics.getHeight() - gameOverLayout.height * 2);

        highScoreFont.getData().setScale(0.6f * SCALE_FACTOR);
        highScoreFont.draw(batch, highScoreLayout, Gdx.graphics.getWidth() / 2 - highScoreLayout.width / 2, Gdx.graphics.getHeight() - gameOverLayout.height * 3.9f - 5);
        totalMoneyFont.draw(batch, totalMoneyLayout, Gdx.graphics.getWidth() / 2 - totalMoneyLayout.width / 2, Gdx.graphics.getHeight() - gameOverLayout.height * 4.6f - 5);

        scoreFont.getData().setScale(0.7f * SCALE_FACTOR);
        scoreFont.draw(batch, scoreLayout, Gdx.graphics.getWidth() / 2 - scoreLayout.width / 2, Gdx.graphics.getHeight() - gameOverLayout.height * 6.45f - 15);
        coinsFont.draw(batch, coinsLayout, Gdx.graphics.getWidth() / 2 - coinsLayout.width / 2, Gdx.graphics.getHeight() - gameOverLayout.height * 7.45f - 15);

        batch.end();

        // Display buttons
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
    public void dispose() {
    }
}
