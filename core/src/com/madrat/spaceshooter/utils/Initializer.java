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

        if (BuildConfig.DEBUG) {
            System.out.println("[+] Local storage path: " + MainGame.localStoragePath);
            System.out.println("[+] Path to ship configs: " + MainGame.pathToShipConfigs);
            System.out.println("[+] Path to default parameters: " + MainGame.pathToDefaultParameters);
            System.out.println("[+] Path to current state: " + MainGame.pathToCurrentState);
        }

        if (data.getBoolean("firstRun", true)
                || !(Gdx.files.local(MainGame.pathToShipConfigs).exists() || Gdx.files.absolute(MainGame.pathToShipConfigs).exists())
                || !(Gdx.files.local(MainGame.pathToDefaultParameters).exists() || Gdx.files.absolute(MainGame.pathToDefaultParameters).exists())
                || !(Gdx.files.local(MainGame.pathToCurrentState).exists() || Gdx.files.absolute(MainGame.pathToCurrentState).exists())) {

            if (BuildConfig.DEBUG) {
                System.out.println("[+] Creating new state files...");
                System.out.println("Gdx.files.local(MainGame.pathToShipConfigs).exists(): " + Gdx.files.local(MainGame.pathToShipConfigs).exists());
                System.out.println("Gdx.files.absolute(MainGame.pathToShipConfigs).exists(): " + Gdx.files.absolute(MainGame.pathToShipConfigs).exists());
                System.out.println("Gdx.files.local(MainGame.pathToDefaultParameters).exists(): " + Gdx.files.local(MainGame.pathToDefaultParameters).exists());
                System.out.println("Gdx.files.absolute(MainGame.pathToDefaultParameters).exists(): " + Gdx.files.absolute(MainGame.pathToDefaultParameters).exists());
                System.out.println("Gdx.files.local(MainGame.pathToCurrentState).exists(): " + Gdx.files.local(MainGame.pathToCurrentState).exists());
                System.out.println("Gdx.files.absolute(MainGame.pathToCurrentState).exists(): " + Gdx.files.absolute(MainGame.pathToCurrentState).exists());
            }

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
            // handle.writeString(builder.toJson(ships), false);
            if (BuildConfig.DEBUG) {
                System.out.println("initShipConfigsJsonDump:\n" + builder.toJson(ships));
            }
            handle.writeString(MainGame.cryptor.encrypt(builder.toJson(ships)), false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void initDefaultParameters(String path) {
        FileHandle handle;

        Gson builder = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        JsonParser parser = new JsonParser();

        ParametersHandler zapper = new ParametersHandler();
        JsonObject defaultJson = new JsonObject(); // Whole object
        JsonObject defaultShip; // Default ship
        JsonObject defaultStats; // Default stats data

        try {
            defaultStats = parser.parse("{\n" +
                    "    \"totalKilledEnemies\": 0,\n" +
                    "    \"DestroyedAsteroids\": 0,\n" +
                    "    \"totalEarnedMoneys\": 0,\n" +
                    "    \"totalDeaths\": 0,\n" +
                    "    \"healPickedUp\": 0,\n" +
                    "    \"ammoPickedUp\": 0,\n" +
                    "    \"shieldPickedUp\": 0,\n" +
                    "    \"money\": 750,\n" +
                    "    \"highscore\": 0\n" +
                    "}").getAsJsonObject();

            // SetUp default values and create json object
            zapper.setUpDefaultShip();
            defaultShip = builder.toJsonTree(zapper).getAsJsonObject();

            // Add already created object to parent Json object
            defaultJson.add("currentShip", defaultShip);

            // Add stats json object to parent
            defaultJson.add("stats", defaultStats);
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
            if (BuildConfig.DEBUG) {
                System.out.println("DefaultParametersJsonDump:\n" + builder.toJson(defaultJson));
            }
            // handle.writeString(builder.toJson(defaultJson), false);
            handle.writeString(MainGame.cryptor.encrypt(builder.toJson(defaultJson)), false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void initCurrentState(String path) {
        FileHandle handle;

        Gson builder = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        JsonParser parser = new JsonParser();

        ParametersHandler zapper = new ParametersHandler();
        JsonObject currentStateJson = new JsonObject(); // Whole object
        JsonObject currentShipJson; // Default ship
        JsonObject defaultStats; // Default stats data

        try {
            defaultStats = parser.parse("{\n" +
                    "    \"totalKilledEnemies\": 0,\n" +
                    "    \"DestroyedAsteroids\": 0,\n" +
                    "    \"totalEarnedMoneys\": 0,\n" +
                    "    \"totalDeaths\": 0,\n" +
                    "    \"healPickedUp\": 0,\n" +
                    "    \"ammoPickedUp\": 0,\n" +
                    "    \"shieldPickedUp\": 0,\n" +
                    "    \"money\": 750,\n" +
                    "    \"highscore\": 0\n" +
                    "}").getAsJsonObject();

            zapper.setUpDefaultShip();
            currentShipJson = builder.toJsonTree(zapper).getAsJsonObject();
            currentStateJson.add("currentShip", currentShipJson);

            // Add stats json object to parent
            currentStateJson.add("stats", defaultStats);
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
            if (BuildConfig.DEBUG) {
                System.out.println("initCurrentStateJsonDump:\n" + builder.toJson(currentStateJson));
            }
            handle.writeString(MainGame.cryptor.encrypt(builder.toJson(currentStateJson)), false);
            // handle.writeString(builder.toJson(currentStateJson), false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
