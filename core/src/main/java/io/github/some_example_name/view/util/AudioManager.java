package io.github.some_example_name.view.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioManager {
    private static Music menuMusic;
    private static Sound clickSound;
    private static Sound hoverSound;

    public static void init() {
        if (menuMusic == null) {
            menuMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/Title.wav"));
            clickSound = Gdx.audio.newSound(Gdx.files.internal("audio/ui_option_click.wav"));
            hoverSound = Gdx.audio.newSound(Gdx.files.internal("audio/ui_change_selection.wav"));
        }
    }

    public static void playMenuMusic() {
        init();
        if (!menuMusic.isPlaying()) {
            updateVolume();
            menuMusic.setLooping(true);
            menuMusic.play();
        }
    }

    public static void updateVolume() {
        if (menuMusic != null) {
            float musicVol = Show.getManager().getData().musicVolume * Show.getManager().getData().masterVolume;
            menuMusic.setVolume(musicVol);
        }
    }

    public static void stopMenuMusic() {
        if (menuMusic != null && menuMusic.isPlaying()) {
            menuMusic.stop();
        }
    }

    public static void playClick() {
        init();
        if (clickSound != null) {
            float sfxVol = Show.getManager().getData().sfxVolume * Show.getManager().getData().masterVolume;
            clickSound.play(sfxVol);
        }
    }

    public static void playHover() {
        init();
        if (hoverSound != null) {
            float sfxVol = Show.getManager().getData().sfxVolume * Show.getManager().getData().masterVolume;
            hoverSound.play(sfxVol);
        }
    }

    public static void dispose() {
        if (menuMusic != null) menuMusic.dispose();
        if (clickSound != null) clickSound.dispose();
        if (hoverSound != null) hoverSound.dispose();
    }
}
