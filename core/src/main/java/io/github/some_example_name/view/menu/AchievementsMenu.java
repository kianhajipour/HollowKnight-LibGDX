package io.github.some_example_name.view.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import io.github.some_example_name.controller.AchiveController;
import io.github.some_example_name.view.util.AudioManager;
import io.github.some_example_name.view.util.Show;
import io.github.some_example_name.model.SaveData;
import io.github.some_example_name.model.SaveManager;

import static io.github.some_example_name.view.util.AspectRatio.aspectRatio;

public class AchievementsMenu extends BaseMenuScreen {

    private final AchiveController achiveController;
    private final Array<Texture> allocatedTextures = new Array<>();
    Table achieveTable = new Table();
    public AchievementsMenu(AchiveController controller) {
        super();
        this.achiveController = controller;
        setupUI();
    }

    @Override
    protected void setupUI() {
        skin = new Skin(Gdx.files.internal("Hollow Knight skin.json"));

        Texture bgTexture = new Texture(Gdx.files.internal("menu ui/Menu_Background.png"));
        allocatedTextures.add(bgTexture);
        Image backgroundImage = new Image(bgTexture);
        backgroundImage.setSize(stage.getViewport().getWorldWidth(), stage.getViewport().getWorldHeight());
        stage.getActors().insert(0, backgroundImage);

        Table mainButtonsTable = new Table();
        mainButtonsTable.setFillParent(true);
        mainButtonsTable.center().pad(30);
        stage.addActor(mainButtonsTable);

        achieveTable.top().left().pad(10);

        SaveData data = SaveManager.getInstance().getData();

        createAchievementCard(Show.get("Completion"), Show.get("Finish the game"),
            "achievements/achievement_pure_completion.png", data.achCompletion);

        createAchievementCard(Show.get("Speedrun"), Show.get("Finish the game within a specified time limit"),
            "achievements/achievement_fast_finish.png", data.achSpeedrun);

        createAchievementCard(Show.get("True Hunter"), Show.get("Kill all types of enemies in the game"),
            "achievements/achievement_Hunter_Marks.png", data.achTrueHunter);

        createAchievementCard(Show.get("Defeat False Knight"), Show.get("Defeat the False Knight boss"),
            "achievements/achievement_false_knight.png", data.achFalseKnight);

        ScrollPane achieveScroll = new ScrollPane(achieveTable, skin);
        achieveScroll.setFadeScrollBars(false);
        mainButtonsTable.add(achieveScroll).size(850, 400).center().padBottom(20).row();

        TextButton backbtn = new TextButton(Show.get("BACK"), skin);
        backbtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                achiveController.backToMain();
            }
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor fromActor) {
                if (pointer == -1) {
                    AudioManager.playHover();
                }
            }
        });
        mainButtonsTable.add(backbtn).width(150);

        mainButtonsTable.getColor().a = 0f;
        mainButtonsTable.addAction(Actions.sequence(
            Actions.moveBy(0, 100),
            Actions.parallel(Actions.fadeIn(1.2f), Actions.moveBy(0, -100, 1.5f, Interpolation.swingOut))
        ));
    }

    private Table createAchievementCard(String nameKey, String detailKey, String imagePath, boolean isUnlocked) {
        Table card = new Table();
        card.left().pad(10);

        Texture tx = new Texture(Gdx.files.internal(imagePath));
        allocatedTextures.add(tx);
        Image img = new Image(tx);

        if (!isUnlocked) {
            img.setColor(0.2f, 0.2f, 0.2f, 1f);
        }

        Label nameLabel = new Label(Show.get(nameKey), skin);
        Label detailLabel = new Label(Show.get(detailKey), skin);

        Table textTable = new Table();
        textTable.left();
        textTable.add(nameLabel).left().row();
        textTable.add(detailLabel).left().padTop(10);

        card.add(img).left().size(100, aspectRatio(100, img)).padRight(20);
        card.add(textTable).left().expandX().fillX();
        achieveTable.add(card).expandX().fillX().padBottom(15).row();

        return card;
    }

    @Override
    public void dispose() {
        super.dispose();
        for (Texture tx : allocatedTextures) {
            if (tx != null) tx.dispose();
        }
        allocatedTextures.clear();
    }
}
