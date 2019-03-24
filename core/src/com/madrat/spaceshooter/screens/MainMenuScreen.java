package com.madrat.spaceshooter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
import com.madrat.spaceshooter.utils.ObjectHandler;
import com.madrat.spaceshooter.utils.ScrollingBackground;

import java.util.ArrayList;

import static com.madrat.spaceshooter.MainGame.SCALE_FACTOR;

public class MainMenuScreen implements Screen {

    MainGame game;

    private Stage stage;
    private Skin skin;
    private Table menuTable;

    private TextButton playButton;
    private TextButton shopButton;
    private TextButton settButton;
    private TextButton exitButton;

    private ScrollingBackground scrollingBackground;
    private ArrayList<ObjectHandler> sprites;

    private SpriteBatch batch;
    private Sprite background;

    private DialogAlert exit;

    // Other runs constructor
    public MainMenuScreen(MainGame newGame, SpriteBatch oldBatch, ScrollingBackground scrBack) {
        this.game = newGame;

        // Use old background
        this.scrollingBackground = scrBack;

        // Use old SpriteBatch
        this.batch = oldBatch;

        setup();
    }

    // First run constructor
    public MainMenuScreen(MainGame newGame) {
        this.game = newGame;

        // Create Sprite batch
        batch = new SpriteBatch();

        // Create base background for scrolling background
        background = new Sprite(new Texture(Gdx.files.internal(Assets.backgroundSpace)));
        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Create objects for scrolling background and run it
        sprites = ScrollingBackground.initStarBackground();
        scrollingBackground = new ScrollingBackground(background, sprites);

        setup();
    }

    private void setup() {
        skin = new Skin(Gdx.files.internal(Assets.uiskin));
        stage = new Stage(new ScreenViewport());

        menuTable = new Table();
        menuTable.setWidth(stage.getWidth());
        menuTable.align(Align.center | Align.top);
        menuTable.setPosition(0, MainGame.GENERAL_HEIGHT);

        // Create buttons
        playButton = new TextButton("Play", skin);
        shopButton = new TextButton("Shop", skin);
        settButton = new TextButton("Settings", skin);
        exitButton = new TextButton("Exit", skin);


        // Increase size of play button
        playButton.getLabel().setFontScale(2.2f * SCALE_FACTOR);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameTypeScreen(game, batch, scrollingBackground));
            }
        });

        shopButton.getLabel().setFontScale(1.4f * SCALE_FACTOR);
        shopButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new ShopScreen(game, scrollingBackground, batch));
            }
        });

        settButton.getLabel().setFontScale(1.2f * SCALE_FACTOR);
        exitButton.getLabel().setFontScale(SCALE_FACTOR);
        settButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SettingsScreen(game, batch, scrollingBackground));
            }
        });

        // Exit Confirm Dialog
        exit = new DialogAlert("", skin);
        exit.text("Do you really\nwant to exit?");
        exit.yesButton("YES", new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
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


        // Exit Button Listener
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // exitDialog.show(stage);
                exit.show(stage);
            }
        });

        // Setup all relative positions
        menuTable.padTop(100 * SCALE_FACTOR);
        menuTable.add(playButton).padBottom(48 * SCALE_FACTOR);
        menuTable.row();
        menuTable.add(shopButton).padBottom(48 * SCALE_FACTOR);
        menuTable.row();
        menuTable.add(settButton).padBottom(48 * SCALE_FACTOR);
        menuTable.row();
        menuTable.add(exitButton);

        // Add table to stage (buttons)
        stage.addActor(menuTable);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        scrollingBackground.draw(batch);
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
        stage.dispose();
        skin.dispose();
        batch.dispose();
        background.getTexture().dispose();
        scrollingBackground.dispose();
    }

    public SpriteBatch getBatch() {
        return batch;
    }
}
