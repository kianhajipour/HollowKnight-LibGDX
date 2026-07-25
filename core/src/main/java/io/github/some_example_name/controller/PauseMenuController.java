package io.github.some_example_name.controller;

import com.badlogic.gdx.Game;
import io.github.some_example_name.model.GameModel;
import io.github.some_example_name.model.PlayerModel;
import io.github.some_example_name.model.EnemyModel;
import io.github.some_example_name.model.SaveData;
import io.github.some_example_name.model.SaveManager;
import io.github.some_example_name.view.menu.StartGameMenu;

public class PauseMenuController {
    private final GameModel gameModel;
    private final Game game;
    private Runnable onSettingsRequested;
    private Runnable onCheatsRequested;

    public PauseMenuController(Game game, GameModel gameModel) {
        this.game = game;
        this.gameModel = gameModel;
    }

    public void setOnSettingsRequested(Runnable onSettingsRequested) {
        this.onSettingsRequested = onSettingsRequested;
    }

    public void setOnCheatsRequested(Runnable onCheatsRequested) {
        this.onCheatsRequested = onCheatsRequested;
    }

    public void resumeGame() {
        gameModel.currentGameState = GameModel.GameState.PLAYING;
    }

    public void exitGame() {
        if (gameModel != null && gameModel.getPlayerModel() != null) {
            PlayerModel player = gameModel.getPlayerModel();
            SaveData data = SaveManager.getInstance().getData();

            data.playerX = player.bounds.x;
            data.playerY = player.bounds.y;
            data.masks = player.currentHp;
            data.soul = player.currentSoul;

            data.isBossActive = gameModel.isBossActive;
            if (gameModel.bossDoorBlock != null) {
                data.isBossDoorActive = gameModel.bossDoorBlock.isActive;
            }
            if (gameModel.falseKnight != null) {
                data.bossX = gameModel.falseKnight.bounds.x;
                data.bossY = gameModel.falseKnight.bounds.y;
                data.bossHp = gameModel.falseKnight.currentHp;
                data.bossState = gameModel.falseKnight.currentState.name();
            }

            data.enemyStates.clear();
            for (EnemyModel enemy : gameModel.enemies) {
                SaveData.EnemySaveState es = new SaveData.EnemySaveState();
                es.type = enemy.type.name();
                es.x = enemy.bounds.x;
                es.y = enemy.bounds.y;
                es.hp = enemy.hp;
                es.state = enemy.currentState.name();
                es.hbWidth = enemy.bounds.width;
                es.hbHeight = enemy.bounds.height;
                es.renderWidth = enemy.renderWidth;
                es.renderHeight = enemy.renderHeight;
                es.speed = enemy.speed;
                es.isFlying = enemy.isFlying;
                data.enemyStates.add(es);
            }

            SaveManager.getInstance().save();
        }
        game.setScreen(new StartGameMenu(new StartMenuController(game)));
    }

    public void goSetting() {
        if (onSettingsRequested != null) {
            onSettingsRequested.run();
        }
    }

    public void openCheats() {
        if (onCheatsRequested != null) {
            onCheatsRequested.run();
        }
    }
}
