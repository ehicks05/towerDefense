package hicks.td.audio;

import hicks.td.util.Log;
import javazoom.jl.player.Player;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

public class MP3
{
    private String filename;
    private Player player;

    public MP3(String filename)
    {
        this.filename = filename;
    }

    public void close()
    {
        if (player != null) player.close();
    }

    public void play()
    {
        try
        {
            FileInputStream fis = new FileInputStream(filename);
            BufferedInputStream bis = new BufferedInputStream(fis);
            player = new Player(bis);
        }
        catch (Throwable e)
        {
            Log.info(e.getMessage(), true);
            StackTraceElement[] stackTraceElements = e.getStackTrace();
            for (StackTraceElement stackTraceElement : stackTraceElements)
            {
                Log.info(stackTraceElement.toString(), true);
            }

            System.out.println("Problem playing file " + filename);
            System.out.println(e);
        }

        // run in new thread to play in background
        new Thread()
        {
            public void run()
            {
                try
                {
                    player.play();
                }
                catch (Exception e)
                {
                    System.out.println(e);
                }
            }
        }.start();
    }
}