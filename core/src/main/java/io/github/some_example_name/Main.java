package io.github.some_example_name;

import io.github.some_example_name.view.menu.MainMenuScreen;
import com.badlogic.gdx.Game;
import io.github.some_example_name.controller.MainMenuController;
import io.github.some_example_name.controller.SettingsController;


public class Main extends Game {
    @Override
    public void create() {

        SettingsController settingsController = new SettingsController(this);

        MainMenuController mainMenuController = new MainMenuController(this);

        setScreen(new MainMenuScreen(mainMenuController));
    }
}
