package com.madrat.spaceshooter.gameobjects;

public class CollisionRect {

    private float x, y;
    private int width, height;
    private String colliderTag;

    public CollisionRect(float x, float y, int width, int height, String colliderTag) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.colliderTag = colliderTag;
    }

    public void move(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public boolean collidesWith(CollisionRect rect) {
        return x < rect.x + rect.width && y < rect.y + rect.height && x + width > rect.x && y + height > rect.y;
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

    public String getColliderTag() {
        return this.colliderTag;
    }
}
