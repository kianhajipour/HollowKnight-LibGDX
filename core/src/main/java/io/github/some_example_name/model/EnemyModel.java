package io.github.some_example_name.model;

import com.badlogic.gdx.math.Rectangle;

public class EnemyModel {
    public enum EnemyType { CRAWLID, MOSSCREEP, MOSQUITO, MOSSFLY, HUSK, CRYSTAL }
    public enum State { WALKING, HIT, DEAD, IDLE, ANTICIPATING, ATTACKING, HIDING, SHAKING, APPEARING, FLYING, RESTING, LUNGING, SHOOTING, ENRAGED }

    public EnemyType type;
    public State currentState;
    public State previousState;

    public Rectangle bounds;

    public float renderWidth;
    public float renderHeight;

    public float speed;
    public boolean movingRight = false;
    public boolean isFlying;

    public int hp = 3;
    public float hitTimer = 0f;
    public final float HIT_DURATION = 0.2f;
    public float knockbackSpeed = 300f;
    public boolean knockedRight = false;

    public float velocityY = 0;
    public boolean isGrounded = false;
    public final float GRAVITY = -9.8f * 120;

    public float stateTimer = 0f;
    public float targetX = 0f;
    public float targetY = 0f;
    public float dashVx = 0f;
    public float dashVy = 0f;
    public float aggroRange = 400f;

    public float animationTime = 0f;

    public EnemyModel(EnemyType type, float x, float y, float hbWidth, float hbHeight, float renderW, float renderH, float speed, boolean isFlying, int hp) {
        this.type = type;
        this.bounds = new Rectangle(x, y, hbWidth, hbHeight);
        this.renderWidth = renderW;
        this.renderHeight = renderH;
        this.speed = speed;
        this.isFlying = isFlying;
        this.hp = hp;

        if (type == EnemyType.MOSSFLY) {
            this.currentState = State.HIDING;
        } else if (type == EnemyType.MOSQUITO) {
            this.currentState = State.IDLE;
        } else if (type == EnemyType.HUSK) {
            this.currentState = State.WALKING;
            this.stateTimer = 3.0f;
        } else if (type == EnemyType.CRYSTAL) {
            this.currentState = State.IDLE;
        } else {
            this.currentState = State.WALKING;
        }

        this.previousState = this.currentState;
    }
}
