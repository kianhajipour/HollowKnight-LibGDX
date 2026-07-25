package io.github.some_example_name.view.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.some_example_name.model.PlayerModel;
import io.github.some_example_name.view.util.AnimationHelper;

public class PlayerView {
    private Animation<TextureRegion> idleAnimation, runAnimation, landingAnimation, fallAnimation,
        doubleJumpAnimation, dashAnimation, slashAnimation, airborneAnimation,
        hurtAnimation, deathAnimation;
    private Animation<TextureRegion> focusStartAnim, focusGetAnim, focusEndAnim;
    private Animation<TextureRegion> slashEffectAnimation;

    private float stateTime = 0f;
    private PlayerModel.State previousState = PlayerModel.State.IDLE;

    public PlayerView() {
        this.idleAnimation = AnimationHelper.createFromSheet("player/Idle.png", 1, 9, 0.1f, Animation.PlayMode.LOOP);
        this.runAnimation = AnimationHelper.createFromSheet("player/Run.png", 1, 13, 0.05f, Animation.PlayMode.LOOP);
        this.airborneAnimation = AnimationHelper.createFromSheet("player/Airborne.png", 1, 12, 0.05f, Animation.PlayMode.LOOP);
        this.fallAnimation = AnimationHelper.createFromSheet("player/Fall.png", 1, 12, 0.06f, Animation.PlayMode.LOOP);
        this.landingAnimation = AnimationHelper.createFromSheet("player/Landing.png", 1, 4, 0.05f, Animation.PlayMode.NORMAL);
        this.doubleJumpAnimation = AnimationHelper.createFromSheet("player/Double Jump.png", 1, 8, 0.05f, Animation.PlayMode.NORMAL);
        this.dashAnimation = AnimationHelper.createFromSheet("player/Dash.png", 1, 12, 0.04f, Animation.PlayMode.NORMAL);
        this.slashAnimation = AnimationHelper.createFromSheet("player/SlashAlt.png", 1, 5, 0.05f, Animation.PlayMode.NORMAL);

        this.focusStartAnim = AnimationHelper.createFromSheet("player/Focus Start.png", 1, 3, 0.1f, Animation.PlayMode.NORMAL);
        this.focusGetAnim = AnimationHelper.createFromSheet("player/Focus Get.png", 1, 6, 0.1f, Animation.PlayMode.LOOP);
        this.focusEndAnim = AnimationHelper.createFromSheet("player/Focus End.png", 1, 3, 0.1f, Animation.PlayMode.NORMAL);

        this.hurtAnimation = AnimationHelper.createFromSheet("player/Idle Hurt.png", 1, 12, 0.08f, Animation.PlayMode.NORMAL);
        this.deathAnimation = AnimationHelper.createFromSheet("player/Death.png", 1, 18, 0.1f, Animation.PlayMode.NORMAL);

        this.slashEffectAnimation = AnimationHelper.createFromSheet("player/SlashEffect.png", 1, 4, 0.05f, Animation.PlayMode.NORMAL);
    }

    public void draw(SpriteBatch batch, PlayerModel model, float delta) {
        if (model.currentState != previousState) {
            stateTime = 0f;
            previousState = model.currentState;
        }

        stateTime += delta;
        TextureRegion currentFrame;

        if (model.noclipMode) {
            currentFrame = fallAnimation.getKeyFrame(fallAnimation.getAnimationDuration());
        } else if (model.currentState == PlayerModel.State.CASTING) {
            currentFrame = focusStartAnim.getKeyFrame(0f);
        } else if (model.isFocusing) {
            currentFrame = (model.focusTimer < 0.2f) ? focusStartAnim.getKeyFrame(model.focusTimer) : focusGetAnim.getKeyFrame(model.focusTimer - 0.2f);
        } else {
            switch (model.currentState) {
                case RUNNING:       currentFrame = runAnimation.getKeyFrame(stateTime); break;
                case JUMPING:       currentFrame = airborneAnimation.getKeyFrame(stateTime); break;
                case DOUBLE_JUMPING: currentFrame = doubleJumpAnimation.getKeyFrame(stateTime); break;
                case FALLING:       currentFrame = fallAnimation.getKeyFrame(stateTime); break;
                case LANDING:       currentFrame = landingAnimation.getKeyFrame(stateTime); break;
                case DASHING:       currentFrame = dashAnimation.getKeyFrame(stateTime); break;
                case ATTACKING:     currentFrame = slashAnimation.getKeyFrame(stateTime); break;
                case HURT:
                    currentFrame = hurtAnimation.getKeyFrame(stateTime);
                    if (hurtAnimation.isAnimationFinished(stateTime)) model.currentState = PlayerModel.State.IDLE;
                    break;
                case DEAD:
                    currentFrame = deathAnimation.getKeyFrame(stateTime);
                    if (deathAnimation.isAnimationFinished(stateTime)) {
                        model.resetToSpawn();
                    }
                    break;
                case IDLE:
                default:            currentFrame = idleAnimation.getKeyFrame(stateTime); break;
            }
        }

        float renderX = model.bounds.x - (model.renderWidth - model.bounds.width) / 2f;
        batch.draw(currentFrame, !model.facingRight ? renderX : renderX + model.renderWidth, model.bounds.y,
            !model.facingRight ? model.renderWidth : -model.renderWidth, model.renderHeight);

        if (!model.noclipMode && model.currentState == PlayerModel.State.ATTACKING && !slashEffectAnimation.isAnimationFinished(stateTime)) {
            TextureRegion effectFrame = slashEffectAnimation.getKeyFrame(stateTime);
            float effectOffsetX = 45f;
            float effectX = !model.facingRight ? renderX - effectOffsetX : renderX + model.renderWidth + effectOffsetX;
            batch.draw(effectFrame, effectX, model.bounds.y, !model.facingRight ? model.renderWidth : -model.renderWidth, model.renderHeight);
        }
    }

    public void dispose() {}
}
