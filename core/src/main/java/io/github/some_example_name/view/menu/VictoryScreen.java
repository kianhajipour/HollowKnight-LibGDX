package io.github.some_example_name.view.menu;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.some_example_name.controller.StartMenuController;
import io.github.some_example_name.model.SaveData;
import io.github.some_example_name.model.SaveManager;

public class VictoryScreen implements Screen {
    private final Game game;
    private Stage stage;
    private Skin skin;

    public VictoryScreen(Game game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("Hollow Knight skin.json"));

        Table table = new Table();
        table.setFillParent(true);
        table.center();

        SaveData data = SaveManager.getInstance().getData();

        Label title = new Label("VICTORY!", skin);
        title.setColor(Color.GOLD);
        title.setFontScale(2f);

        int minutes = (int) (data.timePlayed / 60);
        int seconds = (int) (data.timePlayed % 60);
        String timeStr = String.format("%02d:%02d", minutes, seconds);

        Label deathsLabel = new Label("Total Deaths: " + data.deaths, skin);
        Label killsLabel = new Label("Enemies Killed: " + data.enemiesKilled, skin);
        Label timeLabel = new Label("Time Played: " + timeStr, skin);

        TextButton backButton = new TextButton("Back to Main Menu", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new StartGameMenu(new StartMenuController(game)));
            }
        });

        table.add(title).padBottom(40).row();
        table.add(deathsLabel).padBottom(10).row();
        table.add(killsLabel).padBottom(10).row();
        table.add(timeLabel).padBottom(30).row();
        table.add(backButton).width(250).height(50);

        stage.addActor(table);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
