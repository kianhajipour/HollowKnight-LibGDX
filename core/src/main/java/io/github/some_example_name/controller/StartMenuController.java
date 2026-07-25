package io.github.some_example_name.controller;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.some_example_name.model.SaveData;
import io.github.some_example_name.view.game.GameScreen;
import io.github.some_example_name.view.menu.MainMenuScreen;

public class StartMenuController {
    private Game game;
    private Gson gson;
    private final String SAVE_PATH = "database/saveGame.json";

    public StartMenuController(Game game) {
        this.game = game;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public void backToMain() {
        game.setScreen(new MainMenuScreen(new MainMenuController(game)));
    }

    public SaveData startNewGame(int slotNumber) {
        SaveData newGame = new SaveData();
        newGame.slotNumber = slotNumber;
        saveGame(newGame);
        game.setScreen(new GameScreen(game, new GameController(game)));
        return newGame;
    }

    public void saveGame(SaveData data) {
        FileHandle file = Gdx.files.absolute(SAVE_PATH);
        file.writeString(gson.toJson(data), false);
    }
}
