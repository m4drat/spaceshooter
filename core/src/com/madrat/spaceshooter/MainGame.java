package com.madrat.spaceshooter;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.madrat.spaceshooter.screens.MainMenuScreen;
import com.madrat.spaceshooter.utils.Assets;
import com.madrat.spaceshooter.utils.BuildConfig;
import com.madrat.spaceshooter.utils.DebugUtils;
import com.madrat.spaceshooter.utils.Encryptor;
import com.madrat.spaceshooter.utils.Initializer;

public class MainGame extends Game {

    public static String localStoragePath;
    public static String pathToShipConfigs;
    public static String pathToDefaultParameters;
    public static String pathToDefaultShipParameters;
    public static String pathToCurrentState;
    public static Encryptor cryptor;
    public static DebugUtils debugUtils;

    public static Application.ApplicationType applicationType;
    public static int GENERAL_WIDTH = 480; // 480
    public static int GENERAL_HEIGHT = 720; // 720
    public static float SCALE_FACTOR;
    public static float SCALE_X;
    public static float SCALE_Y;

    @Override
    public void create() {
        // Load Assets
        loadAssets();

        // Create "crypto" object :)
        cryptor = new Encryptor();

        // System info
        applicationType = Gdx.app.getType();
        GENERAL_WIDTH = Gdx.graphics.getWidth();
        GENERAL_HEIGHT = Gdx.graphics.getHeight();

        SCALE_FACTOR = (100 / (720 * 100 / (float) Math.max(GENERAL_HEIGHT, GENERAL_WIDTH)) + 100 / (480 * 100 / (float) Math.min(GENERAL_HEIGHT, GENERAL_WIDTH))) / 2;
        SCALE_X = 100 / (480 * 100 / (float) GENERAL_WIDTH);
        SCALE_Y = 100 / (720 * 100 / (float) GENERAL_HEIGHT);

        if (BuildConfig.DEBUG) {
            debugUtils = new DebugUtils();
            System.out.println("Scale Factor: " + SCALE_FACTOR);
            System.out.println("Scale FactorX: " + SCALE_X);
            System.out.println("Scale FactorY: " + SCALE_Y);
        }


        // Initialize initializer to init initFunctions, which will initialize user data :)
        Initializer.init();

        // Catch backKey
        Gdx.input.setCatchBackKey(true);

        this.setScreen(new MainMenuScreen(this));
    }

    void loadAssets() {
        Assets.manager = new AssetManager();
        Assets.loadFont();
        Assets.loadBackground();
        Assets.loadSkin();
        Assets.loadExplosions();
        Assets.loadAsteroids();
        Assets.loadUiButtons();
        Assets.loadPowerUps();
        Assets.loadShips();
        Assets.loadBullets();
        Assets.manager.finishLoading();
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        Assets.dispose();
    }
}
