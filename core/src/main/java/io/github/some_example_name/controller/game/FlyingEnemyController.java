package io.github.some_example_name.controller.game;

import com.badlogic.gdx.utils.Array;
import io.github.some_example_name.model.EnemyModel;
import io.github.some_example_name.model.PlayerModel;
import io.github.some_example_name.model.SaveData;
import io.github.some_example_name.model.SaveManager;
import io.github.some_example_name.view.game.SolidBlock;

public class FlyingEnemyController {

    public void takeDamage(EnemyModel enemy, int amount, boolean attackFromRight) {
        if (enemy.currentState == EnemyModel.State.DEAD) return;

        enemy.hp -= amount;
        if (enemy.hp <= 0) {
            enemy.currentState = EnemyModel.State.DEAD;
            SaveData data = SaveManager.getInstance().getData();
            data.enemiesKilled++;
            data.killedEnemyTypes[enemy.type.ordinal()] = true;
            boolean allKilled = true;
            for (boolean k : data.killedEnemyTypes) {
                if (!k) {
                    allKilled = false;
                    break;
                }
            }
            if (allKilled) {
                data.achTrueHunter = true;
            }
            SaveManager.getInstance().save();
        } else {
            enemy.currentState = EnemyModel.State.HIT;
            enemy.stateTimer = 0.2f;
            enemy.knockedRight = attackFromRight;
            enemy.dashVx = (enemy.knockedRight ? 400f : -400f);
            enemy.dashVy = 100f;
        }
    }

    public void update(EnemyModel enemy, PlayerModel player, float delta, Array<SolidBlock> blocks) {
        if (enemy.currentState == EnemyModel.State.DEAD) return;

        if (enemy.currentState == EnemyModel.State.HIT) {
            enemy.stateTimer -= delta;
            enemy.bounds.x += enemy.dashVx * delta;
            enemy.bounds.y += enemy.dashVy * delta;

            if (enemy.stateTimer <= 0) {
                enemy.currentState = (enemy.type == EnemyModel.EnemyType.MOSQUITO) ? EnemyModel.State.IDLE : EnemyModel.State.FLYING;
            }
            return;
        }

        float dx = player.bounds.x - enemy.bounds.x;
        float dy = player.bounds.y - enemy.bounds.y;
        float dist = (float) Math.sqrt(dx * dx + dy * dy);
        boolean playerIsRight = dx > 0;

        if (enemy.type == EnemyModel.EnemyType.MOSQUITO) {
            if (enemy.currentState == EnemyModel.State.IDLE) {
                enemy.movingRight = playerIsRight;
                enemy.bounds.y += (float) Math.sin(System.currentTimeMillis() / 200.0) * 0.5f;

                if (dist < enemy.aggroRange) {
                    enemy.currentState = EnemyModel.State.ANTICIPATING;
                    enemy.stateTimer = 0.6f;
                    enemy.targetX = player.bounds.x;
                    enemy.targetY = player.bounds.y;
                }
            } else if (enemy.currentState == EnemyModel.State.ANTICIPATING) {
                enemy.stateTimer -= delta;
                if (enemy.stateTimer <= 0) {
                    enemy.currentState = EnemyModel.State.ATTACKING;
                    enemy.stateTimer = 1.0f;

                    float targetDx = enemy.targetX - enemy.bounds.x;
                    float targetDy = enemy.targetY - enemy.bounds.y;
                    float targetDist = (float) Math.sqrt(targetDx * targetDx + targetDy * targetDy);

                    if (targetDist > 0) {
                        float dashSpeed = 500f;
                        enemy.dashVx = (targetDx / targetDist) * dashSpeed;
                        enemy.dashVy = (targetDy / targetDist) * dashSpeed;
                    }
                }
            } else if (enemy.currentState == EnemyModel.State.ATTACKING) {
                enemy.stateTimer -= delta;
                float moveX = enemy.dashVx * delta;
                float moveY = enemy.dashVy * delta;

                enemy.bounds.x += moveX;
                enemy.bounds.y += moveY;

                boolean hitObstacle = false;
                for (SolidBlock block : blocks) {
                    if (enemy.bounds.overlaps(block.bounds)) {
                        hitObstacle = true;
                        enemy.bounds.x -= moveX;
                        enemy.bounds.y -= moveY;
                        break;
                    }
                }

                if (hitObstacle || enemy.stateTimer <= 0) {
                    enemy.currentState = EnemyModel.State.IDLE;
                }
            }
        } else if (enemy.type == EnemyModel.EnemyType.MOSSFLY) {
            if (enemy.currentState == EnemyModel.State.HIDING) {
                if (dist < enemy.aggroRange) {
                    enemy.currentState = EnemyModel.State.SHAKING;
                    enemy.stateTimer = 0.45f;
                }
            } else if (enemy.currentState == EnemyModel.State.SHAKING) {
                enemy.stateTimer -= delta;
                if (enemy.stateTimer <= 0) {
                    enemy.currentState = EnemyModel.State.APPEARING;
                    enemy.stateTimer = 0.9f;
                }
            } else if (enemy.currentState == EnemyModel.State.APPEARING) {
                enemy.stateTimer -= delta;
                if (enemy.stateTimer <= 0) {
                    enemy.currentState = EnemyModel.State.FLYING;
                }
            } else if (enemy.currentState == EnemyModel.State.FLYING) {
                enemy.movingRight = playerIsRight;
                if (dist > 0 && dist < enemy.aggroRange * 1.5f) {
                    float moveX = (dx / dist) * enemy.speed * delta;
                    float moveY = (dy / dist) * enemy.speed * delta;
                    enemy.bounds.x += moveX;
                    enemy.bounds.y += moveY;

                    for (SolidBlock block : blocks) {
                        if (enemy.bounds.overlaps(block.bounds)) {
                            enemy.bounds.x -= moveX;
                            enemy.bounds.y -= moveY;
                            break;
                        }
                    }
                } else if (dist >= enemy.aggroRange * 1.5f) {
                    enemy.bounds.y += (float) Math.sin(System.currentTimeMillis() / 200.0) * 0.5f;
                }
            }
        }
    }
}
