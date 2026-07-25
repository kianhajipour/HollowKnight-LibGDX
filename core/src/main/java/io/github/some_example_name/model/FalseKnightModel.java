package io.github.some_example_name.model;

import com.badlogic.gdx.math.Rectangle;

public class FalseKnightModel {
    public enum State {
        IDLE, RUN_ANTIC, RUNNING, JUMP_ANTIC, JUMPING, FALLING, LANDING,
        ATTACK_ANTIC, ATTACKING, ATTACK_RECOVER,
        JUMP_ATTACK, STUNNED, STUN_RECOVER,
        DEATH_FALL, DEATH_HIT, DEATH_LAND, DEAD
    }
    public State currentState = State.IDLE;
    public State previousAction = State.IDLE;
    public Rectangle bounds;
    public Rectangle headHitbox;
    public float renderWidth = 450f;
    public float renderHeight = 400f;
    public float velocityX = 0f;
    public float velocityY = 0f;
    public boolean isGrounded = false;
    public final float GRAVITY = -9.8f * 120;
    public int maxHp = 60;
    public int currentHp = 60;
    public int phase = 1;
    public boolean isStunned = false;
    public float stunTimer = 0f;
    public boolean facingRight = false;
    public float stateTimer = 0f;
    public float actionCooldown = 2.0f;
    public int recentDamage = 0;
    public float damageTimer = 0f;
    public float speedMultiplier = 1.0f;

    public FalseKnightModel(float x, float y) {
        this.bounds = new Rectangle(x, y, 180, 250);
        this.headHitbox = new Rectangle(x, y, 100, 100);
    }
}
