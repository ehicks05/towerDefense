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
        new File("log.txt").delete();

        map.setWidth(WIDTH);
        map.setHeight(HEIGHT);

        // create a barracks for each team
        List<Unit> units = new ArrayList<>();

        for (int i = 0; i < 1; i++)
        {
            units.add(new Barracks(1));
            units.add(new Barracks(2));
        }

        // create some initial units
        for (int i = 0; i < 0; i++)
        {
            units.add(new Footman(1));
            units.add(new Footman(2));
        }

        for (int i = 0; i < 0; i++)
        {
            units.add(new Berserker(1));
            units.add(new Berserker(2));
        }

        for (int i = 0; i < 0; i++)
        {
            units.add(new Archer(1));
            units.add(new Archer(2));
        }

        for (int i = 0; i < 0; i++)
        {
            units.add(new Knight(1));
            units.add(new Knight(2));
        }

        // place units on the map - this method adds the units to the list of existing units the map is aware of
        map.placeUnitsRandomlyOnEachHalfOfMap(units);
    }
}
