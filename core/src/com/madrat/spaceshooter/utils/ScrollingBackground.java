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

    public static ArrayList<ObjectHandler> initStarBackground() {
        Random ran = new Random();
        ArrayList<ObjectHandler> sprites = new ArrayList<ObjectHandler>();

        for (int i = 0; i < 25; ++i) {
            size = ran.nextInt(4) + 1;
            sprites.add(new ObjectHandler(new Sprite(new Texture(Gdx.files.internal(Assets.star1))), size, size, ran.nextInt(5) + 3));
        }
        for (int i = 0; i < 25; ++i) {
            size = ran.nextInt(4) + 1;
            sprites.add(new ObjectHandler(new Sprite(new Texture(Gdx.files.internal(Assets.star2))), size, size, ran.nextInt(5) + 3));
        }
        for (int i = 0; i < 25; ++i) {
            size = ran.nextInt(4) + 1;
            sprites.add(new ObjectHandler(new Sprite(new Texture(Gdx.files.internal(Assets.star3))), size, size, ran.nextInt(5) + 3));
        }

        // Generate 3 Random not repeating int's
        Integer[] randomSizes = new Integer[3];
        for (int i = 0; i < randomSizes.length; i++) {
            randomSizes[i] = i;
        }
        // Shuffle int array
        Collections.shuffle(Arrays.asList(randomSizes));

        // Add objects
        size = ran.nextInt(100) + 60;
        sprites.add(new ObjectHandler(new Sprite(new Texture(Gdx.files.internal(Assets.earth1))), size, size, randomSizes[0] + 1));
        size = ran.nextInt(100) + 60;
        sprites.add(new ObjectHandler(new Sprite(new Texture(Gdx.files.internal(Assets.jupiter1))), size, size, randomSizes[1] + 1));
        size = ran.nextInt(100) + 60;
        sprites.add(new ObjectHandler(new Sprite(new Texture(Gdx.files.internal(Assets.mars1))), size, size, randomSizes[2] + 1));

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

                if (object.y + object.sprite.getHeight() < 0) {
                    object.y = ran.nextInt(MainGame.GENERAL_HEIGHT / 2) + MainGame.GENERAL_HEIGHT;
                    object.x = ran.nextInt(MainGame.GENERAL_WIDTH + 1 - object.preferredWidth);
                }
            }
        }
    }

    public void pause() {
        stopper = 0;
    }

    public void _continue() {
        stopper = 1;
    }

    public void dispose() {
        for (ObjectHandler object : this.miniObjects) {
            object.dispose();
        }
    }
}
