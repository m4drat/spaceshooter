package com.madrat.spaceshooter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
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
import com.madrat.spaceshooter.screens.settingsscreens.SettingsScreen;
import com.madrat.spaceshooter.screens.shopscreens.ShopScreenSpaceShips;
import com.madrat.spaceshooter.utils.Assets;
import com.madrat.spaceshooter.utils.BuildConfig;
import com.madrat.spaceshooter.utils.Initializer;
import com.madrat.spaceshooter.utils.uiutils.DialogAlert;
import com.madrat.spaceshooter.utils.ObjectHandler;
import com.madrat.spaceshooter.utils.ScrollingBackground;
import com.madrat.spaceshooter.utils.uiutils.TextInputDialog;

import java.util.ArrayList;
import java.util.regex.Pattern;

import static com.madrat.spaceshooter.MainGame.GENERAL_HEIGHT;
import static com.madrat.spaceshooter.MainGame.GENERAL_WIDTH;
import static com.madrat.spaceshooter.MainGame.SCALE_FACTOR;
import static com.madrat.spaceshooter.MainGame.SCALE_Y;

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

    private DialogAlert exit, error;

    // Other runs constructor
    public MainMenuScreen(MainGame newGame, ScrollingBackground scrBack) {
        this.game = newGame;

        // Use old background
        this.scrollingBackground = scrBack;

        setup();
    }

    // First run constructor
    public MainMenuScreen(MainGame newGame) {
        // Initialize initializer to init initFunctions, which will initialize user data :)
        Initializer.init();

        this.game = newGame;

        // Create base background for scrolling background
        background = new Sprite(Assets.manager.get(Assets.backgroundSpace, Texture.class));
        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Create objects for scrolling background and run it
        sprites = ScrollingBackground.initStarBackground();
        scrollingBackground = new ScrollingBackground(background, sprites);
        setup();

        final Preferences data = Gdx.app.getPreferences("spacegame");
        if (data.getString("username").length() == 0) {
            final TextInputDialog textInputDialog = new TextInputDialog(skin, stage, "Enter Your name", 0.7f, 300, 200);
            textInputDialog.setPosition(GENERAL_WIDTH / 2 - textInputDialog.getPrefWidth() / 2, (GENERAL_HEIGHT - textInputDialog.getPrefHeight()) / 2 + 40 * SCALE_Y);

            textInputDialog.getActBtn().addListener(new InputListener() {
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    if (textInputDialog.getTextField().getText().length() == 0) {
                        nameError(stage, "Name cannot\nbe empty!");
                        error.show(stage);
                    } else if (!checkName(textInputDialog.getTextField().getText())) {
                        nameError(stage, "Name Cannot\ncontain special\ncharacters!");
                        error.show(stage);
                    } else { // All - ok
                        data.putString("username", textInputDialog.getTextField().getText());
                        data.flush();
                        Gdx.input.setOnscreenKeyboardVisible(false);
                        textInputDialog.hide();
                    }
                    return true;
                }
            });

            textInputDialog.show(stage);
        }
    }

    private boolean checkName(String name) {
        Pattern regex = Pattern.compile("[ `$&+,:;=\\\\?@#|/'<>.^*()%!-]");
        if (regex.matcher(name).find()) {
            return false;
        } else {
            return true;
        }
    }

    private void nameError(Stage stage, String text) {
        DialogAlert successfullySelectedLoc = new DialogAlert("", skin, stage);
        successfullySelectedLoc.text(text).yesButton("OK", new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        successfullySelectedLoc.buttonYes.getLabel().setColor(Assets.lightPinky);
        successfullySelectedLoc.buttonYes.getLabel().setFontScale(SCALE_FACTOR);

        this.error = successfullySelectedLoc;
    }

    private void setup() {
        // Create Sprite batch
        batch = new SpriteBatch();

        skin = Assets.manager.get(Assets.uiskin, Skin.class);
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
                batch.dispose();
                game.setScreen(new GameTypeScreen(game, scrollingBackground));

            }
        });

        shopButton.getLabel().setFontScale(1.4f * SCALE_FACTOR);
        shopButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                batch.dispose();
                game.setScreen(new ShopScreenSpaceShips(game, scrollingBackground));
            }
        });

        settButton.getLabel().setFontScale(1.2f * SCALE_FACTOR);
        exitButton.getLabel().setFontScale(SCALE_FACTOR);
        settButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                batch.dispose();
                game.setScreen(new SettingsScreen(game, scrollingBackground));
            }
        });

        // Exit Confirm Dialog
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
        exit.buttonYes.getLabel().setColor(Assets.lightPinky);
        exit.buttonYes.getLabel().setFontScale(SCALE_FACTOR);
        exit.buttonNo.getLabel().setColor(Assets.lightGreen_2);
        exit.buttonNo.getLabel().setFontScale(SCALE_FACTOR);


        // Exit Button Listener
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // exitDialog.show(stage);
                displayExit();
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

        if (BuildConfig.UIDEBUG)
            menuTable.debug();

        // Add table to stage (buttons)
        stage.addActor(menuTable);

        // Add backButtonPressed Listener
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
                    displayExit();
                    return true;
                }
                return false;
            }
        });

        Gdx.input.setInputProcessor(stage);
    }

    public void displayExit() {
        exit.show(stage);
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
