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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
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
import com.madrat.spaceshooter.utils.BuildConfig;
import com.madrat.spaceshooter.utils.ScrollingBackground;
import com.madrat.spaceshooter.utils.Strings;

import static com.madrat.spaceshooter.MainGame.SCALE_FACTOR;
import static com.madrat.spaceshooter.MainGame.SCALE_Y;

public class StatsScreen implements Screen {

    MainGame game;

    private ScrollingBackground scrollingBackground;
    private SpriteBatch batch;

    private TextButton backBtn;
    private BitmapFont defaultFont;

    private Label killedEnemiesLabel, destroyedAsteroidsLabel, totalEarnedMoneysLabel, totalDeathsLabel, healPickedUpLabel, ammoPickedUpLabel, shieldPickedUpLabel;

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

        killedEnemiesLabel = new Label(Strings.destroyedEnemies + stats.get("totalKilledEnemies").getAsString(), skin);
        killedEnemiesLabel.setFontScale(SCALE_FACTOR / 1.8f, SCALE_FACTOR / 1.6f);

        destroyedAsteroidsLabel = new Label(Strings.destroyedAsteroids + stats.get("DestroyedAsteroids").getAsString(), skin);
        destroyedAsteroidsLabel.setFontScale(SCALE_FACTOR / 1.8f, SCALE_FACTOR / 1.6f);

        totalEarnedMoneysLabel = new Label(Strings.totalEarned + stats.get("totalEarnedMoneys").getAsString(), skin);
        totalEarnedMoneysLabel.setFontScale(SCALE_FACTOR / 1.8f, SCALE_FACTOR / 1.6f);

        totalDeathsLabel = new Label(Strings.totalDeaths + stats.get("totalDeaths").getAsString(), skin);
        totalDeathsLabel.setFontScale(SCALE_FACTOR / 1.8f, SCALE_FACTOR / 1.6f);

        healPickedUpLabel = new Label(Strings.healPicked + stats.get("healPickedUp").getAsString(), skin);
        healPickedUpLabel.setFontScale(SCALE_FACTOR / 1.8f, SCALE_FACTOR / 1.6f);
        ammoPickedUpLabel = new Label(Strings.ammoPicked + stats.get("ammoPickedUp").getAsString(), skin);
        ammoPickedUpLabel.setFontScale(SCALE_FACTOR / 1.8f, SCALE_FACTOR / 1.6f);
        shieldPickedUpLabel = new Label(Strings.shieldPicked + stats.get("shieldPickedUp").getAsString(), skin);
        shieldPickedUpLabel.setFontScale(SCALE_FACTOR / 1.8f, SCALE_FACTOR / 1.6f);

        buttonsTable = new Table();
        buttonsTable.setWidth(stage.getWidth());
        buttonsTable.align(Align.center | Align.top);
        buttonsTable.setPosition(0, MainGame.GENERAL_HEIGHT);
        buttonsTable.padTop(120 * SCALE_FACTOR);
        buttonsTable.row();
        buttonsTable.add(killedEnemiesLabel).padBottom(10 * SCALE_FACTOR);
        buttonsTable.row();
        buttonsTable.add(destroyedAsteroidsLabel).padBottom(10 * SCALE_FACTOR);
        buttonsTable.row();
        buttonsTable.add(totalEarnedMoneysLabel).padBottom(10 * SCALE_FACTOR);
        buttonsTable.row();
        buttonsTable.add(totalDeathsLabel).padBottom(10 * SCALE_FACTOR);
        buttonsTable.row();
        buttonsTable.add(healPickedUpLabel).padBottom(10 * SCALE_FACTOR);
        buttonsTable.row();
        buttonsTable.add(ammoPickedUpLabel).padBottom(10 * SCALE_FACTOR);
        buttonsTable.row();
        buttonsTable.add(shieldPickedUpLabel).padBottom(221 * SCALE_FACTOR);
        buttonsTable.row();

        backBtn = new TextButton(Strings.backTxtU, skin);
        backBtn.getLabel().setFontScale(SCALE_FACTOR);
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setPreviousScreen();
            }
        });
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
