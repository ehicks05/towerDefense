package hicks.td.audio;

import hicks.td.util.Log;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;

public class SoundManager
{
    private static final float GLOBAL_VOLUME_OFFSET = -10f;

    public static void init()
    {
        MP3 music = new MP3("ass\\aud\\spacy.mp3");
        music.play();
    }

    public static void playSFX(SoundEffect soundEffect)
    {
        playSound(new File(soundEffect.getPath()), GLOBAL_VOLUME_OFFSET + soundEffect.getVolumeOffset());
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
