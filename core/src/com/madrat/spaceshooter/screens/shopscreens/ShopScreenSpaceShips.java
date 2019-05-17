package com.madrat.spaceshooter.screens.shopscreens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.madrat.spaceshooter.MainGame;
import com.madrat.spaceshooter.screens.MainMenuScreen;
import com.madrat.spaceshooter.utils.Assets;
import com.madrat.spaceshooter.utils.BuildConfig;
import com.madrat.spaceshooter.utils.Strings;
import com.madrat.spaceshooter.utils.uiutils.DescriptionRow;
import com.madrat.spaceshooter.utils.ScrollingBackground;
import com.madrat.spaceshooter.utils.uiutils.DialogAlert;
import com.madrat.spaceshooter.utils.uiutils.InfoDialog;
import com.madrat.spaceshooter.utils.uiutils.TileObject;

import static com.madrat.spaceshooter.MainGame.GENERAL_HEIGHT;
import static com.madrat.spaceshooter.MainGame.GENERAL_WIDTH;
import static com.madrat.spaceshooter.MainGame.SCALE_FACTOR;
import static com.madrat.spaceshooter.MainGame.SCALE_X;
import static com.madrat.spaceshooter.MainGame.SCALE_Y;

public class ShopScreenSpaceShips implements Screen {

    MainGame game;

    private ScrollingBackground scrollingBackground;
    private SpriteBatch batch;

    private TextButton backBtn;
    private Label balance;

    private Stage stage;
    private Skin skin;
    private Table container;
    private Image arrow;

    private DialogAlert notEnoughMoneyDialog;
    private DialogAlert successfullyBoughtDialog;
    private DialogAlert successfullySelectedDialog;

    private int money;

    private JsonObject currentPlayerState;
    private JsonObject shipStatsJson;

    private InfoDialog buyDialog, currentDialog;

