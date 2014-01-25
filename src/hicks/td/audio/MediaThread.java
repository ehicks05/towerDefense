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
            InputStream soundtrack = new FileInputStream(new File("ass\\main.mp3"));
            Player player = new Player(soundtrack);

//            player.play();
        }
        catch(Exception e)
        {
            Log.info(e.getMessage());
        }
    }
}
