package io.github.some_example_name.view.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import io.github.some_example_name.model.GameModel;
import io.github.some_example_name.model.PlayerModel;
import io.github.some_example_name.view.util.Show;

public class CheatsMenu extends Table {

    private final Skin skin;
    private final GameModel gameModel;
    private final Vector2 bossSpawnPos;
    private final Runnable onClose;

    public CheatsMenu(Skin skin, GameModel gameModel, Vector2 bossSpawnPos, Runnable onClose) {
        this.skin = skin;
        this.gameModel = gameModel;
        this.bossSpawnPos = bossSpawnPos;
        this.onClose = onClose;
        this.setFillParent(true);
        setupUI();
    }

    private void setupUI() {
        this.clear();

        Texture bgTexture = new Texture(Gdx.files.internal("menu ui/Menu_Background.png"));
        this.setBackground(new TextureRegionDrawable(new TextureRegion(bgTexture)));

        Table innerTable = new Table(skin);
        innerTable.pad(30);
        innerTable.defaults().pad(15).fillX();

        addCheatButton(innerTable, "CH_TELEPORT", "CH_TELEPORT_DESC", () -> {
            PlayerModel p = gameModel.getPlayerModel();
            p.bounds.x = bossSpawnPos.x;
            p.bounds.y = bossSpawnPos.y;
            p.velocityY = 0;
        });

        addCheatToggle(innerTable, "CH_DOOR_OVERRIDE", "CH_DOOR_OVERRIDE_DESC", () -> {
            gameModel.isBossActive = !gameModel.isBossActive;
            return gameModel.isBossActive;
        }, gameModel.isBossActive);

        addCheatToggle(innerTable, "CH_NOCLIP", "CH_NOCLIP_DESC", () -> {
            PlayerModel p = gameModel.getPlayerModel();
            p.noclipMode = !p.noclipMode;
            if (p.noclipMode) {
                p.velocityY = 0;
            }
            return p.noclipMode;
        }, gameModel.getPlayerModel().noclipMode);

        addCheatButton(innerTable, "CH_EMERGENCY_HEAL", "CH_EMERGENCY_HEAL_DESC", () -> {
            PlayerModel p = gameModel.getPlayerModel();
            if (p.currentHp <= 0) {
                p.currentHp = 1;
                p.currentState = PlayerModel.State.IDLE;
            } else if (p.currentHp < p.maxHp) {
                p.currentHp++;
            }
        });

        addCheatButton(innerTable, "CH_REFILL_SOUL", "CH_REFILL_SOUL_DESC", () -> {
            gameModel.getPlayerModel().currentSoul = gameModel.getPlayerModel().MAX_SOUL;
        });

        addCheatToggle(innerTable, "CH_ONE_HIT", "CH_ONE_HIT_DESC", () -> {
            PlayerModel p = gameModel.getPlayerModel();
            p.cheatOneHitKill = !p.cheatOneHitKill;
            return p.cheatOneHitKill;
        }, gameModel.getPlayerModel().cheatOneHitKill);

        addCheatToggle(innerTable, "CH_GOD_MODE", "CH_GOD_MODE_DESC", () -> {
            PlayerModel p = gameModel.getPlayerModel();
            p.cheatGodMode = !p.cheatGodMode;
            return p.cheatGodMode;
        }, gameModel.getPlayerModel().cheatGodMode);

        ScrollPane scrollPane = new ScrollPane(innerTable, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);

        Table mainLayout = new Table();
        mainLayout.setFillParent(true);
        mainLayout.center().pad(40);

        Label titleLabel = new Label(Show.get("CHEAT CODES"), skin);
        mainLayout.add(titleLabel).padBottom(25).row();
        mainLayout.add(scrollPane).width(850).height(400).row();

        TextButton backBtn = new TextButton(Show.get("BACK"), skin);
        backBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (onClose != null) {
                    onClose.run();
                }
            }
        });
        mainLayout.add(backBtn).width(180).padTop(25);

        this.add(mainLayout).grow();
    }

    private void addCheatButton(Table table, String nameKey, String descKey, Runnable action) {
        Label nameLabel = new Label(Show.get(nameKey), skin);
        Label descLabel = new Label(Show.get(descKey), skin);
        TextButton activateBtn = new TextButton(Show.get("ACTIVATE"), skin);

        table.add(nameLabel).left().width(200);
        table.add(descLabel).left().expandX().padLeft(15);
        table.add(activateBtn).right().width(120).row();

        activateBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                action.run();
            }
        });
    }

    private void addCheatToggle(Table table, String nameKey, String descKey, java.util.function.Supplier<Boolean> toggleAction, boolean initialState) {
        Label nameLabel = new Label(Show.get(nameKey), skin);
        Label descLabel = new Label(Show.get(descKey), skin);
        final TextButton activateBtn = new TextButton(initialState ? Show.get("ON") : Show.get("OFF"), skin);

        table.add(nameLabel).left().width(200);
        table.add(descLabel).left().expandX().padLeft(15);
        table.add(activateBtn).right().width(120).row();

        activateBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean newState = toggleAction.get();
                activateBtn.setText(newState ? Show.get("ON") : Show.get("OFF"));
            }
        });
    }
}
