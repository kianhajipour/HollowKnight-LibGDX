package io.github.some_example_name.model;

import com.badlogic.gdx.utils.Array;

public class SaveData {
    public int slotNumber = 1;
    public int masks = 5;
    public float soul = 99f;
    public float playerX = 100f;
    public float playerY = 200f;
    public String currentEnvironment = "Tutorial";

    public boolean isBossActive = false;
    public boolean isBossDoorActive = false;
    public float bossX = 0f;
    public float bossY = 0f;
    public int bossHp = 60;
    public String bossState = "IDLE";

    public Array<EnemySaveState> enemyStates = new Array<>();

    public int deaths = 0;
    public int enemiesKilled = 0;
    public float timePlayed = 0f;
    public boolean[] killedEnemyTypes = new boolean[6];

    public boolean achCompletion = false;
    public boolean achSpeedrun = false;
    public boolean achTrueHunter = false;
    public boolean achFalseKnight = false;

    public SaveData() {
    }

    public static class EnemySaveState {
        public String type;
        public float x;
        public float y;
        public int hp;
        public String state;
        public float hbWidth;
        public float hbHeight;
        public float renderWidth;
        public float renderHeight;
        public float speed;
        public boolean isFlying;
    }
}
