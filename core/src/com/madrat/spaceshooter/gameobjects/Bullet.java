package com.madrat.spaceshooter.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.madrat.spaceshooter.utils.Assets;

public class Bullet {

    private float bulletSpeed;
    private static Texture bulletTexture;

    float x, y;

    public boolean remove = false, byPlayer = true;

    public Bullet(float bulletSpeed, float x, float y, boolean byPlayer) {
        this.bulletSpeed = bulletSpeed;
        this.x = x;
        this.y = y;

        this.byPlayer = byPlayer;

        if (bulletTexture == null)
            bulletTexture = new Texture(Assets.bullet1);
    }

    public void update(float deltaTime) {
        y += bulletSpeed * deltaTime;
        if (y > Gdx.graphics.getHeight())
            remove = true;
    }

    public void render(SpriteBatch batch) {
        batch.draw(bulletTexture, x, y, 4, 10);
    }
}
