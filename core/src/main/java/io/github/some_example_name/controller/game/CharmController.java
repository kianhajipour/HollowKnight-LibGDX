package io.github.some_example_name.controller.game;

import io.github.some_example_name.model.PlayerModel;

public class CharmController {

    private final PlayerModel player;

    public CharmController(PlayerModel player) {
        this.player = player;
    }

    public void toggleCharm(String charmName) {
        switch (charmName) {
            case "Soul Catcher":
                if (player.charmSoulCatcher) {
                    player.charmSoulCatcher = false;
                    player.usedNotches--;
                } else if (player.usedNotches < player.MAX_NOTCHES) {
                    player.charmSoulCatcher = true;
                    player.usedNotches++;
                }
                break;
            case "Dashmaster":
                if (player.charmDashmaster) {
                    player.charmDashmaster = false;
                    player.usedNotches--;
                    player.DASH_COOLDOWN = 0.6f;
                } else if (player.usedNotches < player.MAX_NOTCHES) {
                    player.charmDashmaster = true;
                    player.usedNotches++;
                    player.DASH_COOLDOWN = 0.3f;
                }
                break;
            case "Unbreakable Strength":
                if (player.charmUnbreakableStrength) {
                    player.charmUnbreakableStrength = false;
                    player.usedNotches--;
                } else if (player.usedNotches < player.MAX_NOTCHES) {
                    player.charmUnbreakableStrength = true;
                    player.usedNotches++;
                }
                break;
            case "Quick Slash":
                if (player.charmQuickSlash) {
                    player.charmQuickSlash = false;
                    player.usedNotches--;
                    player.ATTACK_DURATION = 0.25f;
                } else if (player.usedNotches < player.MAX_NOTCHES) {
                    player.charmQuickSlash = true;
                    player.usedNotches++;
                    player.ATTACK_DURATION = 0.15f;
                }
                break;
            case "Quick Focus":
                if (player.charmQuickFocus) {
                    player.charmQuickFocus = false;
                    player.usedNotches--;
                    player.FOCUS_DURATION = 1.5f;
                } else if (player.usedNotches < player.MAX_NOTCHES) {
                    player.charmQuickFocus = true;
                    player.usedNotches++;
                    player.FOCUS_DURATION = 0.8f;
                }
                break;
        }
    }
}
