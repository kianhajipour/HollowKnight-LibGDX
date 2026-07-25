package io.github.some_example_name.view.menu;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import io.github.some_example_name.controller.PauseMenuController;
import io.github.some_example_name.view.util.Show;

public class PauseMenu extends Table {
    private final PauseMenuController controller;

    public PauseMenu(Skin skin, final PauseMenuController controller) {
        this.controller = controller;

        this.setFillParent(true);
        this.center();

        TextButton resumeButton = new TextButton(Show.get("Resume"), skin);
        TextButton settingBotton = new TextButton(Show.get("Settings") , skin);
        TextButton cheatsButton = new TextButton(Show.get("Cheats"), skin);
        TextButton exitButton = new TextButton(Show.get("Exit"), skin);

        this.add(resumeButton).width(200).height(50).padBottom(10).row();
        this.add(settingBotton).width(200).height(50).padBottom(10).row();
        this.add(cheatsButton).width(200).height(50).padBottom(10).row();
        this.add(exitButton).width(200).height(50);

        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.resumeGame();
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.exitGame();
            }
        });

        settingBotton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.goSetting();
            }
        });

        cheatsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.openCheats();
            }
        });
    }
}
