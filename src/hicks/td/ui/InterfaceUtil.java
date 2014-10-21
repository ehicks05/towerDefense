package hicks.td.ui;

import hicks.td.World;
import hicks.td.entities.Point;
import hicks.td.entities.Tower;
import hicks.td.entities.Unit;
import hicks.td.util.Util;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class InterfaceUtil
{
    public static void setSizeFields(JComponent component, Dimension dimension)
    {
        component.setMinimumSize(dimension);
        component.setPreferredSize(dimension);
        component.setMaximumSize(dimension);
    }

    public static int snapToMiddleOfTile(int input)
    {
        int tile = input / 32;
        return (tile * 32) + 16;
    }

    public static boolean isValidLocation(int x, int y, int radiusOfNewBuilding)
    {
        Point attemptedBuildLocation = new Point(x, y);
        List<Tower> towers = new ArrayList<>(Util.getTowers());

        // check against existing towers
        for (Tower tower : towers)
        {
            Point towerLocation = tower.getLocation();
            double distance = attemptedBuildLocation.getDistance(towerLocation);
            if (distance < tower.getSizeRadius() + radiusOfNewBuilding)
                return false;
        }

        // check against edge of map
        if (!isObjectInBounds(x, y, radiusOfNewBuilding)) return false;

        // check against terrain - the simple and inflexible way
        if (!isObjectOffTheRoad(x, y, radiusOfNewBuilding)) return false;

        return true;
    }

    private static boolean isObjectInBounds(int x, int y, int radius)
    {
        return (x - radius >= 0 && x + radius < World.getGameMap().getWidth() &&
                y - radius >= 0 && y + radius < World.getGameMap().getHeight());
    }

    private static boolean isObjectOffTheRoad(int x, int y, int radius)
    {
        return (x - radius >= 64 && x + radius <= World.getGameMap().getWidth() - 64 &&
                y - radius >= 64 && y + radius <= World.getGameMap().getHeight() - 64);
    }
}
