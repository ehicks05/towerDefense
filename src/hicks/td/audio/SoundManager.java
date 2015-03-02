package hicks.td.audio;

import hicks.td.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SoundManager
{
    private static final List<String> EFFECTS_TRIGGERED_THIS_TICK = new ArrayList<>();
    private static final SoundEngine SOUND_ENGINE = new SoundEngine(32);

    public static void init()
    {
        try
        {
            for (SoundEffect soundEffect : SoundEffect.values())
            {
                File file = new File(soundEffect.getPath());
                SOUND_ENGINE.loadSoundEffects(soundEffect.name(), file.toURI().toURL(), soundEffect.getVolumeOffset(), false);
            }
            SOUND_ENGINE.loadSoundEffects("music", new File("ass\\aud\\spacy.mp3").toURI().toURL(), 0, true);
        }
        catch (Exception e)
        {
            Log.info(e.getMessage());
        }

        SOUND_ENGINE.playSound("music");
    }

    public static void shutDown()
    {
        SOUND_ENGINE.shutdown();
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

        SOUND_ENGINE.playSound(soundEffect.name());
    }
}