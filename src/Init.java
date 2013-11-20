import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Init
{
    public static void init(GameMap map)
    {
        // delete previous log
        new File("log.txt").delete();

        map.setWidth(800);
        map.setHeight(600);

        // create a barracks for each team
        List<Unit> units = new ArrayList<>();

        for (int i = 0; i < 5; i++)
        {
            units.add(new Barracks(1));
            units.add(new Barracks(2));
        }

        // create some initial units
        for (int i = 0; i < 20; i++)
        {
            units.add(new Footman(1));
            units.add(new Footman(2));
        }

        for (int i = 0; i < 6; i++)
        {
            units.add(new Berserker(1));
            units.add(new Berserker(2));
        }

        for (int i = 0; i < 2; i++)
        {
            units.add(new Archer(1));
            units.add(new Archer(2));
        }

        for (int i = 0; i < 2; i++)
        {
            units.add(new Knight(1));
            units.add(new Knight(2));
        }

        // place units on the map - this method adds the units to the list of existing units the map is aware of
        map.placeUnitsRandomlyOnEachHalfOfMap(units);
    }
}
