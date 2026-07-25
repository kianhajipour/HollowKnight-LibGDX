package io.github.some_example_name.model;

import com.badlogic.gdx.math.Rectangle;

public class ZoteModel {
    public enum State { IDLE, TALKING }
    public State currentState = State.IDLE;

    public Rectangle bounds;
    public Rectangle interactionBounds;

    public final float renderWidth = 349f;
    public final float renderHeight = 186f;

    public float velocityY = 0f;
    public boolean isGrounded = false;
    public final float GRAVITY = -9.8f * 120;

    public String[] dialogues = {
        "Curse this foul place! It is damp, dark, and full of beasts.",
        "I am Zote the Mighty, a knight of great renown!",
        "Do not stand in my way, you pathetic little creature."
    };

    public int currentDialogueIndex = 0;
    public String currentDisplayedText = "";
    public int charIndex = 0;
    public float typeTimer = 0f;

    public boolean isPlayerInRange = false;
    public boolean isDialogueActive = false;

    public ZoteModel(float x, float y) {
        this.bounds = new Rectangle(x, y, 60, 100);
        this.interactionBounds = new Rectangle(x - 120, y, 330, 150);
    }
}
