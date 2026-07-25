package io.github.some_example_name.controller.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import io.github.some_example_name.model.FalseKnightModel;
import io.github.some_example_name.model.PlayerModel;
import io.github.some_example_name.view.game.SolidBlock;
import io.github.some_example_name.model.SaveManager;
import io.github.some_example_name.model.SaveData;

public class FalseKnightController {
    public void update(FalseKnightModel boss, PlayerModel player, float delta, Array<SolidBlock> blocks, Array<Sound> attackRoars, Sound jumpSound, Sound landSound, Sound slamSound, Sound swingSound, float globalSfxVolume) {
        if (boss == null || boss.currentState == FalseKnightModel.State.DEAD) return;

        float dist = Math.abs(boss.bounds.x - player.bounds.x);
        float activationDistance = 4000f;

        boss.velocityY += boss.GRAVITY * delta;

        boss.bounds.x += boss.velocityX * delta;
        for (SolidBlock block : blocks) {
            if (block.isActive && !block.isDeadly && boss.bounds.overlaps(block.bounds)) {
                if (boss.velocityX > 0) {
                    boss.bounds.x = block.bounds.x - boss.bounds.width;
                } else if (boss.velocityX < 0) {
                    boss.bounds.x = block.bounds.x + block.bounds.width;
                }
                boss.velocityX = 0;
            }
        }

        boss.bounds.y += boss.velocityY * delta;
        boss.isGrounded = false;
        for (SolidBlock block : blocks) {
            if (block.isActive && !block.isDeadly && boss.bounds.overlaps(block.bounds)) {
                if (boss.velocityY < 0) {
                    boss.bounds.y = block.bounds.y + block.bounds.height;
                    boss.velocityY = 0;
                    boss.isGrounded = true;
                } else if (boss.velocityY > 0) {
                    boss.bounds.y = block.bounds.y - boss.bounds.height;
                    boss.velocityY = 0;
                }
            }
        }

        boss.headHitbox.setPosition(boss.facingRight ? boss.bounds.x + 80 : boss.bounds.x, boss.bounds.y + 20);

        if (dist > activationDistance && !boss.isStunned && boss.currentHp > 0) {
            boss.velocityX = 0;
            boss.currentState = FalseKnightModel.State.IDLE;
            return;
        }

        boss.damageTimer += delta;
        if (boss.damageTimer > 3.0f) {
            boss.recentDamage = 0;
            boss.damageTimer = 0f;
        }

        if (boss.isStunned) {
            boss.velocityX = 0;
            boss.stunTimer -= delta;
            if (boss.stunTimer <= 0) {
                boss.isStunned = false;
                boss.phase = 2;
                boss.speedMultiplier = 1.35f;
                boss.currentHp = Math.min(boss.currentHp, boss.maxHp / 2);
                changeState(boss, FalseKnightModel.State.STUN_RECOVER);
            }
            return;
        }

        if (boss.currentHp <= boss.maxHp / 2 && boss.phase == 1 && !boss.isStunned) {
            boss.isStunned = true;
            boss.stunTimer = 5.0f;
            boss.recentDamage = 0;
            changeState(boss, FalseKnightModel.State.STUNNED);
            return;
        }

        if (boss.currentHp <= 0 && boss.currentState != FalseKnightModel.State.DEATH_FALL && boss.currentState != FalseKnightModel.State.DEATH_HIT && boss.currentState != FalseKnightModel.State.DEATH_LAND) {
            changeState(boss, FalseKnightModel.State.DEATH_FALL);
            return;
        }

        boss.stateTimer += delta * boss.speedMultiplier;
        handleStates(boss, player, attackRoars, jumpSound, landSound, slamSound, swingSound, dist, globalSfxVolume);
    }

