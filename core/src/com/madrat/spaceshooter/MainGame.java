package com.madrat.spaceshooter;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.madrat.spaceshooter.screens.MainMenuScreen;
import com.madrat.spaceshooter.utils.Assets;

public class MainGame extends Game {

    public static Application.ApplicationType applicationType;
    public static int GENERAL_WIDTH = 480;
    public static int GENERAL_HEIGHT = 720;
    public static float SCALE_FACTOR;
    public static float SCALE_X;
    public static float SCALE_Y;


    @Override
    public void create() {
        // System info
        applicationType = Gdx.app.getType();
        GENERAL_WIDTH = Gdx.graphics.getWidth();
        GENERAL_HEIGHT = Gdx.graphics.getHeight();

        SCALE_FACTOR = (100 / (720 * 100 / (float) Math.max(GENERAL_HEIGHT, GENERAL_WIDTH)) + 100 / (480 * 100 / (float) Math.min(GENERAL_HEIGHT, GENERAL_WIDTH))) / 2;
        SCALE_X = 100 / (480 * 100 / (float) GENERAL_WIDTH);
        SCALE_Y = 100 / (720 * 100 / (float) GENERAL_HEIGHT);

        // System.out.println("Scale Factor: " + SCALE_FACTOR);
        // System.out.println("Scale FactorX: " + SCALE_X);
        // System.out.println("Scale FactorY: " + SCALE_Y);

        Preferences data = Gdx.app.getPreferences("spacegame");

        if (data.getBoolean("firstRun", true)) {
            // First run variable
            data.putBoolean("firstRun", false);

            // Default money
            data.putInteger("money", 1000);

            // Default spaceship
            data.putString("animationTexture", Assets.ship1Animation);
            data.putFloat("animationSpeed", 0.14f);
            data.putFloat("maxHealth", 1f);
            data.putFloat("damage", 0.1f);
            data.putFloat("delayBetweenShootsBullets", 0.3f);
            data.putFloat("bulletsSpeed", 600f);
            data.putFloat("speed", 300f);
            data.putFloat("frameLength", 0.14f);
            data.putString("handle", "Zapper");

            // Ship sizes
            data.putInteger("realShipWidth", 24);
            data.putInteger("realShipHeight", 23);
            data.putInteger("preferredShipWidth", 60);
            data.putInteger("preferredShipHeight", 50);
            data.putInteger("colliderWidth", 60);
            data.putInteger("colliderHeight", 50);
            data.putInteger("colliderXcoordOffset", 0);
            data.putInteger("colliderYcoordOffset", 0);

            // Ship default healing value
            data.putFloat("maxHealing", 0.2f);

            // Ship Bullets
            data.putInteger("preferredBulletHeight", 10);
            data.putInteger("preferredBulletWidth", 4);
            data.putString("bulletTexture", Assets.bullet1);

            data.flush();
        }

        this.setScreen(new MainMenuScreen(this));
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
