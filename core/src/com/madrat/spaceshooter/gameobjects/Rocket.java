package com.madrat.spaceshooter.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static com.madrat.spaceshooter.MainGame.SCALE_X;
import static com.madrat.spaceshooter.MainGame.SCALE_Y;

public class Rocket extends Bullet {
    public Rocket(Texture bulletTexture, float bulletSpeed, float x, float y, int preferredWidth, int preferredHeight, String colliderTag) {
        super(bulletTexture, bulletSpeed, x, y, (int) (preferredWidth * SCALE_X), (int) (preferredHeight * SCALE_Y), colliderTag);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
    }

    @Override
    public CollisionRect getCollisionRect() {
        return super.getCollisionRect();
    }

    @Override
    public Texture getBulletTexture() {
        return super.getBulletTexture();
    }
}
