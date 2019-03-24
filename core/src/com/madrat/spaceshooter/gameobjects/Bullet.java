package com.madrat.spaceshooter.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static com.madrat.spaceshooter.MainGame.SCALE_X;
import static com.madrat.spaceshooter.MainGame.SCALE_Y;

public class Bullet {
    private float bulletSpeed;
    private Texture bulletTexture;

    private int preferredWidth;
    private int preferredHeight;

    float x, y;

    CollisionRect rect;

    public boolean remove = false;

    public Bullet(Texture bulletTexture, float bulletSpeed, float x, float y, int preferredWidth, int preferredHeight, String colliderTag) {
        this.bulletSpeed = bulletSpeed;
        this.x = x;
        this.y = y;
        this.preferredWidth = (int) (preferredWidth * SCALE_X);
        this.preferredHeight = (int) (preferredHeight * SCALE_Y);

        this.rect = new CollisionRect(x, y, preferredWidth, preferredHeight, colliderTag);
        this.bulletTexture = bulletTexture;
    }

    public void update(float deltaTime) {
        y += bulletSpeed * deltaTime;

        if (y > Gdx.graphics.getHeight())
            remove = true;

        rect.move(x, y);
    }

    public void render(SpriteBatch batch) {
        batch.draw(this.bulletTexture, x, y, preferredWidth, preferredHeight);
    }

    public CollisionRect getCollisionRect() {
        return rect;
    }

    public Texture getBulletTexture() {
        return bulletTexture;
    }

    public void dispose() {
        // this.bulletTexture.dispose();
    }
}
