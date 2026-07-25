package io.github.some_example_name.controller;

import com.badlogic.gdx.Game;
import io.github.some_example_name.view.menu.MainMenuScreen;

public class GuideController {
    private final Game game;
    private final SettingsController settingsController;
    private Runnable onCloseCallback;

    public GuideController(Game game) {
        this.game = game;
        this.settingsController = new SettingsController(game);
    }

    public void setOnCloseCallback(Runnable onCloseCallback) {
        this.onCloseCallback = onCloseCallback;
    }

    public void backToMain() {
        if (onCloseCallback != null) {
            onCloseCallback.run();
        } else {
            game.setScreen(new MainMenuScreen(new MainMenuController(game)));
        }
    }

    public Game getGame() {
        return game;
    }

    public SettingsController getSettingsController() {
        return settingsController;
    }
}
