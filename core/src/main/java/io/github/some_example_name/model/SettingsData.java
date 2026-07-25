package io.github.some_example_name.model;

import com.badlogic.gdx.Input;
import java.util.HashMap;
import java.util.Map;

public class SettingsData {
    public float masterVolume = 0.5f;
    public float musicVolume = 0.5f;
    public float sfxVolume = 0.5f;
    public float brightness = 0.5f;
    public String language = "en";

    public Map<String, Integer> keyBindings = new HashMap<>();

    public SettingsData() {
        keyBindings.put("MOVE_LEFT", Input.Keys.LEFT);
        keyBindings.put("MOVE_RIGHT", Input.Keys.RIGHT);
        keyBindings.put("JUMP", Input.Keys.SPACE);
        keyBindings.put("ATTACK", Input.Keys.X);
        keyBindings.put("DASH", Input.Keys.C);
        keyBindings.put("Focus", Input.Keys.A);
        keyBindings.put("VENGEFUL_SPIRIT", Input.Keys.Q);
        keyBindings.put("HOWLING_WRAITHS", Input.Keys.W);
    }
}
