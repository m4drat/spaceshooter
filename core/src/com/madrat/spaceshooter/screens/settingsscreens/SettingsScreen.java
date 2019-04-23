package com.madrat.spaceshooter.screens.settingsscreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.madrat.spaceshooter.MainGame;
import com.madrat.spaceshooter.screens.MainMenuScreen;
import com.madrat.spaceshooter.utils.Assets;
import com.madrat.spaceshooter.utils.BuildConfig;
import com.madrat.spaceshooter.utils.uiutils.CheckBox;
import com.madrat.spaceshooter.utils.uiutils.DialogAlert;
import com.madrat.spaceshooter.utils.ScrollingBackground;

import static com.madrat.spaceshooter.MainGame.SCALE_FACTOR;

public class SettingsScreen implements Screen {

    MainGame game;

    private ScrollingBackground scrollingBackground;
    private SpriteBatch batch;

    private DialogAlert confirmDialog;

    private TextButton resetProgressBtn;
    private TextButton aboutBtn, statsBtn, globalScoreBoarBtn, backBtn;
    private CheckBox checkBox;

    private Stage stage;
    private Skin skin;
    private Table buttonsTable;

    public SettingsScreen(MainGame newGame, ScrollingBackground scrBack) {
        this.game = newGame;
        this.batch = new SpriteBatch();
        this.scrollingBackground = scrBack;

        skin = Assets.manager.get(Assets.uiskin, Skin.class);
        stage = new Stage(new ScreenViewport());

        buttonsTable = new Table();
        buttonsTable.setWidth(stage.getWidth());
        buttonsTable.align(Align.center | Align.top);
        buttonsTable.setPosition(0, MainGame.GENERAL_HEIGHT);
        buttonsTable.padTop(120 * SCALE_FACTOR);

        resetProgressBtn = new TextButton("Reset", skin);
        resetProgressBtn.getLabel().setFontScale(SCALE_FACTOR);
        resetProgressBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                confirmDialog = new DialogAlert("", skin, stage);
                confirmDialog.text("Do you really\nwant to reset\nyour progress?");
                confirmDialog.yesButton("YES", new InputListener() {
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        setDefaultValues();
                        game.setScreen(new MainMenuScreen(game));
                        return true;
                    }
                }).noButton("NO", new InputListener() {
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        confirmDialog.hide();
                        return true;
                    }
                });
                confirmDialog.buttonYes.getLabel().setColor(Assets.lightPinky);
                confirmDialog.buttonYes.getLabel().setFontScale(SCALE_FACTOR);
                confirmDialog.buttonNo.getLabel().setColor(Assets.lightGreen_2);
                confirmDialog.buttonNo.getLabel().setFontScale(SCALE_FACTOR);
                confirmDialog.show(stage);
                // confirmDialog.scaleBy(SCALE_FACTOR);
            }
        });

        aboutBtn = new TextButton("About", skin);
        aboutBtn.getLabel().setFontScale(SCALE_FACTOR);
        aboutBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                batch.dispose();
                game.setScreen(new AboutScreen(game, scrollingBackground));
            }
        });

        statsBtn = new TextButton("Stats", skin);
        statsBtn.getLabel().setFontScale(SCALE_FACTOR);
        statsBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                batch.dispose();
                game.setScreen(new StatsScreen(game, scrollingBackground));
            }
        });

        globalScoreBoarBtn = new TextButton("scoreboard", skin);
        globalScoreBoarBtn.getLabel().setFontScale(SCALE_FACTOR);
        globalScoreBoarBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                batch.dispose();
                game.setScreen(new GlobalScoreBoardScreen(game, scrollingBackground));
            }
        });

        Preferences data = Gdx.app.getPreferences("spacegame");
        checkBox = new CheckBox(skin, "send score", Assets.checkBoxImageUp, Assets.checkBoxImageDown, 32, 32);
        checkBox.setChecked(data.getBoolean("sendscore", true));
        checkBox.getCheckBox().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                Preferences data = Gdx.app.getPreferences("spacegame");
                if (checkBox.isChecked()) {
                    if (BuildConfig.DEBUG)
                        System.out.println("[+] UnChecked");
                    data.putBoolean("sendscore", false);
                    checkBox.setChecked(false);
                } else {
                    if (BuildConfig.DEBUG)
                        System.out.println("[+] Checked");
                    data.putBoolean("sendscore", true);
                    checkBox.setChecked(true);
                }
                data.flush();
            }
        });

        backBtn = new TextButton("Back", skin);
        backBtn.getLabel().setFontScale(SCALE_FACTOR);
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setPreviousScreen();
            }
        });


        buttonsTable.add(globalScoreBoarBtn).padBottom(36 * SCALE_FACTOR);
        buttonsTable.row();
        buttonsTable.add(statsBtn).padBottom(36 * SCALE_FACTOR);
        buttonsTable.row();
        buttonsTable.add(aboutBtn).padBottom(36 * SCALE_FACTOR);
        buttonsTable.row();
        buttonsTable.add(resetProgressBtn).padBottom(36 * SCALE_FACTOR);
        buttonsTable.row();
        buttonsTable.add(checkBox).padBottom(119 * SCALE_FACTOR);
        buttonsTable.row();
        buttonsTable.add(backBtn);

        if (BuildConfig.UIDEBUG)
            buttonsTable.debug();

        stage.addActor(buttonsTable);

        // Add backButtonPressed Listener
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

    public void setPreviousScreen() {
        batch.dispose();
        game.setScreen(new MainMenuScreen(game, scrollingBackground));
    }

    private void setDefaultValues() {
        Preferences data = Gdx.app.getPreferences("spacegame");
        data.putBoolean("firstRun", true);
        data.flush();
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

    }
}
