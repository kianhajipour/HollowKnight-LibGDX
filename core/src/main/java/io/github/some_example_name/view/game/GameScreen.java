package io.github.some_example_name.view.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import io.github.some_example_name.controller.GameController;
import io.github.some_example_name.model.EnemyModel;
import io.github.some_example_name.model.GameModel;


public class GameScreen implements Screen {
    private final Game game;
    private final GameController controller;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private GameModel gameModel;
    private PlayerView playerView;
    private PlayerHealthView playerHealthView;
    private EnemyView enemyView;
    private SpellView spellView;
    private ZoteView zoteView;
    private FalseKnightView bossView;
    private SpriteBatch batch;
    private final Matrix4 hudMatrix;
    private BitmapFont defaultFont;
    private ShapeRenderer shapeRenderer;

    private final MapLoaderHelper mapLoaderHelper;
    private final ScreenUIManager uiManager;
    private boolean isInitialized = false;

    public GameScreen(Game game, GameController controller) {
        this.game = game;
        this.controller = controller;
        this.hudMatrix = new Matrix4();
        this.mapLoaderHelper = new MapLoaderHelper();
        this.uiManager = new ScreenUIManager();
    }

    public Stage getUiStage() {
        return uiManager.getUiStage();
    }

    @Override
    public void show() {
        shapeRenderer = new ShapeRenderer();
        if (!isInitialized) {
            mapLoaderHelper.loadMapData();
            gameModel = mapLoaderHelper.getGameModel();

            renderer = new OrthogonalTiledMapRenderer(mapLoaderHelper.getMap());
            camera = new OrthographicCamera();
            camera.setToOrtho(false, 400, 200);
            batch = new SpriteBatch();

            controller.init(gameModel);
            playerView = new PlayerView();
            playerHealthView = new PlayerHealthView();
            enemyView = new EnemyView();
            spellView = new SpellView();
            zoteView = new ZoteView();
            bossView = new FalseKnightView();

            uiManager.initUI(game, gameModel, mapLoaderHelper.getBossSpawnPos(), this);
            defaultFont = uiManager.getSkin().getFont("default-font");

            isInitialized = true;
        }
        Gdx.input.setInputProcessor(uiManager.getUiStage());
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        handleInput();

        controller.update(delta, mapLoaderHelper.getCollisionBlocks());

        camera.position.set(gameModel.getPlayerModel().bounds.x, gameModel.getPlayerModel().bounds.y, 0);
        camera.update();

        MapLayer bDoorLayer = mapLoaderHelper.getMap().getLayers().get("bossdoor");
        if (bDoorLayer != null) {
            bDoorLayer.setVisible(gameModel.isBossActive);
        }

        renderer.setView(camera);
        renderer.render();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        float animationDelta = (gameModel.currentGameState == GameModel.GameState.PAUSED) ? 0f : delta;

        if (gameModel.zote != null) {
            zoteView.draw(batch, gameModel.zote, animationDelta);
            if (gameModel.zote.isPlayerInRange && !gameModel.zote.isDialogueActive && gameModel.currentGameState == GameModel.GameState.PLAYING) {
                defaultFont.draw(batch, "Press UP to Interact", gameModel.zote.bounds.x - 30f, gameModel.zote.bounds.y + 160f);
            }
        }

        playerView.draw(batch, gameModel.getPlayerModel(), animationDelta);

        for (EnemyModel enemy : gameModel.enemies) {
            enemyView.draw(batch, enemy, animationDelta);
        }

        enemyView.drawProjectiles(batch, gameModel);
        spellView.draw(batch, gameModel, animationDelta);

        if (gameModel.falseKnight != null) {
            bossView.draw(batch, gameModel.falseKnight);
        }

        batch.end();

        hudMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.setProjectionMatrix(hudMatrix);
        batch.begin();

        playerHealthView.draw(batch, gameModel.getPlayerModel(), 20f, Gdx.graphics.getHeight() - 80f);

        batch.end();

        float brightness = controller.getBrightness();
        if (brightness != 0.5f) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.setProjectionMatrix(hudMatrix);
            shapeRenderer.begin(com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled);
            if (brightness < 0.5f) {
                float alpha = (0.5f - brightness) * 2f;
                shapeRenderer.setColor(0f, 0f, 0f, alpha * 0.8f);
            } else {
                float alpha = (brightness - 0.5f) * 2f;
                shapeRenderer.setColor(1f, 1f, 1f, alpha * 0.4f);
            }
            shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }

        uiManager.updateUIVisibility(gameModel);

        uiManager.getUiStage().act(delta);
        uiManager.getUiStage().draw();
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            if (gameModel.currentGameState == GameModel.GameState.PLAYING) {
                gameModel.currentGameState = GameModel.GameState.PAUSED;
                uiManager.getInventoryMenu().setVisible(true);
            } else if (gameModel.currentGameState == GameModel.GameState.PAUSED && uiManager.getInventoryMenu().isVisible()) {
                uiManager.getInventoryMenu().setVisible(false);
                gameModel.currentGameState = GameModel.GameState.PLAYING;
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
        uiManager.getUiStage().getViewport().update(width, height, true);
        uiManager.getDialogueTable().setPosition(width / 2f - 320f, 50f);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        if (mapLoaderHelper.getMap() != null) mapLoaderHelper.getMap().dispose();
        if (renderer != null) renderer.dispose();
        if (playerView != null) playerView.dispose();
        if (playerHealthView != null) playerHealthView.dispose();
        if (spellView != null) spellView.dispose();
        if (zoteView != null) zoteView.dispose();
        if (bossView != null) bossView.dispose();
        if (batch != null) batch.dispose();
        if (shapeRenderer != null) shapeRenderer.dispose();
        uiManager.dispose();
    }
}
