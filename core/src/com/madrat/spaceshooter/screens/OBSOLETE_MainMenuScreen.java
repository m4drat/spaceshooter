package com.madrat.spaceshooter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.madrat.spaceshooter.MainGame;
import com.madrat.spaceshooter.utils.Assets;
import com.madrat.spaceshooter.utils.OBSOLETE_Button;

public class OBSOLETE_MainMenuScreen implements Screen {

    MainGame game;
    private SpriteBatch batch;

    // Play OBSOLETE_Button
    private static final int PLAY_BTN_WIDTH = 320;
    private static final int PLAY_BTN_HEIGHT = 90;
    private static final int PLAY_BTN_X = MainGame.GENERAL_WIDTH / 2 - PLAY_BTN_WIDTH / 2;
    private static final int PLAY_BTN_Y = 470;
    Texture playBtnActive;
    Texture playBtnInactive;
    OBSOLETE_Button play;

    // Shop OBSOLETE_Button
    private static final int SHOP_BTN_WIDTH = 220;
    private static final int SHOP_BTN_HEIGHT = 60;
    private static final int SHOP_BTN_X = MainGame.GENERAL_WIDTH / 2 - SHOP_BTN_WIDTH / 2;
    private static final int SHOP_BTN_Y = 350;
    Texture shopBtnActive;
    Texture shopBtnInactive;
    OBSOLETE_Button shop;

    // Settings OBSOLETE_Button
    private static final int SETTINGS_BTN_WIDTH = 270;
    private static final int SETTINGS_BTN_HEIGHT = 45;
    private static final int SETTINGS_BTN_X = MainGame.GENERAL_WIDTH / 2 - SETTINGS_BTN_WIDTH / 2;
    private static final int SETTINGS_BTN_Y = 250;
    Texture settingsBtnActive;
    Texture settingsBtnInactive;
    OBSOLETE_Button settings;

    // Exit OBSOLETE_Button
    private static final int EXIT_BTN_WIDTH = 190;
    private static final int EXIT_BTN_HEIGHT = 50;
    private static final int EXIT_BTN_X = MainGame.GENERAL_WIDTH / 2 - EXIT_BTN_WIDTH / 2;
    private static final int EXIT_BTN_Y = 135;
    Texture exitBtnActive;
    Texture exitBtnInactive;
    OBSOLETE_Button exit;

    public OBSOLETE_MainMenuScreen(MainGame game) {
        batch = new SpriteBatch();
        this.game = game;

        // Initialize play button
        playBtnActive = new Texture(Assets.playBtnPressed);
        playBtnInactive = new Texture(Assets.playBtn);
        play = new OBSOLETE_Button(playBtnActive, playBtnInactive, PLAY_BTN_X, PLAY_BTN_Y, PLAY_BTN_WIDTH, PLAY_BTN_HEIGHT);

        // Initialize shop button
        shopBtnActive = new Texture(Assets.shopBtnPressed);
        shopBtnInactive = new Texture(Assets.shopBtn);
        shop = new OBSOLETE_Button(shopBtnActive, shopBtnInactive, SHOP_BTN_X, SHOP_BTN_Y, SHOP_BTN_WIDTH, SHOP_BTN_HEIGHT);

        // Initialize settings button
        settingsBtnActive = new Texture(Assets.settBtnPressed);
        settingsBtnInactive = new Texture(Assets.settBtn);
        settings = new OBSOLETE_Button(settingsBtnActive, settingsBtnInactive, SETTINGS_BTN_X, SETTINGS_BTN_Y, SETTINGS_BTN_WIDTH, SETTINGS_BTN_HEIGHT);

        // Initialize exit button
        exitBtnActive = new Texture(Assets.exitBtnPressed);
        exitBtnInactive = new Texture(Assets.exitBtn);
        exit = new OBSOLETE_Button(exitBtnActive, exitBtnInactive, EXIT_BTN_X, EXIT_BTN_Y, EXIT_BTN_WIDTH, EXIT_BTN_HEIGHT);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (play.isPressed()) {
            this.dispose();
            game.setScreen(new MainGameScreen(game));
        }
        if (shop.isPressed())
            System.out.println("Shop");
        if (settings.isPressed())
            System.out.println("Settings");
        if (exit.isPressed() || exit.isPressedBefore()) {
            // Gdx.app.exit();
        }

        batch.begin();

        play.draw(batch);
        shop.draw(batch);
        settings.draw(batch);
        exit.draw(batch);

        batch.end();
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
        playBtnActive.dispose();
        playBtnInactive.dispose();

        shopBtnActive.dispose();
        shopBtnInactive.dispose();

        settingsBtnActive.dispose();
        settingsBtnInactive.dispose();

        exitBtnActive.dispose();
        exitBtnInactive.dispose();
    }
}
