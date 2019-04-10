package com.madrat.spaceshooter.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.utils.Collision;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.madrat.spaceshooter.MainGame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.madrat.spaceshooter.MainGame.SCALE_FACTOR;

public class ScrollingBackground {

    private ArrayList<ObjectHandler> miniObjects;
    private Sprite baseBackground;
    private Random ran;
    private static int size;

    private int stopper;

    public ScrollingBackground(Sprite background, List<ObjectHandler> objects) {
        ran = new Random();

        this.miniObjects = new ArrayList<ObjectHandler>();
        for (ObjectHandler object : objects) {
            this.miniObjects.add(object);
        }
        this.baseBackground = background;

        stopper = 1;
    }

    // Create array of objects (stars, planets)
    public static ArrayList<ObjectHandler> initStarBackground() {
        Random ran = new Random();
        ArrayList<ObjectHandler> sprites = new ArrayList<ObjectHandler>();

        for (int i = 0; i < 50; ++i) {
            size = ran.nextInt(3) + 1;
            sprites.add(new ObjectHandler(new Sprite(Assets.manager.get(Assets.star1, Texture.class)), (int) (size * SCALE_FACTOR), (int) (size * SCALE_FACTOR), (int) (ran.nextInt(5) + 3 * SCALE_FACTOR)));
        }
        for (int i = 0; i < 25; ++i) {
            size = ran.nextInt(4) + 1;
            sprites.add(new ObjectHandler(new Sprite(Assets.manager.get(Assets.star2, Texture.class)), (int) (size * SCALE_FACTOR), (int) (size * SCALE_FACTOR), (int) (ran.nextInt(5) + 3 * SCALE_FACTOR)));
        }
        for (int i = 0; i < 25; ++i) {
            size = ran.nextInt(4) + 1;
            sprites.add(new ObjectHandler(new Sprite(Assets.manager.get(Assets.star3, Texture.class)), (int) (size * SCALE_FACTOR), (int) (size * SCALE_FACTOR), (int) (ran.nextInt(5) + 3 * SCALE_FACTOR)));
        }

        // Generate 3 Random not repeating int's
        Integer[] randomSizes = new Integer[3];
        for (int i = 0; i < randomSizes.length; i++) {
            randomSizes[i] = i;
        }
        // Shuffle int array
        Collections.shuffle(Arrays.asList(randomSizes));

        // Add objects
        size = (int) ((ran.nextInt(140) + 80) * SCALE_FACTOR);
        sprites.add(new ObjectHandler(new Sprite(Assets.manager.get(Assets.earth1, Texture.class)), size, size, (int) ((randomSizes[0] + 1) * SCALE_FACTOR)));
        size = (int) ((ran.nextInt(140) + 80) * SCALE_FACTOR);
        sprites.add(new ObjectHandler(new Sprite(Assets.manager.get(Assets.jupiter1, Texture.class)), size, size, (int) ((randomSizes[1] + 1) * SCALE_FACTOR)));
        size = (int) ((ran.nextInt(140) + 80) * SCALE_FACTOR);
        sprites.add(new ObjectHandler(new Sprite(Assets.manager.get(Assets.mars1, Texture.class)), size, size, (int) ((randomSizes[2] + 1) * SCALE_FACTOR)));

        // Sort object by sizes (place bigger object in front of other)
        Collections.sort(sprites);

        return sprites;
    }

    public void draw(SpriteBatch batch) {
        baseBackground.draw(batch);
        if (this.miniObjects.size() > 0) {
            for (ObjectHandler object : this.miniObjects) {
                batch.draw(object.sprite, object.x, object.y, object.preferredWidth, object.preferredHeight);

                // stopper - will be 0 if you want to stop background
                object.y -= object.speed * stopper;

                if (object.y + object.getPreferredHeight() < 0) {
                    object.y = ran.nextInt(MainGame.GENERAL_HEIGHT / 2) + MainGame.GENERAL_HEIGHT;
                    object.x = ran.nextInt(MainGame.GENERAL_WIDTH + 1 - object.preferredWidth);
                }
            }
        }
    }

    // pause scrolling background
    public void pause() {
        stopper = 0;
    }

    // continue scrolling background
    public void _continue() {
        stopper = 1;
    }

    public void dispose() {
        for (ObjectHandler object : this.miniObjects) {
            object.dispose();
        }
        baseBackground.getTexture().dispose();
    }
}
