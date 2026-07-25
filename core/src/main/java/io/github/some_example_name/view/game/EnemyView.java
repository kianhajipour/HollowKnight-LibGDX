package io.github.some_example_name.view.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.some_example_name.model.EnemyModel;
import io.github.some_example_name.model.GameModel;
import io.github.some_example_name.model.ProjectileModel;
import io.github.some_example_name.view.util.AnimationHelper;

public class EnemyView {
    private Animation<TextureRegion> crawlidWalk, crawlidDeathLand, crawlidDeathAir;
    private Animation<TextureRegion> mossWalk, mossDeathLand, mossDeathAir, mossTurn;

    private Animation<TextureRegion> mosquitoIdle, mosquitoAnticipate, mosquitoAttack, mosquitoDeathAir;
    private Animation<TextureRegion> mossflyAppear, mossflyDeathAir, mossflyDeathLand, mossflyFly, mossflyShake;

    private Animation<TextureRegion> huskIdle, huskWalk, huskAnticipate, huskLunge, huskDeathLand;

    private Animation<TextureRegion> crystalIdle, crystalRun, crystalShoot, crystalDeathLand, crystalDeathAir;
    private TextureRegion laserballTex;

    public EnemyView() {
        this.crawlidWalk = AnimationHelper.createFromSheet("Enemies/Crawlid/Walk3.png", 1, 4, 0.15f, Animation.PlayMode.LOOP);
        this.crawlidDeathLand = AnimationHelper.createFromSheet("Enemies/Crawlid/Death Land.png", 1, 2, 0.2f, Animation.PlayMode.NORMAL);
        this.crawlidDeathAir = AnimationHelper.createFromSheet("Enemies/Crawlid/Death Air.png", 1, 3, 0.2f, Animation.PlayMode.NORMAL);

        this.mossWalk = AnimationHelper.createFromSheet("Enemies/Mosscreep/Walk.png", 1, 3, 0.15f, Animation.PlayMode.LOOP);
        this.mossDeathLand = AnimationHelper.createFromSheet("Enemies/Mosscreep/Death Land.png", 1, 2, 0.2f, Animation.PlayMode.NORMAL);
        this.mossDeathAir = AnimationHelper.createFromSheet("Enemies/Mosscreep/Death Air.png", 1, 4, 0.2f, Animation.PlayMode.NORMAL);
        this.mossTurn = AnimationHelper.createFromSheet("Enemies/Mosscreep/Turn.png", 1, 3, 0.15f, Animation.PlayMode.NORMAL);

        this.mosquitoIdle = AnimationHelper.createFromSheet("Enemies/Mosquito/Idle.png", 1, 8, 0.1f, Animation.PlayMode.LOOP);
        this.mosquitoAnticipate = AnimationHelper.createFromSheet("Enemies/Mosquito/Attack Anticipate.png", 1, 6, 0.1f, Animation.PlayMode.NORMAL);
        this.mosquitoAttack = AnimationHelper.createFromSheet("Enemies/Mosquito/Attack.png", 1, 3, 0.1f, Animation.PlayMode.LOOP);
        this.mosquitoDeathAir = AnimationHelper.createFromSheet("Enemies/Mosquito/Death Air.png", 1, 3, 0.2f, Animation.PlayMode.NORMAL);

        this.mossflyAppear = AnimationHelper.createFromSheet("Enemies/Mossfly/Appear.png", 1, 6, 0.15f, Animation.PlayMode.NORMAL);
        this.mossflyDeathAir = AnimationHelper.createFromSheet("Enemies/Mossfly/Death Air.png", 1, 4, 0.2f, Animation.PlayMode.NORMAL);
        this.mossflyDeathLand = AnimationHelper.createFromSheet("Enemies/Mossfly/Death Land.png", 1, 2, 0.2f, Animation.PlayMode.NORMAL);
        this.mossflyFly = AnimationHelper.createFromSheet("Enemies/Mossfly/Fly.png", 1, 4, 0.1f, Animation.PlayMode.LOOP);
        this.mossflyShake = AnimationHelper.createFromSheet("Enemies/Mossfly/Shake.png", 1, 3, 0.15f, Animation.PlayMode.LOOP);

        this.huskIdle = AnimationHelper.createFromSheet("Enemies/Husk/Idle.png", 1, 6, 0.15f, Animation.PlayMode.LOOP);
        this.huskWalk = AnimationHelper.createFromSheet("Enemies/Husk/Walk.png", 1, 7, 0.1f, Animation.PlayMode.LOOP);
        this.huskAnticipate = AnimationHelper.createFromSheet("Enemies/Husk/Attack Anticipate.png", 1, 5, 0.1f, Animation.PlayMode.NORMAL);
        this.huskLunge = AnimationHelper.createFromSheet("Enemies/Husk/Attack Lunge.png", 1, 12, 0.08f, Animation.PlayMode.LOOP);
        this.huskDeathLand = AnimationHelper.createFromSheet("Enemies/Husk/Death Land.png", 1, 8, 0.15f, Animation.PlayMode.NORMAL);

        this.crystalIdle = AnimationHelper.createFromSheet("Enemies/Crystallized/Idle.png", 1, 5, 0.15f, Animation.PlayMode.LOOP);
        this.crystalRun = AnimationHelper.createFromSheet("Enemies/Crystallized/Run.png", 1, 6, 0.1f, Animation.PlayMode.LOOP);
        this.crystalShoot = AnimationHelper.createFromSheet("Enemies/Crystallized/Shoot.png", 1, 7, 0.1f, Animation.PlayMode.NORMAL);
        this.crystalDeathLand = AnimationHelper.createFromSheet("Enemies/Crystallized/Death Land.png", 1, 3, 0.2f, Animation.PlayMode.NORMAL);
        this.crystalDeathAir = AnimationHelper.createFromSheet("Enemies/Crystallized/Death Air.png", 1, 3, 0.2f, Animation.PlayMode.NORMAL);

        Texture tex = new Texture(Gdx.files.internal("Enemies/Crystallized/laserball.png"));
        this.laserballTex = new TextureRegion(tex);
    }

