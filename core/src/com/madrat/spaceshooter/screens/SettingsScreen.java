package com.madrat.spaceshooter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
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

public class SettingsScreen implements Screen {

    MainGame game;

    private ScrollingBackground scrollingBackground;
    private SpriteBatch batch;

    private DialogAlert confirmDialog;

    private TextButton resetProgressBtn;
    private TextButton backBtn;

    private Stage stage;
    private Skin skin;
    private Table buttonsTable;

    public SettingsScreen(MainGame newgame, SpriteBatch oldBatch, ScrollingBackground scrBack) {
        this.game = newgame;
        this.batch = oldBatch;
        this.scrollingBackground = scrBack;

        skin = new Skin(Gdx.files.internal(Assets.uiskin));
        stage = new Stage(new ScreenViewport());

        buttonsTable = new Table();
        buttonsTable.setWidth(stage.getWidth());
        buttonsTable.align(Align.center | Align.top);
        buttonsTable.setPosition(0, MainGame.GENERAL_HEIGHT);
        buttonsTable.padTop(200);

        resetProgressBtn = new TextButton("Reset", skin);
        resetProgressBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                confirmDialog = new DialogAlert("", skin);
                confirmDialog.text("Do you really\nwant to reset\nyour progress?");
                confirmDialog.yesButton("YES", new InputListener() {
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        Preferences data = Gdx.app.getPreferences("spacegame");
                        data.putInteger("highscore", 0);
                        data.flush();
                        return true;
                    }
                }).noButton("NO", new InputListener() {
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        confirmDialog.hide();
                        return true;
                    }
                });
                confirmDialog.buttonYes.getLabel().setColor(new Color(0xe57575ff));
                confirmDialog.buttonNo.getLabel().setColor(new Color(0x94dd99ff));
                confirmDialog.show(stage);
            }
        });

        backBtn = new TextButton("Back", skin);
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game, batch, scrollingBackground));
            }
        });

        buttonsTable.add(resetProgressBtn).padBottom(48);
        buttonsTable.row();
        buttonsTable.add(backBtn);

        stage.addActor(buttonsTable);
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

    }
}