    public ShopScreenSpaceShips(MainGame newGame, ScrollingBackground scrBack) {
        this.game = newGame;
        this.scrollingBackground = scrBack;
        this.batch = new SpriteBatch();

        skin = Assets.manager.get(Assets.uiskin, Skin.class);
        stage = new Stage(new ScreenViewport());

        // Init Json files with ships and user info
        getActualData(MainGame.pathToCurrentState, MainGame.pathToShipConfigs);
        money = getCurrentMoney(currentPlayerState);

        arrow = new Image(new TextureRegion(Assets.manager.get(Assets.arrow, Texture.class), 0, 0, 87, 57));

        container = new Table();
        container.setWidth(stage.getWidth());
        container.align(Align.center | Align.top);
        container.setPosition(0, MainGame.GENERAL_HEIGHT);
        container.padTop(15 * SCALE_FACTOR);

        balance = new Label(Strings.balancePlaceHolder + money, skin, Strings.fontName, Assets.lightYellow_2);
        balance.setFontScale(SCALE_FACTOR / 1.6f, SCALE_FACTOR / 1.5f);
        container.add(balance).align(Align.right).padTop(8 * SCALE_FACTOR).padRight(25 * SCALE_FACTOR).row();

        Table nestedContainer = new Table();

        final SelectBox selectBox = new SelectBox(skin);
        selectBox.getStyle().font.getData().setScale(SCALE_FACTOR / 1.2f);
        selectBox.getStyle().listStyle.selection.setTopHeight(15 * SCALE_FACTOR);
        selectBox.setItems(Strings.spaceshipsTxt, Strings.labelPowerUps);
        selectBox.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                switch (selectBox.getSelectedIndex()) {
                    // SpaceShips
                    case 0: {
                        break;
                    }
                    // PowerUps
                    case 1: {
                        game.setScreen(new ShopScreenPowerUps(game, scrollingBackground));
                        break;
                    }
                    default: {
                        break;
                    }
                }
            }
        });
        if (BuildConfig.UIDEBUG)
            selectBox.debug();

        nestedContainer.add(selectBox).padLeft(20 * SCALE_FACTOR);
        nestedContainer.add(arrow).size(20 * SCALE_FACTOR, 13 * SCALE_FACTOR).padTop(14 * SCALE_FACTOR).padLeft(5 * SCALE_FACTOR);
        container.add(nestedContainer).padTop(50 * SCALE_FACTOR);
        container.row();

        Table tileTable = new Table();
        tileTable.align(Align.center | Align.top);
        tileTable.padTop(45 * SCALE_FACTOR);

        if (BuildConfig.UIDEBUG) {
            tileTable.debug();
            container.debug();
        }

        TileObject zapper = new TileObject(Strings.zapperHandler, skin, Assets.ship0Animation, 160 * SCALE_FACTOR, 160 * SCALE_FACTOR, 24, 24);
        zapper.getLabel().getActor().setFontScale(SCALE_FACTOR / 1.7f);
        zapper.getLabel().getActor().setColor(Assets.lightGreen_1);
        zapper.getIcon().size(95 * SCALE_FACTOR, 95 * SCALE_FACTOR);
        zapper.getLabel().padTop(15 * SCALE_FACTOR);
        zapper.getButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentDialog = displayShip(Strings.zapperHandler, 0.86f);
                currentDialog.show(stage);
            }
        });
        tileTable.add(zapper).padRight(30 * SCALE_FACTOR);

        TileObject destroyer = new TileObject(Strings.destroyerHandler, skin, Assets.ship2Animation, 160 * SCALE_FACTOR, 160 * SCALE_FACTOR, 32, 32);
        destroyer.getLabel().getActor().setFontScale(SCALE_FACTOR / 1.88f);
        destroyer.getLabel().getActor().setColor(Assets.lightGreen_1);
        destroyer.getIcon().size(95 * SCALE_FACTOR, 95 * SCALE_FACTOR);
        destroyer.getLabel().padTop(15 * SCALE_FACTOR);
        destroyer.getButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentDialog = displayShip(Strings.destroyerHandler, 0.75f);
                currentDialog.show(stage);
            }
        });
        tileTable.add(destroyer).padRight(30 * SCALE_FACTOR);

        TileObject ignitor = new TileObject(Strings.ignitorHandler, skin, Assets.ship4Animation, 160 * SCALE_FACTOR, 160 * SCALE_FACTOR, 32, 32);
        ignitor.getLabel().getActor().setFontScale(SCALE_FACTOR / 1.8f);
        ignitor.getLabel().getActor().setColor(Assets.lightGreen_1);
        ignitor.getIcon().size(95 * SCALE_FACTOR, 95 * SCALE_FACTOR);
        ignitor.getLabel().padTop(15 * SCALE_FACTOR);
        ignitor.getButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentDialog = displayShip(Strings.ignitorHandler, 0.75f);
                currentDialog.show(stage);
            }
        });
        tileTable.add(ignitor).padRight(30 * SCALE_FACTOR); // .padRight(30 * SCALE_FACTOR).padTop(30 * SCALE_FACTOR)

        TileObject turtle = new TileObject(Strings.turtleHandler, skin, Assets.ship6Animation, 160 * SCALE_FACTOR, 160 * SCALE_FACTOR, 32, 32);
        turtle.getLabel().getActor().setFontScale(SCALE_FACTOR / 1.86f);
        turtle.getLabel().getActor().setColor(Assets.lightGreen_1);
        turtle.getIcon().size(95 * SCALE_FACTOR, 95 * SCALE_FACTOR);
        turtle.getLabel().padTop(15 * SCALE_FACTOR);
        turtle.getButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentDialog = displayShip(Strings.turtleHandler, 0.86f);
                currentDialog.show(stage);
            }
        });
        tileTable.add(turtle).row(); // .padRight(30 * SCALE_FACTOR).padTop(30 * SCALE_FACTOR)

        TileObject star = new TileObject(Strings.starHandler, skin, Assets.ship8Animation, 160 * SCALE_FACTOR, 160 * SCALE_FACTOR, 32, 32);
        star.getLabel().getActor().setFontScale(SCALE_FACTOR / 1.86f);
        star.getLabel().getActor().setColor(Assets.lightGreen_1);
        star.getIcon().size(95 * SCALE_FACTOR, 95 * SCALE_FACTOR);
        star.getLabel().padTop(15 * SCALE_FACTOR);
        star.getButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentDialog = displayShip(Strings.starHandler, 0.86f);
                currentDialog.show(stage);
            }
        });
        tileTable.add(star).padRight(30 * SCALE_FACTOR).padTop(30 * SCALE_FACTOR);

        TileObject pinky = new TileObject(Strings.pinkyHandler, skin, Assets.ship10Animation, 160 * SCALE_FACTOR, 160 * SCALE_FACTOR, 32, 32);
        pinky.getLabel().getActor().setFontScale(SCALE_FACTOR / 1.86f);
        pinky.getLabel().getActor().setColor(Assets.lightGreen_1);
        pinky.getIcon().size(95 * SCALE_FACTOR, 95 * SCALE_FACTOR);
        pinky.getLabel().padTop(15 * SCALE_FACTOR);
        pinky.getButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentDialog = displayShip(Strings.pinkyHandler, 0.86f);
                currentDialog.show(stage);
            }
        });
        tileTable.add(pinky).padRight(30 * SCALE_FACTOR).padTop(30 * SCALE_FACTOR);

        TileObject ufo = new TileObject(Strings.ufoHandler, skin, Assets.ship12Animation, 160 * SCALE_FACTOR, 160 * SCALE_FACTOR, 32, 32);
        ufo.getLabel().getActor().setFontScale(SCALE_FACTOR / 1.86f);
        ufo.getLabel().getActor().setColor(Assets.lightGreen_1);
        ufo.getIcon().size(95 * SCALE_FACTOR, 95 * SCALE_FACTOR);
        ufo.getLabel().padTop(15 * SCALE_FACTOR);
        ufo.getButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentDialog = displayShip(Strings.ufoHandler, 0.86f);
                currentDialog.show(stage);
            }
        });
        tileTable.add(ufo).padTop(30 * SCALE_FACTOR).padRight(30 * SCALE_FACTOR);

        ScrollPane scrollPane = new ScrollPane(tileTable.padLeft(25 * SCALE_FACTOR).padRight(25 * SCALE_FACTOR));
        container.add(scrollPane).size(GENERAL_WIDTH, 400 * SCALE_FACTOR);
        container.row();

        backBtn = new TextButton(Strings.backTxt, skin);
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

    private InfoDialog displayShip(String shipName, final float objectNameFontScale) {

        final JsonObject currentShipJson = shipStatsJson.getAsJsonObject(shipName);

        if (BuildConfig.DEBUG) {
            Gson builder = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
            System.out.println("[+] currentShipJson: " + builder.toJson(currentShipJson));
        }

        final DescriptionRow descRow1; // Text description
        final DescriptionRow descRow2; // Health
        final DescriptionRow descRow3; // Speed
        final DescriptionRow descRow4; // Damage
        final DescriptionRow descRow5; // FireRate
        final DescriptionRow priceRow; // Price

        // Array for handling all description rows
        final Array<DescriptionRow> descriptionRows = new Array<DescriptionRow>();

        // button Text
        String btnText;

        // Full text description
        descRow1 = new DescriptionRow(15, 0, 25, 0);
        descRow1.description = new Label(currentShipJson.get("desc").getAsString(), skin);
        descRow1.description.setFontScale(SCALE_FACTOR / 2f);
        descRow1.description.setColor(Assets.lightYellow_1);

        // Ship Health
        descRow2 = new DescriptionRow(15, 0, 25, 0);
        descRow2.description = new Label(Strings.healthLabel + currentShipJson.get("maxHealth").getAsString(), skin);
        descRow2.description.setFontScale(SCALE_FACTOR / 2f);
        descRow2.description.setColor(Assets.lightPinky);

        // Ship Speed
        descRow3 = new DescriptionRow(5, 0, 25, 0);
        descRow3.description = new Label(Strings.speedLabel + currentShipJson.get("shipSpeed").getAsString(), skin);
        descRow3.description.setFontScale(SCALE_FACTOR / 2f);
        descRow3.description.setColor(Assets.lightGreen_2);

        // Ship damage
        descRow4 = new DescriptionRow(5, 0, 25, 0);
        descRow4.description = new Label(Strings.damageLabel + currentShipJson.get("damage").getAsString(), skin);
        descRow4.description.setFontScale(SCALE_FACTOR / 2f);
        descRow4.description.setColor(Assets.lightYellow_2);

        // Ship firerate
        descRow5 = new DescriptionRow(5, 0, 25, 0);
        descRow5.description = new Label(Strings.firerateLabel + currentShipJson.get("delayBetweenShootsBullets").getAsString(), skin);
        descRow5.description.setFontScale(SCALE_FACTOR / 2f);
        descRow5.description.setColor(Assets.lightBlue_1);

        if (currentShipJson.get("isBought").getAsBoolean()) {
            priceRow = null;

            if (currentPlayerState.getAsJsonObject("currentShip").get("handler").getAsString().equals(currentShipJson.get("handler").getAsString()))
                btnText = Strings.selectedTxt;
            else
                btnText = Strings.selectTxt;

        } else {
            priceRow = new DescriptionRow(8, 0, 25, 0);
            priceRow.description = new Label(Strings.pricePlaceholder + currentShipJson.get("shipPrice").getAsString(), skin);
            priceRow.description.setFontScale(SCALE_FACTOR / 2f);
            priceRow.description.setColor(Assets.lightPinky);

            btnText = Strings.buyPlaceholder;
        }

        descriptionRows.add(descRow1);
        descriptionRows.add(descRow2);
        descriptionRows.add(descRow3);
        descriptionRows.add(descRow4);
        descriptionRows.add(descRow5);

        buyDialog = new InfoDialog(skin, stage, currentShipJson.get("handler").getAsString(), objectNameFontScale, currentShipJson.get("animationTexture").getAsString(), currentShipJson.get("realShipWidth").getAsInt(), currentShipJson.get("realShipHeight").getAsInt(), 200, 200, descriptionRows, priceRow, btnText, 360 * SCALE_X, 480 * SCALE_Y); // 360 * SCALE_X, 480 * SCALE_Y
        buyDialog.setPosition(GENERAL_WIDTH / 2 - buyDialog.getPrefWidth() / 2, (GENERAL_HEIGHT - buyDialog.getPrefHeight()) / 2 + 40 * SCALE_Y);

        final DialogAlert confirmBuy = new DialogAlert("", skin, buyDialog.getMultiplexer());
        confirmBuy.text(Strings.buyShipTxt).yesButton(Strings.yesTxt, new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (getCurrentMoney(currentPlayerState) >= currentShipJson.get("shipPrice").getAsInt()) {
                    // Decrease money in object
                    currentPlayerState.getAsJsonObject("stats").addProperty("money", currentPlayerState.getAsJsonObject("stats").get("money").getAsInt() - currentShipJson.get("shipPrice").getAsInt());

                    money -= currentShipJson.get("shipPrice").getAsInt();

                    // Set label value
                    balance.setText(Strings.balancePlaceHolderL + money);

                    // SetBought state
                    currentShipJson.addProperty("isBought", true);

                    // Write data to files
                    currentPlayerState.add("currentShip", currentShipJson);
                    shipStatsJson.add(currentShipJson.get("handler").getAsString(), currentShipJson);

                    updateData(MainGame.pathToCurrentState, MainGame.pathToShipConfigs, currentPlayerState, shipStatsJson);

                    buyDialog.getActBtn().setText("selected");
                    buyDialog.getActBtn().removeListener(buyDialog.getActBtn().getListeners().get(1));
                    buyDialog.getActBtn().setDisabled(true);
                    priceRow.description.setText("");

                    buyDialog = displayShip(currentShipJson.get("handler").getAsString(), objectNameFontScale);
                    buyDialog.show(stage, 0);
                    currentDialog.hide(0);

                    confirmBuy.hide();
                    successfullyBoughtSetUp(buyDialog.getMultiplexer());
                    successfullyBoughtDialog.show(stage);
                } else {
                    confirmBuy.hide();
                    notEnoughMoneySetUp(buyDialog.getMultiplexer());
                    notEnoughMoneyDialog.show(stage);
                }
                return true;
            }
        }).noButton(Strings.noTxt, new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        confirmBuy.buttonYes.getLabel().setColor(Assets.lightPinky);
        confirmBuy.buttonYes.getLabel().setFontScale(SCALE_FACTOR);
        confirmBuy.buttonNo.getLabel().setColor(Assets.lightGreen_2);
        confirmBuy.buttonNo.getLabel().setFontScale(SCALE_FACTOR);

        if (!currentPlayerState.getAsJsonObject("currentShip").get("handler").getAsString().equals(currentShipJson.get("handler").getAsString())
                && currentShipJson.get("isBought").getAsBoolean()) {
            buyDialog.getActBtn().addListener(new InputListener() {
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    // Select Ship
                    currentPlayerState.add("currentShip", currentShipJson);
                    updateData(MainGame.pathToCurrentState, currentPlayerState);

                    buyDialog = displayShip(currentShipJson.get("handler").getAsString(), objectNameFontScale);
                    buyDialog.show(stage, 0);
                    currentDialog.hide(0);

                    successfullySelectedSetUp(buyDialog.getMultiplexer());
                    successfullySelectedDialog.show(stage);
                    return true;
                }
            });
        } else if (!currentShipJson.get("isBought").getAsBoolean()) {
            buyDialog.getActBtn().addListener(new InputListener() {
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                    // BuyShip
                    confirmBuy.show(stage);

                    return true;
                }
            });
        } else if (currentPlayerState.getAsJsonObject("currentShip").get("handler").getAsString().equals(currentShipJson.get("handler").getAsString())) {
            buyDialog.getActBtn().setDisabled(true);
            buyDialog.getActBtn().removeListener(buyDialog.getActBtn().getListeners().get(1));
        }


        return buyDialog;
    }

    private void getActualData(String pathToCurrentState, String pathToShipStats) {
        FileHandle currentStateFile;
        FileHandle shipStatsFile;

        JsonParser parser = new JsonParser();

        // Set appropriate path to file
        if (MainGame.applicationType == Application.ApplicationType.Android) {
            currentStateFile = Gdx.files.local(pathToCurrentState);
            shipStatsFile = Gdx.files.local(pathToShipStats);
        } else if (MainGame.applicationType == Application.ApplicationType.Desktop) {
            currentStateFile = Gdx.files.absolute(pathToCurrentState);
            shipStatsFile = Gdx.files.absolute(pathToShipStats);
        } else {
            currentStateFile = Gdx.files.local(pathToCurrentState);
            shipStatsFile = Gdx.files.local(pathToShipStats);
        }

        try {
            this.currentPlayerState = parser.parse(MainGame.cryptor.decrypt(currentStateFile.readString())).getAsJsonObject();
            this.shipStatsJson = parser.parse(MainGame.cryptor.decrypt(shipStatsFile.readString())).getAsJsonObject();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void updateData(String playerStatePath, JsonObject playerState) {
        FileHandle playerStateFileHandle;

        Gson builder = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

        // Set appropriate path to file
        if (MainGame.applicationType == Application.ApplicationType.Android) {
            playerStateFileHandle = Gdx.files.local(playerStatePath);
        } else if (MainGame.applicationType == Application.ApplicationType.Desktop) {
            playerStateFileHandle = Gdx.files.absolute(playerStatePath);
        } else {
            playerStateFileHandle = Gdx.files.local(playerStatePath);
        }

        try {
            // Write userFile
            playerStateFileHandle.writeString(MainGame.cryptor.encrypt(builder.toJson(playerState)), false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void updateData(String playerStatePath, String shipDefaultsPath, JsonObject playerState, JsonObject shipsState) {
        FileHandle playerStateFileHandle;
        FileHandle shipsStateFileHandle;

        Gson builder = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

        // Set appropriate path to file
        if (MainGame.applicationType == Application.ApplicationType.Android) {
            playerStateFileHandle = Gdx.files.local(playerStatePath);
            shipsStateFileHandle = Gdx.files.local(shipDefaultsPath);
        } else if (MainGame.applicationType == Application.ApplicationType.Desktop) {
            playerStateFileHandle = Gdx.files.absolute(playerStatePath);
            shipsStateFileHandle = Gdx.files.absolute(shipDefaultsPath);
        } else {
            playerStateFileHandle = Gdx.files.local(playerStatePath);
            shipsStateFileHandle = Gdx.files.local(shipDefaultsPath);
        }

        try {
            // Write ShipStates file
            shipsStateFileHandle.writeString(MainGame.cryptor.encrypt(builder.toJson(shipsState)), false);
            // Write userFile
            playerStateFileHandle.writeString(MainGame.cryptor.encrypt(builder.toJson(playerState)), false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private int getCurrentMoney(JsonObject currentState) {
        try {
            // extract and return highscore
            return currentState.getAsJsonObject("stats").get("money").getAsInt();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    // buyDialog.getMultiplexer()

    private void notEnoughMoneySetUp(InputMultiplexer multiplexer) {
        DialogAlert notEnoughMoneyDialogLoc = new DialogAlert("", skin, multiplexer);
        notEnoughMoneyDialogLoc.text(Strings.notEnoughMoney).yesButton(Strings.okTxt, new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        notEnoughMoneyDialogLoc.buttonYes.getLabel().setColor(Assets.lightPinky);
        notEnoughMoneyDialogLoc.buttonYes.getLabel().setFontScale(SCALE_FACTOR);

        this.notEnoughMoneyDialog = notEnoughMoneyDialogLoc;
    }

    private void successfullyBoughtSetUp(InputMultiplexer multiplexer) {
        DialogAlert successfullyBoughtDialogLoc = new DialogAlert("", skin, multiplexer);
        successfullyBoughtDialogLoc.text(Strings.succesfullyBought).yesButton(Strings.okTxt, new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        successfullyBoughtDialogLoc.buttonYes.getLabel().setColor(Assets.lightGreen_2);
        successfullyBoughtDialogLoc.buttonYes.getLabel().setFontScale(SCALE_FACTOR);

        this.successfullyBoughtDialog = successfullyBoughtDialogLoc;
    }

    private void successfullySelectedSetUp(InputMultiplexer multiplexer) {
        DialogAlert successfullySelectedLoc = new DialogAlert("", skin, multiplexer);
        successfullySelectedLoc.text(Strings.successfullySelected).yesButton(Strings.okTxt, new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        successfullySelectedLoc.buttonYes.getLabel().setColor(Assets.lightGreen_2);
        successfullySelectedLoc.buttonYes.getLabel().setFontScale(SCALE_FACTOR);

        this.successfullySelectedDialog = successfullySelectedLoc;
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
