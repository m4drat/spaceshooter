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
import com.madrat.spaceshooter.MainGame;
import com.madrat.spaceshooter.screens.MainMenuScreen;
import com.madrat.spaceshooter.utils.Assets;
import com.madrat.spaceshooter.utils.BuildConfig;
import com.madrat.spaceshooter.utils.Strings;
import com.madrat.spaceshooter.utils.uiutils.DescriptionRow;
import com.madrat.spaceshooter.utils.ScrollingBackground;

import com.google.gson.*;
import com.madrat.spaceshooter.utils.uiutils.DialogAlert;
import com.madrat.spaceshooter.utils.uiutils.InfoDialog;
import com.madrat.spaceshooter.utils.uiutils.TileObject;

import static com.madrat.spaceshooter.MainGame.GENERAL_HEIGHT;
import static com.madrat.spaceshooter.MainGame.GENERAL_WIDTH;
import static com.madrat.spaceshooter.MainGame.SCALE_FACTOR;
import static com.madrat.spaceshooter.MainGame.SCALE_X;
import static com.madrat.spaceshooter.MainGame.SCALE_Y;

public class ShopScreenPowerUps implements Screen {

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

    private boolean isHealFullyUpgraded, isAmmoFullyUpgraded, isShieldFullyUpgraded;
    private int healUpgradeStage = 0, ammoUpgradeStage = 0, shieldUpgradeStage = 0;
    private int money;

    private JsonObject currentPlayerState;
    private JsonObject currentPowerUpsState;

    public ShopScreenPowerUps(final MainGame newGame, ScrollingBackground scrBack) {
        this.game = newGame;
        this.scrollingBackground = scrBack;
        this.batch = new SpriteBatch();

        skin = Assets.manager.get(Assets.uiskin, Skin.class);
        stage = new Stage(new ScreenViewport());

        // SetUp currentPlayerState, currentPowerUpsState
        getActualData(MainGame.pathToCurrentState);

        setUpDefaults();

        arrow = new Image(new TextureRegion(Assets.manager.get(Assets.arrow, Texture.class), 0, 0, 87, 57));

        container = new Table();
        container.setWidth(stage.getWidth());
        container.align(Align.center | Align.top);
        container.setPosition(0, MainGame.GENERAL_HEIGHT);
        container.padTop(15 * SCALE_FACTOR);

        money = getCurrentMoney(currentPlayerState);

        balance = new Label(Strings.balancePlaceHolder + money, skin, Strings.fontName, Assets.lightYellow_2);
        balance.setFontScale(SCALE_FACTOR / 1.6f, SCALE_FACTOR / 1.5f);
        container.add(balance).align(Align.right).padTop(8 * SCALE_FACTOR).padRight(25 * SCALE_FACTOR).row();

        Table nestedContainer = new Table();

        Label calc = new Label(Strings.labelPowerUps, skin);
        calc.setFontScale(SCALE_FACTOR / 1.2f);

        final SelectBox selectBox = new SelectBox(skin);
        selectBox.getStyle().font.getData().setScale(SCALE_FACTOR / 1.2f);
        selectBox.getStyle().listStyle.selection.setTopHeight(15 * SCALE_FACTOR);
        selectBox.setItems(Strings.labelPowerUps, Strings.spaceshipsTxt);
        selectBox.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                switch (selectBox.getSelectedIndex()) {
                    // PowerUps
                    case 0: {
                        break;
                    }
                    // SpaceShips
                    case 1: {
                        game.setScreen(new ShopScreenSpaceShips(game, scrollingBackground));
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

        nestedContainer.add(selectBox).size(calc.getPrefWidth() + 10, selectBox.getHeight()).padLeft(20 * SCALE_FACTOR);
        nestedContainer.add(arrow).size(20 * SCALE_FACTOR, 13 * SCALE_FACTOR).padTop(14 * SCALE_FACTOR).padLeft(SCALE_FACTOR);
        container.add(nestedContainer).padTop(50 * SCALE_FACTOR);
        container.row();

        Table tileTable = new Table();
        tileTable.align(Align.center | Align.top);
        tileTable.padTop(45 * SCALE_FACTOR);

        if (BuildConfig.UIDEBUG) {
            tileTable.debug();
            container.debug();
        }

        final InfoDialog upgradeHeal = setUpHeal();
        TileObject healPowerUp = new TileObject(Strings.medkitTxt, skin, Assets.healPowerUp, 160 * SCALE_FACTOR, 160 * SCALE_FACTOR, 30, 25);
        healPowerUp.getLabel().getActor().setFontScale(SCALE_FACTOR / 1.7f);
        healPowerUp.getLabel().getActor().setColor(Assets.lightGreen_1);
        healPowerUp.getIcon().size(95 * SCALE_FACTOR, 80 * SCALE_FACTOR);
        healPowerUp.getLabel().padTop(15 * SCALE_FACTOR);
        tileTable.add(healPowerUp).padRight(30 * SCALE_FACTOR);
        healPowerUp.getButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                upgradeHeal.show(stage);
            }
        });

