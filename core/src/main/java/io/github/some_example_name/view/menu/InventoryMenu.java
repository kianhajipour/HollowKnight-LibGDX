package io.github.some_example_name.view.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import io.github.some_example_name.controller.game.CharmController;
import io.github.some_example_name.model.PlayerModel;
import io.github.some_example_name.view.util.Show;

public class InventoryMenu extends Table {
    private final Runnable onClose;
    private final CharmController charmController;
    private final PlayerModel player;
    private final Skin skin;
    private Label notchLabel;

    public InventoryMenu(Skin skin, PlayerModel player, CharmController charmController, Runnable onClose) {
        this.skin = skin;
        this.player = player;
        this.charmController = charmController;
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
        innerTable.defaults().pad(10).fillX();

        addCharmRow(innerTable, "Soul Catcher", "charms/Soul Catcher.png", "Soul Catcher Desc");
        addCharmRow(innerTable, "Dashmaster", "charms/Dashmaster.png", "Dashmaster Desc");
        addCharmRow(innerTable, "Unbreakable Strength", "charms/Unbreakable.png", "Unbreakable Strength Desc");
        addCharmRow(innerTable, "Quick Slash", "charms/Quick Slash.png", "Quick Slash Desc");
        addCharmRow(innerTable, "Quick Focus", "charms/Quick Focus.png", "Quick Focus Desc");

        ScrollPane scrollPane = new ScrollPane(innerTable, skin);
        scrollPane.setFadeScrollBars(false);

        Table mainLayout = new Table();
        mainLayout.setFillParent(true);
        mainLayout.center().pad(40);

        Label titleLabel = new Label(Show.get("INVENTORY & CHARMS"), skin);
        mainLayout.add(titleLabel).padBottom(10).row();

        notchLabel = new Label(Show.get("Notches: ") + player.usedNotches + " / " + player.MAX_NOTCHES, skin);
        mainLayout.add(notchLabel).padBottom(25).row();

        mainLayout.add(scrollPane).width(750).height(400).row();

        TextButton backBtn = new TextButton(Show.get("BACK"), skin);
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (onClose != null) onClose.run();
            }
        });
        mainLayout.add(backBtn).width(180).padTop(25);

        this.add(mainLayout).grow();
    }

    private void addCharmRow(Table table, final String name, String imagePath, String descKey) {
        Table rowContent = new Table();
        rowContent.left();

        Image icon = new Image(new Texture(Gdx.files.internal(imagePath)));
        Label nameLabel = new Label(Show.get(name), skin);
        Label descLabel = new Label(Show.get(descKey), skin);
        descLabel.setWrap(true);

        Table textTable = new Table();
        textTable.left();
        textTable.add(nameLabel).left().row();
        textTable.add(descLabel).left().width(380).padTop(4);

        final TextButton equipBtn = new TextButton(isEquipped(name) ? Show.get("UNEQUIP") : Show.get("EQUIP"), skin);

        rowContent.add(icon).size(64, 64).padRight(15);
        rowContent.add(textTable).left().expandX().fillX();
        rowContent.add(equipBtn).right().width(130);

        table.add(rowContent).expandX().fillX().padBottom(15).row();

        equipBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                charmController.toggleCharm(name);
                equipBtn.setText(isEquipped(name) ? Show.get("UNEQUIP") : Show.get("EQUIP"));
                notchLabel.setText(Show.get("Notches: ") + player.usedNotches + " / " + player.MAX_NOTCHES);
            }
        });
    }

    private boolean isEquipped(String name) {
        switch (name) {
            case "Soul Catcher": return player.charmSoulCatcher;
            case "Dashmaster": return player.charmDashmaster;
            case "Unbreakable Strength": return player.charmUnbreakableStrength;
            case "Quick Slash": return player.charmQuickSlash;
            case "Quick Focus": return player.charmQuickFocus;
            default: return false;
        }
    }
}
