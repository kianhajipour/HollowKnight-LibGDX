package io.github.some_example_name.controller.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import io.github.some_example_name.controller.SettingsController;
import io.github.some_example_name.model.PlayerModel;

public class PlayerFocusController {
    private long chargingSoundId = -1;
    private boolean wasFocusing = false;
    private boolean requireFocusKeyRelease = false;

    public void handleFocus(PlayerModel player, float delta, SettingsController settingsController, Sound focusReadySound, Sound focusChargingSound, Sound focusHealSound) {
        boolean pressingFocus = Gdx.input.isKeyPressed(settingsController.getFocusKey());

        if (!pressingFocus) {
            requireFocusKeyRelease = false;
            player.isFocusing = false;
            player.focusTimer = 0f;
        } else if (player.currentState != PlayerModel.State.HURT) {
            if (!requireFocusKeyRelease && player.isGrounded && Math.abs(player.velocityY) < 0.1f
                && player.currentHp < player.maxHp && player.currentSoul >= 11
                && (player.currentState == PlayerModel.State.IDLE || player.isFocusing)) {
                player.isFocusing = true;
                player.currentState = PlayerModel.State.IDLE;
            } else {
                player.isFocusing = false;
                player.focusTimer = 0f;
            }
        }

        updateFocusSounds(player, settingsController, focusReadySound, focusChargingSound);

        if (player.isFocusing) {
            player.focusTimer += delta;
            if (player.focusTimer >= player.FOCUS_DURATION) {
                player.currentSoul -= 11;
                player.currentHp++;
                if (focusHealSound != null) focusHealSound.play(settingsController.getSfxVolume());
                player.focusTimer = 0f;
                player.isFocusing = false;
                requireFocusKeyRelease = true;
                stopChargingSound(focusChargingSound);
            }
        }
    }

    private void updateFocusSounds(PlayerModel player, SettingsController settingsController, Sound focusReadySound, Sound focusChargingSound) {
        if (player.isFocusing && !wasFocusing) {
            if (focusReadySound != null) focusReadySound.play(settingsController.getSfxVolume());
            if (focusChargingSound != null) {
                chargingSoundId = focusChargingSound.loop(settingsController.getSfxVolume());
            }
        } else if (!player.isFocusing && wasFocusing) {
            stopChargingSound(focusChargingSound);
        }
        wasFocusing = player.isFocusing;
    }

    public void stopChargingSound(Sound focusChargingSound) {
        if (chargingSoundId != -1 && focusChargingSound != null) {
            focusChargingSound.stop(chargingSoundId);
            chargingSoundId = -1;
            wasFocusing = false;
        }
    }
}