        final InfoDialog upgradeAmmo = setUpAmmo();
        TileObject ammoPowerUp = new TileObject(Strings.rocketsTxt, skin, Assets.ammoPowerUp, 160 * SCALE_FACTOR, 160 * SCALE_FACTOR, 58, 53);
        ammoPowerUp.getLabel().getActor().setFontScale(SCALE_FACTOR / 1.7f);
        ammoPowerUp.getLabel().getActor().setColor(Assets.lightGreen_1);
        ammoPowerUp.getIcon().size(95 * SCALE_FACTOR, 86 * SCALE_FACTOR);
        ammoPowerUp.getLabel().padTop(15 * SCALE_FACTOR);
        ammoPowerUp.getButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                upgradeAmmo.show(stage);
            }
        });
        tileTable.add(ammoPowerUp).row();

        final InfoDialog upgradeShield = setUpShield();
        TileObject shieldPowerUp = new TileObject(Strings.shieldTxt, skin, Assets.shieldPowerUp, 160 * SCALE_FACTOR, 160 * SCALE_FACTOR, 32, 32);
        shieldPowerUp.getLabel().getActor().setFontScale(SCALE_FACTOR / 1.7f);
        shieldPowerUp.getLabel().getActor().setColor(Assets.lightGreen_1);
        shieldPowerUp.getIcon().size(95 * SCALE_FACTOR, 95 * SCALE_FACTOR);
        shieldPowerUp.getLabel().padTop(15 * SCALE_FACTOR);
        shieldPowerUp.getButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                upgradeShield.show(stage);
            }
        });
        tileTable.add(shieldPowerUp).padRight(30 * SCALE_FACTOR).padTop(30 * SCALE_FACTOR);

        ScrollPane scrollPane = new ScrollPane(tileTable);
        container.add(scrollPane).size(GENERAL_WIDTH, 400 * SCALE_FACTOR);
        container.row();

        backBtn = new TextButton(Strings.backTxtU, skin);
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

    private InfoDialog setUpHeal() {
        final JsonObject heal = currentPowerUpsState.getAsJsonObject("heal").getAsJsonObject(Integer.toString(healUpgradeStage));

        final DescriptionRow descRow1;
        final DescriptionRow priceRow;

        final Array<DescriptionRow> descriptionRows = new Array<DescriptionRow>();

        if (!isHealFullyUpgraded) {
            descRow1 = new DescriptionRow(15, 0, 25, 0);
            descRow1.description = new Label(heal.get("desc").getAsString(), skin);
            descRow1.description.setFontScale(SCALE_FACTOR / 1.5f);
            descRow1.description.setColor(Assets.lightYellow_1);

            priceRow = new DescriptionRow(30, 0, 25, 0);
            priceRow.description = new Label(Strings.pricePlaceholder + heal.get("price").getAsString(), skin);
            priceRow.description.setFontScale(SCALE_FACTOR / 1.5f);
            priceRow.description.setColor(Assets.lightPinky);

            descriptionRows.add(descRow1);
        } else {
            descRow1 = new DescriptionRow(15, 0, 25, 0);
            descRow1.description = new Label(Strings.powerUpFullUpgrade, skin);
            descRow1.description.setFontScale(SCALE_FACTOR / 1.5f);
            descRow1.description.setColor(Assets.lightYellow_1);

            priceRow = null;

            descriptionRows.add(descRow1);
        }

        final InfoDialog buyDialog = new InfoDialog(skin, stage, Strings.medkitTxt, 1.15f, Assets.healPowerUp, 30, 25, 220, 183, descriptionRows, priceRow, "UPGRADE", 360 * SCALE_X, 480 * SCALE_Y); // 360 * SCALE_X, 480 * SCALE_Y
        buyDialog.setPosition(GENERAL_WIDTH / 2 - buyDialog.getPrefWidth() / 2, (GENERAL_HEIGHT - buyDialog.getPrefHeight()) / 2 + 40 * SCALE_Y);

        final DialogAlert confirmBuy = new DialogAlert("", skin, buyDialog.getMultiplexer());
        confirmBuy.text(Strings.buyUpgrade).yesButton(Strings.yesTxt, new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (getCurrentMoney(currentPlayerState) >= heal.get("price").getAsInt()) {
                    // Decrease money in object
                    currentPlayerState.getAsJsonObject("stats").addProperty("money", currentPlayerState.getAsJsonObject("stats").get("money").getAsInt() - heal.get("price").getAsInt());

                    money -= heal.get("price").getAsInt();

                    // Set label value
                    balance.setText(Strings.balancePlaceHolderL + money);

                    // SetBought state
                    currentPowerUpsState.getAsJsonObject("heal").getAsJsonObject(Integer.toString(healUpgradeStage)).addProperty("isBought", true);

                    // Write data to files
                    updateData(MainGame.pathToCurrentState, currentPlayerState, currentPowerUpsState);

                    setUpIsHealFullyUpgraded();

                    if (isHealFullyUpgraded) {
                        descRow1.description.setText(Strings.powerUpFullUpgrade);
                        priceRow.description.setText("");

                        buyDialog.getDescriptionRowCells().get(0).padBottom(-28 * SCALE_FACTOR);

                        buyDialog.getActBtn().setVisible(false);
                        buyDialog.getActBtn().setDisabled(true);
                    }

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

        if (!isHealFullyUpgraded) {
            buyDialog.getActBtn().addListener(new InputListener() {
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    confirmBuy.show(stage);
                    return true;
                }
            });
        } else {
            buyDialog.getActBtn().setVisible(false);
            buyDialog.getActBtn().setDisabled(true);
        }

        return buyDialog;
    }

    private InfoDialog setUpAmmo() {
        final JsonObject ammo = currentPowerUpsState.getAsJsonObject("ammo").getAsJsonObject(Integer.toString(ammoUpgradeStage));

        final DescriptionRow descRow1;
        final DescriptionRow priceRow;

        final Array<DescriptionRow> descriptionRows = new Array<DescriptionRow>();

        if (!isAmmoFullyUpgraded) {
            descRow1 = new DescriptionRow(15, 0, 25, 0);
            descRow1.description = new Label(ammo.get("desc").getAsString(), skin);
            descRow1.description.setFontScale(SCALE_FACTOR / 1.5f);
            descRow1.description.setColor(Assets.lightYellow_1);

            priceRow = new DescriptionRow(30, 0, 25, 0);
            priceRow.description = new Label(Strings.pricePlaceholder + ammo.get("price").getAsString(), skin);
            priceRow.description.setFontScale(SCALE_FACTOR / 1.5f);
            priceRow.description.setColor(Assets.lightPinky);

            descriptionRows.add(descRow1);
        } else {
            descRow1 = new DescriptionRow(15, 0, 25, 0);
            descRow1.description = new Label(Strings.powerUpFullUpgrade, skin);
            descRow1.description.setFontScale(SCALE_FACTOR / 1.5f);
            descRow1.description.setColor(Assets.lightYellow_1);

            priceRow = null;

            descriptionRows.add(descRow1);
        }

        final InfoDialog buyDialog = new InfoDialog(skin, stage, Strings.ammoTxt, 1.15f, Assets.ammoPowerUp, 58, 54, 180, 167, descriptionRows, priceRow, "UPGRADE", 360 * SCALE_X, 480 * SCALE_Y); // 360 * SCALE_X, 480 * SCALE_Y
        buyDialog.setPosition(GENERAL_WIDTH / 2 - buyDialog.getPrefWidth() / 2, (GENERAL_HEIGHT - buyDialog.getPrefHeight()) / 2 + 40 * SCALE_Y);

        final DialogAlert confirmBuy = new DialogAlert("", skin, buyDialog.getMultiplexer());
        confirmBuy.text(Strings.buyUpgrade).yesButton(Strings.yesTxt, new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (getCurrentMoney(currentPlayerState) >= ammo.get("price").getAsInt()) {
                    // Decrease money in object
                    currentPlayerState.getAsJsonObject("stats").addProperty("money", currentPlayerState.getAsJsonObject("stats").get("money").getAsInt() - ammo.get("price").getAsInt());

                    money -= ammo.get("price").getAsInt();

                    // Set label value
                    balance.setText(Strings.balancePlaceHolderL + money);

                    // SetBought state
                    currentPowerUpsState.getAsJsonObject("ammo").getAsJsonObject(Integer.toString(ammoUpgradeStage)).addProperty("isBought", true);

                    // Write data to files
                    updateData(MainGame.pathToCurrentState, currentPlayerState, currentPowerUpsState);

                    setUpIsAmmoFullyUpgraded();

                    if (isHealFullyUpgraded) {
                        descRow1.description.setText(Strings.powerUpFullUpgrade);
                        priceRow.description.setText("");

                        buyDialog.getDescriptionRowCells().get(0).padBottom(-28 * SCALE_FACTOR);

                        buyDialog.getActBtn().setVisible(false);
                        buyDialog.getActBtn().setDisabled(true);
                    }

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

        if (!isAmmoFullyUpgraded) {
            buyDialog.getActBtn().addListener(new InputListener() {
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    confirmBuy.show(stage);
                    return true;
                }
            });
        } else {
            buyDialog.getActBtn().setVisible(false);
            buyDialog.getActBtn().setDisabled(true);
        }

        return buyDialog;
    }

    private InfoDialog setUpShield() {
        final JsonObject shield = currentPowerUpsState.getAsJsonObject("shield").getAsJsonObject(Integer.toString(shieldUpgradeStage));

        final DescriptionRow descRow1;
        final DescriptionRow priceRow;

        final Array<DescriptionRow> descriptionRows = new Array<DescriptionRow>();

        if (!isShieldFullyUpgraded) {
            descRow1 = new DescriptionRow(15, 0, 25, 0);
            descRow1.description = new Label(shield.get("desc").getAsString(), skin);
            descRow1.description.setFontScale(SCALE_FACTOR / 1.5f);
            descRow1.description.setColor(Assets.lightYellow_1);

            priceRow = new DescriptionRow(30, 0, 25, 0);
            priceRow.description = new Label(Strings.pricePlaceholder + shield.get("price").getAsString(), skin);
            priceRow.description.setFontScale(SCALE_FACTOR / 1.5f);
            priceRow.description.setColor(Assets.lightPinky);

            descriptionRows.add(descRow1);
        } else {
            descRow1 = new DescriptionRow(15, 0, 25, 0);
            descRow1.description = new Label(Strings.powerUpFullUpgrade, skin);
            descRow1.description.setFontScale(SCALE_FACTOR / 1.5f);
            descRow1.description.setColor(Assets.lightYellow_1);

            priceRow = null;

            descriptionRows.add(descRow1);
        }

        final InfoDialog buyDialog = new InfoDialog(skin, stage, Strings.shieldTxt, 1.15f, Assets.shieldPowerUp, 32, 32, 200, 200, descriptionRows, priceRow, "UPGRADE", 360 * SCALE_X, 480 * SCALE_Y); // 360 * SCALE_X, 480 * SCALE_Y
        buyDialog.setPosition(GENERAL_WIDTH / 2 - buyDialog.getPrefWidth() / 2, (GENERAL_HEIGHT - buyDialog.getPrefHeight()) / 2 + 40 * SCALE_Y);

        final DialogAlert confirmBuy = new DialogAlert("", skin, buyDialog.getMultiplexer());
        confirmBuy.text(Strings.buyUpgrade).yesButton(Strings.yesTxt, new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (getCurrentMoney(currentPlayerState) >= shield.get("price").getAsInt()) {
                    // Decrease money in object
                    currentPlayerState.getAsJsonObject("stats").addProperty("money", currentPlayerState.getAsJsonObject("stats").get("money").getAsInt() - shield.get("price").getAsInt());

                    money -= shield.get("price").getAsInt();

                    // Set label value
                    balance.setText(Strings.balancePlaceHolderL + money);

                    // SetBought state
                    currentPowerUpsState.getAsJsonObject("shield").getAsJsonObject(Integer.toString(shieldUpgradeStage)).addProperty("isBought", true);

                    // Write data to files
                    updateData(MainGame.pathToCurrentState, currentPlayerState, currentPowerUpsState);

                    setUpIsShieldFullyUpgraded();

                    if (isShieldFullyUpgraded) {
                        descRow1.description.setText(Strings.powerUpFullUpgrade);
                        priceRow.description.setText("");

                        buyDialog.getDescriptionRowCells().get(0).padBottom(-28 * SCALE_FACTOR);

                        buyDialog.getActBtn().setVisible(false);
                        buyDialog.getActBtn().setDisabled(true);
                    } else {
                        descRow1.description.setText(currentPowerUpsState.getAsJsonObject("shield").getAsJsonObject(Integer.toString(shieldUpgradeStage)).get("desc").getAsString());
                        priceRow.description.setText(Strings.pricePlaceholder + currentPowerUpsState.getAsJsonObject("shield").getAsJsonObject(Integer.toString(shieldUpgradeStage)).get("price").getAsString());
                    }

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

        if (!isShieldFullyUpgraded) {
            buyDialog.getActBtn().addListener(new InputListener() {
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    confirmBuy.show(stage);
                    return true;
                }
            });
        } else {
            buyDialog.getActBtn().setVisible(false);
            buyDialog.getActBtn().setDisabled(true);
        }

        return buyDialog;
    }

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

    private void setUpDefaults() {
        setUpIsHealFullyUpgraded();
        setUpIsAmmoFullyUpgraded();
        setUpIsShieldFullyUpgraded();
    }

    private void setUpIsHealFullyUpgraded() {
        isHealFullyUpgraded = true;
        healUpgradeStage = currentPowerUpsState.getAsJsonObject("heal").size() - 1;

        for (int i = 0; i < currentPowerUpsState.getAsJsonObject("heal").size(); ++i) {
            if (currentPowerUpsState.getAsJsonObject("heal").getAsJsonObject(Integer.toString(i)).get("isBought").getAsBoolean()) {
                continue;
            } else {
                isHealFullyUpgraded = false;
                healUpgradeStage = i;
                break;
            }
        }

        if (BuildConfig.DEBUG) {
            System.out.println("isHealFullyUpgraded: " + isHealFullyUpgraded);
        }
    }

    private void setUpIsAmmoFullyUpgraded() {
        isAmmoFullyUpgraded = true;
        ammoUpgradeStage = currentPowerUpsState.getAsJsonObject("ammo").size() - 1;

        for (int i = 0; i < currentPowerUpsState.getAsJsonObject("ammo").size(); ++i) {
            if (currentPowerUpsState.getAsJsonObject("ammo").getAsJsonObject(Integer.toString(i)).get("isBought").getAsBoolean()) {
                continue;
            } else {
                isAmmoFullyUpgraded = false;
                ammoUpgradeStage = i;
                break;
            }
        }

        if (BuildConfig.DEBUG) {
            System.out.println("isAmmoFullyUpgraded: " + isAmmoFullyUpgraded);
        }
    }

    private void setUpIsShieldFullyUpgraded() {
        shieldUpgradeStage = currentPowerUpsState.getAsJsonObject("shield").size() - 1;
        isShieldFullyUpgraded = true;

        for (int i = 0; i < currentPowerUpsState.getAsJsonObject("shield").size(); ++i) {
            if (currentPowerUpsState.getAsJsonObject("shield").getAsJsonObject(Integer.toString(i)).get("isBought").getAsBoolean()) {
                continue;
            } else {
                isShieldFullyUpgraded = false;
                shieldUpgradeStage = i;
                break;
            }
        }

        if (BuildConfig.DEBUG) {
            System.out.println("isShieldFullyUpgraded: " + isShieldFullyUpgraded);
            System.out.println("shieldUpgradeStage: " + shieldUpgradeStage);
        }
    }

    private void getActualData(String pathToCurrentState) {
        FileHandle currentStateFile;
        JsonParser parser = new JsonParser();

        // Set appropriate path to file
        if (MainGame.applicationType == Application.ApplicationType.Android) {
            currentStateFile = Gdx.files.local(pathToCurrentState);
        } else if (MainGame.applicationType == Application.ApplicationType.Desktop) {
            currentStateFile = Gdx.files.absolute(pathToCurrentState);
        } else {
            currentStateFile = Gdx.files.local(pathToCurrentState);
        }

        try {
            this.currentPowerUpsState = parser.parse(MainGame.cryptor.decrypt(currentStateFile.readString())).getAsJsonObject().getAsJsonObject("powerUpUpgradesState");
            this.currentPlayerState = parser.parse(MainGame.cryptor.decrypt(currentStateFile.readString())).getAsJsonObject();

            if (BuildConfig.DEBUG) {
                System.out.println("[+] currentPowerUpsState: " + this.currentPowerUpsState);
                System.out.println("[+] currentPlayerState: " + this.currentPlayerState);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void updateData(String playerStatePath, JsonObject playerState, JsonObject powerUpsState) {
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
            playerState.add("powerUpUpgradesState", powerUpsState);
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
