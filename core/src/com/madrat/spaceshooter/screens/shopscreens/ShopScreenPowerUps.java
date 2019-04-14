package com.madrat.spaceshooter.screens.shopscreens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.madrat.spaceshooter.MainGame;
import com.madrat.spaceshooter.screens.MainMenuScreen;
import com.madrat.spaceshooter.utils.Assets;
import com.madrat.spaceshooter.utils.BuildConfig;
import com.madrat.spaceshooter.utils.ScrollingBackground;

import com.google.gson.*;

import static com.madrat.spaceshooter.MainGame.SCALE_FACTOR;

public class ShopScreenPowerUps implements Screen {

    MainGame game;

    private ScrollingBackground scrollingBackground;
    private SpriteBatch batch;

    private ImageButton healPowerUp;
    private ImageButton ammoPowerUp;
    private ImageButton shieldPowerUp;
    private TextButton backBtn;
    private Label balance;

    private Stage stage;
    private Skin skin;
    private Table container;

    public ShopScreenPowerUps(MainGame newGame, ScrollingBackground scrBack) {
        this.game = newGame;
        this.scrollingBackground = scrBack;
        this.batch = new SpriteBatch();

        skin = Assets.manager.get(Assets.uiskin, Skin.class);
        stage = new Stage(new ScreenViewport());

        container = new Table();
        container.setWidth(stage.getWidth());
        container.align(Align.center | Align.top);
        container.setPosition(0, MainGame.GENERAL_HEIGHT);
        container.padTop(15 * SCALE_FACTOR);

        balance = new Label("Balance:" + 11230, skin, "emulogic", new Color(0xfce959ff));
        balance.setFontScale(SCALE_FACTOR / 1.6f, SCALE_FACTOR / 1.5f);
        container.add(balance).align(Align.top | Align.right).padTop(8 * SCALE_FACTOR);
        container.row();

        TextButton shipsBtn = new TextButton("powerups", skin);
        shipsBtn.getLabel().setFontScale(SCALE_FACTOR / 1.2f);

        container.add(shipsBtn).padTop(50 * SCALE_FACTOR);
        container.row();

        Table tileTable = new Table();
        tileTable.align(Align.center | Align.top);
        tileTable.padTop(45 * SCALE_FACTOR);
        // tileTable.padTop(40 * SCALE_FACTOR);

        if (BuildConfig.UIDEBUG) {
            tileTable.debug();
            container.debug();
        }

        healPowerUp = new ImageButton(new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.healTileUp, Texture.class))), new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.healTileDown, Texture.class))));
        healPowerUp.getImageCell().width(160 * SCALE_FACTOR);
        healPowerUp.getImageCell().height(160 * SCALE_FACTOR);

        ammoPowerUp = new ImageButton(new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.ammoTileUp, Texture.class))), new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.ammoTileDown, Texture.class))));
        ammoPowerUp.getImageCell().width(160 * SCALE_FACTOR);
        ammoPowerUp.getImageCell().height(160 * SCALE_FACTOR);

        shieldPowerUp = new ImageButton(new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.shieldTileUp, Texture.class))), new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.shieldTileDown, Texture.class))));
        shieldPowerUp.getImageCell().width(160 * SCALE_FACTOR);
        shieldPowerUp.getImageCell().height(160 * SCALE_FACTOR);

        tileTable.add(healPowerUp).padBottom(30 * SCALE_FACTOR).padRight(30 * SCALE_FACTOR);
        tileTable.add(ammoPowerUp).padBottom(30 * SCALE_FACTOR);
        tileTable.row();
        tileTable.add(shieldPowerUp).padRight(30 * SCALE_FACTOR);

        ScrollPane scrollPane = new ScrollPane(tileTable);
        container.add(scrollPane).size(400 * SCALE_FACTOR, 400 * SCALE_FACTOR).padLeft(10 * SCALE_FACTOR).padRight(10 * SCALE_FACTOR);
        container.row();

        backBtn = new TextButton("Back", skin);
        backBtn.getLabel().setFontScale(SCALE_FACTOR);
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setPreviousScreen();
            }
        });
        container.add(backBtn).padTop(80 * SCALE_FACTOR);

        stage.addActor(container);
        addKeyListener(stage);
        Gdx.input.setInputProcessor(stage);
    }

    private void setPreviousScreen() {
        batch.dispose();
        game.setScreen(new MainMenuScreen(game, scrollingBackground));
    }

    private void addKeyListener(Stage stage) {
        // Back Key listener
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
