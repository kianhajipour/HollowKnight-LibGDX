package io.github.some_example_name.controller.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import io.github.some_example_name.model.GameModel;
import io.github.some_example_name.model.PlayerModel;
import io.github.some_example_name.model.ZoteModel;
import io.github.some_example_name.view.game.SolidBlock;

public class ZoteController {
    public void update(ZoteModel zote, PlayerModel player, GameModel gameModel, float delta, Array<Sound> zoteVoices, Array<SolidBlock> collisionBlocks, float sfxVolume) {
        if (zote == null) return;

        zote.velocityY += zote.GRAVITY * delta;
        if (zote.velocityY < -1500f) zote.velocityY = -1500f;

        zote.bounds.y += zote.velocityY * delta;
        zote.isGrounded = false;

        for (SolidBlock block : collisionBlocks) {
            if (!block.isDeadly && zote.bounds.overlaps(block.bounds)) {
                if (zote.velocityY <= 0 && zote.bounds.y + (zote.bounds.height / 2f) >= block.bounds.y + block.bounds.height) {
                    zote.bounds.y = block.bounds.y + block.bounds.height;
                    zote.velocityY = 0;
                    zote.isGrounded = true;
                }
            }
        }

        zote.interactionBounds.setPosition(zote.bounds.x - 120, zote.bounds.y);
        zote.isPlayerInRange = zote.interactionBounds.overlaps(player.bounds);

        if (!zote.isDialogueActive && zote.isPlayerInRange && (Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.E))) {
            zote.isDialogueActive = true;
            gameModel.currentGameState = GameModel.GameState.DIALOGUE;
            player.currentState = PlayerModel.State.IDLE;
            player.velocityY = 0;
            zote.currentState = ZoteModel.State.TALKING;
            zote.currentDialogueIndex = 0;
            startDialogueLine(zote, zoteVoices, sfxVolume);
        } else if (zote.isDialogueActive) {
            if (zote.charIndex < zote.dialogues[zote.currentDialogueIndex].length()) {
                zote.typeTimer += delta;
                if (zote.typeTimer >= 0.05f) {
                    zote.charIndex++;
                    zote.currentDisplayedText = zote.dialogues[zote.currentDialogueIndex].substring(0, zote.charIndex);
                    zote.typeTimer = 0f;
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                    zote.charIndex = zote.dialogues[zote.currentDialogueIndex].length();
                    zote.currentDisplayedText = zote.dialogues[zote.currentDialogueIndex];
                }
            } else {
                zote.currentState = ZoteModel.State.IDLE;
                if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                    zote.currentDialogueIndex++;
                    if (zote.currentDialogueIndex >= zote.dialogues.length) {
                        zote.isDialogueActive = false;
                        gameModel.currentGameState = GameModel.GameState.PLAYING;
                        zote.currentDisplayedText = "";
                    } else {
                        zote.currentState = ZoteModel.State.TALKING;
                        startDialogueLine(zote, zoteVoices, sfxVolume);
                    }
                }
            }
        }
    }

    private void startDialogueLine(ZoteModel zote, Array<Sound> voices, float sfxVolume) {
        zote.charIndex = 0;
        zote.currentDisplayedText = "";
        zote.typeTimer = 0f;
        if (voices.size > 0) {
            int rand = com.badlogic.gdx.math.MathUtils.random(0, voices.size - 1);
            voices.get(rand).play(sfxVolume);
        }
    }
}
