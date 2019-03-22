package com.madrat.spaceshooter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.madrat.spaceshooter.MainGame;
import com.madrat.spaceshooter.gameobjects.Bullet;
import com.madrat.spaceshooter.gameobjects.PlayerShip;
import com.madrat.spaceshooter.utils.Assets;
import com.madrat.spaceshooter.utils.ObjectHandler;
import com.madrat.spaceshooter.utils.ScrollingBackground;

import java.util.ArrayList;

public class MainGameScreen implements Screen {

    MainGame game;
    PlayerShip playerShip;

    private boolean isRunning;
    private Stage stage;
    private Sprite background;
    private SpriteBatch batch;
    private ScrollingBackground scrollingBackground;
    private ArrayList<ObjectHandler> sprites;

    public MainGameScreen(MainGame newgame) {
        playerShip = new PlayerShip(new Texture(Assets.ship1Animation), 3, 5, 1, 0.18f, 600f, 300f, "Zapper");

        batch = new SpriteBatch();
        Gdx.input.setInputProcessor(stage);

        background = new Sprite(new Texture(Gdx.files.internal(Assets.backgroundSpace)));
        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        sprites = ScrollingBackground.initStarBackground();
        scrollingBackground = new ScrollingBackground(background, sprites);

        isRunning = true;

        playerShip.setNeedToShow(true);
    }

    @Override
    public void show() { // Screen first appears
    }

    @Override
    public void render(float delta) { // gets called at every new frame
        // Moving
        if (Gdx.input.isKeyPressed(Input.Keys.UP))
            playerShip.setY(playerShip.getY() + playerShip.getSpeed() * Gdx.graphics.getDeltaTime());
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
            playerShip.setY(playerShip.getY() - playerShip.getSpeed() * Gdx.graphics.getDeltaTime());
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            playerShip.setX(playerShip.getX() - playerShip.getSpeed() * Gdx.graphics.getDeltaTime());
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            playerShip.setX(playerShip.getX() + playerShip.getSpeed() * Gdx.graphics.getDeltaTime());

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        scrollingBackground.draw(batch);

        // Player Handlers
        playerShip.incStateTime(delta);
        // check for bounds
        playerShip.correctBounds();
        // Draw ship with animations bullets etc
        playerShip.draw(batch, delta);

        // batch.draw(ship.getShipTexture(), ship.getX(), ship.getY(), 60, 50);
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
    public void dispose() { // get rid of the screen
        batch.dispose();
        background.getTexture().dispose();
        scrollingBackground.dispose();
    }
}
