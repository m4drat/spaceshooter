package com.madrat.spaceshooter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
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
import com.madrat.spaceshooter.utils.Assets;
import com.madrat.spaceshooter.utils.ScrollingBackground;

import static com.madrat.spaceshooter.MainGame.SCALE_FACTOR;

public class ShopScreen implements Screen {

    MainGame game;

    private ScrollingBackground scrollingBackground;
    private SpriteBatch batch;

    private TextButton defaultShip;
    private TextButton destroyerShip;
    private TextButton ignitorShip;
    private TextButton turtleShip;
    private TextButton ufoShip;
    private TextButton starShip;
    private TextButton pinkyShip;
    private TextButton backBtn;

    private Stage stage;
    private Skin skin;
    private Table buttonsTable;

    public ShopScreen(MainGame newGame, ScrollingBackground scrBack, SpriteBatch oldBatch) {
        this.game = newGame;
        this.scrollingBackground = scrBack;
        this.batch = oldBatch;

        skin = new Skin(Gdx.files.internal(Assets.uiskin));
        stage = new Stage(new ScreenViewport());

        buttonsTable = new Table();
        buttonsTable.setWidth(stage.getWidth());
        buttonsTable.align(Align.center | Align.top);
        buttonsTable.setPosition(0, MainGame.GENERAL_HEIGHT);
        buttonsTable.padTop(40 * SCALE_FACTOR);

        defaultShip = new TextButton("DefaultShip", skin);
        defaultShip.getLabel().setFontScale(SCALE_FACTOR);
        defaultShip.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setDefault();
            }
        });

        destroyerShip = new TextButton("Destroyer", skin);
        destroyerShip.getLabel().setFontScale(SCALE_FACTOR);
        destroyerShip.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setDestroyer();
            }
        });

        ignitorShip = new TextButton("Ignitor", skin);
        ignitorShip.getLabel().setFontScale(SCALE_FACTOR);
        ignitorShip.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setIgnitor();
            }
        });

        turtleShip = new TextButton("Turtle", skin);
        turtleShip.getLabel().setFontScale(SCALE_FACTOR);
        turtleShip.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setTurtle();
            }
        });

        ufoShip = new TextButton("UFO", skin);
        ufoShip.getLabel().setFontScale(SCALE_FACTOR);
        ufoShip.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setUfo();
            }
        });

        starShip = new TextButton("Star", skin);
        starShip.getLabel().setFontScale(SCALE_FACTOR);
        starShip.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setStar();
            }
        });

        pinkyShip = new TextButton("Pinky", skin);
        pinkyShip.getLabel().setFontScale(SCALE_FACTOR);
        pinkyShip.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setPinky();
            }
        });

        backBtn = new TextButton("Back", skin);
        backBtn.getLabel().setFontScale(SCALE_FACTOR);
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game, batch, scrollingBackground));
            }
        });

        buttonsTable.add(defaultShip).padBottom(48 * SCALE_FACTOR);
        buttonsTable.row();
        buttonsTable.add(destroyerShip).padBottom(48 * SCALE_FACTOR);
        buttonsTable.row();
        buttonsTable.add(ignitorShip).padBottom(48 * SCALE_FACTOR);
        buttonsTable.row();
        buttonsTable.add(turtleShip).padBottom(48 * SCALE_FACTOR);
        buttonsTable.row();
        buttonsTable.add(ufoShip).padBottom(48 * SCALE_FACTOR);
        buttonsTable.row();
        buttonsTable.add(starShip).padBottom(48 * SCALE_FACTOR);
        buttonsTable.row();
        buttonsTable.add(pinkyShip).padBottom(48 * SCALE_FACTOR);
        buttonsTable.row();
        buttonsTable.add(backBtn);


        stage.addActor(buttonsTable);
        Gdx.input.setInputProcessor(stage);
    }

    private void setDefault() {
        Preferences data = Gdx.app.getPreferences("spacegame");

        // Default spaceship
        data.putString("animationTexture", Assets.ship1Animation);
        data.putFloat("animationSpeed", 0.14f);
        data.putFloat("maxHealth", 1f);
        data.putFloat("damage", 0.1f);
        data.putFloat("delayBetweenShoots", 0.3f);
        data.putFloat("bulletsSpeed", 600f);
        data.putFloat("speed", 300f);
        data.putFloat("frameLength", 0.14f);
        data.putString("handle", "Zapper");

        data.putFloat("maxHealing", 0.2f);

        data.putInteger("realShipWidth", 24);
        data.putInteger("realShipHeight", 23);
        data.putInteger("preferredShipWidth", 60);
        data.putInteger("preferredShipHeight", 50);

        data.putInteger("preferredBulletHeight", 10);
        data.putInteger("preferredBulletWidth", 4);
        data.putString("bulletTexture", Assets.bullet1);

        data.flush();
    }

    private void setDestroyer() {
        Preferences data = Gdx.app.getPreferences("spacegame");

        // Default spaceship
        data.putString("animationTexture", Assets.ship2Animation);
        data.putFloat("animationSpeed", 0.14f);
        data.putFloat("maxHealth", 2f);
        data.putFloat("damage", 0.1f);
        data.putFloat("delayBetweenShoots", 0.4f);
        data.putFloat("bulletsSpeed", 600f);
        data.putFloat("speed", 400f);
        data.putFloat("frameLength", 0.14f);
        data.putString("handle", "Destroyer");

        data.putFloat("maxHealing", 0.4f);

        data.putInteger("realShipWidth", 64);
        data.putInteger("realShipHeight", 64);
        data.putInteger("preferredShipWidth", 64);
        data.putInteger("preferredShipHeight", 64);

        data.putInteger("preferredBulletHeight", 10);
        data.putInteger("preferredBulletWidth", 4);
        data.putString("bulletTexture", Assets.bullet1);

        data.flush();
    }

    private void setIgnitor() {
        Preferences data = Gdx.app.getPreferences("spacegame");

        // Default spaceship
        data.putString("animationTexture", Assets.ship4Animation);
        data.putFloat("animationSpeed", 0.14f);
        data.putFloat("maxHealth", 1f);
        data.putFloat("damage", 0.1f);
        data.putFloat("delayBetweenShoots", 0.2f);
        data.putFloat("bulletsSpeed", 700f);
        data.putFloat("speed", 500f);
        data.putFloat("frameLength", 0.14f);
        data.putString("handle", "Ignitor");

        data.putFloat("maxHealing", 0.2f);

        data.putInteger("realShipWidth", 64);
        data.putInteger("realShipHeight", 64);
        data.putInteger("preferredShipWidth", 64);
        data.putInteger("preferredShipHeight", 64);

        data.putInteger("preferredBulletHeight", 10);
        data.putInteger("preferredBulletWidth", 4);
        data.putString("bulletTexture", Assets.bullet1);

        data.flush();
    }

    private void setTurtle() {
        Preferences data = Gdx.app.getPreferences("spacegame");

        // Default spaceship
        data.putString("animationTexture", Assets.ship6Animation);
        data.putFloat("animationSpeed", 0.14f);
        data.putFloat("maxHealth", 4f);
        data.putFloat("damage", 0.1f);
        data.putFloat("delayBetweenShoots", 0.5f);
        data.putFloat("bulletsSpeed", 600f);
        data.putFloat("speed", 200f);
        data.putFloat("frameLength", 0.14f);
        data.putString("handle", "Turtle");

        data.putFloat("maxHealing", 0.6f);

        data.putInteger("realShipWidth", 32);
        data.putInteger("realShipHeight", 32);
        data.putInteger("preferredShipWidth", 64);
        data.putInteger("preferredShipHeight", 64);

        data.putInteger("preferredBulletHeight", 10);
        data.putInteger("preferredBulletWidth", 4);
        data.putString("bulletTexture", Assets.bullet1);

        data.flush();
    }

    private void setUfo() {
        Preferences data = Gdx.app.getPreferences("spacegame");

        // Default spaceship
        data.putString("animationTexture", Assets.ship11Animation);
        data.putFloat("animationSpeed", 0.14f);
        data.putFloat("maxHealth", 2f);
        data.putFloat("damage", 0.1f);
        data.putFloat("delayBetweenShoots", 0.3f);
        data.putFloat("bulletsSpeed", 700f);
        data.putFloat("speed", 400f);
        data.putFloat("frameLength", 0.14f);
        data.putString("handle", "Ufo");

        data.putFloat("maxHealing", 0.4f);

        data.putInteger("realShipWidth", 32);
        data.putInteger("realShipHeight", 32);
        data.putInteger("preferredShipWidth", 64);
        data.putInteger("preferredShipHeight", 64);

        data.putInteger("preferredBulletHeight", 10);
        data.putInteger("preferredBulletWidth", 4);
        data.putString("bulletTexture", Assets.bullet2);

        data.flush();
    }

    private void setStar() {
        Preferences data = Gdx.app.getPreferences("spacegame");

        // Default spaceship
        data.putString("animationTexture", Assets.ship8Animation);
        data.putFloat("animationSpeed", 0.14f);
        data.putFloat("maxHealth", 1f);
        data.putFloat("damage", 0.1f);
        data.putFloat("delayBetweenShoots", 0.3f);
        data.putFloat("bulletsSpeed", 700f);
        data.putFloat("speed", 600f);
        data.putFloat("frameLength", 0.14f);
        data.putString("handle", "Star");

        data.putFloat("maxHealing", 0.2f);

        data.putInteger("realShipWidth", 32);
        data.putInteger("realShipHeight", 32);
        data.putInteger("preferredShipWidth", 64);
        data.putInteger("preferredShipHeight", 64);

        data.putInteger("preferredBulletHeight", 10);
        data.putInteger("preferredBulletWidth", 4);
        data.putString("bulletTexture", Assets.bullet2);

        data.flush();
    }

    private void setPinky() {
        Preferences data = Gdx.app.getPreferences("spacegame");

        // Default spaceship
        data.putString("animationTexture", Assets.ship9Animation);
        data.putFloat("animationSpeed", 0.14f);
        data.putFloat("maxHealth", 1f);
        data.putFloat("damage", 0.1f);
        data.putFloat("delayBetweenShoots", 0.3f);
        data.putFloat("bulletsSpeed", 400f);
        data.putFloat("speed", 200f);
        data.putFloat("frameLength", 0.14f);
        data.putString("handle", "Star");

        data.putFloat("maxHealing", 0.2f);

        data.putInteger("realShipWidth", 32);
        data.putInteger("realShipHeight", 32);
        data.putInteger("preferredShipWidth", 64);
        data.putInteger("preferredShipHeight", 64);

        data.putInteger("preferredBulletHeight", 10);
        data.putInteger("preferredBulletWidth", 4);
        data.putString("bulletTexture", Assets.bullet2);

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
