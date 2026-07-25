package io.github.some_example_name.view.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.some_example_name.controller.PauseMenuController;
import io.github.some_example_name.controller.SettingsController;
import io.github.some_example_name.controller.game.CharmController;
import io.github.some_example_name.model.GameModel;
import io.github.some_example_name.view.menu.CheatsMenu;
import io.github.some_example_name.view.menu.InventoryMenu;
import io.github.some_example_name.view.menu.PauseMenu;
import io.github.some_example_name.view.menu.SettingsMenu;
import io.github.some_example_name.view.util.Show;

public class ScreenUIManager {
    private Stage uiStage;
    private Skin skin;
    private PauseMenu pauseMenu;
    private SettingsMenu settingsMenu;
    private CheatsMenu cheatsMenu;
    private InventoryMenu inventoryMenu;
    private CharmController charmController;
    private Table dialogueTable;
    private Label dialogueLabel;
    private Texture dialogueBgTexture;

    public void initUI(Game game, GameModel gameModel, Vector2 bossSpawnPos, Screen previousScreen) {
        uiStage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        PauseMenuController pauseMenuController = new PauseMenuController(game, gameModel);
        pauseMenu = new PauseMenu(skin, pauseMenuController);

        SettingsController settingsController = new SettingsController(game);
        settingsController.setPreviousScreen(previousScreen);
        settingsMenu = new SettingsMenu(skin, settingsController);

        cheatsMenu = new CheatsMenu(skin, gameModel, bossSpawnPos, () -> {
            cheatsMenu.setVisible(false);
            pauseMenu.setVisible(true);
        });

        charmController = new CharmController(gameModel.getPlayerModel());
        inventoryMenu = new InventoryMenu(skin, gameModel.getPlayerModel(), charmController, () -> {
            inventoryMenu.setVisible(false);
            gameModel.currentGameState = GameModel.GameState.PLAYING;
        });

        pauseMenuController.setOnSettingsRequested(() -> {
            pauseMenu.setVisible(false);
            settingsMenu.setVisible(true);
        });

        pauseMenuController.setOnCheatsRequested(() -> {
            pauseMenu.setVisible(false);
            cheatsMenu.setVisible(true);
        });

        settingsController.setOnCloseCallback(() -> {
            settingsMenu.setVisible(false);
            pauseMenu.setVisible(true);
        });

        dialogueTable = new Table(skin);
        dialogueBgTexture = new Texture(Gdx.files.internal("zote/back.png"));
        dialogueTable.setBackground(new TextureRegionDrawable(new TextureRegion(dialogueBgTexture)));

        dialogueLabel = new Label("", skin);
        dialogueLabel.setWrap(true);
        dialogueTable.add(dialogueLabel).width(600).pad(20);
        dialogueTable.setVisible(false);
        dialogueTable.setPosition(Gdx.graphics.getWidth() / 2f - 320f, 50f);
        dialogueTable.setSize(640f, 150f);

        uiStage.addActor(pauseMenu);
        uiStage.addActor(settingsMenu);
        uiStage.addActor(cheatsMenu);
        uiStage.addActor(inventoryMenu);
        uiStage.addActor(dialogueTable);

        hideAllMenus();
    }

    public void hideAllMenus() {
        pauseMenu.setVisible(false);
        settingsMenu.setVisible(false);
        cheatsMenu.setVisible(false);
        inventoryMenu.setVisible(false);
    }

    public void updateUIVisibility(GameModel gameModel) {
        if (gameModel.zote != null && gameModel.zote.isDialogueActive) {
            dialogueTable.setVisible(true);
            dialogueLabel.setText(Show.get(gameModel.zote.currentDisplayedText));
        } else {
            dialogueTable.setVisible(false);
        }

        if (gameModel.currentGameState == GameModel.GameState.PAUSED) {
            if (!settingsMenu.isVisible() && !pauseMenu.isVisible() && !cheatsMenu.isVisible() && !inventoryMenu.isVisible()) {
                pauseMenu.setVisible(true);
            }
        } else if (gameModel.currentGameState == GameModel.GameState.PLAYING) {
            hideAllMenus();
        }
    }

    public Stage getUiStage() { return uiStage; }
    public Skin getSkin() { return skin; }
    public InventoryMenu getInventoryMenu() { return inventoryMenu; }
    public Table getDialogueTable() { return dialogueTable; }

    public void dispose() {
        if (uiStage != null) uiStage.dispose();
        if (dialogueBgTexture != null) dialogueBgTexture.dispose();
    }
}
