package io.github.some_example_name.view.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import io.github.some_example_name.controller.GuideController;
import io.github.some_example_name.view.util.Show;

public class GuideMenu extends Table {

    private final GuideController guideController;
    private Table mainButtonsTable;
    private Table contentOverlayTable;
    private final Skin skin;

    public GuideMenu(Skin skin, GuideController controller) {
        this.skin = skin;
        this.guideController = controller;
        this.setFillParent(true);
        setupUI();
    }

    private void setupUI() {
        this.clear();

        Texture bgTexture = new Texture(Gdx.files.internal("menu ui/Menu_Background.png"));
        this.setBackground(new TextureRegionDrawable(new TextureRegion(bgTexture)));

        mainButtonsTable = new Table();
        mainButtonsTable.setFillParent(true);
        mainButtonsTable.center().pad(30);
        this.addActor(mainButtonsTable);

        TextButton abilitisbtn = new TextButton(Show.get("Abilities"), skin);
        abilitisbtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showContentPopup("Abilities");
            }
        });
        mainButtonsTable.add(abilitisbtn).width(300).padBottom(15).row();

        TextButton cheatbtn = new TextButton(Show.get("Cheat codes"), skin);
        cheatbtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showContentPopup("Cheat codes");
            }
        });
        mainButtonsTable.add(cheatbtn).width(300).padBottom(15).row();

        TextButton controlerbtn = new TextButton(Show.get("Controller guide"), skin);
        controlerbtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showContentPopup("Controller guide");
            }
        });
        mainButtonsTable.add(controlerbtn).width(300).padBottom(30).row();

        TextButton backbtn = new TextButton(Show.get("BACK"), skin);
        backbtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                guideController.backToMain();
            }
        });
        mainButtonsTable.add(backbtn).width(150);

        mainButtonsTable.getColor().a = 0f;
        mainButtonsTable.addAction(Actions.sequence(
            Actions.sequence(Actions.moveBy(0, 100), Actions.parallel(Actions.fadeIn(1.2f), Actions.moveBy(0, -100, 1.5f, Interpolation.swingOut)))
        ));
    }

    private void showContentPopup(String contentType) {
        mainButtonsTable.setVisible(false);

        contentOverlayTable = new Table();
        contentOverlayTable.setFillParent(true);
        contentOverlayTable.center().pad(50);
        this.addActor(contentOverlayTable);

        Table scrollContent = new Table();
        scrollContent.top().center();

        if (contentType.equals("Abilities")) {
            Label singleLabel = new Label(Show.get("GUIDE_ALL_ABILITIES_TEXT"), skin);
            singleLabel.setWrap(true);
            scrollContent.add(singleLabel).width(550).center().row();

        } else if (contentType.equals("Cheat codes")) {
            Label singleLabel = new Label(Show.get("GUIDE_ALL_CHEATS_TEXT"), skin);
            singleLabel.setWrap(true);
            scrollContent.add(singleLabel).width(550).center().row();

        } else if (contentType.equals("Controller guide")) {
            io.github.some_example_name.controller.SettingsController sc = guideController.getSettingsController();

            scrollContent.add(new Label(Show.get("MOVE_LEFT") + " : [ " + Input.Keys.toString(sc.getLeftKey()) + " ]", skin)).pad(10).center().row();
            scrollContent.add(new Label(Show.get("MOVE_RIGHT") + " : [ " + Input.Keys.toString(sc.getRightKey()) + " ]", skin)).pad(10).center().row();
            scrollContent.add(new Label(Show.get("JUMP") + " : [ " + Input.Keys.toString(sc.getJumpKey()) + " ]", skin)).pad(10).center().row();
            scrollContent.add(new Label(Show.get("ATTACK") + " : [ " + Input.Keys.toString(sc.getAttackKey()) + " ]", skin)).pad(10).center().row();
            scrollContent.add(new Label(Show.get("DASH") + " : [ " + Input.Keys.toString(sc.getDashKey()) + " ]", skin)).pad(10).center().row();
            scrollContent.add(new Label(Show.get("Focus") + " : [ " + Input.Keys.toString(sc.getFocusKey()) + " ]", skin)).pad(10).center().row();
            scrollContent.add(new Label(Show.get("VENGEFUL_SPIRIT") + " : [ " + Input.Keys.toString(sc.getVengefulSpiritKey()) + " ]", skin)).pad(10).center().row();
            scrollContent.add(new Label(Show.get("HOWLING_WRAITHS") + " : [ " + Input.Keys.toString(sc.getHowlingWraithsKey()) + " ]", skin)).pad(10).center().row();
        }

        ScrollPane scrollPane = new ScrollPane(scrollContent, skin);
        scrollPane.setFadeScrollBars(false);
        contentOverlayTable.add(scrollPane).size(600, 400).padBottom(20).row();

        TextButton innerBackBtn = new TextButton(Show.get("BACK"), skin);
        innerBackBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                contentOverlayTable.addAction(Actions.sequence(
                    Actions.parallel(Actions.fadeOut(0.3f), Actions.moveBy(0, -50, 0.3f, Interpolation.pow2In)),
                    Actions.run(() -> {
                        contentOverlayTable.remove();
                        mainButtonsTable.setVisible(true);
                        mainButtonsTable.getColor().a = 0f;
                        mainButtonsTable.addAction(Actions.sequence(
                            Actions.moveBy(0, 50),
                            Actions.parallel(Actions.fadeIn(0.4f), Actions.moveBy(0, -50, 0.4f, Interpolation.pow2Out))
                        ));
                    })
                ));
            }
        });
        contentOverlayTable.add(innerBackBtn).width(150);

        contentOverlayTable.getColor().a = 0f;
        contentOverlayTable.addAction(Actions.sequence(
            Actions.moveBy(0, 100),
            Actions.parallel(Actions.fadeIn(0.5f), Actions.moveBy(0, -100, 0.6f, Interpolation.pow2Out))
        ));
    }
}
