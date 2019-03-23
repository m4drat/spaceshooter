package com.madrat.spaceshooter.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.madrat.spaceshooter.utils.Assets;

public class Bullet {

    public static final int WIDTH = 4;
    public static final int HEIGHT = 10;

    private float bulletSpeed;
    private static Texture bulletTexture;

    float x, y;

    CollisionRect rect;

    public boolean remove = false;

    public Bullet(float bulletSpeed, float x, float y, String colliderTag) {
        this.bulletSpeed = bulletSpeed;
        this.x = x;
        this.y = y;

        this.rect = new CollisionRect(x, y, WIDTH, HEIGHT, colliderTag);

        if (bulletTexture == null)
            bulletTexture = new Texture(Assets.bullet1);
    }

    public void update(float deltaTime) {
        y += bulletSpeed * deltaTime;

        if (y > Gdx.graphics.getHeight())
            remove = true;

        rect.move(x, y);
    }

    public void render(SpriteBatch batch) {
        batch.draw(bulletTexture, x, y, WIDTH, HEIGHT);
    }

    public CollisionRect getCollisionRect() {
        return rect;
    }

    public static Texture getBulletTexture() {
        return bulletTexture;
    }
}
