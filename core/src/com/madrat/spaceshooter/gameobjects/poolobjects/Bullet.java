package com.madrat.spaceshooter.gameobjects.poolobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;
import com.madrat.spaceshooter.physics2d.CollisionRect;
import com.madrat.spaceshooter.utils.Assets;

import static com.madrat.spaceshooter.MainGame.SCALE_X;
import static com.madrat.spaceshooter.MainGame.SCALE_Y;

public class Bullet implements Pool.Poolable {
    private float bulletSpeed;

    private Texture bulletTexture;
    private Animation<TextureRegion> bulletAnimation;
    private float stateTime;

    String bulletType;

    private int preferredWidth;
    private int preferredHeight;

    float x, y;

    private CollisionRect rect;

    public boolean remove;

    @Override
    public void reset() {
        // Called when bullet is freed
        this.remove = false;
        this.stateTime = 0;

        this.x = 0;
        this.y = 0;
        // System.out.println("[+] Resetting bullet");
    }

    public Bullet(String bulletTexturePath, float animationSpeed, int realWidth, int realHeight, String bulletType) {
        this.rect = new CollisionRect(0, y, preferredWidth, preferredHeight, "none");

        this.bulletTexture = Assets.manager.get(bulletTexturePath, Texture.class);
        this.bulletAnimation = new Animation(animationSpeed, TextureRegion.split(bulletTexture, realWidth, realHeight)[0]);

        this.bulletType = bulletType;

        remove = false;
    }

    public void setupBullet(float bulletSpeed, float x, float y, int preferredWidth, int preferredHeight, String colliderTag) {
        this.bulletSpeed = bulletSpeed;
        this.x = x;
        this.y = y;
        this.preferredWidth = (int) (preferredWidth * SCALE_X);
        this.preferredHeight = (int) (preferredHeight * SCALE_Y);

        this.rect.move(this.x, this.y);
        this.rect.resize(preferredWidth, preferredHeight);
        this.rect.setColliderTag(colliderTag);
    }

    public void update(float deltaTime) {
        stateTime += deltaTime;

        y += bulletSpeed * deltaTime;

        if (y > Gdx.graphics.getHeight())
            remove = true;
        if (y < -preferredHeight)
            remove = true;

        rect.move(x, y);
    }

    public void render(SpriteBatch batch) {
        batch.draw(bulletAnimation.getKeyFrame(stateTime, false), this.x, this.y, preferredWidth, preferredHeight);
    }

    public CollisionRect getCollisionRect() {
        return rect;
    }

    public Texture getBulletTexture() {
        return bulletTexture;
    }

    public String getBulletType() {
        return bulletType;
    }

    public int getPreferredWidth() {
        return preferredWidth;
    }

    public int getPreferredHeight() {
        return preferredHeight;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void dispose() {
        // this.bulletTexture.dispose();
    }
}
