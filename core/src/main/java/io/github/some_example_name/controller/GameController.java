package io.github.some_example_name.controller;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Array;
import io.github.some_example_name.controller.game.*;
import io.github.some_example_name.model.*;
import io.github.some_example_name.view.game.SolidBlock;

public class GameController {
    private final Game game;
    private final SettingsController settingsController;
    private GameModel gameModel;

    private final GroundEnemyController groundEnemyController;
    private final FlyingEnemyController flyingEnemyController;
    private final PlayerMovementController movementController;
    private final PlayerActionController actionController;
    private final PlayerFocusController focusController;
    private final PlayerSpellController spellController;
    private final ZoteController zoteController;
    private final FalseKnightController falseKnightController;

    private final GameAudioSystem audioSystem;
    private final GameCollisionSystem collisionSystem;
    private final GameLoopUpdater loopUpdater;

    private float startDelayTimer = 1.0f;
    private float bossDoorTimer = -1f;
    private boolean isDoorTriggered = false;

    public GameController(Game game) {
        this.game = game;
        this.settingsController = new SettingsController(game);
        this.groundEnemyController = new GroundEnemyController();
        this.flyingEnemyController = new FlyingEnemyController();
        this.movementController = new PlayerMovementController();
        this.actionController = new PlayerActionController();
        this.focusController = new PlayerFocusController();
        this.spellController = new PlayerSpellController();
        this.zoteController = new ZoteController();
        this.falseKnightController = new FalseKnightController();

        this.audioSystem = new GameAudioSystem();
        this.collisionSystem = new GameCollisionSystem(groundEnemyController, flyingEnemyController, falseKnightController);
        this.loopUpdater = new GameLoopUpdater(groundEnemyController, flyingEnemyController);

        this.audioSystem.loadSounds();
    }

    public void init(GameModel gameModel) {
        this.gameModel = gameModel;
        if (gameModel != null && gameModel.getPlayerModel() != null) {
            PlayerModel player = gameModel.getPlayerModel();
            if (SaveManager.getInstance().isLoadAction) {
                SaveData data = SaveManager.getInstance().getData();

                player.bounds.x = data.playerX;
                player.bounds.y = data.playerY;
                player.currentHp = data.masks;
                player.currentSoul = data.soul;

                gameModel.isBossActive = data.isBossActive;
                if (gameModel.bossDoorBlock != null) {
                    gameModel.bossDoorBlock.isActive = data.isBossDoorActive;
                }
                if (gameModel.falseKnight != null) {
                    gameModel.falseKnight.bounds.x = data.bossX;
                    gameModel.falseKnight.bounds.y = data.bossY;
                    gameModel.falseKnight.currentHp = data.bossHp;
                    gameModel.falseKnight.currentState = FalseKnightModel.State.valueOf(data.bossState);
                }

                gameModel.enemies.clear();
                for (SaveData.EnemySaveState es : data.enemyStates) {
                    EnemyModel.EnemyType eType = EnemyModel.EnemyType.valueOf(es.type);
                    EnemyModel.State eState = EnemyModel.State.valueOf(es.state);

                    EnemyModel enemy = new EnemyModel(eType, es.x, es.y, es.hbWidth, es.hbHeight, es.renderWidth, es.renderHeight, es.speed, es.isFlying, es.hp);
                    enemy.currentState = eState;
                    gameModel.enemies.add(enemy);
                }
            } else {
                player.resetToSpawn();
            }
            SaveManager.getInstance().isLoadAction = false;
        }
    }

    public SettingsController getSettingsController() {
        return settingsController;
    }