    private void handleStates(FalseKnightModel boss, PlayerModel player, Array<Sound> attackRoars, Sound jumpSound, Sound landSound, Sound slamSound, Sound swingSound, float dist, float globalSfxVolume) {
        float vertDist = Math.abs(boss.bounds.y - player.bounds.y);
        boolean playerToRight = player.bounds.x > boss.bounds.x;

        float maxDistance = 2000f;
        float volume = 0f;

        if (dist <= maxDistance && vertDist <= 300f) {
            volume = (1f - (dist / maxDistance)) * globalSfxVolume;
        }

        switch (boss.currentState) {
            case IDLE:
                boss.velocityX = 0;
                if (boss.actionCooldown > 0) {
                    boss.actionCooldown -= Gdx.graphics.getDeltaTime() * boss.speedMultiplier;
                } else if (dist <= maxDistance && vertDist <= 700f) {
                    boss.facingRight = playerToRight;
                    decideNextMove(boss, player, dist, attackRoars, volume);
                }
                break;
            case RUN_ANTIC:
                boss.velocityX = 0;
                if (boss.stateTimer >= 0.2f) changeState(boss, FalseKnightModel.State.RUNNING);
                break;
            case RUNNING:
                boss.facingRight = playerToRight;
                boss.velocityX = boss.facingRight ? 240f * boss.speedMultiplier : -240f * boss.speedMultiplier;

                if (vertDist > 200f) {
                    finishAction(boss);
                    break;
                }

                if (dist < 110f || boss.stateTimer > 3.0f) {
                    changeState(boss, FalseKnightModel.State.ATTACK_ANTIC);
                }
                break;
            case ATTACK_ANTIC:
                boss.velocityX = 0;
                if (boss.stateTimer >= 0.4f) {
                    if (swingSound != null && volume > 0.05f) swingSound.play(volume);
                    changeState(boss, FalseKnightModel.State.ATTACKING);
                }
                break;
            case ATTACKING:
                if (boss.stateTimer >= 0.3f) {
                    if (slamSound != null && volume > 0.05f) slamSound.play(volume);
                    changeState(boss, FalseKnightModel.State.ATTACK_RECOVER);
                }
                break;
            case ATTACK_RECOVER:
                if (boss.stateTimer >= 0.5f) finishAction(boss);
                break;
            case JUMP_ANTIC:
                boss.velocityX = 0;
                if (boss.stateTimer >= 0.2f) {
                    if (jumpSound != null && volume > 0.05f) jumpSound.play(volume);

                    if (boss.previousAction == FalseKnightModel.State.JUMP_ATTACK) {
                        boss.velocityY = 1250f;
                    } else if (player.bounds.y > boss.bounds.y + 150f) {
                        boss.velocityY = 1100f;
                    } else {
                        boss.velocityY = 900f;
                    }

                    if (boss.previousAction == FalseKnightModel.State.FALLING) {
                        boss.velocityX = boss.facingRight ? -380f : 380f;
                    } else {
                        boss.velocityX = boss.facingRight ? 450f : -450f;
                    }
                    changeState(boss, boss.previousAction == FalseKnightModel.State.JUMP_ATTACK ? FalseKnightModel.State.JUMP_ATTACK : FalseKnightModel.State.JUMPING);
                }
                break;
            case JUMPING:
            case JUMP_ATTACK:
                if (boss.velocityY <= 0) changeState(boss, FalseKnightModel.State.FALLING);
                break;
            case FALLING:
                if (boss.isGrounded) {
                    if (landSound != null && volume > 0.05f) landSound.play(volume);

                    if (boss.previousAction == FalseKnightModel.State.JUMP_ATTACK) {
                        if (slamSound != null && volume > 0.05f) {
                            slamSound.play(volume * 1.2f);
                        }
                    }
                    boss.velocityX = 0;
                    changeState(boss, FalseKnightModel.State.LANDING);
                }
                break;
            case LANDING:
                if (boss.stateTimer >= 0.4f) finishAction(boss);
                break;
            case STUN_RECOVER:
                if (boss.stateTimer >= 0.6f) finishAction(boss);
                break;
            case DEATH_FALL:
                if (boss.isGrounded) changeState(boss, FalseKnightModel.State.DEATH_HIT);
                break;
            case DEATH_HIT:
                if (boss.stateTimer >= 0.3f) changeState(boss, FalseKnightModel.State.DEATH_LAND);
                break;
            case DEATH_LAND:
                if (boss.stateTimer >= 1.1f) {
                    changeState(boss, FalseKnightModel.State.DEAD);
                    SaveData data = SaveManager.getInstance().getData();
                    data.achFalseKnight = true;
                    data.achCompletion = true;
                    if (data.timePlayed <= 3600f) {
                        data.achSpeedrun = true;
                    }
                    SaveManager.getInstance().save();
                }
                break;
        }
    }

