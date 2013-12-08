package hicks.combat;

import hicks.combat.entities.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Init
{
    public static int WIDTH = 800;
    public static int HEIGHT = 600;

    public static void init(GameMap map)
    {
        // delete previous log
        if (new File("log.txt").delete())
            Log.logInfo(CombatPanel.getSimulationStart(), "Previous log deleted...");

        map.setWidth(WIDTH);
        map.setHeight(HEIGHT);
        Unit.setMap(map);

        // create a barracks for each team
        List<Unit> units = new ArrayList<>();

        for (int i = 0; i < 1; i++)
        {
            units.add(new Barracks(1));
            units.add(new Barracks(2));
        }

        // place units on the map - this method adds the units to the list of existing units the map is aware of
        map.placeUnitsRandomlyOnEachHalfOfMap(units);
    }
}
