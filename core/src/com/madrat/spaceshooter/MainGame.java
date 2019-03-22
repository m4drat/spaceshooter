package com.madrat.spaceshooter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.madrat.spaceshooter.screens.MainMenuScreen;

public class MainGame extends Game {

    public static int GENERAL_WIDTH = 480;
    public static int GENERAL_HEIGHT = 720;

    @Override
    public void create() {
        GENERAL_WIDTH = Gdx.graphics.getWidth();
        GENERAL_HEIGHT = Gdx.graphics.getHeight();
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