    public void update(float delta, Array<SolidBlock> collisionBlocks) {
        if (gameModel == null) return;

        handleGameStateInput();

        float currentSfxVolume = settingsController.getSfxVolume();

        if (gameModel.currentGameState == GameModel.GameState.DIALOGUE) {
            zoteController.update(gameModel.zote, gameModel.getPlayerModel(), gameModel, delta, audioSystem.zoteVoices, collisionBlocks, currentSfxVolume);
            return;
        }

        if (gameModel.currentGameState != GameModel.GameState.PLAYING) return;

        SaveManager.getInstance().getData().timePlayed += delta;

        PlayerModel player = gameModel.getPlayerModel();
        if (player.currentState == PlayerModel.State.DEAD) return;

        if (gameModel.falseKnight != null && gameModel.falseKnight.currentState == FalseKnightModel.State.DEAD) {
            gameModel.currentGameState = GameModel.GameState.VICTORY;
            game.setScreen(new io.github.some_example_name.view.menu.VictoryScreen(game));
            return;
        }

        if (startDelayTimer > 0) {
            startDelayTimer -= delta;
            player.velocityY = 0;
            return;
        }

        zoteController.update(gameModel.zote, player, gameModel, delta, audioSystem.zoteVoices, collisionBlocks, currentSfxVolume);

        if (!gameModel.isBossActive && gameModel.bossTriggerArea != null && gameModel.bossDoorBlock != null) {
            if (!isDoorTriggered && player.bounds.y < gameModel.bossTriggerArea.y) {
                isDoorTriggered = true;
                bossDoorTimer = 0.3f;
            }

            if (isDoorTriggered && bossDoorTimer > 0) {
                bossDoorTimer -= delta;
                if (bossDoorTimer <= 0) {
                    gameModel.isBossActive = true;
                    if (audioSystem.bossActivateSound != null) audioSystem.bossActivateSound.play(currentSfxVolume);
                }
            }
        }

        if (gameModel.bossDoorBlock != null) {
            gameModel.bossDoorBlock.isActive = gameModel.isBossActive && !player.noclipMode;
        }

        if (gameModel.falseKnight != null) {
            falseKnightController.update(gameModel.falseKnight, player, delta, collisionBlocks, audioSystem.bossRoars, audioSystem.bossJump, audioSystem.bossLand, audioSystem.bossSlam, audioSystem.bossSwing, currentSfxVolume);
        }

        loopUpdater.updateEnemies(gameModel, delta, collisionBlocks);
        loopUpdater.updateProjectiles(gameModel, delta, collisionBlocks, audioSystem, currentSfxVolume);
        loopUpdater.updateSpells(gameModel, delta, collisionBlocks);
        loopUpdater.updatePlayerTimers(player, delta);

        actionController.updateStates(player, delta, movementController, collisionBlocks);
        spellController.updateCastingState(player, delta);
        movementController.handlePhysics(player, delta, settingsController, collisionBlocks);

        if (player.currentState != PlayerModel.State.CASTING) {
            focusController.handleFocus(player, delta, settingsController, audioSystem.focusReadySound, audioSystem.focusChargingSound, audioSystem.focusHealSound);
        }

        if (!player.isFocusing && player.currentState != PlayerModel.State.HURT && player.currentState != PlayerModel.State.CASTING) {
            movementController.handleMovement(player, delta, settingsController, collisionBlocks);
            actionController.handleActions(player, settingsController, audioSystem.dashSound, audioSystem.attackSound, () -> collisionSystem.checkAttackCollision(player, gameModel, audioSystem));
            spellController.handleSpells(player, gameModel, settingsController);
        }

        collisionSystem.checkEnemyCollisions(player, gameModel, audioSystem, currentSfxVolume);
        collisionSystem.checkSpikeCollisions(player, collisionBlocks, audioSystem, currentSfxVolume);
    }

    private void handleGameStateInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (gameModel.currentGameState == GameModel.GameState.PLAYING) {
                gameModel.currentGameState = GameModel.GameState.PAUSED;
            } else if (gameModel.currentGameState == GameModel.GameState.PAUSED) {
                gameModel.currentGameState = GameModel.GameState.PLAYING;
            }
        }
    }

    public float getBrightness() {
        return settingsController.getBrightness();
    }

    public void dispose() {
        focusController.stopChargingSound(audioSystem.focusChargingSound);
        audioSystem.dispose();
    }
}
