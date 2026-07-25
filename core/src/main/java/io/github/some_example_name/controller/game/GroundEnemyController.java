package io.github.some_example_name.controller.game;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import io.github.some_example_name.model.EnemyModel;
import io.github.some_example_name.model.GameModel;
import io.github.some_example_name.model.PlayerModel;
import io.github.some_example_name.model.ProjectileModel;
import io.github.some_example_name.model.SaveData;
import io.github.some_example_name.model.SaveManager;
import io.github.some_example_name.view.game.SolidBlock;

public class GroundEnemyController {

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
            enemy.hitTimer = enemy.HIT_DURATION;
            enemy.knockedRight = attackFromRight;
            enemy.velocityY = 150f;
        }
    }

    public void update(EnemyModel enemy, GameModel gameModel, float delta, Array<SolidBlock> blocks) {
        if (enemy.currentState == EnemyModel.State.DEAD) return;

        PlayerModel player = gameModel.getPlayerModel();

        enemy.velocityY += enemy.GRAVITY * delta;
        enemy.bounds.y += enemy.velocityY * delta;
        enemy.isGrounded = false;

        for (SolidBlock block : blocks) {
            if (enemy.bounds.overlaps(block.bounds)) {
                if (enemy.velocityY < 0) {
                    enemy.bounds.y = block.bounds.y + block.bounds.height;
                    enemy.velocityY = 0;
                    enemy.isGrounded = true;
                } else if (enemy.velocityY > 0) {
                    enemy.bounds.y = block.bounds.y - enemy.bounds.height;
                    enemy.velocityY = 0;
                }
            }
        }

        if (enemy.currentState == EnemyModel.State.HIT) {
            enemy.hitTimer -= delta;
            float knockX = (enemy.knockedRight ? enemy.knockbackSpeed : -enemy.knockbackSpeed) * delta;
            enemy.bounds.x += knockX;

            for (SolidBlock block : blocks) {
                if (enemy.bounds.overlaps(block.bounds)) {
                    enemy.bounds.x -= knockX;
                    break;
                }
            }

            if (enemy.hitTimer <= 0) {
                if (enemy.hp <= 0) {
                    enemy.currentState = EnemyModel.State.DEAD;
                } else {
                    if (enemy.type == EnemyModel.EnemyType.HUSK) {
                        enemy.currentState = EnemyModel.State.RESTING;
                        enemy.stateTimer = 1.0f;
                    } else if (enemy.type == EnemyModel.EnemyType.CRYSTAL) {
                        enemy.currentState = EnemyModel.State.IDLE;
                    } else {
                        enemy.currentState = EnemyModel.State.WALKING;
                    }
                }
            }
            return;
        }

        if (enemy.isGrounded) {
            if (enemy.type == EnemyModel.EnemyType.HUSK) {
                if (enemy.currentState == EnemyModel.State.WALKING || enemy.currentState == EnemyModel.State.RESTING) {
                    float visionW = 350f;
                    Rectangle vision = new Rectangle(
                        enemy.movingRight ? enemy.bounds.x + enemy.bounds.width : enemy.bounds.x - visionW,
                        enemy.bounds.y, visionW, enemy.bounds.height
                    );
                    if (vision.overlaps(player.bounds)) {
                        enemy.currentState = EnemyModel.State.ANTICIPATING;
                        enemy.stateTimer = 0.5f;
                    }
                }

                if (enemy.currentState == EnemyModel.State.WALKING) {
                    enemy.stateTimer -= delta;
                    if (enemy.stateTimer <= 0) {
                        enemy.currentState = EnemyModel.State.RESTING;
                        enemy.stateTimer = 2.0f;
                    } else {
                        moveAndCheckEdges(enemy, enemy.speed, delta, blocks);
                    }
                } else if (enemy.currentState == EnemyModel.State.RESTING) {
                    enemy.stateTimer -= delta;
                    if (enemy.stateTimer <= 0) {
                        enemy.currentState = EnemyModel.State.WALKING;
                        enemy.stateTimer = 3.0f;
                    }
                } else if (enemy.currentState == EnemyModel.State.ANTICIPATING) {
                    enemy.stateTimer -= delta;
                    if (enemy.stateTimer <= 0) {
                        enemy.currentState = EnemyModel.State.LUNGING;
                    }
                } else if (enemy.currentState == EnemyModel.State.LUNGING) {
                    boolean hitObstacle = moveAndCheckEdges(enemy, enemy.speed * 3.5f, delta, blocks);
                    if (hitObstacle) {
                        enemy.currentState = EnemyModel.State.RESTING;
                        enemy.stateTimer = 1.5f;
                    }
                }
            } else if (enemy.type == EnemyModel.EnemyType.CRYSTAL) {
                if (enemy.currentState == EnemyModel.State.IDLE) {
                    float visionW = 600f;
                    Rectangle vision = new Rectangle(
                        enemy.movingRight ? enemy.bounds.x + enemy.bounds.width : enemy.bounds.x - visionW,
                        enemy.bounds.y, visionW, enemy.bounds.height
                    );
                    if (vision.overlaps(player.bounds)) {
                        enemy.currentState = EnemyModel.State.SHOOTING;
                        enemy.stateTimer = 0.7f;
                    }
                } else if (enemy.currentState == EnemyModel.State.SHOOTING) {
                    enemy.stateTimer -= delta;
                    if (enemy.stateTimer <= 0) {
                        float pX = enemy.movingRight ? enemy.bounds.x + enemy.bounds.width : enemy.bounds.x - 40f;
                        float pY = enemy.bounds.y + enemy.bounds.height / 2f - 10f;
                        float pVx = enemy.movingRight ? 800f : -800f;
                        gameModel.projectiles.add(new ProjectileModel(pX, pY, 40f, 40f, pVx));

                        enemy.currentState = EnemyModel.State.ENRAGED;
                        enemy.stateTimer = 3.5f;
                    }
                } else if (enemy.currentState == EnemyModel.State.ENRAGED) {
                    enemy.stateTimer -= delta;
                    if (enemy.stateTimer <= 0) {
                        enemy.currentState = EnemyModel.State.IDLE;
                    } else {
                        enemy.movingRight = player.bounds.x > enemy.bounds.x;
                        moveAndCheckEdges(enemy, enemy.speed * 2.5f, delta, blocks);
                    }
                }
            } else {
                moveAndCheckEdges(enemy, enemy.speed, delta, blocks);
            }
        }
    }

    private boolean moveAndCheckEdges(EnemyModel enemy, float moveSpeed, float delta, Array<SolidBlock> blocks) {
        float moveX = (enemy.movingRight ? moveSpeed : -moveSpeed) * delta;
        enemy.bounds.x += moveX;

        boolean hitWall = false;
        for (SolidBlock block : blocks) {
            if (enemy.bounds.overlaps(block.bounds)) {
                hitWall = true;
                enemy.bounds.x -= moveX;
                break;
            }
        }

        boolean floorAhead = false;
        float checkX = enemy.movingRight ? enemy.bounds.x + enemy.bounds.width + 2f : enemy.bounds.x - 2f;
        float checkY = enemy.bounds.y - 5f;

        for (SolidBlock block : blocks) {
            if (checkX >= block.bounds.x && checkX <= block.bounds.x + block.bounds.width &&
                checkY >= block.bounds.y && checkY <= block.bounds.y + block.bounds.height) {
                floorAhead = true;
                break;
            }
        }

        if (hitWall || !floorAhead) {
            if (enemy.currentState != EnemyModel.State.LUNGING) {
                enemy.movingRight = !enemy.movingRight;
            }
            return true;
        }
        return false;
    }
}
