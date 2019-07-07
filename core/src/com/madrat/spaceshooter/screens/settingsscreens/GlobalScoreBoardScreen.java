package com.madrat.spaceshooter.screens.settingsscreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.madrat.spaceshooter.MainGame;
import com.madrat.spaceshooter.utils.Strings;
import com.madrat.spaceshooter.utils.api.ApiRequest;
import com.madrat.spaceshooter.utils.Assets;
import com.madrat.spaceshooter.utils.BuildConfig;
import com.madrat.spaceshooter.utils.ScrollingBackground;
import com.madrat.spaceshooter.utils.api.resourcereprs.User;
import com.madrat.spaceshooter.utils.uiutils.DialogAlert;

import java.util.List;

import static com.madrat.spaceshooter.MainGame.SCALE_FACTOR;

public class GlobalScoreBoardScreen implements Screen {

    MainGame game;

    private Stage stage;
    private Skin skin;
    private Table menuTable;

    private SpriteBatch batch;
    private ScrollingBackground scrollingBackground;

    private Label scoreBoardLabel, currentUser;
    private TextButton backBtn;

    private boolean isDataFetched = false, isDisposed = false;
    private Thread fetchScoreboard;

    private List<User> users;
    private DialogAlert error;
    private String clientLocalUUID;

    public GlobalScoreBoardScreen(MainGame newGame, ScrollingBackground scrBack) {

        this.game = newGame;
        this.batch = new SpriteBatch();
        this.scrollingBackground = scrBack;

        Assets.unloadFont();
        Assets.loadFont();
        Assets.manager.finishLoading();

        skin = Assets.manager.get(Assets.uiskin, Skin.class);
        stage = new Stage(new ScreenViewport());

        Preferences data = Gdx.app.getPreferences("spacegame");
        clientLocalUUID = data.getString("clientUUID");

        fetchScoreboard = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ApiRequest apiHandler = new ApiRequest();
                    users = apiHandler.getScoreBoard(Assets.protocol + Gdx.app.getPreferences("spacegame").getString("apiAddress") + Assets.apiServerPort, Assets.apiEndpointScoreboard, 10);
                    isDataFetched = true;
                } catch (Exception e) {
                    error = new DialogAlert("", skin, stage);
                    error.text(Strings.errFetch);
                    error.getTextLabel().setFontScale(SCALE_FACTOR / 1.6f);
                    error.yesButton(Strings.okTxt, new InputListener() {
                        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                            error.hide();
                            return true;
                        }
                    });
                    error.buttonYes.getLabel().setColor(Assets.lightPinky);
                    error.buttonYes.getLabel().setFontScale(SCALE_FACTOR);
                    if (!isDisposed) {
                        error.show(stage);
                    }
                }
            }
        });
        fetchScoreboard.start();

        scoreBoardLabel = new Label(Strings.scoreboard, skin, Strings.fontName, Assets.lightGreen_3);
        scoreBoardLabel.setFontScale(SCALE_FACTOR);

        backBtn = new TextButton(Strings.backTxt, skin);
        backBtn.getLabel().setFontScale(SCALE_FACTOR);

        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setSettingsScreen();
            }
        });

        // Create table for button
        menuTable = new Table();
        menuTable.setWidth(stage.getWidth());
        menuTable.align(Align.center | Align.top);
        menuTable.setPosition(0, MainGame.GENERAL_HEIGHT);

        // Setup all relative positions
        menuTable.padTop(60 * SCALE_FACTOR);
        menuTable.add(scoreBoardLabel).padBottom(45 * SCALE_FACTOR);
        menuTable.row();

        Table backBtnTable = new Table();
        backBtnTable.setWidth(stage.getWidth());
        backBtnTable.align(Align.center);
        backBtnTable.setPosition(0, 0);
        backBtnTable.add(backBtn).padBottom(278 * SCALE_FACTOR);

        if (BuildConfig.UIDEBUG)
            menuTable.debug();

        // Add table to stage (buttons)
        stage.addActor(menuTable);
        stage.addActor(backBtnTable);
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
                    setSettingsScreen();
                    return true;
                }
                return false;
            }
        });

        // Set input processor
        Gdx.input.setInputProcessor(stage);
    }

    public void setSettingsScreen() {
        isDisposed = true;
        batch.dispose();
        game.setScreen(new SettingsScreen(game, scrollingBackground));
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
        batch.end();

        if (!fetchScoreboard.isAlive() && isDataFetched) {
            isDataFetched = false;
            for (User user : users) {
                if (BuildConfig.DEBUG)
                    System.out.println(user.getClientuuid());
                if (clientLocalUUID.equals(user.getClientuuid())) {
                    currentUser = new Label(user.getUsername() + ":" + user.getScore(), skin, Strings.fontName, Assets.lightPinky);
                } else {
                    currentUser = new Label(user.getUsername() + ":" + user.getScore(), skin, Strings.fontName, Assets.lightYellow_1);
                }
                currentUser.setFontScale(SCALE_FACTOR / 1.65f);
                menuTable.add(currentUser).padBottom(9 * SCALE_FACTOR).row();
            }
        }

        // Display buttons + update UI
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