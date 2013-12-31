package hicks.td;

import java.io.File;
import java.text.SimpleDateFormat;

public class Init
{
    public final static int WORLD_WIDTH = 1024;
    public final static int WORLD_HEIGHT = 704;

    public final static int INTERFACE_HEIGHT = 64;
    public final static int TOTAL_SCREEN_HEIGHT = WORLD_HEIGHT + INTERFACE_HEIGHT;

    public static void init()
    {
        // delete previous log
        if (new File("log.txt").delete())
            Log.logInfo("Deleting old logs...");

        GameState.setStartTime(GameLogic.now());
        Log.logInfo("Simulation starting at " + new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a").format(GameState.getStartTime()));

        GameMap map = new GameMap();
        GameState.setGameMap(map);

        map.setWidth(WORLD_WIDTH);
        map.setHeight(WORLD_HEIGHT);

        Player player = new Player();
        player.setGold(100);
        player.setLives(20);
        player.setRound(1);
        GameState.setPlayer(player);
    }
}
