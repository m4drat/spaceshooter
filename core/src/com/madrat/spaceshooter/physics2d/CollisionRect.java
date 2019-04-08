package com.madrat.spaceshooter.physics2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class CollisionRect {

    public enum colliderTag {
        player,
        enemy,
        healPowerUp,
        ammoPowerUp,
        shieldPowerUp,
        enemybullet,
        none
    }

    private colliderTag tag;

    private float x, y;
    private int width, height;

    private ShapeRenderer shapeRenderer;

    public CollisionRect(float x, float y, int width, int height, colliderTag tag) {
        this.shapeRenderer = new ShapeRenderer();

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.tag = tag;
    }

    public void move(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public boolean collidesWith(CollisionRect rect) { // rect - rect
        return x < rect.x + rect.width && y < rect.y + rect.height && x + width > rect.x && y + height > rect.y;
    }

    public boolean collidesWith(CollisionCircle circle) { // rect - circle
        float DeltaX = circle.getX() - Math.max(this.x, Math.min(circle.getX(), this.x + this.width));
        float DeltaY = circle.getY() - Math.max(this.y, Math.min(circle.getY(), this.y + this.height));
        return (DeltaX * DeltaX + DeltaY * DeltaY) < (circle.getRadius() * circle.getRadius());
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public colliderTag getTag() {
        return tag;
    }

    public void setTag(colliderTag tag) {
        this.tag = tag;
    }

    public void drawCollider(SpriteBatch batch) {
        batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(this.x, this.y, this.width, this.height);
        shapeRenderer.end();
        batch.begin();
    }
}
