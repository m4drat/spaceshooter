package com.madrat.spaceshooter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.madrat.spaceshooter.screens.MainMenuScreen;
import com.madrat.spaceshooter.utils.Assets;

public class MainGame extends Game {

    public static int GENERAL_WIDTH = 480;
    public static int GENERAL_HEIGHT = 720;

    @Override
    public void create() {

        GENERAL_WIDTH = Gdx.graphics.getWidth();
        GENERAL_HEIGHT = Gdx.graphics.getHeight();

        Preferences data = Gdx.app.getPreferences("spacegame");
        if (data.getBoolean("firstRun", true)) {
            data.putBoolean("firstRun", false);

            // Default spaceship
            data.putString("animationTexture", Assets.ship1Animation);
            data.putFloat("animationSpeed", 0.14f);
            data.putFloat("maxHealth", 1f);
            data.putFloat("damage", 0.1f);
            data.putFloat("delayBetweenShoots", 0.3f);
            data.putFloat("bulletsSpeed", 600f);
            data.putFloat("speed", 300f);
            data.putString("handle", "Zapper");

            data.putInteger("realShipWidth", 24);
            data.putInteger("realShipHeight", 23);
            data.putInteger("prefferedShipWidth", 60);
            data.putInteger("prefferedShipHeight", 50);
        }

        this.setScreen(new MainMenuScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {

    }
}
