package hicks.td.audio;

import hicks.td.util.Log;
import javafx.embed.swing.JFXPanel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SoundManager
{
    private static final List<String> EFFECTS_TRIGGERED_THIS_TICK = new ArrayList<>();
    private static SoundEngine soundEngine;

    public static void init()
    {
        soundEngine = new SoundEngine(16);
        JFXPanel panel = new JFXPanel(); // using a javafx.scene.media.MediaPlayer requires us to do this???
        try
        {
            for (SoundEffect soundEffect : SoundEffect.values())
            {
                File file = new File(soundEffect.getPath());
                soundEngine.loadSoundEffects(soundEffect.name(), file.toURI().toURL(), soundEffect.getVolumeOffset());
            }
            soundEngine.loadMusic("music", new File("ass\\aud\\spacy.mp3").toURI().toURL());
        }
        catch (Exception e)
        {
            Log.info(e.getMessage());
        }

        soundEngine.playMusic("music");
    }

    public static void shutDown()
    {
        soundEngine.shutdown();
    }

    public static void clearEffectsTriggeredThisTick()
    {
        EFFECTS_TRIGGERED_THIS_TICK.clear();
    }

    public static void playSFX(SoundEffect soundEffect)
    {
        try
        {
            if (EFFECTS_TRIGGERED_THIS_TICK.contains(soundEffect.name()))
                return;

            EFFECTS_TRIGGERED_THIS_TICK.add(soundEffect.name());
        }
        catch (Exception e)
        {
            Log.info(e.getMessage());
        }

        soundEngine.playSound(soundEffect.name());
    }
}