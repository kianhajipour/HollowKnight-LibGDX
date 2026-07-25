package io.github.some_example_name.view.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import io.github.some_example_name.controller.SettingsController;
import io.github.some_example_name.view.util.Show;

public class SettingControllerMenu extends BaseMenuScreen {
    private SettingsController settingController;
    private boolean isRebinding = false;
    private String activeAction = null;
    private TextButton activeBtn = null;
    private Table controlsTable;

    public SettingControllerMenu(SettingsController controller) {
        super();
        this.settingController = controller;
        setupUI();
    }

    @Override
    protected void setupUI() {
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        float worldWidth = stage.getViewport().getWorldWidth();
        float worldHeight = stage.getViewport().getWorldHeight();

        Texture bgTexture = new Texture(Gdx.files.internal("menu ui/Menu_Background.png"));
        Image backgroundImage = new Image(bgTexture);
        backgroundImage.setSize(worldWidth, worldHeight);
        stage.addActor(backgroundImage);

        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.bottom().pad(20);
        stage.addActor(mainTable);

        TextButton backbtn = new TextButton(Show.get("BACK"), skin);
        mainTable.add(backbtn).left().expandX().fill();

        backbtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!isRebinding) {
                    settingController.goBack();
                }
            }
        });

        TextButton resetbtn = new TextButton(Show.get("RESET"), skin);
        mainTable.add(resetbtn).right().width(140).fill();

        resetbtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!isRebinding) {
                    settingController.resetKeysToDefault();
                    refreshControlsTable();
                }
            }
        });

        controlsTable = new Table(skin);
        controlsTable.pad(20);
        controlsTable.defaults().pad(10).fillX();

        refreshControlsTable();

        ScrollPane controls = new ScrollPane(controlsTable, skin);

        float scrollWidth = 540f;
        float scrollHeight = 420f;
        float scrollX = (worldWidth - scrollWidth) / 2f;
        float scrollY = (worldHeight - scrollHeight) / 2f;
        controls.setBounds(scrollX, scrollY, scrollWidth, scrollHeight);

        stage.addActor(controls);

        stage.addListener(new com.badlogic.gdx.scenes.scene2d.InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (isRebinding && activeAction != null && activeBtn != null) {
                    settingController.updateKey(activeAction, keycode);
                    activeBtn.setText(Input.Keys.toString(keycode));
                    isRebinding = false;
                    activeAction = null;
                    activeBtn = null;
                    return true;
                }
                return false;
            }
        });
    }

    private void refreshControlsTable() {
        controlsTable.clearChildren();
        addKeyBindingRow(controlsTable, "MOVE_LEFT", settingController.getLeftKey());
        addKeyBindingRow(controlsTable, "MOVE_RIGHT", settingController.getRightKey());
        addKeyBindingRow(controlsTable, "JUMP", settingController.getJumpKey());
        addKeyBindingRow(controlsTable, "ATTACK", settingController.getAttackKey());
        addKeyBindingRow(controlsTable, "DASH", settingController.getDashKey());
        addKeyBindingRow(controlsTable, "Focus", settingController.getFocusKey());
        addKeyBindingRow(controlsTable, "VENGEFUL_SPIRIT", settingController.getVengefulSpiritKey());
        addKeyBindingRow(controlsTable, "HOWLING_WRAITHS", settingController.getHowlingWraithsKey());
    }

    private void addKeyBindingRow(Table table, final String action, int currentKeyCode) {
        Label actionLabel = new Label(Show.get(action), skin);
        final TextButton changeBtn = new TextButton(Input.Keys.toString(currentKeyCode), skin);

        table.add(actionLabel).left().expandX();
        table.add(changeBtn).right().width(140).row();

        changeBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!isRebinding) {
                    isRebinding = true;
                    activeAction = action;
                    activeBtn = changeBtn;
                    changeBtn.setText("Press Key...");
                }
            }
        });
    }
}
