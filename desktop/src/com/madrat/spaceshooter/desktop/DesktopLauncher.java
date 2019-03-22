package com.madrat.spaceshooter.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.madrat.spaceshooter.MainGame;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.foregroundFPS = 60;
        config.width = MainGame.GENERAL_WIDTH;
        config.height = MainGame.GENERAL_HEIGHT;
        config.resizable = false;
        new LwjglApplication(new MainGame(), config);
    }
}
