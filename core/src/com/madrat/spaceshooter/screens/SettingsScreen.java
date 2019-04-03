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

import static com.madrat.spaceshooter.MainGame.SCALE_FACTOR;

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
        buttonsTable.padTop(200 * SCALE_FACTOR);

        resetProgressBtn = new TextButton("Reset", skin);
        resetProgressBtn.getLabel().setFontScale(SCALE_FACTOR);
        resetProgressBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                confirmDialog = new DialogAlert("", skin);
                confirmDialog.text("Do you really\nwant to reset\nyour progress?");
                confirmDialog.yesButton("YES", new InputListener() {
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        setDefaultValues();
                        return true;
                    }
                }).noButton("NO", new InputListener() {
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        confirmDialog.hide();
                        return true;
                    }
                });
                confirmDialog.buttonYes.getLabel().setColor(new Color(0xe57575ff));
                confirmDialog.buttonYes.getLabel().setFontScale(SCALE_FACTOR);
                confirmDialog.buttonNo.getLabel().setColor(new Color(0x94dd99ff));
                confirmDialog.buttonNo.getLabel().setFontScale(SCALE_FACTOR);
                confirmDialog.show(stage);
                // confirmDialog.scaleBy(SCALE_FACTOR);
            }
        });

        backBtn = new TextButton("Back", skin);
        backBtn.getLabel().setFontScale(SCALE_FACTOR);
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                batch.dispose();
                game.setScreen(new MainMenuScreen(game, scrollingBackground));
            }
        });

        buttonsTable.add(resetProgressBtn).padBottom(48 * SCALE_FACTOR);
        buttonsTable.row();
        buttonsTable.add(backBtn);

        stage.addActor(buttonsTable);
        Gdx.input.setInputProcessor(stage);
    }

    private void setDefaultValues() {
        Preferences data = Gdx.app.getPreferences("spacegame");

        // reset Highscore
        data.putInteger("highscore", 0);

        // Default money
        data.putInteger("money", 1000);

        // Default spaceship
        data.putString("animationTexture", Assets.ship1Animation);
        data.putFloat("maxHealth", 1f);
        data.putFloat("damage", 0.1f);
        data.putFloat("delayBetweenShootsBullets", 0.3f);
        data.putFloat("bulletsSpeed", 600f);
        data.putFloat("speed", 300f);
        data.putFloat("frameLength", 0.14f);
        data.putString("handle", "Zapper");

        // Ship sizes
        data.putInteger("realShipWidth", 24);
        data.putInteger("realShipHeight", 23);
        data.putInteger("preferredShipWidth", 60);
        data.putInteger("preferredShipHeight", 50);
        data.putInteger("colliderWidth", 60);
        data.putInteger("colliderHeight", 50);
        data.putInteger("colliderXcoordOffset", 0);
        data.putInteger("colliderYcoordOffset", 0);

        // Ship default healing value
        data.putFloat("maxHealing", 0.2f);

        // Ship Bullets
        data.putInteger("preferredBulletHeight", 10);
        data.putInteger("preferredBulletWidth", 4);
        data.putString("bulletTexture", Assets.bullet1);

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
