package io.github.some_example_name.model;

import com.badlogic.gdx.math.Rectangle;

public class ProjectileModel {
    public Rectangle bounds;
    public float velocityX;

    public ProjectileModel(float x, float y, float w, float h, float vx) {
        this.bounds = new Rectangle(x, y, w, h);
        this.velocityX = vx;
    }
}
