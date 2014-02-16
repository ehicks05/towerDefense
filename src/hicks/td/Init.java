package hicks.td;

import hicks.td.audio.SoundManager;
import hicks.td.entities.GameMap;
import hicks.td.entities.Player;
import hicks.td.ui.DisplayInfo;
import hicks.td.util.HumanTileLoader;
import hicks.td.util.Log;
import hicks.td.util.MapBuilder;
import hicks.td.util.Util;

import java.io.File;
import java.text.SimpleDateFormat;

public final class Init
{
    public static void init()
    {
        GameState.setStartTime(Util.now());
        Log.info("Initializing " + new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a").format(GameState.getStartTime()));

        deleteLogs();

        DisplayInfo.setDisplayProperties();
        SoundManager.init();
        HumanTileLoader.init();

        GameState.setPlayer(new Player(300, 20, 1));
        GameState.setGameMap(new GameMap(1024, 768));
        GameState.getGameMap().setWorldWidthInTiles(GameState.getGameMap().getWidth() / 32);
        GameState.getGameMap().setWorldHeightInTiles(GameState.getGameMap().getHeight() / 32);
        GameState.setTerrainImage(MapBuilder.buildMap());
    }

    private static void deleteLogs()
    {
        boolean deleteSuccess = new File("log.txt").delete();
        Log.info("Clearing logs..." + (deleteSuccess ? "done." : "log not found."));
    }
}