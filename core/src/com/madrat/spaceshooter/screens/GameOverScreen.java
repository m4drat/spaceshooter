package com.madrat.spaceshooter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
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
import com.github.czyzby.kiwi.util.gdx.asset.Asset;
import com.madrat.spaceshooter.MainGame;
import com.madrat.spaceshooter.utils.Assets;
import com.madrat.spaceshooter.utils.DialogAlert;
import com.madrat.spaceshooter.utils.ScrollingBackground;

import static com.madrat.spaceshooter.MainGame.SCALE_FACTOR;

public class GameOverScreen implements Screen {

    MainGame game;

    private Stage stage;
    private Skin skin;
    private Table menuTable;

    private SpriteBatch batch;
    private ScrollingBackground scrollingBackground;

    private BitmapFont highScoreFont, scoreFont, gameOverFont;
    private GlyphLayout highScoreLayout, scoreLayout, gameOverLayout;

    private TextButton restartBtn;
    private TextButton backBtn;
    private TextButton exitBtn;

    private DialogAlert exit;

    private int score, highScore;

    public GameOverScreen(MainGame newgame, ScrollingBackground scrBack, int score) {
        this.game = newgame;
        this.batch = new SpriteBatch();
        this.scrollingBackground = scrBack;
        this.score = score;

        Assets.unloadFont();
        Assets.loadFont();
        Assets.manager.finishLoading();

        skin = Assets.manager.get(Assets.uiskin, Skin.class);
        stage = new Stage(new ScreenViewport());

        // Get highscore from file
        Preferences data = Gdx.app.getPreferences("spacegame");
        this.highScore = data.getInteger("highscore", 0);

        if (score > highScore) {
            data.putInteger("highscore", score);
            data.flush();
        }

        gameOverFont = Assets.manager.get(Assets.emulogicfnt, BitmapFont.class);
        gameOverFont.setColor(new Color(0x30db88ff));
        gameOverFont.getData().setScale(1.2f * SCALE_FACTOR);
        gameOverLayout = new GlyphLayout(gameOverFont, "GAME OVER");

        highScoreFont = Assets.manager.get(Assets.emulogicfnt, BitmapFont.class);
        highScoreFont.setColor(new Color(0x7a9af1ff));
        highScoreFont.getData().setScale(0.9f * SCALE_FACTOR);
        highScoreLayout = new GlyphLayout(highScoreFont, "Highscore:" + data.getInteger("highscore", 0));

        scoreFont = Assets.manager.get(Assets.emulogicfnt, BitmapFont.class);
        scoreFont.setColor(new Color(0xceb963ff));
        scoreFont.getData().setScale(0.7f * SCALE_FACTOR);
        scoreLayout = new GlyphLayout(scoreFont, "Current score:" + score);

        // Create buttons
        restartBtn = new TextButton("restart", skin);
        restartBtn.getLabel().setFontScale(SCALE_FACTOR);

        backBtn = new TextButton("back", skin);
        backBtn.getLabel().setFontScale(SCALE_FACTOR);

        exitBtn = new TextButton("exit", skin);
        exitBtn.getLabel().setFontScale(SCALE_FACTOR);

        restartBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                batch.dispose();
                game.setScreen(new MainGameScreen(game));
            }
        });
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                batch.dispose();
                game.setScreen(new MainMenuScreen(game, scrollingBackground));
            }
        });

        exit = new DialogAlert("", skin);
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
        menuTable.padTop(310 * SCALE_FACTOR);
        menuTable.add(restartBtn).padBottom(48 * SCALE_FACTOR);
        menuTable.row();
        menuTable.add(backBtn).padBottom(48 * SCALE_FACTOR);
        menuTable.row();
        menuTable.add(exitBtn).padBottom(120 * SCALE_FACTOR);

        // Add table to stage (buttons)
        stage.addActor(menuTable);

        Gdx.input.setInputProcessor(stage);
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
        gameOverFont.draw(batch, gameOverLayout, Gdx.graphics.getWidth() / 2 - gameOverLayout.width / 2, Gdx.graphics.getHeight() - gameOverLayout.height * 2 - 5);
        highScoreFont.draw(batch, highScoreLayout, Gdx.graphics.getWidth() / 2 - highScoreLayout.width / 2, Gdx.graphics.getHeight() - gameOverLayout.height * 4 - 5);
        scoreFont.draw(batch, scoreLayout, Gdx.graphics.getWidth() / 2 - scoreLayout.width / 2, Gdx.graphics.getHeight() - gameOverLayout.height * 6);

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
