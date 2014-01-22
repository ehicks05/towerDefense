package hicks.td;

import hicks.td.audio.SoundManager;
import hicks.td.entities.Player;
import hicks.td.util.Log;
import hicks.td.util.Util;

import java.text.SimpleDateFormat;

public final class Init
{
    public static void init()
    {
        GameState.setStartTime(Util.now());
        Log.info("Clearing logs...");
        Log.info("Initializing " + new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a").format(GameState.getStartTime()));

        GameState.setPlayer(new Player(300, 20, 1));

        SoundManager.init();
    }
}