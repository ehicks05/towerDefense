package hicks.combat;

import hicks.combat.entities.Barracks;
import hicks.combat.entities.Unit;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Init
{
    public static int WIDTH = 800;
    public static int HEIGHT = 600;

    public static void init()
    {
        // delete previous log
        if (new File("log.txt").delete())
            Log.logInfo("Previous log deleted...");

        GameState.setStartTime(GameLogic.now());
        Log.logInfo("Simulation starting at " + new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a").format(GameState.getStartTime()));

        GameMap map = new GameMap();
        GameState.setGameMap(map);

        map.setWidth(WIDTH);
        map.setHeight(HEIGHT);

        // create a barracks for each team
        for (int i = 0; i < 1; i++)
        {
            GameState.addUnit(new Barracks(1));
            GameState.addUnit(new Barracks(2));
        }

        // place units on the map - this method adds the units to the list of existing units the map is aware of
        GameLogic.placeUnitsRandomlyOnEachHalfOfMap(map, GameState.getUnits());
    }
}
