package io.github.some_example_name.view.game;

import com.badlogic.gdx.math.Rectangle;

public class SolidBlock {
    public Rectangle bounds;
    public boolean isDeadly;
    public boolean isActive = true;

    public SolidBlock(float x, float y, float width, float height, boolean isDeadly) {
        this.bounds = new Rectangle(x, y, width, height);
        this.isDeadly = isDeadly;
    }
}
