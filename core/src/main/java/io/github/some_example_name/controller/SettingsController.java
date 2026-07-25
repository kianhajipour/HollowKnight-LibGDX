package io.github.some_example_name.controller;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import io.github.some_example_name.model.SettingsManager;
import io.github.some_example_name.view.menu.SettingControllerMenu;
import io.github.some_example_name.view.util.Show;


public class SettingsController {
    private final Game game;
    private final SettingsManager manager;
    private Runnable onCloseCallback;
    private com.badlogic.gdx.Screen previousScreen;

    public SettingsController(Game game) {
        this.game = game;
        this.manager = Show.getManager();
    }

    public void setPreviousScreen(com.badlogic.gdx.Screen screen) {
        this.previousScreen = screen;
    }

    public float getBrightness() {
        return manager.getData().brightness;
    }

    public float getMusicVolume() {
        return manager.getData().musicVolume * manager.getData().masterVolume;
    }

    public float getSfxVolume() {
        return manager.getData().sfxVolume * manager.getData().masterVolume;
    }

    public void setOnCloseCallback(Runnable onCloseCallback) {
        this.onCloseCallback = onCloseCallback;
    }

    public SettingsManager getManager() {
        return manager;
    }

    public void toggleLanguage() {
        String current = manager.getData().language;
        manager.getData().language = current.equals("en") ? "de" : "en";
        manager.save();
    }

    public void goBack() {
        if (game.getScreen() instanceof SettingControllerMenu) {
            if (previousScreen != null) {
                game.setScreen(previousScreen);
                if (previousScreen instanceof io.github.some_example_name.view.game.GameScreen) {
                    Gdx.input.setInputProcessor(((io.github.some_example_name.view.game.GameScreen) previousScreen).getUiStage());
                } else if (previousScreen instanceof io.github.some_example_name.view.menu.MainMenuScreen) {
                    Gdx.input.setInputProcessor(((io.github.some_example_name.view.menu.MainMenuScreen) previousScreen).getStage());
                }
            }
        } else {
            if (onCloseCallback != null) {
                onCloseCallback.run();
            }
        }
    }

    public void gotocontrollerSettings() {
        if (game.getScreen() != null) {
            this.setPreviousScreen(game.getScreen());
        }
        game.setScreen(new SettingControllerMenu(this));
    }

    public int getLeftKey() {
        return manager.getData().keyBindings.getOrDefault("MOVE_LEFT", Input.Keys.LEFT);
    }

    public int getRightKey() {
        return manager.getData().keyBindings.getOrDefault("MOVE_RIGHT", Input.Keys.RIGHT);
    }

    public int getJumpKey() {
        return manager.getData().keyBindings.getOrDefault("JUMP", Input.Keys.SPACE);
    }

    public int getAttackKey() {
        return manager.getData().keyBindings.getOrDefault("ATTACK", Input.Keys.X);
    }

    public int getDashKey() {
        return manager.getData().keyBindings.getOrDefault("DASH", Input.Keys.C);
    }

    public int getFocusKey() {
        return manager.getData().keyBindings.getOrDefault("Focus", Input.Keys.A);
    }

    public int getVengefulSpiritKey() {
        return manager.getData().keyBindings.getOrDefault("VENGEFUL_SPIRIT", Input.Keys.Q);
    }

    public int getHowlingWraithsKey() {
        return manager.getData().keyBindings.getOrDefault("HOWLING_WRAITHS", Input.Keys.W);
    }

    public void updateKey(String action, int newKeyCode) {
        manager.getData().keyBindings.put(action, newKeyCode);
        manager.save();
    }

    public void resetKeysToDefault() {
        manager.getData().keyBindings.put("MOVE_LEFT", Input.Keys.LEFT);
        manager.getData().keyBindings.put("MOVE_RIGHT", Input.Keys.RIGHT);
        manager.getData().keyBindings.put("JUMP", Input.Keys.SPACE);
        manager.getData().keyBindings.put("ATTACK", Input.Keys.X);
        manager.getData().keyBindings.put("DASH", Input.Keys.C);
        manager.getData().keyBindings.put("Focus", Input.Keys.A);
        manager.getData().keyBindings.put("VENGEFUL_SPIRIT", Input.Keys.Q);
        manager.getData().keyBindings.put("HOWLING_WRAITHS", Input.Keys.W);
        manager.save();
    }

    public void resetAudioToDefaults() {
        manager.resetAudioToDefaults();
    }
}
