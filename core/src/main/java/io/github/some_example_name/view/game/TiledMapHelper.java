package io.github.some_example_name.view.game;

import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class TiledMapHelper {
    private TiledMap tiledMap;

    public TiledMapHelper(TiledMap map) {
        this.tiledMap = map;
    }

    public TiledMap loadMap(String path) {
        tiledMap = new com.badlogic.gdx.maps.tiled.TmxMapLoader().load(path);
        return tiledMap;
    }

    public Array<SolidBlock> getSolidRectangles() {
        Array<SolidBlock> solidBlocks = new Array<>();
        MapLayer layer = tiledMap.getLayers().get("Spawnplayer");

        if (layer != null) {
            for (MapObject object : layer.getObjects()) {
                if (object.getName() != null && (object.getName().equals("Spawnplayer") || object.getName().equals("zotespawn"))) {
                    continue;
                }

                if (object instanceof RectangleMapObject) {
                    Rectangle rect = ((RectangleMapObject) object).getRectangle();
                    boolean isDeadly = false;

                    if (object.getProperties().containsKey("danger")) {
                        isDeadly = object.getProperties().get("danger", Boolean.class);
                    }

                    solidBlocks.add(new SolidBlock(rect.x, rect.y, rect.width, rect.height, isDeadly));
                }
            }
        }

        MapLayer doorLayer = tiledMap.getLayers().get("bossdoor");
        if (doorLayer != null) {
            for (MapObject object : doorLayer.getObjects()) {
                if (object.getProperties().containsKey("door") && object.getProperties().get("door", Boolean.class)) {
                    if (object instanceof RectangleMapObject) {
                        Rectangle rect = ((RectangleMapObject) object).getRectangle();
                        SolidBlock bossDoor = new SolidBlock(rect.x, rect.y, rect.width, rect.height, false);
                        bossDoor.isActive = false;
                        solidBlocks.add(bossDoor);
                    }
                }
            }
        }

        return solidBlocks;
    }
}
