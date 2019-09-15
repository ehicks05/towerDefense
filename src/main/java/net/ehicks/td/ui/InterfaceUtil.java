package net.ehicks.td.ui;

import net.ehicks.td.World;
import net.ehicks.td.entities.Point;
import net.ehicks.td.entities.Tower;
import net.ehicks.td.entities.PathPoint;
import net.ehicks.td.util.Util;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

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

    public static int convertToTile(int input)
    {
        return input / 32;
    }

    public static boolean isValidLocation(int x, int y, int radiusOfNewBuilding)
    {
        Point attemptedBuildLocation = new Point(x, y);

        // check against existing towers
        boolean onExistingTower = isOnExistingTower(radiusOfNewBuilding, attemptedBuildLocation);
        if (onExistingTower) return false;

        // check against edge of map
        boolean inBounds = isInBounds(x, y, radiusOfNewBuilding);
        if (!inBounds) return false;

        // check against terrain
        boolean onRoad = isOnTheRoad(x, y, radiusOfNewBuilding);
        if (onRoad) return false;

        return true;
    }

    private static boolean isOnExistingTower(int radiusOfNewBuilding, Point attemptedBuildLocation)
    {
        for (Tower tower : new ArrayList<>(Util.getTowers()))
        {
            Point towerLocation = tower.getLocation();
            double distance = attemptedBuildLocation.getDistance(towerLocation);
            if (distance < tower.getSizeRadius() + radiusOfNewBuilding)
                return true;
        }
        return false;
    }

    private static boolean isInBounds(int x, int y, int radius)
    {
        int col = convertToTile(x);
        int row = convertToTile(y);
        return (col >= 0 && col < World.getGameMap().getLogicalMap().length &&
                row >= 0 && row < World.getGameMap().getLogicalMap()[0].length);
    }

    private static boolean isOnTheRoad(int x, int y, int radius)
    {
        int col = convertToTile(x);
        int row = convertToTile(y);
        for (PathPoint pathPoint : World.getGameMap().getMobPath())
            if (pathPoint.getCol() == col && pathPoint.getRow() == row)
                return true;
        return false;
    }
}
