package com.madrat.spaceshooter.utils;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.madrat.spaceshooter.MainGame;

import com.google.gson.*;

public class Initializer {

    public static void init() {
        // Create preferences object
        Preferences data = Gdx.app.getPreferences("spacegame");

        // set relevant paths
        if (MainGame.applicationType == Application.ApplicationType.Android) {
            MainGame.localStoragePath = Gdx.files.getLocalStoragePath();
            MainGame.pathToShipConfigs = Assets.shipConfigs;
            MainGame.pathToDefaultParameters = Assets.defaultParameters;
            MainGame.pathToCurrentState = Assets.currentState;
        } else if (MainGame.applicationType == Application.ApplicationType.Desktop) {
            MainGame.localStoragePath = Gdx.files.getExternalStoragePath() + ".prefs\\files\\";
            MainGame.pathToShipConfigs = MainGame.localStoragePath + Assets.shipConfigs;
            MainGame.pathToDefaultParameters = MainGame.localStoragePath + Assets.defaultParameters;
            MainGame.pathToCurrentState = MainGame.localStoragePath + Assets.currentState;
        }

        System.out.println("[+] Local storage path: " + MainGame.localStoragePath);
        System.out.println("[+] Path to ship configs: " + MainGame.pathToShipConfigs);
        System.out.println("[+] Path to default parameters: " + MainGame.pathToDefaultParameters);
        System.out.println("[+] Path to current state: " + MainGame.pathToCurrentState);

        // First run check
        if (data.getBoolean("firstRun", true)) {
            // First run variable
            data.putBoolean("firstRun", false);

            // References to data files
            data.putString("shipConfigs", "shipConfigs.json");
            data.putString("defaultParameters", "defaultParameters.json");
            data.putString("currentState", "currentState.json");

            // Write changes to file
            data.flush();

            // Initialize all possible ships
            initShipConfigs(MainGame.pathToShipConfigs);

            // Set up user default parameters (base highscore, ship, money, etc)
            initDefaultParameters(MainGame.pathToDefaultParameters);

            // Set up current state (right now == defaultParameters)
            initCurrentState(MainGame.pathToCurrentState);
        }

        // initialize ship configs

    }

    public static void initShipConfigs(String path) {

        FileHandle handle;

        Gson builder = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

        JsonObject ships = new JsonObject();
        JsonElement zapperJson; // Default ship
        JsonElement destroyerJson;
        JsonElement ignitorJson;
        JsonElement turtleJson;
        JsonElement ufoJson;
        JsonElement starJson;
        JsonElement pinkyJson;

        ParametersHandler zapper = new ParametersHandler();
        ParametersHandler destroyer = new ParametersHandler();
        ParametersHandler ignitor = new ParametersHandler();
        ParametersHandler turtle = new ParametersHandler();
        ParametersHandler ufo = new ParametersHandler();
        ParametersHandler star = new ParametersHandler();
        ParametersHandler pinky = new ParametersHandler();

        zapper.setUpDefaultShip();
        destroyer.setUpDestroyer();
        ignitor.setUpIgnitor();
        turtle.setUpTurtle();
        ufo.setUpUfo();
        star.setUpStar();
        pinky.setUpPinky();

        zapperJson = builder.toJsonTree(zapper);
        destroyerJson = builder.toJsonTree(destroyer);
        ignitorJson = builder.toJsonTree(ignitor);
        turtleJson = builder.toJsonTree(turtle);
        ufoJson = builder.toJsonTree(ufo);
        starJson = builder.toJsonTree(star);
        pinkyJson = builder.toJsonTree(pinky);

        try {
            ships.add("zapper", zapperJson);
            ships.add("destroyer", destroyerJson);
            ships.add("ignitor", ignitorJson);
            ships.add("turtle", turtleJson);
            ships.add("ufo", ufoJson);
            ships.add("star", starJson);
            ships.add("pinky", pinkyJson);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (MainGame.applicationType == Application.ApplicationType.Android) {
            handle = Gdx.files.local(path);
        } else if (MainGame.applicationType == Application.ApplicationType.Desktop) {
            handle = Gdx.files.absolute(path);
        } else {
            handle = Gdx.files.local(path);
        }

        try {
            handle.writeString(builder.toJson(ships), false);
            // handle.writeString(MainGame.cryptor.encrypt(ships.toString(4)), false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void initDefaultParameters(String path) {
        FileHandle handle;

        Gson builder = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

        ParametersHandler zapper = new ParametersHandler();
        JsonObject defaultJson = new JsonObject(); // Whole object
        JsonObject defaultShip; // Default ship

        try {
            zapper.setUpDefaultShip();
            defaultShip = builder.toJsonTree(zapper).getAsJsonObject();
            defaultJson.add("currentShip", defaultShip);
            defaultJson.addProperty("money", 1000);
            defaultJson.addProperty("highscore", 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (MainGame.applicationType == Application.ApplicationType.Android) {
            handle = Gdx.files.local(path);
        } else if (MainGame.applicationType == Application.ApplicationType.Desktop) {
            handle = Gdx.files.absolute(path);
        } else {
            handle = Gdx.files.local(path);
        }

        try {
            handle.writeString(builder.toJson(defaultJson), false);
            // handle.writeString(MainGame.cryptor.encrypt(defaultJson.toString(4)), false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void initCurrentState(String path) {
        FileHandle handle;

        Gson builder = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

        // TODO place here zapper instead ufo
        ParametersHandler ufo = new ParametersHandler();
        JsonObject currentStateJson = new JsonObject(); // Whole object
        JsonObject currentShipJson; // Default ship

        try {
            ufo.setUpUfo();
            currentShipJson = builder.toJsonTree(ufo).getAsJsonObject();
            currentStateJson.add("currentShip", currentShipJson);
            currentStateJson.addProperty("money", 1000);
            currentStateJson.addProperty("highscore", 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (MainGame.applicationType == Application.ApplicationType.Android) {
            handle = Gdx.files.local(path);
        } else if (MainGame.applicationType == Application.ApplicationType.Desktop) {
            handle = Gdx.files.absolute(path);
        } else {
            handle = Gdx.files.local(path);
        }

        try {
            // handle.writeString(MainGame.cryptor.encrypt(currentStateJson.toString(4)), false);
            handle.writeString(builder.toJson(currentStateJson), false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
