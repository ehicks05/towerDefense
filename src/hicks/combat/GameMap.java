package hicks.combat;

import hicks.combat.entities.Unit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class GameMap
{
    private int m_width;
    private int m_height;
    private List<Unit> m_existingUnits;

    public GameMap()
    {
        m_existingUnits = new ArrayList<>();
    }

    public Point getRandomPointOnMap()
    {
        return getRandomPointOnMap(0, m_width, 0, m_height);
    }

    public Point getRandomPointOnMap(double minX, double maxX, double minY, double maxY)
    {
        Random random = new Random();
        double x = minX + random.nextDouble() * maxX;
        double y = minY + random.nextDouble() * maxY;

        return new Point(x, y);
    }

    public Point getRandomAvailablePoint()
    {
        Point point = null;
        while (point == null)
        {
            Point possiblePoint = getRandomPointOnMap();
            if (!isPositionOccupied(possiblePoint))
                point = possiblePoint;
        }

        return point;
    }

    public Point getAvailableAdjacentLocation(Point location)
    {
        double originX = location.getX();
        double originY = location.getY();

        List<Point> possibleLocations = new ArrayList<>();
        possibleLocations.add(new Point(originX - 1, originY - 1));
        possibleLocations.add(new Point(originX,     originY - 1));
        possibleLocations.add(new Point(originX + 1, originY - 1));

        possibleLocations.add(new Point(originX - 1, originY));
        possibleLocations.add(new Point(originX + 1, originY));

        possibleLocations.add(new Point(originX - 1, originY + 1));
        possibleLocations.add(new Point(originX,     originY + 1));
        possibleLocations.add(new Point(originX + 1, originY + 1));

        for (Iterator<Point> i = possibleLocations.iterator(); i.hasNext(); )
        {
            Point candidate = i.next();
            if (!isPositionOnMap(candidate) || isPositionOccupied(candidate)) i.remove();
        }

        if (possibleLocations.size() > 0)
            return possibleLocations.get(0);
        else
            return getRandomAvailablePoint();

    }

    public void placeUnitsRandomlyOnEachHalfOfMap(List<Unit> unitsToPlace)
    {
        for (Unit unit : unitsToPlace)
        {
            boolean unitPlaced = false;
            while (!unitPlaced)
            {
                Point point = null;
                if (unit.getTeam() == 1) point = getRandomPointOnMap(0, m_width / 2, 0, m_height);
                if (unit.getTeam() == 2) point = getRandomPointOnMap(m_width / 2, m_width / 2, 0, m_height);
                if (!isPositionOccupied(point))
                {
                    unit.setLocation(point);
                    addUnitToExistingUnits(unit);
                    unitPlaced = true;
                }
            }
        }
    }

    public boolean isPositionOccupied(Point location)
    {
        for (Unit unit : getExistingUnits())
            if (unit.getLocation().equals(location))
                return true;

        return false;
    }

    public boolean isPositionOnMap(Point location)
    {
        double x = location.getX();
        double y = location.getY();
        return x >= 0 && x < m_width && y >= 0 && y < m_height;
    }

    public void addUnitToExistingUnits(Unit unit)
    {
        m_existingUnits.add(unit);
    }

    public void removeUnitFromExistingUnits(Unit unit)
    {
        m_existingUnits.remove(unit);
    }

    public List<Unit> getExistingUnits()
    {
        return m_existingUnits;
    }

    public int getWidth()
    {
        return m_width;
    }

    public void setWidth(int width)
    {
        m_width = width;
    }

    public int getHeight()
    {
        return m_height;
    }

    public void setHeight(int height)
    {
        m_height = height;
    }
}
