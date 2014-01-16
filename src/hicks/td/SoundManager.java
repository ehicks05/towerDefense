package hicks.td;

import javax.sound.sampled.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SoundManager
{
    private static Clip bowFire;
    private static Clip bowHit;

    private static AudioInputStream bowFireStream;
    private static AudioInputStream bowHitStream;

    private static List<Clip> clips = new ArrayList<>();

    public static void init()
    {
        try
        {
            Mixer.Info[] mixerInfoArray = AudioSystem.getMixerInfo();
            Mixer.Info mixerInfo = mixerInfoArray[0];

            Mixer mixer = AudioSystem.getMixer(mixerInfo);

            DataLine.Info info = new DataLine.Info(Clip.class, new AudioFormat(44100, 16, 2, true, true));
            Line line = AudioSystem.getLine(info);

            bowFireStream  = AudioSystem.getAudioInputStream(new File("ass\\bowFire.wav"));
            bowHitStream   = AudioSystem.getAudioInputStream(new File("ass\\bowHit.wav"));

            bowFire = AudioSystem.getClip();
            bowFire.open(bowFireStream);

            bowHit = AudioSystem.getClip();
            bowHit.open(bowHitStream);

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
            Clip tempClip = AudioSystem.getClip();
            tempClip.open(bowFireStream);
            clips.add(tempClip);

            tempClip.start();
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
            Clip tempClip = AudioSystem.getClip();
            tempClip.open(bowHitStream);
            clips.add(tempClip);

            tempClip.start();
        }
        catch (Exception e)
        {
            Log.info(e.getMessage());
        }
    }
}
