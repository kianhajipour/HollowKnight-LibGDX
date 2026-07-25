package io.github.some_example_name.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;

public class GameAudioSystem {
    public Sound focusReadySound;
    public Sound focusChargingSound;
    public Sound focusHealSound;
    public Sound attackSound;
    public Sound damageSound;
    public Sound dashSound;
    public Sound deathSound;
    public Sound bossActivateSound;
    public Array<Sound> zoteVoices;
    public Array<Sound> bossRoars;
    public Sound bossArmorDamage;
    public Sound bossHeadDamage;
    public Sound bossJump;
    public Sound bossLand;
    public Sound bossSlam;
    public Sound bossSwing;

    public void loadSounds() {
        focusReadySound = Gdx.audio.newSound(Gdx.files.internal("audio/focus_ready.wav"));
        focusChargingSound = Gdx.audio.newSound(Gdx.files.internal("audio/focus_health_charging.wav"));
        focusHealSound = Gdx.audio.newSound(Gdx.files.internal("audio/focus_health_heal.wav"));
        attackSound = Gdx.audio.newSound(Gdx.files.internal("audio/sword_2.wav"));
        damageSound = Gdx.audio.newSound(Gdx.files.internal("audio/hero_damage.wav"));
        dashSound = Gdx.audio.newSound(Gdx.files.internal("audio/hero_dash.wav"));
        deathSound = Gdx.audio.newSound(Gdx.files.internal("audio/hero_death_extra_details.wav"));
        bossActivateSound = Gdx.audio.newSound(Gdx.files.internal("audio/boss_door.wav"));

        zoteVoices = new Array<>();
        zoteVoices.add(Gdx.audio.newSound(Gdx.files.internal("audio/Zote_01.wav")));
        zoteVoices.add(Gdx.audio.newSound(Gdx.files.internal("audio/Zote_02.wav")));
        zoteVoices.add(Gdx.audio.newSound(Gdx.files.internal("audio/Zote_03.wav")));
        zoteVoices.add(Gdx.audio.newSound(Gdx.files.internal("audio/Zote_04.wav")));
        zoteVoices.add(Gdx.audio.newSound(Gdx.files.internal("audio/Zote_05.wav")));

        bossRoars = new Array<>();
        bossRoars.add(Gdx.audio.newSound(Gdx.files.internal("audio/boss/False_Knight_Attack_New_01.wav")));
        bossRoars.add(Gdx.audio.newSound(Gdx.files.internal("audio/boss/False_Knight_Attack_New_02.wav")));
        bossRoars.add(Gdx.audio.newSound(Gdx.files.internal("audio/boss/False_Knight_Attack_New_03.wav")));
        bossRoars.add(Gdx.audio.newSound(Gdx.files.internal("audio/boss/False_Knight_Attack_New_04.wav")));
        bossRoars.add(Gdx.audio.newSound(Gdx.files.internal("audio/boss/False_Knight_Attack_New_05.wav")));
        bossArmorDamage = Gdx.audio.newSound(Gdx.files.internal("audio/boss/false_knight_damage_armour.wav"));
        bossHeadDamage = Gdx.audio.newSound(Gdx.files.internal("audio/boss/false_knight_head_damage_2.wav"));
        bossJump = Gdx.audio.newSound(Gdx.files.internal("audio/boss/false_knight_jump.wav"));
        bossLand = Gdx.audio.newSound(Gdx.files.internal("audio/boss/false_knight_land.wav"));
        bossSlam = Gdx.audio.newSound(Gdx.files.internal("audio/boss/false_knight_strike_ground.wav"));
        bossSwing = Gdx.audio.newSound(Gdx.files.internal("audio/boss/false_knight_swing.wav"));
    }

    public void dispose() {
        if (focusReadySound != null) focusReadySound.dispose();
        if (focusChargingSound != null) focusChargingSound.dispose();
        if (focusHealSound != null) focusHealSound.dispose();
        if (attackSound != null) attackSound.dispose();
        if (damageSound != null) damageSound.dispose();
        if (dashSound != null) dashSound.dispose();
        if (deathSound != null) deathSound.dispose();
        if (bossActivateSound != null) bossActivateSound.dispose();
        for (Sound s : zoteVoices) s.dispose();
        for (Sound s : bossRoars) s.dispose();
        if (bossArmorDamage != null) bossArmorDamage.dispose();
        if (bossHeadDamage != null) bossHeadDamage.dispose();
        if (bossJump != null) bossJump.dispose();
        if (bossLand != null) bossLand.dispose();
        if (bossSlam != null) bossSlam.dispose();
        if (bossSwing != null) bossSwing.dispose();
    }
}
