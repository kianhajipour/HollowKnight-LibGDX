package io.github.some_example_name.view.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class AnimationHelper {

    public static Animation<TextureRegion> createFromFiles(String prefix, String suffix, int frameCount, float frameDuration, Animation.PlayMode playMode) {
        Array<TextureRegion> frames = new Array<>();
        for (int i = 1; i < frameCount; i++) {
            Texture texture = new Texture(Gdx.files.internal(prefix + i + suffix));
            frames.add(new TextureRegion(texture));
        }
        Animation<TextureRegion> animation = new Animation<>(frameDuration, frames);
        animation.setPlayMode(playMode);
        return animation;
    }

    public static Animation<TextureRegion> createFromSheet(String path, int rows, int cols, float frameDuration, Animation.PlayMode playMode) {
        Texture sheet = new Texture(Gdx.files.internal(path));
        TextureRegion[][] tmp = TextureRegion.split(sheet, sheet.getWidth() / cols, sheet.getHeight() / rows);
        Array<TextureRegion> frames = new Array<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                frames.add(tmp[i][j]);
            }
        }
        Animation<TextureRegion> animation = new Animation<>(frameDuration, frames);
        animation.setPlayMode(playMode);
        return animation;
    }
}