    private void decideNextMove(FalseKnightModel boss, PlayerModel player, float dist, Array<Sound> attackRoars, float volume) {
        Array<Integer> possibleMoves = new Array<>();
        float playerVertDist = player.bounds.y - boss.bounds.y;

        if (boss.recentDamage >= 3) {
            boss.previousAction = FalseKnightModel.State.FALLING;
            boss.recentDamage = 0;
            changeState(boss, FalseKnightModel.State.JUMP_ANTIC);
            return;
        }

        if (playerVertDist > 150f) {
            if (boss.previousAction != FalseKnightModel.State.JUMPING) possibleMoves.add(2);
            if (boss.phase == 2 && boss.previousAction != FalseKnightModel.State.JUMP_ATTACK) possibleMoves.add(3);
            if (boss.previousAction != FalseKnightModel.State.RUNNING) possibleMoves.add(1);
        } else {
            if (dist > 250f) {
                if (boss.previousAction != FalseKnightModel.State.RUNNING) possibleMoves.add(1);
                if (boss.previousAction != FalseKnightModel.State.JUMPING) possibleMoves.add(2);
                if (boss.phase == 2 && boss.previousAction != FalseKnightModel.State.JUMP_ATTACK) possibleMoves.add(3);
            } else {
                if (boss.previousAction != FalseKnightModel.State.ATTACKING) possibleMoves.add(4);
                if (boss.previousAction != FalseKnightModel.State.JUMPING) possibleMoves.add(2);
            }
        }

        if (possibleMoves.size == 0) {
            possibleMoves.add(boss.previousAction == FalseKnightModel.State.ATTACKING ? 2 : 4);
        }

        int choice = possibleMoves.random();

        if ((choice == 1 || choice == 3 || choice == 4) && attackRoars != null && attackRoars.size > 0 && volume > 0.01f) {
            attackRoars.get(MathUtils.random(0, attackRoars.size - 1)).play(volume);
        }

        switch (choice) {
            case 1:
                boss.previousAction = FalseKnightModel.State.RUNNING;
                changeState(boss, FalseKnightModel.State.RUN_ANTIC);
                break;
            case 2:
                boss.previousAction = FalseKnightModel.State.JUMPING;
                changeState(boss, FalseKnightModel.State.JUMP_ANTIC);
                break;
            case 3:
                boss.previousAction = FalseKnightModel.State.JUMP_ATTACK;
                changeState(boss, FalseKnightModel.State.JUMP_ANTIC);
                break;
            case 4:
                boss.previousAction = FalseKnightModel.State.ATTACKING;
                changeState(boss, FalseKnightModel.State.ATTACK_ANTIC);
                break;
        }
    }

    private void changeState(FalseKnightModel boss, FalseKnightModel.State newState) {
        boss.currentState = newState;
        boss.stateTimer = 0f;
    }

    private void finishAction(FalseKnightModel boss) {
        boss.velocityX = 0;
        boss.actionCooldown = 0.8f;
        changeState(boss, FalseKnightModel.State.IDLE);
    }

    public void takeDamage(FalseKnightModel boss, int amount, Sound armorSound, Sound headSound) {
        if (boss.currentState == FalseKnightModel.State.DEAD || boss.currentState == FalseKnightModel.State.DEATH_FALL || boss.currentState == FalseKnightModel.State.DEATH_HIT || boss.currentState == FalseKnightModel.State.DEATH_LAND) return;

        float volume = 1f;

        if (boss.isStunned) {
            boss.currentHp -= amount;
            if (headSound != null) headSound.play(volume);
        } else {
            boss.currentHp -= amount;
            boss.recentDamage += amount;
            boss.damageTimer = 0f;
            if (armorSound != null) armorSound.play(volume);
        }
    }
}
