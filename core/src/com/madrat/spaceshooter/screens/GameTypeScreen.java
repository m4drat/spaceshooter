package com.madrat.spaceshooter.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
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
import com.madrat.spaceshooter.MainGame;
import com.madrat.spaceshooter.utils.Assets;
import com.madrat.spaceshooter.utils.DialogAlert;
import com.madrat.spaceshooter.utils.ScrollingBackground;

import com.google.gson.*;

import static com.madrat.spaceshooter.MainGame.SCALE_FACTOR;

public class GameTypeScreen implements Screen {

    MainGame game;

    private Stage stage;
    private Skin skin;
    private Table menuTable;

    private TextButton singleplayer;
    private TextButton multiplayer;
    private TextButton back;

    private ScrollingBackground scrollingBackground;
    private SpriteBatch batch;

    private int highScore;
    private BitmapFont highScoreFont;
    private GlyphLayout highScoreLayout;

    public GameTypeScreen(MainGame newGame, ScrollingBackground scrBack) {
        this.game = newGame;
        this.batch = new SpriteBatch();
        this.scrollingBackground = scrBack;

        this.highScore = getCurrentHighscore(MainGame.pathToCurrentState);

        highScoreFont = Assets.manager.get(Assets.emulogicfnt, BitmapFont.class);
        highScoreFont.getData().setScale(0.7f * SCALE_FACTOR);
        highScoreFont.setColor(new Color(0x7a9af1));
        highScoreLayout = new GlyphLayout(highScoreFont, "" + this.highScore);
        highScoreLayout.setText(highScoreFont, "Highscore:" + this.highScore);

        skin = Assets.manager.get(Assets.uiskin, Skin.class);
        stage = new Stage(new ScreenViewport());

        menuTable = new Table();
        menuTable.setWidth(stage.getWidth());
        menuTable.align(Align.center | Align.top);
        menuTable.setPosition(0, MainGame.GENERAL_HEIGHT);

        // Create buttons
        singleplayer = new TextButton("singleplayer", skin);
        multiplayer = new TextButton("multiplayer", skin);
        back = new TextButton("Back", skin);

        singleplayer.getLabel().setFontScale(1f * SCALE_FACTOR);
        singleplayer.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                batch.dispose();
                game.setScreen(new MainGameScreen(game));
            }
        });

        multiplayer.getLabel().setFontScale(1f * SCALE_FACTOR);
        multiplayer.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                DialogAlert confirm = new DialogAlert("", skin);
                confirm.text("Sorry, not\nimplemented");
                confirm.yesButton("OK", new InputListener() {
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
                });
                confirm.buttonYes.align(Align.center);
                confirm.buttonYes.getLabel().setColor(new Color(0xe57575ff));
                confirm.buttonYes.getLabel().setFontScale(SCALE_FACTOR);
                confirm.show(stage);
            }
        });

        back.getLabel().setFontScale(1f * SCALE_FACTOR);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                batch.dispose();
                game.setScreen(new MainMenuScreen(game, scrollingBackground));
            }
        });

        // Setup all relative positions
        menuTable.padTop(120 * SCALE_FACTOR);
        menuTable.add(singleplayer).padBottom(48 * SCALE_FACTOR);
        menuTable.row();
        menuTable.add(multiplayer).padBottom(120 * SCALE_FACTOR);
        menuTable.row();
        menuTable.add(back);

        // Add table to stage (buttons)
        stage.addActor(menuTable);

        Gdx.input.setInputProcessor(stage);
    }

    private int getCurrentHighscore(String path) {
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
            currentState = parser.parse(currentFileHandle.readString()).getAsJsonObject();

            return currentState.get("highscore").getAsInt(); // "highscore"
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        scrollingBackground.draw(batch);
        highScoreFont.draw(batch, highScoreLayout, Gdx.graphics.getWidth() / 2 - highScoreLayout.width / 2, Gdx.graphics.getHeight() - highScoreLayout.height * 2 - 5);

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
    public void dispose() {

    }
}
