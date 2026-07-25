package io.github.some_example_name.model;

import com.badlogic.gdx.Gdx;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SaveManager {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private SaveData data;
    private static SaveManager instance;
    private int currentSlot = 1;
    public boolean isLoadAction = false;

    public SaveManager() {
    }

    public static SaveManager getInstance() {
        if (instance == null) {
            instance = new SaveManager();
        }
        return instance;
    }

    private String getFilePath(int slot) {
        return "database/save" + slot + ".json";
    }

    public void load(int slot) {
        this.currentSlot = slot;
        this.isLoadAction = true;
        String path = getFilePath(slot);
        if (Gdx.files.local(path).exists()) {
            String json = Gdx.files.local(path).readString();
            data = gson.fromJson(json, SaveData.class);
        } else {
            data = new SaveData();
            data.slotNumber = slot;
            save();
        }
    }

    public void save() {
        if (data == null) data = new SaveData();
        String path = getFilePath(currentSlot);
        Gdx.files.local(path).writeString(gson.toJson(data), false);
    }

    public SaveData getData() {
        if (data == null) load(1);
        return data;
    }

    public void resetToDefaults(int slot) {
        this.currentSlot = slot;
        this.isLoadAction = false;
        data = new SaveData();
        data.slotNumber = slot;
        save();
    }

    public boolean doesSaveExist(int slot) {
        return Gdx.files.local(getFilePath(slot)).exists();
    }
}
