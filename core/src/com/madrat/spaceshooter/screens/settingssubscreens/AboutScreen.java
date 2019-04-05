package com.madrat.spaceshooter.screens.settingssubscreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.madrat.spaceshooter.MainGame;
import com.madrat.spaceshooter.screens.SettingsScreen;
import com.madrat.spaceshooter.utils.Assets;
import com.madrat.spaceshooter.utils.ScrollingBackground;

import static com.madrat.spaceshooter.MainGame.SCALE_FACTOR;

public class AboutScreen implements Screen {

    MainGame game;

    private Stage stage;
    private Skin skin;
    private Table menuTable;

    private SpriteBatch batch;
    private ScrollingBackground scrollingBackground;

    private BitmapFont devNameFont;
    private GlyphLayout devNameLayout, infoLayout;

    private TextButton backBtn;
    private Color nameColor, strColor;

    public AboutScreen(MainGame newgame, ScrollingBackground scrBack) {

        this.game = newgame;
        this.batch = new SpriteBatch();
        this.scrollingBackground = scrBack;

        Assets.unloadFont();
        Assets.loadFont();
        Assets.manager.finishLoading();

        skin = Assets.manager.get(Assets.uiskin, Skin.class);
        stage = new Stage(new ScreenViewport());

        nameColor = new Color(0x4699E0ff);
        strColor = new Color(0x30db88ff);

        devNameFont = Assets.manager.get(Assets.emulogicfnt, BitmapFont.class);
        devNameFont.setColor(nameColor);
        devNameFont.getData().setScale(SCALE_FACTOR / 1.5f); // Created By
        devNameLayout = new GlyphLayout(devNameFont, "madrat");
        devNameFont.setColor(strColor);
        devNameFont.getData().setScale(1.2f * SCALE_FACTOR); // Created By
        infoLayout = new GlyphLayout(devNameFont, "Created By");

        backBtn = new TextButton("back", skin);
        backBtn.getLabel().setFontScale(SCALE_FACTOR);

        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                batch.dispose();
                game.setScreen(new SettingsScreen(game, scrollingBackground));
            }
        });

        // Create table for button
        menuTable = new Table();
        menuTable.setWidth(stage.getWidth());
        menuTable.align(Align.center | Align.top);
        menuTable.setPosition(0, MainGame.GENERAL_HEIGHT);

        // Setup all relative positions
        menuTable.padTop(376 * SCALE_FACTOR);
        menuTable.add(backBtn).padBottom(90 * SCALE_FACTOR);

        // Add table to stage (buttons)
        stage.addActor(menuTable);
        Gdx.input.setInputProcessor(stage);
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

        // Draw "created by"
        devNameFont.draw(batch, infoLayout, Gdx.graphics.getWidth() / 2 - infoLayout.width / 2, Gdx.graphics.getHeight() - infoLayout.height * 3 - 5);

        // Draw name
        devNameFont.setColor(nameColor);
        devNameFont.getData().setScale(SCALE_FACTOR / 1.5f);
        devNameFont.draw(batch, devNameLayout, Gdx.graphics.getWidth() / 2 - devNameLayout.width / 2, Gdx.graphics.getHeight() - infoLayout.height * 4 - 54);

        // Revert changes
        devNameFont.setColor(strColor);
        devNameFont.getData().setScale(1.2f * SCALE_FACTOR);

        batch.end();

        // Display buttons
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
