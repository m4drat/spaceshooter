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
import com.madrat.spaceshooter.gameobjects.SpaceShip;
import com.madrat.spaceshooter.utils.Assets;
import com.madrat.spaceshooter.utils.ObjectHandler;
import com.madrat.spaceshooter.utils.ScrollingBackground;

import java.util.ArrayList;

public class MainGameScreen implements Screen {

    MainGame game;
    SpaceShip ship;

    private Stage stage;
    private Sprite background;
    private SpriteBatch batch;
    private ScrollingBackground scrollingBackground;
    private ArrayList<ObjectHandler> sprites;

    public MainGameScreen(MainGame newgame) {
        ship = new SpaceShip(new Texture(Assets.ship1), 3, 5, 1, 10f, 260f, "Zapper");

        batch = new SpriteBatch();
        Gdx.input.setInputProcessor(stage);

        background = new Sprite(new Texture(Gdx.files.internal(Assets.backgroundSpace)));
        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        sprites = ScrollingBackground.initStarBackground();
        scrollingBackground = new ScrollingBackground(background, sprites);
    }

    @Override
    public void show() { // Screen first appears
    }

    @Override
    public void render(float delta) { // gets called at every new frame
        if (Gdx.input.isKeyPressed(Input.Keys.UP))
            ship.setY(ship.getY() + ship.getSpeed() * Gdx.graphics.getDeltaTime());
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
            ship.setY(ship.getY() - ship.getSpeed() * Gdx.graphics.getDeltaTime());
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            ship.setX(ship.getX() - ship.getSpeed() * Gdx.graphics.getDeltaTime());
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            ship.setX(ship.getX() + ship.getSpeed() * Gdx.graphics.getDeltaTime());

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        scrollingBackground.draw(batch);
        ship.incStateTime(delta);

        // Draw ship with animations
        batch.draw(ship.getEngines()[ship.getFrame()].getKeyFrame(ship.getStateTime(), true), ship.getX(), ship.getY(), ship.preffered_SHIP_WIDTH, ship.preffered_SHIP_HEIGHT);

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
        ship.dispose();
        background.getTexture().dispose();
        scrollingBackground.dispose();
    }
}
