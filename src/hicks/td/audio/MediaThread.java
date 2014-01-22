package hicks.td.audio;

import hicks.td.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class MediaThread extends Thread
{
    public void run()
    {
        try
        {
            InputStream soundtrack = new FileInputStream(new File("ass\\main.mp3"));
            javazoom.jl.player.Player player = new javazoom.jl.player.Player(soundtrack);
            player.play();
        }
        catch(Exception e)
        {
            Log.info(e.getMessage());
        }
    }
}
