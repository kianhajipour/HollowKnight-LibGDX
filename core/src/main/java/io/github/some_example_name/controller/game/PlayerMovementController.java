package io.github.some_example_name.controller.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Array;
import io.github.some_example_name.controller.SettingsController;
import io.github.some_example_name.model.PlayerModel;
import io.github.some_example_name.view.game.SolidBlock;

public class PlayerMovementController {

    public void handleMovement(PlayerModel player, float delta, SettingsController settingsController, Array<SolidBlock> collisionBlocks) {
        if (player.currentState == PlayerModel.State.ATTACKING || player.currentState == PlayerModel.State.DASHING || player.currentState == PlayerModel.State.CASTING) return;

        if (player.noclipMode) {
            float flySpeed = player.SPEED * 2.5f;
            if (Gdx.input.isKeyPressed(settingsController.getLeftKey())) {
                player.bounds.x -= flySpeed * delta;
                player.facingRight = false;
            } else if (Gdx.input.isKeyPressed(settingsController.getRightKey())) {
                player.bounds.x += flySpeed * delta;
                player.facingRight = true;
            }

            if (Gdx.input.isKeyPressed(settingsController.getJumpKey())) {
                player.bounds.y += flySpeed * delta;
            } else if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                player.bounds.y -= flySpeed * delta;
            }

            player.currentState = PlayerModel.State.JUMPING;
            return;
        }

        boolean moved = false;
        if (Gdx.input.isKeyPressed(settingsController.getLeftKey())) {
            player.bounds.x -= player.SPEED * delta;
            player.facingRight = false;
            moved = true;
        } else if (Gdx.input.isKeyPressed(settingsController.getRightKey())) {
            player.bounds.x += player.SPEED * delta;
            player.facingRight = true;
            moved = true;
        }

        if (moved) {
            if (player.isGrounded && player.currentState != PlayerModel.State.LANDING) player.currentState = PlayerModel.State.RUNNING;
            resolveHorizontalCollisions(player, collisionBlocks);
        } else if (player.isGrounded && player.currentState != PlayerModel.State.LANDING) {
            player.currentState = PlayerModel.State.IDLE;
        }
    }

    public void handlePhysics(PlayerModel player, float delta, SettingsController settingsController, Array<SolidBlock> collisionBlocks) {
        if (player.noclipMode || player.currentState == PlayerModel.State.DASHING || player.currentState == PlayerModel.State.CASTING) return;

        float currentGravity = player.GRAVITY;
        if (!player.isGrounded && !Gdx.input.isKeyPressed(settingsController.getJumpKey()) && player.velocityY > 0) {
            currentGravity = player.GRAVITY * 3.5f;
        }

        player.velocityY += currentGravity * delta;
        player.bounds.y += player.velocityY * delta;
        boolean wasGrounded = player.isGrounded;
        player.isGrounded = false;

        for (SolidBlock block : collisionBlocks) {
            if (block.isActive && !block.isDeadly && player.bounds.overlaps(block.bounds)) {
                if (player.velocityY < 0) {
                    player.bounds.y = block.bounds.y + block.bounds.height;
                    player.velocityY = 0;
                    player.isGrounded = true;
                    player.canDoubleJump = true;
                } else if (player.velocityY > 0) {
                    player.bounds.y = block.bounds.y - player.bounds.height;
                    player.velocityY = 0;
                }
            }
        }

        if (player.isGrounded && !wasGrounded && player.currentState != PlayerModel.State.ATTACKING) {
            player.currentState = PlayerModel.State.LANDING;
            player.landingTimer = player.LANDING_DURATION;
        }
    }

    public void resolveHorizontalCollisions(PlayerModel player, Array<SolidBlock> collisionBlocks) {
        if (player.noclipMode) return;

        for (SolidBlock block : collisionBlocks) {
            if (block.isActive && !block.isDeadly && player.bounds.overlaps(block.bounds)) {
                player.bounds.x = player.facingRight ? block.bounds.x - player.bounds.width : block.bounds.x + block.bounds.width;
            }
        }
    }
}
