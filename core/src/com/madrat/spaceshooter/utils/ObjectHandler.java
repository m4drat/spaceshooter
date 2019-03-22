package com.madrat.spaceshooter.utils;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.madrat.spaceshooter.MainGame;

import java.util.Random;

public class ObjectHandler {

    public int speed;
    public int x, y;
    public Sprite sprite;
    public int preferredHeight, preferredWidth;

    public ObjectHandler(Sprite sprite, int height, int width, int speed) {
        Random ran = new Random();
        this.x = ran.nextInt(MainGame.GENERAL_WIDTH - width + 2);
        this.y = ran.nextInt(MainGame.GENERAL_HEIGHT + 2);
        this.speed = speed;
        this.sprite = sprite;
        this.preferredHeight = height;
        this.preferredWidth = width;
    }

    public void dispose() {
        this.sprite.getTexture().dispose();
    }
}
