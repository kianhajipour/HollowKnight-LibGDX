package io.github.some_example_name.view.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.some_example_name.view.util.AnimationHelper;
import io.github.some_example_name.model.PlayerModel;

public class PlayerHealthView {
    private final Animation<TextureRegion> maskAnimation;
    private final Texture soulLiquid;

    private final float vesselScale = 0.8f;
    private final float maskScale = 0.2f;

    public PlayerHealthView() {
        maskAnimation = AnimationHelper.createFromSheet("health/mask.png", 1, 5, 0.1f, Animation.PlayMode.NORMAL);
        soulLiquid = new Texture(Gdx.files.internal("health/soul.png"));
    }

    public void draw(SpriteBatch batch, PlayerModel model, float x, float y) {
        float adjustedY = y - 30f;
        float soulPercent = Math.min(1.0f, Math.max(0.0f, model.currentSoul / model.MAX_SOUL));

        float scaledLiquidWidth = soulLiquid.getWidth() * vesselScale;
        float scaledLiquidHeight = soulLiquid.getHeight() * vesselScale;
        float currentLiquidHeight = scaledLiquidHeight * soulPercent;

        if (currentLiquidHeight > 0) {
            batch.draw(
                soulLiquid,
                x, adjustedY,
                scaledLiquidWidth, currentLiquidHeight,
                0, (int) (soulLiquid.getHeight() * (1 - soulPercent)),
                soulLiquid.getWidth(), (int) (soulLiquid.getHeight() * soulPercent),
                false, false
            );
        }

        TextureRegion fullMask = maskAnimation.getKeyFrame(0.4f);
        float maskWidth = fullMask.getRegionWidth() * maskScale;
        float maskHeight = fullMask.getRegionHeight() * maskScale;

        float startMaskX = x + scaledLiquidWidth + (10 * vesselScale);
        float maskY = adjustedY + (scaledLiquidHeight / 2f) - (maskHeight / 2f);

        for (int i = 0; i < model.maxHp; i++) {
            TextureRegion maskToDraw = (i < model.currentHp) ? maskAnimation.getKeyFrame(0.4f) : maskAnimation.getKeyFrame(0f);
            batch.draw(maskToDraw, startMaskX + (i * (maskWidth + (5 * maskScale))), maskY, maskWidth, maskHeight);
        }
    }

    public void dispose() {
        if (soulLiquid != null) soulLiquid.dispose();
    }
}
