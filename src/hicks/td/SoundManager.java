package hicks.td;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.sound.sampled.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class SoundManager
{
    private static Mixer mixer;
    private static AudioFormat format = new AudioFormat(44100, 16, 2, false, true);
    private static Line.Info targetLineInfo = new DataLine.Info(TargetDataLine.class, format);

    private static Clip bowFire;
    private static Clip bowHit;

    private static AudioInputStream bowFireStream;
    private static AudioInputStream bowHitStream;

    public static void init()
    {

        try
        {
//            Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
//            Mixer mixer = AudioSystem.getMixer(mixerInfo[0]);
//            mixer.open();

            new MediaThread().start();


//            Clip clip = AudioSystem.getClip();
//            clip.open(soundtrack);
//            clip.setFramePosition(0);
//            clip.start();
//            bowFireStream = AudioSystem.getAudioInputStream(new File("ass\\bowFire.wav"));
//            bowHitStream  = AudioSystem.getAudioInputStream(new File("ass\\bowHit.wav"));
//            bowFire = AudioSystem.getClip();
//            bowFire.open(bowFireStream);
//            bowHit = AudioSystem.getClip();
//            bowHit.open(bowHitStream);

        }
        catch (Exception e)
        {
            Log.info("sound error");
        }
    }

    public static void playShootSFX()
    {
        try
        {
            bowFireStream = AudioSystem.getAudioInputStream(new File("ass\\bowFire.wav"));

            Clip clip = AudioSystem.getClip();

            clip.open(bowFireStream);

            FloatControl masterGain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            masterGain.setValue(masterGain.getValue() - 10f);

            clip.stop();
            clip.setFramePosition(0);
            clip.start();

//            bowFire.stop();
//            bowFire.setFramePosition(0);
//            bowFire.start();
        }
        catch (Exception e)
        {
            Log.info(e.getMessage());
        }
    }

    public static void playHitSFX()
    {
        try
        {
            bowHitStream = AudioSystem.getAudioInputStream(new File("ass\\bowHit.wav"));
            Clip clip = AudioSystem.getClip();

            clip.open(bowHitStream);

            FloatControl masterGain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            masterGain.setValue(masterGain.getValue() - 10f);

            clip.stop();
            clip.setFramePosition(0);
            clip.start();
//            bowFire.stop();
//            bowFire.setFramePosition(0);
//            bowFire.start();
        }
        catch (Exception e)
        {

        }

//        bowHit.stop();
//        bowHit.setFramePosition(0);
//        bowHit.start();
    }
}
