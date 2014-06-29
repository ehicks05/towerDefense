package hicks.td.audio;

import hicks.td.util.Log;
import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;

public class SoundManager
{
    private static final float GLOBAL_VOLUME_OFFSET = -10f;

    public static void init()
    {
        File wav = convertToWav();
        playSound(wav, 0.7f, true);
    }

    public static void playSFX(SoundEffect soundEffect)
    {
        playSound(new File(soundEffect.getPath()), GLOBAL_VOLUME_OFFSET + soundEffect.getVolumeOffset());
    }

    private static File convertToWav()
    {
        try
        {
            File source = new File("ass\\aud\\spacy.mp3");
            File target = new File("ass\\aud\\spacy.wav");
            target.deleteOnExit();

            Encoder encoder = new Encoder();

            EncodingAttributes encodingAttributes = new EncodingAttributes();
            encodingAttributes.setFormat("wav");

            AudioAttributes audioAttributes = new AudioAttributes();
            audioAttributes.setCodec("pcm_s16le");

            encodingAttributes.setAudioAttributes(audioAttributes);
            encoder.encode(source, target, encodingAttributes);

            return target;
        }
        catch (EncoderException e)
        {
            System.out.println(e.getMessage());
        }

        return null;
    }

    private static void playSound(File soundFile, float gainAdjustment)
    {
        playSound(soundFile, gainAdjustment, false);
    }
    private static void playSound(File soundFile, float gainAdjustment, boolean loopContinuously)
    {
        try
        {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(soundFile));

            FloatControl masterGain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            masterGain.setValue(masterGain.getValue() + gainAdjustment);

            clip.setFramePosition(0);

            if (loopContinuously)
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            else
                clip.start();
        }
        catch (Exception e)
        {
            Log.info(e.getMessage());
        }
    }
}
