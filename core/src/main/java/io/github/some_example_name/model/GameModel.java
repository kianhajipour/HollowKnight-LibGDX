package io.github.some_example_name.model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import io.github.some_example_name.view.game.SolidBlock;

public class GameModel {
    public enum GameState { PLAYING, PAUSED, GAME_OVER, VICTORY, DIALOGUE }
    public GameState currentGameState = GameState.PLAYING;

    private final PlayerModel playerModel;
    public Array<EnemyModel> enemies;
    public Array<ProjectileModel> projectiles;
    public Array<SpellModel> playerSpells;
    public ZoteModel zote;
    public FalseKnightModel falseKnight;

    public boolean isBossActive = false;
    public Rectangle bossTriggerArea = null;
    public SolidBlock bossDoorBlock = null;
    public Vector2 bossSpawnPos = new Vector2();

    public GameModel(float playerStartX, float playerStartY) {
        this.playerModel = new PlayerModel(playerStartX, playerStartY);
        this.enemies = new Array<>();
        this.projectiles = new Array<>();
        this.playerSpells = new Array<>();
    }

    public PlayerModel getPlayerModel() {
        return playerModel;
    }
}
