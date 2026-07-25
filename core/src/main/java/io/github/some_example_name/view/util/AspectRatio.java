package io.github.some_example_name.view.util;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class AspectRatio {
    public static float aspectRatio(float width, Image image){
        float aspectRatioL = image.getHeight() / image.getWidth();
        float desiredHeight = width * aspectRatioL;

        return desiredHeight;
    }
}
