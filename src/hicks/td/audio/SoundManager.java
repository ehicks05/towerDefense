package hicks.td.audio;

import hicks.td.util.Log;

import javax.sound.sampled.*;
import java.io.File;

public class SoundManager
{
    public static void init()
    {
        try
        {
            new MediaThread().start();
        }
        catch (Exception e)
        {
            Log.info(e.getMessage());
        }
    }

    public static void playSFX(SoundEffect soundEffect)
    {
        playSound(new File(soundEffect.getPath()), -10f);
    }

    private static void playSound(File soundFile, float gainAdjustment)
    {
        try
        {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(soundFile));

            FloatControl masterGain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            masterGain.setValue(masterGain.getValue() + gainAdjustment);

            clip.setFramePosition(0);
            clip.start();
        }
        catch (Exception e)
        {
            Log.info(e.getMessage());
        }
    }
}
