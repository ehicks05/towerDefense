import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class GameMap
{
    private int m_size;
    private List<Unit> m_existingUnits;

    public GameMap()
    {
        m_existingUnits = new ArrayList<>();
    }

    public Point getRandomPointOnMap()
    {
        Random random = new Random();
        double x = random.nextDouble() * m_size;
        double y = random.nextDouble() * m_size;

        return new Point(x, y);
    }

    public Point getRandomPointOnMap(double minX, double maxX, double minY, double maxY)
    {
        Random random = new Random();
        double x = minX + random.nextDouble() * maxX;
        double y = minY + random.nextDouble() * maxY;

        return new Point(x, y);
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

        return possibleLocations.get(0);
    }

    public void placeUnitsRandomlyOnEachHalfOfMap(List<Unit> unitsToPlace)
    {
        for (Unit unit : unitsToPlace)
        {
            boolean unitPlaced = false;
            while (!unitPlaced)
            {
                Point point = null;
                if (unit.getTeam() == 1) point = getRandomPointOnMap(0, m_size / 2, 0, m_size);
                if (unit.getTeam() == 2) point = getRandomPointOnMap(m_size / 2, m_size / 2, 0, m_size);
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
        return x >= 0 && x < getSize() && y >= 0 && y < getSize();
    }

    public void addUnitToExistingUnits(Unit unit)
    {
        m_existingUnits.add(unit);
    }

    public void removeUnitFromExistingUnits(Unit unit)
    {
        m_existingUnits.remove(unit);
    }

    public int getSize()
    {
        return m_size;
    }

    public void setSize(int size)
    {
        m_size = size;
    }

    public List<Unit> getExistingUnits()
    {
        return m_existingUnits;
    }
}
