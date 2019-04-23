package com.madrat.spaceshooter.gameobjects.poolobjects;

import com.badlogic.gdx.utils.Pool;

public class AsteroidPool extends Pool<Asteroid> {

    private int radius, realWidth, realHeight;
    private float animationSpeed;

    // Default values fro asteroids in Pool
    public AsteroidPool(float animationSpeed, int radius, int realWidth, int realHeight) {
        super();

        this.animationSpeed = animationSpeed;

        this.radius = radius;
        this.realWidth = realWidth;
        this.realHeight = realHeight;
    }

    // Default object which created in pool
    @Override
    protected Asteroid newObject() {
        return new Asteroid(animationSpeed, radius, realWidth, realHeight);
    }
}
