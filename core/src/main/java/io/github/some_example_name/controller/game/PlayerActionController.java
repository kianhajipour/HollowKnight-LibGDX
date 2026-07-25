package io.github.some_example_name.controller.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import io.github.some_example_name.controller.SettingsController;
import io.github.some_example_name.model.PlayerModel;
import io.github.some_example_name.view.game.SolidBlock;

public class PlayerActionController {

    public void handleActions(PlayerModel player, SettingsController settingsController, Sound dashSound, Sound attackSound, Runnable attackCollisionCheck) {
        if (player.noclipMode || player.currentState == PlayerModel.State.DASHING || player.currentState == PlayerModel.State.CASTING) return;

        if (Gdx.input.isKeyJustPressed(settingsController.getJumpKey())) {
            if (player.isGrounded) {
                player.velocityY = player.JUMP_VELOCITY;
                player.isGrounded = false;
                player.currentState = PlayerModel.State.JUMPING;
            } else if (player.canDoubleJump) {
                player.velocityY = player.JUMP_VELOCITY * 0.9f;
                player.canDoubleJump = false;
                player.currentState = PlayerModel.State.DOUBLE_JUMPING;
            }
        }

        if (Gdx.input.isKeyJustPressed(settingsController.getDashKey()) && player.dashCooldownTimer <= 0) {
            player.currentState = PlayerModel.State.DASHING;
            player.dashTimer = player.DASH_DURATION;
            player.dashCooldownTimer = player.DASH_COOLDOWN;
            player.velocityY = 0;
            if (dashSound != null) {
                dashSound.stop();
                dashSound.play(settingsController.getSfxVolume());
            }
            return;
        }

        if (Gdx.input.isKeyJustPressed(settingsController.getAttackKey())) {
            player.currentState = PlayerModel.State.ATTACKING;
            player.attackTimer = player.ATTACK_DURATION;
            if (attackSound != null) {
                attackSound.stop();
                attackSound.play(settingsController.getSfxVolume());
            }
            attackCollisionCheck.run();
            return;
        }

        if (player.currentState != PlayerModel.State.ATTACKING && player.currentState != PlayerModel.State.LANDING && !player.isGrounded) {
            if (player.velocityY > 0 && player.currentState != PlayerModel.State.DOUBLE_JUMPING) player.currentState = PlayerModel.State.JUMPING;
            else if (player.velocityY <= 0) player.currentState = PlayerModel.State.FALLING;
        }
    }

    public void updateStates(PlayerModel player, float delta, PlayerMovementController movementController, Array<SolidBlock> collisionBlocks) {
        if (player.noclipMode) return;

        if (player.currentState == PlayerModel.State.DASHING) {
            player.dashTimer -= delta;
            player.bounds.x += (player.facingRight ? player.DASH_SPEED : -player.DASH_SPEED) * delta;
            movementController.resolveHorizontalCollisions(player, collisionBlocks);
            if (player.dashTimer <= 0) player.currentState = PlayerModel.State.FALLING;
        } else if (player.currentState == PlayerModel.State.ATTACKING) {
            player.attackTimer -= delta;
            if (player.attackTimer <= 0) player.currentState = player.isGrounded ? PlayerModel.State.IDLE : PlayerModel.State.FALLING;
        } else if (player.currentState == PlayerModel.State.LANDING) {
            player.landingTimer -= delta;
            if (player.landingTimer <= 0) player.currentState = player.isGrounded ? PlayerModel.State.IDLE : PlayerModel.State.FALLING;
        }
    }
}
