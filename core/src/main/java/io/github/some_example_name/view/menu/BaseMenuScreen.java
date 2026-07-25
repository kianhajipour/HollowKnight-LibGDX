package io.github.some_example_name.view.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.some_example_name.view.util.AudioManager;

public abstract class BaseMenuScreen implements Screen {
    protected Stage stage;
    protected Skin skin;

    public BaseMenuScreen() {
        this.stage = new Stage(new FitViewport(1280, 720));
        Gdx.input.setInputProcessor(stage);

        stage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (event.getTarget() instanceof Button || event.getTarget().getParent() instanceof Button) {
                    AudioManager.playClick();
                }
                return false;
            }
        });
    }

    protected abstract void setupUI();

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void show() {
        AudioManager.playMenuMusic();
    }

    protected void exitScreen(Runnable action, com.badlogic.gdx.scenes.scene2d.Actor... actors) {
        float duration = 0.8f;
        com.badlogic.gdx.math.Interpolation ease = com.badlogic.gdx.math.Interpolation.pow2In;

        for (com.badlogic.gdx.scenes.scene2d.Actor a : actors) {
            a.addAction(com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel(
                com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut(duration, ease),
                com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy(0, -100, duration, ease),
                com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleTo(0.9f, 0.9f, duration, ease)
            ));
        }

        stage.addAction(com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence(
            com.badlogic.gdx.scenes.scene2d.actions.Actions.delay(duration),
            com.badlogic.gdx.scenes.scene2d.actions.Actions.run(action)
        ));
    }

    @Override public void resize(int w, int h) { stage.getViewport().update(w, h, true); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() { if (stage != null) stage.dispose(); }
}
