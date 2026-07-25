package io.github.some_example_name.model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class SpellModel {
    public enum SpellType { VENGEFUL_SPIRIT, HOWLING_WRAITHS }
    public SpellType type;
    public Rectangle bounds;
    public float velocityX;
    public boolean facingRight;
    public float stateTimer = 0f;

    public int ticksDone = 0;
    public float tickTimer = 0f;
    public Array<EnemyModel> hitEnemies = new Array<>();

    public SpellModel(SpellType type, float x, float y, boolean facingRight) {
        this.type = type;
        this.facingRight = facingRight;
        if (type == SpellType.VENGEFUL_SPIRIT) {
            this.bounds = new Rectangle(x, y, 120, 80);
            this.velocityX = facingRight ? 1200f : -1200f;
        } else if (type == SpellType.HOWLING_WRAITHS) {
            this.bounds = new Rectangle(x - 60, y, 180, 250);
            this.velocityX = 0;
        }
    }
}
