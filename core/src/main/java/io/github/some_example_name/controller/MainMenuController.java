package io.github.some_example_name.controller;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import io.github.some_example_name.view.menu.AchievementsMenu;
import io.github.some_example_name.view.menu.StartGameMenu;

public class MainMenuController {
    private Game game;
    private Runnable onSettingsRequested;
    private Runnable onGuideRequested;

    public MainMenuController(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public void setOnSettingsRequested(Runnable onSettingsRequested) {
        this.onSettingsRequested = onSettingsRequested;
    }

    public void setOnGuideRequested(Runnable onGuideRequested) {
        this.onGuideRequested = onGuideRequested;
    }

    public void startGame() {
        game.setScreen(new StartGameMenu(new StartMenuController(game)));
    }

    public void openSettings() {
        if (onSettingsRequested != null) {
            onSettingsRequested.run();
        }
    }

    public void openAchievements() {
        game.setScreen(new AchievementsMenu(new AchiveController(game)));
    }

    public void openGuide(){
        if (onGuideRequested != null) {
            onGuideRequested.run();
        }
    }

    public void quitGame() {
        Gdx.app.exit();
    }

    public String getText(String key) {
        switch (key) {
            case "START_GAME": return "START GAME";
            case "OPTIONS": return "OPTIONS";
            case "ACHIEVEMENTS": return "ACHIEVEMENTS";
            case "EXTRAS": return "EXTRAS";
            case "QUIT_GAME": return "QUIT GAME";
            default: return "";
        }
    }
}
