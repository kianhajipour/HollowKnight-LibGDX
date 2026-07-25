package io.github.some_example_name.view.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import io.github.some_example_name.controller.SettingsController;
import io.github.some_example_name.view.util.AudioManager;
import io.github.some_example_name.view.util.Show;

public class SettingsMenu extends Table {

    private final SettingsController settingsController;
    private final Skin skin;
    private TextButton savebtn;

    public SettingsMenu(Skin skin, SettingsController controller) {
        this.skin = skin;
        this.settingsController = controller;
        this.setFillParent(true);
        setupUI();
    }

    private void setupUI() {
        this.clear();

        Texture bgTexture = new Texture(Gdx.files.internal("menu ui/Menu_Background.png"));
        this.setBackground(new TextureRegionDrawable(new TextureRegion(bgTexture)));

        Table slidertable = new Table();
        Table root = new Table();

        slidertable.center().pad(50);
        root.bottom().pad(100);

        Stack stack = new Stack();
        stack.add(slidertable);
        stack.add(root);

        this.add(stack).grow();

        slidertable.add(new Label(Show.get("Music Volume"), skin)).pad(10);
        Slider musicSlider = new Slider(0, 1, 0.1f, false, skin);
        musicSlider.setValue(settingsController.getManager().getData().musicVolume);
        musicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settingsController.getManager().getData().musicVolume = musicSlider.getValue();
                savebtn.setDisabled(false);
                AudioManager.updateVolume();
            }
        });
        slidertable.add(musicSlider).row();

        slidertable.add(new Label(Show.get("SFX Volume"), skin)).pad(10);
        Slider sfxSlider = new Slider(0, 1, 0.1f, false, skin);
        sfxSlider.setValue(settingsController.getManager().getData().sfxVolume);
        sfxSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settingsController.getManager().getData().sfxVolume = sfxSlider.getValue();
                savebtn.setDisabled(false);
            }
        });
        slidertable.add(sfxSlider).row();

        slidertable.add(new Label(Show.get("Brightness"), skin)).pad(10);
        Slider brightSlider = new Slider(0, 1, 0.1f, false, skin);
        brightSlider.setValue(settingsController.getManager().getData().brightness);
        brightSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settingsController.getManager().getData().brightness = brightSlider.getValue();
                savebtn.setDisabled(false);
            }
        });
        slidertable.add(brightSlider).row();

        TextButton resetAudio = new TextButton(Show.get("Reset Audio"), skin);
        resetAudio.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settingsController.resetAudioToDefaults();
                musicSlider.setValue(settingsController.getManager().getData().musicVolume);
                sfxSlider.setValue(settingsController.getManager().getData().sfxVolume);
                savebtn.setDisabled(false);
                AudioManager.updateVolume();
            }
        });
        slidertable.add(resetAudio).colspan(2).fillX().padTop(15).row();

        Label langLabel = new Label(Show.get("Language"), skin);
        slidertable.add(langLabel).colspan(2).padTop(20).center().row();

        Table langBtnTable = new Table();
        TextButton enBtn = new TextButton("English", skin);
        TextButton deBtn = new TextButton("Deutsch", skin);

        String currentLang = settingsController.getManager().getData().language;
        if ("en".equals(currentLang)) {
            enBtn.setDisabled(true);
        } else if ("de".equals(currentLang)) {
            deBtn.setDisabled(true);
        }

        enBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!settingsController.getManager().getData().language.equals("en")) {
                    settingsController.getManager().getData().language = "en";
                    savebtn.setDisabled(false);
                    setupUI();
                }
            }
        });

        deBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!settingsController.getManager().getData().language.equals("de")) {
                    settingsController.getManager().getData().language = "de";
                    savebtn.setDisabled(false);
                    setupUI();
                }
            }
        });

        langBtnTable.add(enBtn).padRight(10).uniformX().fillX();
        langBtnTable.add(deBtn).uniformX().fillX();
        slidertable.add(langBtnTable).colspan(2).fillX().padTop(5).row();

        TextButton controlbtn = new TextButton(Show.get("Game Controller") , skin);
        slidertable.add(controlbtn).colspan(2).fillX().padTop(25).row();

        controlbtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                settingsController.gotocontrollerSettings();
            }
        });

        TextButton backBtn = new TextButton(Show.get("BACK"), skin);
        backBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settingsController.goBack();
            }
        });
        root.add(backBtn).colspan(2).left().expandX().padTop(30);

        savebtn = new TextButton(Show.get("SAVE"), skin);
        savebtn.setDisabled(true);
        savebtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!savebtn.isDisabled()) {
                    settingsController.getManager().save();
                    savebtn.setDisabled(true);
                }
            }
        });
        root.add(savebtn).colspan(2).right().padTop(30);
    }
}
