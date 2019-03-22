package com.madrat.spaceshooter.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.madrat.spaceshooter.MainGame;

public class OBSOLETE_Button {

    private Texture active;
    private Texture inactive;

    private int x;
    private int y;

    private int width;
    private int height;

    private boolean visible;
    private boolean isPressed;

    public OBSOLETE_Button(Texture active, Texture inactive, int x, int y) {
        this.active = active;
        this.inactive = inactive;
        this.x = x;
        this.y = y;
        this.width = active.getWidth();
        this.height = active.getHeight();
        this.visible = true;
        this.isPressed = false;
    }

    public OBSOLETE_Button(Texture active, Texture inactive, int x, int y, int width, int height) {
        this.active = active;
        this.inactive = inactive;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.visible = true;
        this.isPressed = false;
    }

    public void move(int new_x, int new_y) {
        this.x = new_x;
        this.y = new_y;
    }

    public void resize(int new_width, int new_height) {
        this.width = new_width;
        this.height = new_height;
    }

    public void draw(SpriteBatch batch) {
        if (this.visible) {
            if (Gdx.input.getX() < this.x + this.width && Gdx.input.getX() > this.x && MainGame.GENERAL_HEIGHT - Gdx.input.getY() < this.y + this.height && MainGame.GENERAL_HEIGHT - Gdx.input.getY() > this.y) {
                batch.draw(this.active, this.x, this.y, this.width, this.height);
            } else {
                batch.draw(this.inactive, this.x, this.y, this.width, this.height);
            }
        }
    }

    public int get_x() {
        return this.x;
    }

    public int get_y() {
        return this.y;
    }

    public int get_width() {
        return this.width;
    }

    public int get_height() {
        return this.height;
    }

    public boolean isPressed() {
        if (this.visible) {
            if (Gdx.input.isTouched() && (Gdx.input.getX() < this.x + this.width && Gdx.input.getX() > this.x && MainGame.GENERAL_HEIGHT - Gdx.input.getY() < this.y + this.height && MainGame.GENERAL_HEIGHT - Gdx.input.getY() > this.y)) {
                isPressed = true;
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public void setVisible(boolean flag) {
        this.visible = flag;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public boolean isPressedBefore() {
        return this.isPressed;
    }
}
