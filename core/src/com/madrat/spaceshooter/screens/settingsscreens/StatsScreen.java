package com.madrat.spaceshooter.screens.settingsscreens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.madrat.spaceshooter.MainGame;
import com.madrat.spaceshooter.utils.Assets;
import com.madrat.spaceshooter.utils.ScrollingBackground;

import static com.madrat.spaceshooter.MainGame.SCALE_FACTOR;

public class StatsScreen implements Screen {

    MainGame game;

    private ScrollingBackground scrollingBackground;
    private SpriteBatch batch;

    private TextButton backBtn;
    private BitmapFont defaultFont;
    private GlyphLayout killedEnemiesLayout, destroyedAsteroidsLayout, totalEarnedMoneysLayout, totalDeathsLayout, healPickedUpLayout, ammoPickedUpLayout, shieldPickedUpLayout;

    private Stage stage;
    private Skin skin;
    private Table buttonsTable;

    public StatsScreen(MainGame newGame, ScrollingBackground scrBack) {
        this.game = newGame;
        this.batch = new SpriteBatch();
        this.scrollingBackground = scrBack;

        skin = Assets.manager.get(Assets.uiskin, Skin.class);
        stage = new Stage(new ScreenViewport());

        defaultFont = Assets.manager.get(Assets.emulogicfnt, BitmapFont.class);
        defaultFont.getData().setScale(SCALE_FACTOR / 1.7f);
        defaultFont.setColor(Color.WHITE);
        // defaultFont.getData().setScale(SCALE_FACTOR / 1.7f, SCALE_FACTOR / 1.6f);

        JsonObject stats = getStatsDataJson(MainGame.pathToCurrentState);

        killedEnemiesLayout = new GlyphLayout(defaultFont, "Killed Enemies:" + stats.get("totalKilledEnemies").getAsString());
        destroyedAsteroidsLayout = new GlyphLayout(defaultFont, "Destroyed Asteroids:" + stats.get("DestroyedAsteroids").getAsString());
        totalEarnedMoneysLayout = new GlyphLayout(defaultFont, "Total earned:" + stats.get("totalEarnedMoneys").getAsString());
        totalDeathsLayout = new GlyphLayout(defaultFont, "Total Deaths:" + stats.get("totalDeaths").getAsString());
        healPickedUpLayout = new GlyphLayout(defaultFont, "Heal picked:" + stats.get("healPickedUp").getAsString());
        ammoPickedUpLayout = new GlyphLayout(defaultFont, "Ammo picked:" + stats.get("ammoPickedUp").getAsString());
        shieldPickedUpLayout = new GlyphLayout(defaultFont, "Shield picked:" + stats.get("shieldPickedUp").getAsString());

        buttonsTable = new Table();
        buttonsTable.setWidth(stage.getWidth());
        buttonsTable.align(Align.center | Align.top);
        buttonsTable.setPosition(0, MainGame.GENERAL_HEIGHT);
        buttonsTable.padTop(400 * SCALE_FACTOR);
        buttonsTable.row();

        backBtn = new TextButton("Back", skin);
        backBtn.getLabel().setFontScale(SCALE_FACTOR);
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setPreviousScreen();
            }
        });
        buttonsTable.add(backBtn);

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
        game.setScreen(new SettingsScreen(game, scrollingBackground));
    }

    private JsonObject getStatsDataJson(String pathToCurrentState) {
        FileHandle currentFileHandle;

        JsonParser parser = new JsonParser();

        // Set appropriate path to file
        if (MainGame.applicationType == Application.ApplicationType.Android) {
            currentFileHandle = Gdx.files.local(pathToCurrentState);
        } else if (MainGame.applicationType == Application.ApplicationType.Desktop) {
            currentFileHandle = Gdx.files.absolute(pathToCurrentState);
        } else {
            currentFileHandle = Gdx.files.local(pathToCurrentState);
        }

        try {
            return parser.parse(MainGame.cryptor.decrypt(currentFileHandle.readString())).getAsJsonObject().getAsJsonObject("stats");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        scrollingBackground.draw(batch);

        defaultFont.draw(batch, killedEnemiesLayout, Gdx.graphics.getWidth() / 2 - killedEnemiesLayout.width / 2, Gdx.graphics.getHeight() - killedEnemiesLayout.height * 5.5f);
        defaultFont.draw(batch, destroyedAsteroidsLayout, Gdx.graphics.getWidth() / 2 - destroyedAsteroidsLayout.width / 2, Gdx.graphics.getHeight() - killedEnemiesLayout.height * 7.4f);
        defaultFont.draw(batch, totalEarnedMoneysLayout, Gdx.graphics.getWidth() / 2 - totalEarnedMoneysLayout.width / 2, Gdx.graphics.getHeight() - killedEnemiesLayout.height * 9.3f);
        defaultFont.draw(batch, totalDeathsLayout, Gdx.graphics.getWidth() / 2 - totalDeathsLayout.width / 2, Gdx.graphics.getHeight() - killedEnemiesLayout.height * 11.2f);
        defaultFont.draw(batch, healPickedUpLayout, Gdx.graphics.getWidth() / 2 - healPickedUpLayout.width / 2, Gdx.graphics.getHeight() - killedEnemiesLayout.height * 13.1f);
        defaultFont.draw(batch, ammoPickedUpLayout, Gdx.graphics.getWidth() / 2 - ammoPickedUpLayout.width / 2, Gdx.graphics.getHeight() - killedEnemiesLayout.height * 15f);
        defaultFont.draw(batch, shieldPickedUpLayout, Gdx.graphics.getWidth() / 2 - shieldPickedUpLayout.width / 2, Gdx.graphics.getHeight() - killedEnemiesLayout.height * 16.9f);

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
