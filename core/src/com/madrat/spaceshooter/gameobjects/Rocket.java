package com.madrat.spaceshooter.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

public class Rocket extends Bullet {

    private ArrayList<Explosion> explosions;

    public Rocket(Texture bulletTexture, float bulletSpeed, float x, float y, int preferredWidth, int preferredHeight, String colliderTag) {
        super(bulletTexture, bulletSpeed, x, y, preferredWidth, preferredHeight, colliderTag);
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
