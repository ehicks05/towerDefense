package hicks.td.audio;

import hicks.td.util.Log;
import org.kc7bfi.jflac.apps.Player;

public class MediaThread extends Thread
{
    public void run()
    {
        try
        {
            Player flacPlayer = new Player();
            flacPlayer.decode("ass\\aud\\DunMorogh.flac");
        }
        catch(Exception e)
        {
            Log.info(e.getMessage());
        }
    }
}
