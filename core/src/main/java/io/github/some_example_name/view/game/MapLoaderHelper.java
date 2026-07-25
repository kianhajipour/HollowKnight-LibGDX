package io.github.some_example_name.view.game;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import io.github.some_example_name.model.EnemyModel;
import io.github.some_example_name.model.FalseKnightModel;
import io.github.some_example_name.model.GameModel;
import io.github.some_example_name.model.ZoteModel;

public class MapLoaderHelper {
    private TiledMap map;
    private Array<SolidBlock> collisionBlocks;
    private GameModel gameModel;
    private final Vector2 bossSpawnPos = new Vector2(0, 0);
    private float cameraX = 6852f;
    private float cameraY = 982f;
    private float zoteStartX = 0;
    private float zoteStartY = 0;
    private boolean zoteFound = false;
    private boolean bossFound = false;

    public void loadMapData() {
        map = new TmxMapLoader().load("map/untitled.tmx");
        TiledMapHelper helper = new TiledMapHelper(map);
        collisionBlocks = helper.getSolidRectangles();

        MapLayer objectLayer = map.getLayers().get("Spawnplayer");
        if (objectLayer != null) {
            for (MapObject object : objectLayer.getObjects()) {
                if (object.getName() != null) {
                    if (object.getName().equals("Spawnplayer") && object.getProperties().containsKey("x")) {
                        cameraX = object.getProperties().get("x", Float.class);
                        cameraY = object.getProperties().get("y", Float.class);
                    }
                    if (object.getName().equals("spawnboss") && object.getProperties().containsKey("x")) {
                        bossSpawnPos.x = object.getProperties().get("x", Float.class);
                        bossSpawnPos.y = object.getProperties().get("y", Float.class);
                        bossFound = true;
                    }
                    if (object.getName().equals("zotespawn") && object.getProperties().containsKey("x")) {
                        zoteStartX = object.getProperties().get("x", Float.class);
                        zoteStartY = object.getProperties().get("y", Float.class);
                        zoteFound = true;
                    }
                }
            }
        }

        gameModel = new GameModel(cameraX, cameraY);
        gameModel.bossSpawnPos.set(bossSpawnPos);

        if (zoteFound) {
            gameModel.zote = new ZoteModel(zoteStartX, zoteStartY);
        }
        if (bossFound) {
            gameModel.falseKnight = new FalseKnightModel(bossSpawnPos.x, bossSpawnPos.y);
        }

        MapLayer doorLayer = map.getLayers().get("bossdoor");
        if (doorLayer != null) {
            for (MapObject object : doorLayer.getObjects()) {
                if (object.getName() != null && object.getName().equals("doorboss") && object.getProperties().containsKey("y")) {
                    if (gameModel.bossTriggerArea == null) {
                        gameModel.bossTriggerArea = new Rectangle();
                    }
                    gameModel.bossTriggerArea.y = object.getProperties().get("y", Float.class);
                }
                if (object.getProperties().containsKey("door") && object.getProperties().get("door", Boolean.class)) {
                    if (object instanceof RectangleMapObject) {
                        Rectangle rect = ((RectangleMapObject) object).getRectangle();
                        SolidBlock doorBlock = new SolidBlock(rect.x, rect.y, rect.width, rect.height, false);
                        doorBlock.isActive = false;
                        collisionBlocks.add(doorBlock);
                        gameModel.bossDoorBlock = doorBlock;
                    }
                }
            }
        }

        if (objectLayer != null) {
            for (MapObject object : objectLayer.getObjects()) {
                if (object.getName() != null) {
                    String name = object.getName();
                    if (name.equalsIgnoreCase("SpawnEnemy1") || name.equalsIgnoreCase("ground2-1") ||
                        name.equalsIgnoreCase("flying1-1") || name.equalsIgnoreCase("flying2-1") ||
                        name.equalsIgnoreCase("huskspawn") || name.equalsIgnoreCase("crystallspawn")) {

                        float ex = object.getProperties().get("x", Float.class);
                        float ey = object.getProperties().get("y", Float.class);

                        if (name.equalsIgnoreCase("SpawnEnemy1")) {
                            gameModel.enemies.add(new EnemyModel(EnemyModel.EnemyType.CRAWLID, ex, ey, 100f, 100f, 303f, 177f, 100f, false, 2));
                        } else if (name.equalsIgnoreCase("ground2-1")) {
                            gameModel.enemies.add(new EnemyModel(EnemyModel.EnemyType.MOSSCREEP, ex, ey, 100f, 100f, 303f, 177f, 100f, false, 3));
                        } else if (name.equalsIgnoreCase("flying1-1")) {
                            gameModel.enemies.add(new EnemyModel(EnemyModel.EnemyType.MOSQUITO, ex, ey, 100f, 100f, 303f, 177f, 100f, true, 2));
                        } else if (name.equalsIgnoreCase("flying2-1")) {
                            gameModel.enemies.add(new EnemyModel(EnemyModel.EnemyType.MOSSFLY, ex, ey, 100f, 100f, 303f, 177f, 100f, true, 4));
                        } else if (name.equalsIgnoreCase("huskspawn")) {
                            gameModel.enemies.add(new EnemyModel(EnemyModel.EnemyType.HUSK, ex, ey, 100f, 100f, 303f, 177f, 150f, false, 5));
                        } else if (name.equalsIgnoreCase("crystallspawn")) {
                            gameModel.enemies.add(new EnemyModel(EnemyModel.EnemyType.CRYSTAL, ex, ey, 100f, 100f, 303f, 177f, 150f, false, 6));
                        }
                    }
                }
            }
        }
    }

    public TiledMap getMap() { return map; }
    public Array<SolidBlock> getCollisionBlocks() { return collisionBlocks; }
    public GameModel getGameModel() { return gameModel; }
    public Vector2 getBossSpawnPos() { return bossSpawnPos; }
}
