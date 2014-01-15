package hicks.td;

import javax.sound.sampled.*;
import java.io.File;

public class SoundManager
{
    private static Clip bowFire;
    private static Clip bowHit;

    public static void init()
    {
        try
        {
            Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
            DataLine.Info info = new DataLine.Info(Clip.class, new AudioFormat(44100, 16, 2, true, true));
            Line line = AudioSystem.getLine(info);

            AudioInputStream bowFireStream = AudioSystem.getAudioInputStream(new File("ass\\bowFire.wav"));
            AudioInputStream bowHitStream = AudioSystem.getAudioInputStream(new File("ass\\bowHit.wav"));
            bowFire = AudioSystem.getClip();
            bowFire.open(bowFireStream);
            bowHit = AudioSystem.getClip();
            bowHit.open(bowHitStream);

        }
        catch (Exception e)
        {
            Log.logInfo("sound error");
        }
    }

    public static void playShootSFX()
    {
        bowFire.stop();
        bowFire.setFramePosition(0);
        bowFire.start();
    }

    public static void playHitSFX()
    {
        bowHit.stop();
        bowHit.setFramePosition(0);
        bowHit.start();
    }
}
