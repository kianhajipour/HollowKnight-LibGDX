package io.github.some_example_name.controller.game;

import com.badlogic.gdx.Gdx;
import io.github.some_example_name.controller.SettingsController;
import io.github.some_example_name.model.GameModel;
import io.github.some_example_name.model.PlayerModel;
import io.github.some_example_name.model.SpellModel;

public class PlayerSpellController {

    public void handleSpells(PlayerModel player, GameModel gameModel, SettingsController settingsController) {
        if (player.noclipMode || player.currentState == PlayerModel.State.DEAD) return;
        if (player.currentState == PlayerModel.State.CASTING) return;

        boolean spiritPressed = Gdx.input.isKeyJustPressed(settingsController.getVengefulSpiritKey());
        boolean wraithsPressed = Gdx.input.isKeyJustPressed(settingsController.getHowlingWraithsKey());

        if ((spiritPressed || wraithsPressed) && player.currentSoul >= 33f) {
            player.currentSoul = player.cheatInfiniteSoul ? player.MAX_SOUL : player.currentSoul - 33f;
            player.currentState = PlayerModel.State.CASTING;
            player.castTimer = 0.4f;
            player.velocityY = 0;

            if (wraithsPressed) {
                SpellModel wraiths = new SpellModel(SpellModel.SpellType.HOWLING_WRAITHS, player.bounds.x, player.bounds.y, player.facingRight);
                gameModel.playerSpells.add(wraiths);
            } else {
                float startX = player.facingRight ? player.bounds.x + player.bounds.width : player.bounds.x - 120f;
                SpellModel spirit = new SpellModel(SpellModel.SpellType.VENGEFUL_SPIRIT, startX, player.bounds.y + 10f, player.facingRight);
                gameModel.playerSpells.add(spirit);
            }
        }
    }

    public void updateCastingState(PlayerModel player, float delta) {
        if (player.currentState == PlayerModel.State.CASTING) {
            player.castTimer -= delta;
            if (player.castTimer <= 0) {
                player.currentState = player.isGrounded ? PlayerModel.State.IDLE : PlayerModel.State.FALLING;
            }
        }
    }
}
