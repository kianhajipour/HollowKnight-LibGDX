package io.github.some_example_name.controller;

import com.badlogic.gdx.Game;
import io.github.some_example_name.view.menu.MainMenuScreen;

public class AchiveController {
    private final Game game;
    public AchiveController(Game game) {
        this.game = game;
    }
    public void backToMain() {
        game.setScreen(new MainMenuScreen(new MainMenuController(game)));
    }
}
