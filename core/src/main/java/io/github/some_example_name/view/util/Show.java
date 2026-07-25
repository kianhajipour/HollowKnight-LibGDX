package io.github.some_example_name.view.util;

import io.github.some_example_name.model.SettingsManager;

public class Show {
    private static final SettingsManager manager = new SettingsManager();

    public static String get(String message) {
        if (manager.getData() == null) return message;

        String language = manager.getData().language;

        if ("de".equals(language)) {
            switch (message) {
                // Main Menu & General Buttons
                case "START GAME": return "SPIEL STARTEN";
                case "SETTINGS": return "EINSTELLUNGEN";
                case "OPTIONS": return "OPTIONEN";
                case "ACHIEVEMENTS": return "ERFOLGE";
                case "EXTRAS": return "EXTRAS";
                case "GUIDE": return "HANDBUCH";
                case "QUIT GAME": return "SPIEL BEENDEN";
                case "BACK": return "ZURÜCK";
                case "SAVE": return "SPEICHERN";
                case "RESET": return "ZURÜCKSETZEN";
                case "new game": return "Neues Spiel";

                // Settings Menu
                case "Music Volume": return "Musik-Lautstärke";
                case "Master Volume": return "Master-Lautstärke";
                case "SFX Volume": return "Effekt-Lautstärke";
                case "Brightness": return "Helligkeit";
                case "Reset Audio": return "Audio zurücksetzen";
                case "Language": return "Sprache";
                case "Game Controller": return "Spielsteuerung";

                // Pause Menu
                case "Resume": return "Fortsetzen";
                case "Cheats": return "Cheats";
                case "Exit": return "Beenden";

                // General UI State
                case "ACTIVATE": return "AKTIVIEREN";
                case "ON": return "AN";
                case "OFF": return "AUS";
                case "EQUIP": return "ANLEGEN";
                case "UNEQUIP": return "ABLEGEN";
                case "Notches: ": return "Kerben: ";

                // Cheats Localized (German)
                case "CHEAT CODES": return "CHEAT-CODES";
                case "CH_TELEPORT": return "Boss-Arena-Teleport";
                case "CH_TELEPORT_DESC": return "Teleportiert den Hauptcharakter sofort zur Arena.";
                case "CH_DOOR_OVERRIDE": return "Boss-Tür Überschreiben";
                case "CH_DOOR_OVERRIDE_DESC": return "Erzwingt das Sperren oder Entsperren der Bosstür.";
                case "CH_NOCLIP": return "Noclip-Modus";
                case "CH_NOCLIP_DESC": return "Freies Fliegen und Ignorieren der Schwerkraft.";
                case "CH_EMERGENCY_HEAL": return "Notheilung";
                case "CH_EMERGENCY_HEAL_DESC": return "Stellt eine Maske wieder her oder belebt wieder.";
                case "CH_REFILL_SOUL": return "Seelengefäß auffüllen";
                case "CH_REFILL_SOUL_DESC": return "Füllt die Seele sofort bis zum Maximum auf.";
                case "CH_ONE_HIT": return "Ein-Treffer-Kill";
                case "CH_ONE_HIT_DESC": return "Tötet normale Gegner sofort mit einem Schlag.";
                case "CH_GOD_MODE": return "Gott-Modus";
                case "CH_GOD_MODE_DESC": return "Vollständige Unbesiegbarkeit aktivieren.";

                // Zote Dialogues (German)
                case "ZOTE_DLG_1": return "Verflucht sei dieser schreckliche Ort! Es ist feucht, dunkel und voller Bestien.";
                case "ZOTE_DLG_2": return "Ich bin Zote der Mächtige, ein Ritter von großem Ruhm!";
                case "ZOTE_DLG_3": return "Steh mir nicht im Weg, du erbärmliche kleine Kreatur.";

                // Guide Categories
                case "Abilities": return "Fähigkeiten";
                case "Cheat codes": return "Cheat-Codes";
                case "Controller guide": return "Steuerung";

                // Controller Bindings
                case "MOVE_LEFT": return "Nach links bewegen";
                case "MOVE_RIGHT": return "Nach rechts bewegen";
                case "JUMP": return "Springen";
                case "ATTACK": return "Angreifen";
                case "DASH": return "Spurt";
                case "Focus": return "Fokus";

                // Achievements
                case "Completion": return "Komplettierung";
                case "Finish the game": return "Beende das Spiel";
                case "Speedrun": return "Speedrun";
                case "Finish the game within a specified time limit": return "Beende das Spiel innerhalb eines festgelegten Zeitlimits";
                case "True Hunter": return "Wahrer Jäger";
                case "Kill all types of enemies in the game": return "Töte alle Arten von Gegnern im Spiel";
                case "Defeat False Knight": return "Besiege den Falschen Ritter";
                case "Defeat the False Knight boss": return "Besiege den Boss 'Falscher Ritter'";

                // Inventory & Charms (German)
                case "INVENTORY & CHARMS": return "INVENTAR & AMULETTE";
                case "Soul Catcher": return "Seelenfänger";
                case "Soul Catcher Desc": return "Erhöht die Menge an gewonnener Seele, wenn Gegner mit dem Nagel getroffen werden.";
                case "Dashmaster": return "Spurtmeister";
                case "Dashmaster Desc": return "Ermöglicht dem Spieler, viel häufiger in kürzeren Abständen zu spurten.";
                case "Unbreakable Strength": return "Unzerbrechliche Stärke";
                case "Unbreakable Strength Desc": return "Stärkt den Ritter und erhöht den Schaden, der Gegnern durch Standard-Nagelangriffe zugefügt wird.";
                case "Quick Slash": return "Schneller Hieb";
                case "Quick Slash Desc": return "Erhöht die Angriffsgeschwindigkeit des Nagels erheblich und verkürzt die Abklingzeit nach jedem Schlag.";
                case "Quick Focus": return "Schneller Fokus";
                case "Quick Focus Desc": return "Erhöht die Geschwindigkeit beim Fokussieren von Magie und beim Heilen, wodurch die Erholungszeit verkürzt wird.";

                // Guide Content (German Translations)
                case "GUIDE_ALL_ABILITIES_TEXT":
                    return "[ BEWEGUNG & GESUNDHEITSSYSTEM ]\n\n" +
                        "• Spurt & Doppelsprung:\n" +
                        "  Der Spieler kann einen schnellen Spurt (Dash) auf dem Boden oder in der Luft sowie einen Doppelsprung ausführen, um Hindernisse zu umgehen und erfolgreich durch die Karte zu manövrieren.\n\n" +
                        "• Gesundheitssystem:\n" +
                        "  Der Spieler beginnt mit 5 Lebensmasken. Jede Kollision mit Gegnern oder Umgebungstacheln zieht 1 Maske ab.\n\n" +
                        "• Fokussieren & Seele:\n" +
                        "  Das Gedrückthalten der Fokus-Taste dauert 1,5 Sekunden, um eine fehlende Lebensmaske zu heilen und aufzufüllen, indem angesammelte Seele verbraucht wird. Das Treffen von Gegnern mit dem Nagel (Schwert) füllt das Seelengefäß um 11 Einheiten pro Treffer auf.\n\n\n" +
                        "[ ZAUBER & FÄHIGKEITEN ]\n\n" +
                        "• Rachsüchtiger Geist:\n" +
                        "  Feuert ein magisches Projektil horizontal in die Richtung, in die der Spieler gerade blickt. Es bewegt sich mit konstanترت Geschwindigkeit, wird nicht von der Schwerkraft beeinflusst und verschwindet sofort, wenn es mit Umgebungshindernissen (wie Wänden) kollidiert. Dieser Geist kann Gegner durchdringen, um Schaden zuzufügen. Kosten: 33 Seele (1/3 des Seelengefäßes).\n\n" +
                        "• Heulende Geister:\n" +
                        "  Erzeugt eine kraftvolle, nach oben gerichtete magische Explosion direkt über dem Kopf des Spielers, die speziell dafür entwickelt wurde, Gegnern in der Luft oder im Fall Schaden zuzufügen. Im Gegensatz zu Projektilen bleibt sie für kurze Zeit stationär und fügt allen Gegnern in ihrer Hitbox drei schnelle und aufeinanderfolgende Schadens-Ticks zu.";

                case "GUIDE_ALL_CHEATS_TEXT":
                    return "[ CHEATS & ENTWICKLERMENÜ ]\n\n" +
                        "• Boss-Arena-Teleport:\n" +
                        "  Teleportiert die Position der Hauptfigur sofort an den Anfang der Arena des Falschen Ritters, um verschiedene Phasen des Bosskampfffes schnell zu testen.\n\n" +
                        "• Noclip / Zuschauermodus:\n" +
                        "  Erhöht die Bewegungsgeschwindigkeit, deaktiviert Bewegungsanimationen und schaltet die Schwerkraft komplett aus. Dies ermöglicht es Entwicklern und Prüfern, frei zu fliegen und strukturelle Wände oder Kartenhindernisse ohne Kollision zu durchdringen.\n\n" +
                        "• Notheilung:\n" +
                        "  Gewährt sofort ein zusätzliches Leben (Lebensmaske), wenn die Gesundheit des Spielers in kritischen Situationen vollständig erschöpft ist.\n\n" +
                        "• Seelengefäß auffüllen:\n" +
                        "  Füllt das Seelengefäß des Spielers sofort und vollständig bis zur maximalen Kapazität auf.\n\n" +
                        "• Gott-Modus:\n" +
                        "  Schaltet die komplette Unbesiegbarkeit ein oder aus. Während er aktiv ist, erleidet der Spieler absolut keinen Schaden durch Fallen, Umgebungstacheln, Gegner oder aktive Bosskämpfe.";

                default: return message;
            }
        }

        switch (message) {
            // General UI State (English)
            case "ACTIVATE": return "ACTIVATE";
            case "ON": return "ON";
            case "OFF": return "OFF";
            case "EQUIP": return "EQUIP";
            case "UNEQUIP": return "UNEQUIP";
            case "Notches: ": return "Notches: ";

            // Cheats Localized (English)
            case "CHEAT CODES": return "CHEAT CODES";
            case "CH_TELEPORT": return "Boss Arena Teleport";
            case "CH_TELEPORT_DESC": return "Instantly teleports player to the boss arena.";
            case "CH_DOOR_OVERRIDE": return "Boss Door Override";
            case "CH_DOOR_OVERRIDE_DESC": return "Force lock or unlock the boss door room.";
            case "CH_NOCLIP": return "Noclip Mode";
            case "CH_NOCLIP_DESC": return "Fly freely through boundaries and ignore gravity.";
            case "CH_EMERGENCY_HEAL": return "Emergency Heal";
            case "CH_EMERGENCY_HEAL_DESC": return "Recover 1 health mask or revive if dead.";
            case "CH_REFILL_SOUL": return "Refill Soul Vessel";
            case "CH_REFILL_SOUL_DESC": return "Instantly fill soul storage to maximum.";
            case "CH_ONE_HIT": return "One-Hit Kill";
            case "CH_ONE_HIT_DESC": return "Kill regular non-boss enemies instantly.";
            case "CH_GOD_MODE": return "God Mode";
            case "CH_GOD_MODE_DESC": return "Toggle complete player invincibility.";

            // Zote Dialogues (English)
            case "ZOTE_DLG_1": return "Curse this foul place! It is damp, dark, and full of beasts.";
            case "ZOTE_DLG_2": return "I am Zote the Mighty, a knight of great renown!";
            case "ZOTE_DLG_3": return "Do not stand in my way, you pathetic little creature.";

            // Inventory & Charms (English)
            case "INVENTORY & CHARMS": return "INVENTORY & CHARMS";
            case "Soul Catcher": return "Soul Catcher";
            case "Soul Catcher Desc": return "Increase the amount of Soul gained when striking enemies with the Nail.";
            case "Dashmaster": return "Dashmaster";
            case "Dashmaster Desc": return "Allow the player to dash much more frequently in shorter intervals.";
            case "Unbreakable Strength": return "Unbreakable Strength";
            case "Unbreakable Strength Desc": return "Strengthen the knight, increasing damage dealt to enemies by standard Nail attacks.";
            case "Quick Slash": return "Quick Slash";
            case "Quick Slash Desc": return "Greatly increase Nail attack speed, reducing cooldown after each slash.";
            case "Quick Focus": return "Quick Focus";
            case "Quick Focus Desc": return "Increase magic focusing and heal speed, shortening recovery time.";

            case "GUIDE_ALL_ABILITIES_TEXT":
                return "[ MOVEMENTS & HEALTH SYSTEM ]\n\n" +
                    "• Dash & Double Jump:\n" +
                    "  The player can perform a rapid Dash on the ground or in mid-air, as well as a Double Jump to bypass obstacles and successfully maneuver through the map.\n\n" +
                    "• Health System:\n" +
                    "  The player starts with 5 Health Masks. Every collision with enemies or environmental spikes deducts 1 Mask.\n\n" +
                    "• Focusing & Soul:\n" +
                    "  Holding down the Focus key takes 1.5 seconds to heal and refill a missing Health Mask by consuming accumulated Soul. Striking enemies with the nail (Sword) refills the Soul Vessel by 11 units per hit.\n\n\n" +
                    "[ SPELLS & ABILITIES ]\n\n" +
                    "• Vengeful Spirit:\n" +
                    "  Fires a magical projectile horizontally in the direction the player is currently facing. It travels at a constant velocity, is unaffected by gravity, and disappears instantly upon colliding with environmental obstacles (like walls). This spirit can pass through enemies to inflict damage. Cost: 33 Soul (1/3 of the Soul Vessel).\n\n" +
                    "• Howling Wraiths:\n" +
                    "  Generates a powerful upward magical explosion directly above the player's head, designed specifically to damage enemies while airborne or falling. Unlike projectiles, it remains stationary for a short duration, dealing three rapid and consecutive damage ticks to all enemies caught inside its hitbox.";

            case "GUIDE_ALL_CHEATS_TEXT":
                return "[ CHEATS & DEVELOPER MENU ]\n\n" +
                    "• Boss Arena Teleport:\n" +
                    "  Instantly teleports the main character's position to the beginning of the False Knight boss arena to allow fast testing of different boss fight phases.\n\n" +
                    "• Noclip / Spectator Mode:\n" +
                    "  Increases movement speed, deactivates movement animations, and disables gravity entirely. This allows developers and reviewers to fly freely and pass through structural walls or map obstacles without collision.\n\n" +
                    "• Emergency Heal:\n" +
                    "  Grants an extra life (Health Mask) immediately when the player's health pool is completely depleted in critical situations.\n\n" +
                    "• Refill Soul Vessel:\n" +
                    "  Instantly and fully refills the player's Soul Vessel to maximum capacity.\n\n" +
                    "• God Mode:\n" +
                    "  Toggles complete invincibility on or off. While active, the player takes absolutely zero damage from traps, environmental spikes, enemies, or active boss fights.";

            default:
                return message;
        }
    }

    public static SettingsManager getManager() {
        return manager;
    }
}
