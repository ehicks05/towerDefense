package hicks.td.audio;

import hicks.td.util.Log;
import hicks.td.util.Util;
import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;

import javax.sound.sampled.*;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SoundManager
{
    private static final int SIMULTANEOUS_SOUNDS = 8;
    private static final float GLOBAL_VOLUME_OFFSET = -10f;
    private static List<BigDecimal> soundEndTimes = new ArrayList<>();
    private static List<Clip> playingClips = new ArrayList<>();

    public static void init()
    {
//        File wav = convertToWav();
//        playSound(wav, -6f, true);
    }

    public static void closeInactiveClips()
    {
        for (Clip clip : playingClips)
        {
            if (!clip.isActive())
            {
                clip.stop();
                clip.close();
            }
        }
    }

    public static void playSFX(SoundEffect soundEffect)
    {
        playSound(new File(soundEffect.getPath()), GLOBAL_VOLUME_OFFSET + soundEffect.getVolumeOffset());
    }

    public static int getNumberOfSoundsPlaying()
    {
        return soundEndTimes.size();
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
        BigDecimal instant = Util.now();
        for (Iterator<BigDecimal> i = soundEndTimes.iterator(); i.hasNext();)
        {
            BigDecimal soundEndTime = i.next();
            if (soundEndTime.compareTo(instant) < 0)
                i.remove();
        }

        if (soundEndTimes.size() > SIMULTANEOUS_SOUNDS) return;

        try
        {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(soundFile));
            playingClips.add(clip);

            FloatControl masterGain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            masterGain.setValue(masterGain.getValue() + gainAdjustment);

            clip.setFramePosition(0);

            if (loopContinuously)
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            else
                clip.start();

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            AudioFormat format = audioInputStream.getFormat();
            long frames = audioInputStream.getFrameLength();
            double durationInSeconds = (frames+0.0) / format.getFrameRate();
            soundEndTimes.add(Util.now().add(new BigDecimal(durationInSeconds * 1000)));
        }
        catch (Exception e)
        {
            Log.info(e.getMessage());
        }
    }
}
