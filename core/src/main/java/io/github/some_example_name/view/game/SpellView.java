package io.github.some_example_name.view.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.some_example_name.model.GameModel;
import io.github.some_example_name.model.SpellModel;
import io.github.some_example_name.view.util.AnimationHelper;

public class SpellView {
    private Animation<TextureRegion> vengefulSpiritAnim;
    private Animation<TextureRegion> howlingWraithsAnim;

    public SpellView() {
        this.vengefulSpiritAnim = AnimationHelper.createFromSheet("Abilities/SoulBall.png", 1, 4, 0.1f, Animation.PlayMode.LOOP);
        this.howlingWraithsAnim = AnimationHelper.createFromSheet("Abilities/SoulScream.png", 1, 13, 0.05f, Animation.PlayMode.NORMAL);
    }

    public void draw(SpriteBatch batch, GameModel model, float delta) {
        for (SpellModel spell : model.playerSpells) {
            spell.stateTimer += delta;
            TextureRegion frame = null;

            if (spell.type == SpellModel.SpellType.VENGEFUL_SPIRIT) {
                frame = vengefulSpiritAnim.getKeyFrame(spell.stateTimer);
                float renderWidth = spell.bounds.width * 1.5f;
                float renderHeight = spell.bounds.height * 1.5f;
                float offsetY = (renderHeight - spell.bounds.height) / 2f;

                batch.draw(frame,
                    !spell.facingRight ? spell.bounds.x + spell.bounds.width + (renderWidth - spell.bounds.width)/2f : spell.bounds.x - (renderWidth - spell.bounds.width)/2f,
                    spell.bounds.y - offsetY,
                    !spell.facingRight ? -renderWidth : renderWidth,
                    renderHeight);

            } else if (spell.type == SpellModel.SpellType.HOWLING_WRAITHS) {
                frame = howlingWraithsAnim.getKeyFrame(spell.stateTimer);
                batch.draw(frame, spell.bounds.x, spell.bounds.y, spell.bounds.width, spell.bounds.height);
            }
        }
    }

    public void dispose() {}
}
