package io.github.some_example_name.model;

import com.badlogic.gdx.Gdx;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SettingsManager {
    private static final String FILE_PATH = "database/settings.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private SettingsData data;

    public SettingsManager() {
        load();
    }

    public void load() {
        if (Gdx.files.local(FILE_PATH).exists()) {
            String json = Gdx.files.local(FILE_PATH).readString();
            data = gson.fromJson(json, SettingsData.class);
        } else {
            data = new SettingsData();
            save();
        }
    }

    public void save() {
        Gdx.files.local(FILE_PATH).writeString(gson.toJson(data), false);
    }

    public SettingsData getData() { return data; }

    public void resetToDefaults() {
        data = new SettingsData();
        save();
    }

    public void resetAudioToDefaults() {
        if (data != null) {
            data.masterVolume = 0.5f;
            data.musicVolume = 0.5f;
            data.sfxVolume = 0.5f;
            save();
        }
    }
}
