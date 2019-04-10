package com.madrat.spaceshooter.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.TimeUtils;

import static com.madrat.spaceshooter.MainGame.SCALE_FACTOR;

/**
 * A nicer class for showing framerate that doesn't spam the console
 * like Logger.log()
 *
 * @author William Hartman.
 * <p>
 * Improved by @madrat [additionally shows memory stats]
 */

public class DebugUtils implements Disposable {

    long lastTimeCounted;
    private float sinceChange;
    private float frameRate;
    private BitmapFont font;
    private SpriteBatch batch;
    private OrthographicCamera cam;

    public DebugUtils() {
        lastTimeCounted = TimeUtils.millis();
        sinceChange = 0;
        frameRate = Gdx.graphics.getFramesPerSecond();
        font = new BitmapFont();
        font.getData().setScale(SCALE_FACTOR);
        batch = new SpriteBatch();
        cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void resize(int screenWidth, int screenHeight) {
        cam = new OrthographicCamera(screenWidth, screenHeight);
        cam.translate(screenWidth / 2, screenHeight / 2);
        cam.update();
        batch.setProjectionMatrix(cam.combined);
    }

    public void update() {
        long delta = TimeUtils.timeSinceMillis(lastTimeCounted);
        lastTimeCounted = TimeUtils.millis();

        sinceChange += delta;
        if (sinceChange >= 1000) {
            sinceChange = 0;
            frameRate = Gdx.graphics.getFramesPerSecond();
        }
    }

    public void render() {
        batch.begin();
        font.draw(batch, (int) frameRate + " fps", 3, Gdx.graphics.getHeight() - 3 * SCALE_FACTOR);
        font.draw(batch, "Java Heap: " + (float) Gdx.app.getJavaHeap() / 1024 / 1024 + " mb", 3, Gdx.graphics.getHeight() - 20 * SCALE_FACTOR);
        font.draw(batch, "Native Heap: " + (float) Gdx.app.getNativeHeap() / 1024 / 1024 + " mb", 3, Gdx.graphics.getHeight() - 40 * SCALE_FACTOR);
        batch.end();
    }

    public void dispose() {
        font.dispose();
        batch.dispose();
    }
}