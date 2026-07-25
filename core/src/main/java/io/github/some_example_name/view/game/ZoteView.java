package io.github.some_example_name.view.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.some_example_name.model.ZoteModel;
import io.github.some_example_name.view.util.AnimationHelper;

public class ZoteView {
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> talkAnimation;
    private float stateTime = 0f;

    public ZoteView() {
        this.idleAnimation = AnimationHelper.createFromSheet("zote/Idle.png", 1, 5, 0.15f, Animation.PlayMode.LOOP);
        this.talkAnimation = AnimationHelper.createFromSheet("zote/Talk.png", 1, 5, 0.1f, Animation.PlayMode.LOOP);
    }

    public void draw(SpriteBatch batch, ZoteModel model, float delta) {
        if (model == null) return;
        stateTime += delta;
        TextureRegion currentFrame = (model.currentState == ZoteModel.State.TALKING)
            ? talkAnimation.getKeyFrame(stateTime)
            : idleAnimation.getKeyFrame(stateTime);

        float renderX = model.bounds.x - (model.renderWidth - model.bounds.width) / 2f;
        batch.draw(currentFrame, renderX, model.bounds.y, model.renderWidth, model.renderHeight);
    }

    public void dispose() {}
}
