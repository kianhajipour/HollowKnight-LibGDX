package io.github.some_example_name.controller;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import io.github.some_example_name.controller.game.GroundEnemyController;
import io.github.some_example_name.controller.game.FlyingEnemyController;
import io.github.some_example_name.controller.game.FalseKnightController;
import io.github.some_example_name.model.*;
import io.github.some_example_name.view.game.SolidBlock;

public class GameCollisionSystem {
    private final GroundEnemyController groundEnemyController;
    private final FlyingEnemyController flyingEnemyController;
    private final FalseKnightController falseKnightController;

    public GameCollisionSystem(GroundEnemyController ground, FlyingEnemyController flying, FalseKnightController boss) {
        this.groundEnemyController = ground;
        this.flyingEnemyController = flying;
        this.falseKnightController = boss;
    }

    public void checkSpikeCollisions(PlayerModel player, Array<SolidBlock> collisionBlocks, GameAudioSystem audio, float sfxVolume) {
        if (player.cheatGodMode || player.noclipMode) return;

        for (SolidBlock block : collisionBlocks) {
            if (block.isActive && block.isDeadly && player.bounds.overlaps(block.bounds) &&
                !player.isInvincible && player.currentState != PlayerModel.State.DEAD) {

                player.takeDamage(1);

                if (player.currentHp <= 0) {
                    player.currentState = PlayerModel.State.DEAD;
                    if (audio.deathSound != null) audio.deathSound.play(sfxVolume);
                } else {
                    if (audio.damageSound != null) audio.damageSound.play(sfxVolume);
                    player.currentState = PlayerModel.State.HURT;
                    player.isInvincible = true;
                    player.invincibilityTimer = 1.0f;
                    player.velocityY = 650f;
                }
            }
        }
    }

    public void checkEnemyCollisions(PlayerModel player, GameModel gameModel, GameAudioSystem audio, float sfxVolume) {
        if (player.cheatGodMode || player.noclipMode) return;

        for (EnemyModel enemy : gameModel.enemies) {
            if (enemy.currentState != EnemyModel.State.DEAD && player.bounds.overlaps(enemy.bounds) &&
                !player.isInvincible && player.currentState != PlayerModel.State.DEAD) {

                player.takeDamage(1);

                if (player.currentHp <= 0) {
                    player.currentState = PlayerModel.State.DEAD;
                    if (audio.deathSound != null) audio.deathSound.play(sfxVolume);
                } else {
                    if (audio.damageSound != null) audio.damageSound.play(sfxVolume);
                    player.currentState = PlayerModel.State.HURT;
                    player.isInvincible = true;
                    player.invincibilityTimer = 1.0f;
                    player.velocityY = 300f;
                    player.bounds.x += (player.facingRight ? -50f : 50f);
                }
            }
        }

        if (gameModel.falseKnight != null && !gameModel.falseKnight.isStunned && gameModel.falseKnight.currentState != FalseKnightModel.State.DEAD) {
            if (player.bounds.overlaps(gameModel.falseKnight.bounds) && !player.isInvincible && player.currentState != PlayerModel.State.DEAD) {
                player.takeDamage(1);
                if (player.currentHp <= 0) {
                    player.currentState = PlayerModel.State.DEAD;
                    if (audio.deathSound != null) audio.deathSound.play(sfxVolume);
                } else {
                    if (audio.damageSound != null) audio.damageSound.play(sfxVolume);
                    player.currentState = PlayerModel.State.HURT;
                    player.isInvincible = true;
                    player.invincibilityTimer = 1.0f;
                    player.velocityY = 300f;
                    player.bounds.x += (player.facingRight ? -50f : 50f);
                }
            }
        }
    }

    public void checkAttackCollision(PlayerModel player, GameModel gameModel, GameAudioSystem audio) {
        float attackWidth = 90f;
        float attackX = player.facingRight ? player.bounds.x + player.bounds.width : player.bounds.x - attackWidth;
        Rectangle attackBounds = new Rectangle(attackX, player.bounds.y, attackWidth, player.bounds.height);

        for (EnemyModel enemy : gameModel.enemies) {
            if (enemy.currentState != EnemyModel.State.DEAD && attackBounds.overlaps(enemy.bounds)) {
                int damageAmount = player.cheatOneHitKill ? enemy.hp : (player.charmUnbreakableStrength ? 2 : 1);

                if (enemy.isFlying) {
                    flyingEnemyController.takeDamage(enemy, damageAmount, player.facingRight);
                } else {
                    groundEnemyController.takeDamage(enemy, damageAmount, player.facingRight);
                }

                float soulGain = player.charmSoulCatcher ? 16f : 11f;
                player.currentSoul = Math.min(player.MAX_SOUL, player.currentSoul + soulGain);
            }
        }

        if (gameModel.falseKnight != null && gameModel.falseKnight.currentState != FalseKnightModel.State.DEAD) {
            if (attackBounds.overlaps(gameModel.falseKnight.bounds)) {
                int damageAmount = player.cheatOneHitKill ? gameModel.falseKnight.maxHp : (player.charmUnbreakableStrength ? 2 : 1);

                if (gameModel.falseKnight.isStunned) {
                    if (attackBounds.overlaps(gameModel.falseKnight.headHitbox)) {
                        falseKnightController.takeDamage(gameModel.falseKnight, damageAmount, audio.bossArmorDamage, audio.bossHeadDamage);
                    }
                } else {
                    falseKnightController.takeDamage(gameModel.falseKnight, damageAmount, audio.bossArmorDamage, audio.bossHeadDamage);
                }
            }
        }
    }
}
