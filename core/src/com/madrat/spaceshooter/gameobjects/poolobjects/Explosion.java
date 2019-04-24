package com.madrat.spaceshooter.gameobjects.poolobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;
import com.madrat.spaceshooter.utils.Assets;

import static com.madrat.spaceshooter.MainGame.SCALE_FACTOR;

public class Explosion implements Pool.Poolable {

    private Texture explosion;
    private Animation<TextureRegion> animation;
    private float x, y;
    private float stateTime;

    private boolean byPlayer;
    // private int tileSize; // 96 for explosion2.png and 64 for explosion1.png
    // private float scale; // 2f for explosion1.png and 0.9f for explosion2.png

    private int preferredWidth, preferredHeight;

    public boolean remove;

    @Override
    public void reset() {
        this.remove = false;
        this.stateTime = 0;
    }

    public Explosion(float frameLength, int tileSize, String explosionTexturePath) {
        this.stateTime = 0;
        this.explosion = Assets.manager.get(explosionTexturePath, Texture.class);
        this.animation = new Animation<TextureRegion>(frameLength, TextureRegion.split(this.explosion, tileSize, tileSize)[0]);

        this.remove = false;
    }

    public void setUpExplosion(float x, float y, int preferredWidth, int preferredHeight, boolean byPlayer) {
        this.x = x;
        this.y = y;

        this.preferredWidth = (int) (preferredWidth * SCALE_FACTOR);
        this.preferredHeight = (int) (preferredHeight * SCALE_FACTOR);

        this.byPlayer = byPlayer;
    }

    public void update(float deltaTime) {
        stateTime += deltaTime;
        if (animation.isAnimationFinished(stateTime))
            remove = true;
    }

    public void render(SpriteBatch batch) {
        batch.draw(animation.getKeyFrame(stateTime), x, y, this.preferredWidth, this.preferredHeight);
    }

    public boolean isByPlayer() {
        return byPlayer;
    }

    public void dispose() {
        this.explosion.dispose();
    }
}
