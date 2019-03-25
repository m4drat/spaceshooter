package com.madrat.spaceshooter.gameobjects.poolobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Pool;
import com.madrat.spaceshooter.physics2d.CollisionRect;
import com.madrat.spaceshooter.utils.Assets;

import static com.madrat.spaceshooter.MainGame.SCALE_X;
import static com.madrat.spaceshooter.MainGame.SCALE_Y;

public class Bullet implements Pool.Poolable {
    private float bulletSpeed;

    private Texture bulletTexture;

    private int preferredWidth;
    private int preferredHeight;

    float x, y;

    private CollisionRect rect;

    public boolean remove;

    @Override
    public void reset() {
        // Called when bullet is freed
        this.remove = false;
        // System.out.println("[+] Resetting bullet");
    }

    public Bullet() {
        this.rect = new CollisionRect(0, y, preferredWidth, preferredHeight, "none");
        this.bulletTexture = Assets.manager.get(Assets.bullet1, Texture.class);
        remove = false;
    }

    public void setupBullet(float bulletSpeed, float x, float y, int preferredWidth, int preferredHeight, String colliderTag) {
        this.bulletSpeed = bulletSpeed;
        this.x = x;
        this.y = y;
        this.preferredWidth = (int) (preferredWidth * SCALE_X);
        this.preferredHeight = (int) (preferredHeight * SCALE_Y);

        this.rect.move(x, y);
        this.rect.resize(preferredWidth, preferredHeight);
        this.rect.setColliderTag(colliderTag);
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
