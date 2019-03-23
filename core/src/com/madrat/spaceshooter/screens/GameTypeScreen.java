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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.madrat.spaceshooter.MainGame;
import com.madrat.spaceshooter.utils.Assets;
import com.madrat.spaceshooter.utils.ScrollingBackground;

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

    public GameTypeScreen(MainGame newgame, final SpriteBatch oldBatch, ScrollingBackground scrBack) {
        this.game = newgame;
        this.batch = oldBatch;
        this.scrollingBackground = scrBack;

        Preferences data = Gdx.app.getPreferences("spacegame");
        this.highScore = data.getInteger("highscore", 0);

        highScoreFont = new BitmapFont(Gdx.files.internal(Assets.emulogicfnt));
        highScoreFont.getData().setScale(0.7f);
        highScoreFont.setColor(new Color(0x7a9af1));
        highScoreLayout = new GlyphLayout(highScoreFont, "" + this.highScore);
        highScoreLayout.setText(highScoreFont, "Highscore:" + this.highScore);

        skin = new Skin(Gdx.files.internal(Assets.uiskin));
        stage = new Stage(new ScreenViewport());

        menuTable = new Table();
        menuTable.setWidth(stage.getWidth());
        menuTable.align(Align.center | Align.top);
        menuTable.setPosition(0, MainGame.GENERAL_HEIGHT);

        // Create buttons
        singleplayer = new TextButton("singleplayer", skin);
        multiplayer = new TextButton("multiplayer", skin);
        back = new TextButton("Back", skin);

        singleplayer.getLabel().setFontScale(1f);
        singleplayer.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainGameScreen(game, batch));
            }
        });

        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game, batch, scrollingBackground));
            }
        });

        // Setup all relative positions
        menuTable.padTop(120);
        menuTable.add(singleplayer).padBottom(48);
        menuTable.row();
        menuTable.add(multiplayer).padBottom(120);
        menuTable.row();
        menuTable.add(back);

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
