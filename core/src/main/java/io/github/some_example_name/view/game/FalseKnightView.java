package io.github.some_example_name.view.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.some_example_name.model.FalseKnightModel;
import io.github.some_example_name.view.util.AnimationHelper;

public class FalseKnightView {
    private Animation<TextureRegion> idleAnim, runAnticAnim, runAnim, jumpAnticAnim, jumpAnim, fallAnim, landAnim;
    private Animation<TextureRegion> attackAnticAnim, attackAnim, attackRecoverAnim, jumpAttackAnim;
    private Animation<TextureRegion> stunAnim, stunRecoverAnim;
    private Animation<TextureRegion> deathFallAnim, deathHitAnim, deathLandAnim;

    public FalseKnightView() {
        idleAnim = AnimationHelper.createFromSheet("boss/Idle.png", 1, 5, 0.1f, Animation.PlayMode.LOOP);
        runAnticAnim = AnimationHelper.createFromSheet("boss/Run Antic.png", 1, 2, 0.1f, Animation.PlayMode.NORMAL);
        runAnim = AnimationHelper.createFromSheet("boss/Run.png", 1, 5, 0.1f, Animation.PlayMode.LOOP);
        jumpAnticAnim = AnimationHelper.createFromSheet("boss/Jump Antic.png", 1, 3, 0.1f, Animation.PlayMode.NORMAL);
        jumpAnim = AnimationHelper.createFromSheet("boss/Jump.png", 1, 4, 0.1f, Animation.PlayMode.NORMAL);
        fallAnim = AnimationHelper.createFromSheet("boss/Jump.png", 1, 4, 0.1f, Animation.PlayMode.REVERSED);
        landAnim = AnimationHelper.createFromSheet("boss/Land.png", 1, 5, 0.1f, Animation.PlayMode.NORMAL);
        attackAnticAnim = AnimationHelper.createFromSheet("boss/Attack Antic.png", 1, 6, 0.08f, Animation.PlayMode.NORMAL);
        attackAnim = AnimationHelper.createFromSheet("boss/Attack.png", 1, 3, 0.1f, Animation.PlayMode.NORMAL);
        attackRecoverAnim = AnimationHelper.createFromSheet("boss/Attack Recover.png", 1, 5, 0.1f, Animation.PlayMode.NORMAL);
        jumpAttackAnim = AnimationHelper.createFromSheet("boss/Jump Attack.png", 1, 8, 0.1f, Animation.PlayMode.NORMAL);
        stunAnim = AnimationHelper.createFromSheet("boss/Body.png", 1, 5, 0.1f, Animation.PlayMode.LOOP);
        stunRecoverAnim = AnimationHelper.createFromSheet("boss/Stun Recover.png", 1, 6, 0.1f, Animation.PlayMode.NORMAL);
        deathFallAnim = AnimationHelper.createFromSheet("boss/DeathFall.png", 1, 3, 0.1f, Animation.PlayMode.LOOP);
        deathHitAnim = AnimationHelper.createFromSheet("boss/DeathHit.png", 1, 3, 0.1f, Animation.PlayMode.NORMAL);
        deathLandAnim = AnimationHelper.createFromSheet("boss/DeathLand.png", 1, 11, 0.1f, Animation.PlayMode.NORMAL);
    }

    public void draw(SpriteBatch batch, FalseKnightModel model) {
        if (model == null) return;

        TextureRegion frame = idleAnim.getKeyFrame(model.stateTimer);

        switch (model.currentState) {
            case IDLE: frame = idleAnim.getKeyFrame(model.stateTimer); break;
            case RUN_ANTIC: frame = runAnticAnim.getKeyFrame(model.stateTimer); break;
            case RUNNING: frame = runAnim.getKeyFrame(model.stateTimer); break;
            case JUMP_ANTIC: frame = jumpAnticAnim.getKeyFrame(model.stateTimer); break;
            case JUMPING: frame = jumpAnim.getKeyFrame(model.stateTimer); break;
            case FALLING: frame = fallAnim.getKeyFrame(model.stateTimer); break;
            case LANDING: frame = landAnim.getKeyFrame(model.stateTimer); break;
            case ATTACK_ANTIC: frame = attackAnticAnim.getKeyFrame(model.stateTimer); break;
            case ATTACKING: frame = attackAnim.getKeyFrame(model.stateTimer); break;
            case ATTACK_RECOVER: frame = attackRecoverAnim.getKeyFrame(model.stateTimer); break;
            case JUMP_ATTACK: frame = jumpAttackAnim.getKeyFrame(model.stateTimer); break;
            case STUNNED: frame = stunAnim.getKeyFrame(model.stunTimer); break;
            case STUN_RECOVER: frame = stunRecoverAnim.getKeyFrame(model.stateTimer); break;
            case DEATH_FALL: frame = deathFallAnim.getKeyFrame(model.stateTimer); break;
            case DEATH_HIT: frame = deathHitAnim.getKeyFrame(model.stateTimer); break;
            case DEATH_LAND: frame = deathLandAnim.getKeyFrame(model.stateTimer); break;
            case DEAD: frame = deathLandAnim.getKeyFrame(100f); break;
        }

        float rx = model.bounds.x - (model.renderWidth - model.bounds.width) / 2f;
        batch.draw(frame, !model.facingRight ? rx : rx + model.renderWidth, model.bounds.y,
            !model.facingRight ? model.renderWidth : -model.renderWidth, model.renderHeight);
    }

    public void dispose() {}
}
