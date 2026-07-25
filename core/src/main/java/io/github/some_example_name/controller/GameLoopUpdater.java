package io.github.some_example_name.controller;

import com.badlogic.gdx.utils.Array;
import io.github.some_example_name.controller.game.GroundEnemyController;
import io.github.some_example_name.controller.game.FlyingEnemyController;
import io.github.some_example_name.model.*;
import io.github.some_example_name.view.game.SolidBlock;

public class GameLoopUpdater {
    private final GroundEnemyController groundEnemyController;
    private final FlyingEnemyController flyingEnemyController;

    public GameLoopUpdater(GroundEnemyController ground, FlyingEnemyController flying) {
        this.groundEnemyController = ground;
        this.flyingEnemyController = flying;
    }

    public void updateSpells(GameModel model, float delta, Array<SolidBlock> collisionBlocks) {
        PlayerModel player = model.getPlayerModel();
        for (int i = model.playerSpells.size - 1; i >= 0; i--) {
            SpellModel spell = model.playerSpells.get(i);

            if (spell.type == SpellModel.SpellType.VENGEFUL_SPIRIT) {
                spell.bounds.x += spell.velocityX * delta;

                boolean hitWall = false;
                for (SolidBlock block : collisionBlocks) {
                    if (block.isActive && !block.isDeadly && spell.bounds.overlaps(block.bounds)) {
                        hitWall = true;
                        break;
                    }
                }
                if (hitWall) {
                    model.playerSpells.removeIndex(i);
                    continue;
                }

                for (EnemyModel enemy : model.enemies) {
                    if (enemy.currentState != EnemyModel.State.DEAD && spell.bounds.overlaps(enemy.bounds)) {
                        if (!spell.hitEnemies.contains(enemy, true)) {
                            spell.hitEnemies.add(enemy);
                            int dmg = player.cheatOneHitKill ? enemy.hp : 2;
                            if (enemy.isFlying) flyingEnemyController.takeDamage(enemy, dmg, spell.facingRight);
                            else groundEnemyController.takeDamage(enemy, dmg, spell.facingRight);
                        }
                    }
                }

            } else if (spell.type == SpellModel.SpellType.HOWLING_WRAITHS) {
                spell.tickTimer += delta;

                if (spell.ticksDone < 3 && spell.tickTimer >= 0.2f) {
                    spell.tickTimer = 0f;
                    spell.ticksDone++;
                    for (EnemyModel enemy : model.enemies) {
                        if (enemy.currentState != EnemyModel.State.DEAD && spell.bounds.overlaps(enemy.bounds)) {
                            int dmg = player.cheatOneHitKill ? enemy.hp : 1;
                            if (enemy.isFlying) flyingEnemyController.takeDamage(enemy, dmg, player.facingRight);
                            else groundEnemyController.takeDamage(enemy, dmg, player.facingRight);
                        }
                    }
                }

                if (spell.stateTimer >= 0.65f) {
                    model.playerSpells.removeIndex(i);
                }
            }
        }
    }

    public void updateEnemies(GameModel model, float delta, Array<SolidBlock> collisionBlocks) {
        for (EnemyModel enemy : model.enemies) {
            if (!enemy.isFlying) {
                groundEnemyController.update(enemy, model, delta, collisionBlocks);
            } else {
                flyingEnemyController.update(enemy, model.getPlayerModel(), delta, collisionBlocks);
            }
        }
    }

    public void updateProjectiles(GameModel model, float delta, Array<SolidBlock> collisionBlocks, GameAudioSystem audio, float sfxVolume) {
        PlayerModel player = model.getPlayerModel();
        for (int i = model.projectiles.size - 1; i >= 0; i--) {
            ProjectileModel p = model.projectiles.get(i);
            p.bounds.x += p.velocityX * delta;

            boolean hitObstacle = false;
            for (SolidBlock block : collisionBlocks) {
                if (block.isActive && !block.isDeadly && p.bounds.overlaps(block.bounds)) {
                    hitObstacle = true;
                    break;
                }
            }
            if (hitObstacle) {
                model.projectiles.removeIndex(i);
                continue;
            }

            if (p.bounds.overlaps(player.bounds) && !player.isInvincible && player.currentState != PlayerModel.State.DEAD) {
                player.takeDamage(1);
                if (player.currentState == PlayerModel.State.DEAD) {
                    if (audio.deathSound != null) audio.deathSound.play(sfxVolume);
                } else {
                    if (audio.damageSound != null) audio.damageSound.play(sfxVolume);
                }
                player.velocityY = 300f;
                player.bounds.x += (p.velocityX > 0 ? 50f : -50f);
                model.projectiles.removeIndex(i);
            }
        }
    }

    public void updatePlayerTimers(PlayerModel player, float delta) {
        if (player.dashCooldownTimer > 0) player.dashCooldownTimer -= delta;

        if (player.isInvincible) {
            player.invincibilityTimer -= delta;
            if (player.invincibilityTimer <= 0) player.isInvincible = false;
        }
    }
}
