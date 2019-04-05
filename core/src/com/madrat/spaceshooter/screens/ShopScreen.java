package com.madrat.spaceshooter.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
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

import com.google.gson.*;

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

    public ShopScreen(MainGame newGame, ScrollingBackground scrBack) {
        this.game = newGame;
        this.scrollingBackground = scrBack;
        this.batch = new SpriteBatch();

        skin = Assets.manager.get(Assets.uiskin, Skin.class);
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
                batch.dispose();
                game.setScreen(new MainMenuScreen(game, scrollingBackground));
            }
        });

        destroyerShip = new TextButton("Destroyer", skin);
        destroyerShip.getLabel().setFontScale(SCALE_FACTOR);
        destroyerShip.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setDestroyer();
                batch.dispose();
                game.setScreen(new MainMenuScreen(game, scrollingBackground));
            }
        });

        ignitorShip = new TextButton("Ignitor", skin);
        ignitorShip.getLabel().setFontScale(SCALE_FACTOR);
        ignitorShip.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setIgnitor();
                batch.dispose();
                game.setScreen(new MainMenuScreen(game, scrollingBackground));
            }
        });

        turtleShip = new TextButton("Turtle", skin);
        turtleShip.getLabel().setFontScale(SCALE_FACTOR);
        turtleShip.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setTurtle();
                batch.dispose();
                game.setScreen(new MainMenuScreen(game, scrollingBackground));
            }
        });

        ufoShip = new TextButton("UFO", skin);
        ufoShip.getLabel().setFontScale(SCALE_FACTOR);
        ufoShip.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setUfo();
                batch.dispose();
                game.setScreen(new MainMenuScreen(game, scrollingBackground));
            }
        });

        starShip = new TextButton("Star", skin);
        starShip.getLabel().setFontScale(SCALE_FACTOR);
        starShip.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setStar();
                batch.dispose();
                game.setScreen(new MainMenuScreen(game, scrollingBackground));
            }
        });

        pinkyShip = new TextButton("Pinky", skin);
        pinkyShip.getLabel().setFontScale(SCALE_FACTOR);
        pinkyShip.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setPinky();
                batch.dispose();
                game.setScreen(new MainMenuScreen(game, scrollingBackground));
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
        setNewShip("zapper");
    }

    private void setDestroyer() {
        setNewShip("destroyer");
    }

    private void setIgnitor() {
        setNewShip("ignitor");

    }

    private void setTurtle() {
        setNewShip("turtle");

    }

    private void setUfo() {
        setNewShip("ufo");

    }

    private void setStar() {
        setNewShip("star");

    }

    private void setPinky() {
        setNewShip("pinky");
    }

    private void setNewShip(String newShipJsonHandler) {
        FileHandle currentStateFileHandle;
        FileHandle allShipsFileHandle;

        JsonParser parser = new JsonParser();
        Gson builder = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

        JsonObject newShip;
        JsonObject curState;

        // Set appropriate path to file
        if (MainGame.applicationType == Application.ApplicationType.Android) {
            currentStateFileHandle = Gdx.files.local(MainGame.pathToCurrentState);
            allShipsFileHandle = Gdx.files.local(MainGame.pathToShipConfigs);
        } else if (MainGame.applicationType == Application.ApplicationType.Desktop) {
            currentStateFileHandle = Gdx.files.absolute(MainGame.pathToCurrentState);
            allShipsFileHandle = Gdx.files.absolute(MainGame.pathToShipConfigs);
        } else {
            currentStateFileHandle = Gdx.files.local(MainGame.pathToCurrentState);
            allShipsFileHandle = Gdx.files.local(MainGame.pathToShipConfigs);
        }

        try {
            curState = parser.parse(currentStateFileHandle.readString()).getAsJsonObject();
            newShip = parser.parse(allShipsFileHandle.readString()).getAsJsonObject().getAsJsonObject(newShipJsonHandler);

            // Place new ship into userFile
            curState.add("currentShip", newShip);

            // currentFileHandle.writeString(MainGame.cryptor.encrypt(currentState.toString(4)), false);
            currentStateFileHandle.writeString(builder.toJson(curState), false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
