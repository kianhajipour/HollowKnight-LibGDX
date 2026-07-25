package io.github.some_example_name.view.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import io.github.some_example_name.controller.MainMenuController;
import io.github.some_example_name.controller.SettingsController;
import io.github.some_example_name.controller.GuideController;
import io.github.some_example_name.view.util.AudioManager;
import io.github.some_example_name.view.util.Show;

public class MainMenuScreen extends BaseMenuScreen {

    private final MainMenuController controller;
    private SettingsMenu settingsMenu;
    private SettingsController settingsController;
    private GuideMenu guideMenu;
    private GuideController guideController;

    public MainMenuScreen(MainMenuController controller) {
        super();
        this.controller = controller;
        setupUI();
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    protected void setupUI() {
        skin = new Skin(Gdx.files.internal("Hollow Knight skin.json"));

        Texture bgTexture = new Texture(Gdx.files.internal("menu ui/Menu_Background.png"));
        Image backgroundImage = new Image(bgTexture);
        backgroundImage.setSize(stage.getViewport().getWorldWidth(), stage.getViewport().getWorldHeight());
        stage.getActors().insert(0, backgroundImage);

        Table logoTable = new Table();
        logoTable.setFillParent(true);
        logoTable.top().padTop(50);
        Texture logoTexture = new Texture(Gdx.files.internal("menu ui/vheart_title.png"));
        Image logoImage = new Image(logoTexture);
        logoTable.add(logoImage).size(600, aspectRatio(600, logoImage));
        stage.addActor(logoTable);

        logoTable.getColor().a = 0f;
        logoTable.addAction(Actions.sequence(
            Actions.moveBy(0, 150),
            Actions.parallel(Actions.fadeIn(1.2f), Actions.moveBy(0, -150, 1.8f, Interpolation.swingOut)),
            Actions.forever(Actions.sequence(Actions.moveBy(0, 15f, 2f, Interpolation.sine), Actions.moveBy(0, -15f, 2f, Interpolation.sine)))
        ));

        Table menuTable = new Table();
        menuTable.setFillParent(true);
        menuTable.center().padTop(150);

        TextButton startButton = new TextButton(Show.get("START GAME"), skin);
        menuTable.add(startButton).padBottom(15).row();

        TextButton optionsButton = new TextButton(Show.get("SETTINGS"), skin);
        menuTable.add(optionsButton).padBottom(15).row();

        TextButton achievementsButton = new TextButton(Show.get("ACHIEVEMENTS"), skin);
        menuTable.add(achievementsButton).padBottom(15).row();

        TextButton guideButton = new TextButton(Show.get("GUIDE"), skin);
        menuTable.add(guideButton).padBottom(15).row();

        TextButton quitButton = new TextButton(Show.get("QUIT GAME"), skin);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) { controller.quitGame(); }
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1) {
                    AudioManager.playHover();
                }
            }
        });
        menuTable.add(quitButton).row();
        stage.addActor(menuTable);

        menuTable.getColor().a = 0f;
        menuTable.addAction(Actions.sequence(Actions.moveBy(0, 100), Actions.parallel(Actions.fadeIn(1.2f), Actions.moveBy(0, -100, 1.5f, Interpolation.swingOut))));

        Table footerTable = new Table();
        footerTable.setFillParent(true);
        footerTable.bottom().pad(40);
        Image logoImageL = new Image(new Texture(Gdx.files.internal("menu ui/menulogoL.png")));
        Image logoImageR = new Image(new Texture(Gdx.files.internal("menu ui/menulogoR.png")));
        footerTable.add(logoImageL).left().expandX().size(100, aspectRatio(100, logoImageL));
        footerTable.add(logoImageR).right().size(100, aspectRatio(100, logoImageR));
        stage.addActor(footerTable);

        Skin defaultSkin = new Skin(Gdx.files.internal("uiskin.json"));

        settingsController = new SettingsController(controller.getGame());
        settingsMenu = new SettingsMenu(defaultSkin, settingsController);
        stage.addActor(settingsMenu);
        settingsMenu.setVisible(false);

        guideController = new GuideController(controller.getGame());
        guideMenu = new GuideMenu(defaultSkin, guideController);
        stage.addActor(guideMenu);
        guideMenu.setVisible(false);

        settingsController.setOnCloseCallback(() -> {
            settingsMenu.setVisible(false);
            menuTable.setVisible(true);
            logoTable.setVisible(true);
            footerTable.setVisible(true);
        });

        guideController.setOnCloseCallback(() -> {
            guideMenu.setVisible(false);
            menuTable.setVisible(true);
            logoTable.setVisible(true);
            footerTable.setVisible(true);
        });

        controller.setOnGuideRequested(() -> {
            menuTable.setVisible(false);
            logoTable.setVisible(false);
            footerTable.setVisible(false);
            guideMenu.setVisible(true);
        });

        optionsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menuTable.setVisible(false);
                logoTable.setVisible(false);
                footerTable.setVisible(false);
                settingsMenu.setVisible(true);
            }
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1) {
                    AudioManager.playHover();
                }
            }
        });

        guideButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.openGuide();
            }
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1) {
                    AudioManager.playHover();
                }
            }
        });

        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                exitScreen(() -> controller.startGame(), menuTable, logoTable, footerTable);
            }
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1) {
                    AudioManager.playHover();
                }
            }
        });

        achievementsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                exitScreen(() -> controller.openAchievements(), menuTable, logoTable, footerTable);
            }
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1) {
                    AudioManager.playHover();
                }
            }
        });

        footerTable.getColor().a = 0f;
        footerTable.addAction(Actions.sequence(Actions.moveBy(0, -100), Actions.parallel(Actions.fadeIn(1.2f), Actions.moveBy(0, 100, 1.5f, Interpolation.swingOut))));
    }

    public com.badlogic.gdx.scenes.scene2d.Stage getStage() {
        return stage;
    }

    public float aspectRatio(float width, Image image) {
        return width * (image.getHeight() / image.getWidth());
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
