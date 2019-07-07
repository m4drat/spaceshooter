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
import com.madrat.spaceshooter.utils.Strings;
import com.madrat.spaceshooter.utils.uiutils.CheckBox;
import com.madrat.spaceshooter.utils.uiutils.DialogAlert;
import com.madrat.spaceshooter.utils.ScrollingBackground;
import com.madrat.spaceshooter.utils.uiutils.TextInputDialog;

import java.util.regex.Pattern;

import static com.madrat.spaceshooter.MainGame.GENERAL_HEIGHT;
import static com.madrat.spaceshooter.MainGame.GENERAL_WIDTH;
import static com.madrat.spaceshooter.MainGame.SCALE_FACTOR;
import static com.madrat.spaceshooter.MainGame.SCALE_Y;

public class SettingsScreen implements Screen {

    MainGame game;

    private ScrollingBackground scrollingBackground;
    private SpriteBatch batch;

    private DialogAlert confirmDialog;

    private TextButton resetProgressBtn;
    private TextButton aboutBtn, statsBtn, globalScoreBoarBtn, changeIpBtn, backBtn;
    private CheckBox checkBox;

    private Stage stage;
    private Skin skin;
    private Table buttonsTable;

    private DialogAlert error;

    public SettingsScreen(MainGame newGame, ScrollingBackground scrBack) {
        this.game = newGame;
        this.batch = new SpriteBatch();
        this.scrollingBackground = scrBack;

        skin = Assets.manager.get(Assets.uiskin, Skin.class);
        stage = new Stage(new ScreenViewport());

        buttonsTable = new Table();
        buttonsTable.setWidth(stage.getWidth());
        buttonsTable.align(Align.center | Align.top);
        buttonsTable.setPosition(0, GENERAL_HEIGHT);
        buttonsTable.padTop(120 * SCALE_FACTOR);

        resetProgressBtn = new TextButton(Strings.resetTxt, skin);
        resetProgressBtn.getLabel().setFontScale(SCALE_FACTOR);
        resetProgressBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                confirmDialog = new DialogAlert("", skin, stage);
                confirmDialog.text(Strings.resetProgress);
                confirmDialog.yesButton(Strings.yesTxt, new InputListener() {
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        setDefaultValues();
                        game.setScreen(new MainMenuScreen(game));
                        return true;
                    }
                }).noButton(Strings.noTxt, new InputListener() {
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

        aboutBtn = new TextButton(Strings.aboutTxt, skin);
        aboutBtn.getLabel().setFontScale(SCALE_FACTOR);
        aboutBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                batch.dispose();
                game.setScreen(new AboutScreen(game, scrollingBackground));
            }
        });

        statsBtn = new TextButton(Strings.statsTxt, skin);
        statsBtn.getLabel().setFontScale(SCALE_FACTOR);
        statsBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                batch.dispose();
                game.setScreen(new StatsScreen(game, scrollingBackground));
            }
        });

        globalScoreBoarBtn = new TextButton(Strings.scoreboardL, skin);
        globalScoreBoarBtn.getLabel().setFontScale(SCALE_FACTOR);
        globalScoreBoarBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                batch.dispose();
                game.setScreen(new GlobalScoreBoardScreen(game, scrollingBackground));
            }
        });

        changeIpBtn = new TextButton(Strings.changeIpL, skin);
        changeIpBtn.getLabel().setFontScale(SCALE_FACTOR / 1.5f);
        changeIpBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                final TextInputDialog textInputDialog = new TextInputDialog(skin, stage, Strings.enterIp, 0.7f, 300, 200);
                textInputDialog.setPosition(GENERAL_WIDTH / 2 - textInputDialog.getPrefWidth() / 2, (GENERAL_HEIGHT - textInputDialog.getPrefHeight()) / 2 + 40 * SCALE_Y);

                textInputDialog.getActBtn().addListener(new InputListener() {
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        if (textInputDialog.getTextField().getText().length() == 0) {
                            apiAddressErr(stage, Strings.errEmptyIp);
                            error.show(stage);
                        }  else if (!checkIp(textInputDialog.getTextField().getText())) {
                            apiAddressErr(stage, Strings.errIllegalIp);
                            error.show(stage);
                        } else { // All - ok
                            Preferences prefs = Gdx.app.getPreferences("spacegame");
                            prefs.putString("apiAddress", textInputDialog.getTextField().getText());
                            prefs.flush();
                            Gdx.input.setOnscreenKeyboardVisible(false);
                            textInputDialog.hide();
                        }
                        return true;
                    }
                });
                textInputDialog.show(stage);
            }
        });

        Preferences data = Gdx.app.getPreferences("spacegame");
        checkBox = new CheckBox(skin, Strings.sendScoreTxt, Assets.checkBoxImageUp, Assets.checkBoxImageDown, 32, 32);
        checkBox.setChecked(data.getBoolean("sendscore", true));
        checkBox.getCheckBox().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                Preferences data = Gdx.app.getPreferences("spacegame");
                if (checkBox.isChecked()) {
                    if (BuildConfig.DEBUG)
                        System.out.println("[+] UnChecked");
                    checkBox.setChecked(false);
                    data.putBoolean("sendscore", false);
                } else {
                    if (BuildConfig.DEBUG)
                        System.out.println("[+] Checked");
                    checkBox.setChecked(true);
                    data.putBoolean("sendscore", true);
                }
                data.flush();
            }
        });

        backBtn = new TextButton(Strings.backTxt, skin);
        backBtn.getLabel().setFontScale(SCALE_FACTOR);
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setPreviousScreen();
            }
        });

        buttonsTable.add(globalScoreBoarBtn).padBottom(22 * SCALE_FACTOR);
        buttonsTable.row();
        buttonsTable.add(changeIpBtn).padBottom(36 * SCALE_FACTOR);
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

    private void apiAddressErr(Stage stage, String text) {
        DialogAlert successfullySelectedLoc = new DialogAlert("", skin, stage);
        successfullySelectedLoc.text(text).yesButton(Strings.okTxt, new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        successfullySelectedLoc.buttonYes.getLabel().setColor(Assets.lightPinky);
        successfullySelectedLoc.buttonYes.getLabel().setFontScale(SCALE_FACTOR);

        this.error = successfullySelectedLoc;
    }

    private boolean checkIp(String name) {
        if (name.matches("([0-9.]+)")) {
            return true;
        } else {
            return false;
        }
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
