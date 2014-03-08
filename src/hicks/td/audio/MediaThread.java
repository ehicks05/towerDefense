package hicks.td.audio;

import hicks.td.util.Log;
import javazoom.jl.player.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class MediaThread extends Thread
{
    public void run()
    {
        try
        {
            File musicFile = new File("ass\\aud\\main.mp3");
            InputStream soundtrack = new FileInputStream(musicFile);
            Player player = new Player(soundtrack);

            player.play();
        }
        catch(Exception e)
        {
            Log.info(e.getMessage());
        }
    }
}
