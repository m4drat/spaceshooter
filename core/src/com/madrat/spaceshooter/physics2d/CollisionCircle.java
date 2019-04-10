package com.madrat.spaceshooter.physics2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.madrat.spaceshooter.utils.BuildConfig;

public class CollisionCircle {

    private CollisionRect.colliderTag tag;

    private float x, y;
    private int radius;

    private static ShapeRenderer shapeRenderer;

    public CollisionCircle(float x, float y, int radius, CollisionRect.colliderTag tag) {
        if (BuildConfig.DEBUG) {
            if (this.shapeRenderer == null) {
                System.out.println("[+] Creating ShapeRenderer");
                this.shapeRenderer = new ShapeRenderer();
            }
        }

        this.x = x;
        this.y = y;
        this.radius = radius;
        this.tag = tag;
    }

    public void move(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void resize(int radius) {
        this.radius = radius;
    }

    // Check for collision between circle and rectangle
    public boolean collidesWith(CollisionRect rect) { // circle - rect
        float DeltaX = this.x - Math.max(rect.getX(), Math.min(this.x, rect.getX() + rect.getWidth()));
        float DeltaY = this.y - Math.max(rect.getY(), Math.min(this.y, rect.getY() + rect.getHeight()));
        return (DeltaX * DeltaX + DeltaY * DeltaY) < (this.radius * this.radius);
    }

    // Check for collision between circle and circle
    public boolean collidesWith(CollisionCircle circle) { // circle - circle
        return Math.pow(circle.getX() - this.x, 2) + Math.pow(this.y - circle.getY(), 2) <= Math.pow(this.radius + circle.getRadius(), 2);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getRadius() {
        return radius;
    }

    public CollisionRect.colliderTag getTag() {
        return tag;
    }

    public void setTag(CollisionRect.colliderTag tag) {
        this.tag = tag;
    }

    // Draw collider
    public void drawCollider(SpriteBatch batch) {
        batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.circle(this.x, this.y, this.radius);
        shapeRenderer.end();
        batch.begin();
    }
}