    public void draw(SpriteBatch batch, EnemyModel model, float delta) {
        if (model.currentState != model.previousState) {
            model.animationTime = 0f;
            model.previousState = model.currentState;
        }

        model.animationTime += delta;
        TextureRegion currentFrame = null;

        if (model.type == EnemyModel.EnemyType.CRAWLID) {
            if (model.currentState == EnemyModel.State.DEAD) {
                currentFrame = model.isFlying ? crawlidDeathAir.getKeyFrame(model.animationTime) : crawlidDeathLand.getKeyFrame(model.animationTime);
            } else {
                currentFrame = crawlidWalk.getKeyFrame(model.animationTime);
            }
        } else if (model.type == EnemyModel.EnemyType.MOSSCREEP) {
            if (model.currentState == EnemyModel.State.DEAD) {
                currentFrame = model.isFlying ? mossDeathAir.getKeyFrame(model.animationTime) : mossDeathLand.getKeyFrame(model.animationTime);
            } else {
                currentFrame = mossWalk.getKeyFrame(model.animationTime);
            }
        } else if (model.type == EnemyModel.EnemyType.MOSQUITO) {
            switch (model.currentState) {
                case DEAD: currentFrame = mosquitoDeathAir.getKeyFrame(model.animationTime); break;
                case ANTICIPATING: currentFrame = mosquitoAnticipate.getKeyFrame(model.animationTime); break;
                case ATTACKING: currentFrame = mosquitoAttack.getKeyFrame(model.animationTime); break;
                case IDLE:
                default: currentFrame = mosquitoIdle.getKeyFrame(model.animationTime); break;
            }
        } else if (model.type == EnemyModel.EnemyType.MOSSFLY) {
            switch (model.currentState) {
                case DEAD: currentFrame = model.isGrounded ? mossflyDeathLand.getKeyFrame(model.animationTime) : mossflyDeathAir.getKeyFrame(model.animationTime); break;
                case HIDING: currentFrame = mossflyShake.getKeyFrame(0); break;
                case SHAKING: currentFrame = mossflyShake.getKeyFrame(model.animationTime); break;
                case APPEARING: currentFrame = mossflyAppear.getKeyFrame(model.animationTime); break;
                case FLYING:
                default: currentFrame = mossflyFly.getKeyFrame(model.animationTime); break;
            }
        } else if (model.type == EnemyModel.EnemyType.HUSK) {
            switch (model.currentState) {
                case DEAD: currentFrame = huskDeathLand.getKeyFrame(model.animationTime); break;
                case ANTICIPATING: currentFrame = huskAnticipate.getKeyFrame(model.animationTime); break;
                case LUNGING: currentFrame = huskLunge.getKeyFrame(model.animationTime); break;
                case RESTING: currentFrame = huskIdle.getKeyFrame(model.animationTime); break;
                case WALKING:
                default: currentFrame = huskWalk.getKeyFrame(model.animationTime); break;
            }
        } else if (model.type == EnemyModel.EnemyType.CRYSTAL) {
            switch (model.currentState) {
                case DEAD: currentFrame = model.isGrounded ? crystalDeathLand.getKeyFrame(model.animationTime) : crystalDeathAir.getKeyFrame(model.animationTime); break;
                case SHOOTING: currentFrame = crystalShoot.getKeyFrame(model.animationTime); break;
                case ENRAGED: currentFrame = crystalRun.getKeyFrame(model.animationTime); break;
                case IDLE:
                default: currentFrame = crystalIdle.getKeyFrame(model.animationTime); break;
            }
        }

        if (currentFrame != null) {
            float offsetX = (model.renderWidth - model.bounds.width) / 2f;
            float offsetY = (model.renderHeight - model.bounds.height) / 2f;

            batch.draw(
                currentFrame,
                !model.movingRight ? model.bounds.x - offsetX : model.bounds.x + model.bounds.width + offsetX,
                model.bounds.y - offsetY,
                !model.movingRight ? model.renderWidth : -model.renderWidth,
                model.renderHeight
            );
        }
    }

    public void drawProjectiles(SpriteBatch batch, GameModel model) {
        for (ProjectileModel p : model.projectiles) {
            batch.draw(laserballTex, p.bounds.x, p.bounds.y, p.bounds.width, p.bounds.height);
        }
    }
}
