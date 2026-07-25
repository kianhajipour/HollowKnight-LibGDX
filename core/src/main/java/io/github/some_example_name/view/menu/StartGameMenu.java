package io.github.some_example_name.view.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import io.github.some_example_name.controller.StartMenuController;
import io.github.some_example_name.view.util.AnimationHelper;
import io.github.some_example_name.view.util.AspectRatio;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import io.github.some_example_name.view.util.Show;
import io.github.some_example_name.model.SaveManager;

public class StartGameMenu extends BaseMenuScreen {
    private final StartMenuController startController;

    public StartGameMenu(StartMenuController controller) {
        super();
        this.startController = controller;
        setupUI();
    }

    @Override
    protected void setupUI() {
        skin = new Skin(Gdx.files.internal("Hollow Knight skin.json"));
        Texture bgTexture = new Texture(Gdx.files.internal("menu ui/Menu_Background.png"));
        Image backgroundImage = new Image(bgTexture);
        backgroundImage.setSize(stage.getViewport().getWorldWidth(), stage.getViewport().getWorldHeight());
        stage.getActors().insert(0, backgroundImage);

        Table saveSlotsTable = new Table();
        Table animationTable = new Table();
        animationTable.setFillParent(true);
        animationTable.top().pad(30);
        saveSlotsTable.setFillParent(true);
        saveSlotsTable.center();

        for (int i = 1; i <= 4; i++) {
            final int slotIndex = i;
            boolean exists = SaveManager.getInstance().doesSaveExist(slotIndex);

            Table rowTable = new Table();

            if (exists) {
                TextButton loadButton = new TextButton(Show.get("Load Slot ") + slotIndex, skin);
                TextButton overwriteButton = new TextButton(Show.get("Overwrite ") + slotIndex, skin);

                loadButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        SaveManager.getInstance().load(slotIndex);
                        startController.startNewGame(slotIndex);
                    }
                });

                overwriteButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        SaveManager.getInstance().resetToDefaults(slotIndex);
                        startController.startNewGame(slotIndex);
                    }
                });

                rowTable.add(loadButton).width(200).height(50).padRight(10);
                rowTable.add(overwriteButton).width(200).height(50);
            } else {
                TextButton newGameButton = new TextButton(Show.get("New Game Slot ") + slotIndex, skin);
                newGameButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        SaveManager.getInstance().resetToDefaults(slotIndex);
                        startController.startNewGame(slotIndex);
                    }
                });
                rowTable.add(newGameButton).width(410).height(50);
            }

            saveSlotsTable.add(rowTable).padBottom(15).row();
        }

        TextButton backButton = new TextButton(Show.get("BACK"), skin);
        saveSlotsTable.add(backButton).width(410).height(50).padTop(15).row();

        backButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                startController.backToMain();
            }
        });

        final Animation<TextureRegion> fleurAnimation = AnimationHelper.createFromFiles(
            "menu ui/fleur_", ".png", 8, 0.1f, Animation.PlayMode.NORMAL
        );

        Image animatedFleur = new Image(fleurAnimation.getKeyFrame(0)) {
            private float stateTime = 0f;
            @Override
            public void act(float delta) {
                super.act(delta);
                stateTime += delta;
                ((TextureRegionDrawable) getDrawable()).setRegion(fleurAnimation.getKeyFrame(stateTime));
            }
        };

        animationTable.add(animatedFleur).padTop(20).size(300, AspectRatio.aspectRatio(300, animatedFleur)).row();
        animationTable.addAction(Actions.sequence(
            Actions.moveBy(0, 100),
            Actions.moveBy(0, -100, 1.2f, Interpolation.swingOut)
        ));

        stage.addActor(animationTable);
        stage.addActor(saveSlotsTable);
    }
}
