package io.github.some_example_name.model;

import com.badlogic.gdx.math.Rectangle;

public class PlayerModel {
    public enum State { IDLE, RUNNING, JUMPING, FALLING, LANDING, DOUBLE_JUMPING, DASHING, ATTACKING, HURT, DEAD, CASTING }
    public State currentState = State.IDLE;

    public int currentHp = 5;
    public int maxHp = 5;
    public float currentSoul = 99f;
    public final float MAX_SOUL = 99f;

    public boolean isInvincible = false;
    public float invincibilityTimer = 0f;
    public final float INVINCIBILITY_DURATION = 1.0f;

    public boolean isFocusing = false;
    public float focusTimer = 0f;
    public float FOCUS_DURATION = 1.5f;

    public Rectangle bounds;
    public float velocityY = 0;
    public boolean isGrounded = false;
    public boolean canDoubleJump = false;
    public boolean facingRight = true;

    private final float startX;
    private final float startY;

    public final float renderWidth = 349f;
    public final float renderHeight = 186f;

    public float dashTimer = 0f;
    public float dashCooldownTimer = 0f;
    public float attackTimer = 0f;
    public float landingTimer = 0f;
    public float castTimer = 0f;

    public final float DASH_DURATION = 0.25f;
    public float DASH_COOLDOWN = 0.6f;
    public final float DASH_SPEED = 800f;
    public float ATTACK_DURATION = 0.25f;
    public final float LANDING_DURATION = 0.2f;

    public final float GRAVITY = -9.8f * 120;
    public final float JUMP_VELOCITY = 800;
    public final float SPEED = 350;

    public boolean cheatGodMode = false;
    public boolean noclipMode = false;
    public boolean cheatOneHitKill = false;
    public boolean cheatInfiniteSoul = false;

    public boolean charmSoulCatcher = false;
    public boolean charmDashmaster = false;
    public boolean charmUnbreakableStrength = false;
    public boolean charmQuickSlash = false;
    public boolean charmQuickFocus = false;

    public int usedNotches = 0;
    public final int MAX_NOTCHES = 3;

    public PlayerModel(float startX, float startY) {
        this.startX = startX;
        this.startY = startY;
        this.bounds = new Rectangle(startX, startY, 60, 100);
    }

    public void resetToSpawn() {
        this.currentHp = this.maxHp;
        this.bounds.x = this.startX;
        this.bounds.y = this.startY;
        this.currentState = State.IDLE;
        this.velocityY = 0;
        this.isInvincible = false;
    }

    public void takeDamage(int amount) {
        if (cheatGodMode || noclipMode) return;

        if (!isInvincible && currentHp > 0) {
            this.currentHp -= amount;
            if (this.currentHp <= 0) {
                this.currentHp = 0;
                this.currentState = State.DEAD;
                SaveManager.getInstance().getData().deaths++;
                SaveManager.getInstance().save();
            } else {
                this.currentState = State.HURT;
                this.isInvincible = true;
                this.invincibilityTimer = INVINCIBILITY_DURATION;
            }
        }
    }
}
