package hicks.td.util;

import hicks.td.World;

import java.awt.image.BufferedImage;
import java.util.*;

public final class MapBuilder
{
    private static int ROWS = 18;
    private static int COLUMNS = 24;

    public static BufferedImage createImageFromLogicalMap(int[][] logicalMap)
    {
        BufferedImage terrain = new BufferedImage(World.getGameMap().getWidth(), World.getGameMap().getHeight(), BufferedImage.TYPE_INT_RGB);

        Map<String, BufferedImage> tiles = TileLoader.createTileList();

        List<int[]> rgbSets = new ArrayList<>();
        int[] grassRGB = tiles.get("GGGG").getRGB(0, 0, 32, 32, null, 0, 32);
        int[] roadRGB  = tiles.get("DDDD").getRGB(0, 0, 32, 32, null, 0, 32);
        rgbSets.add(grassRGB);
        rgbSets.add(roadRGB);

        for (int col = 0; col < COLUMNS; col++)
            for (int row = 0; row < ROWS; row++)
                fillSquare(col, row, rgbSets.get(logicalMap[col][row]), terrain);
        
        return terrain;
    }

    public static int[][] buildRandomMap()
    {
        int[][] logicalMap = new int[COLUMNS][ROWS];

        // determine start and end points
        Random random = new Random();
        PathPoint startPoint = new PathPoint(0, random.nextInt(ROWS), 0, 0);
        PathPoint endPoint = new PathPoint(COLUMNS - 1, random.nextInt(ROWS), 0, 0);

        logicalMap[startPoint.getCol()][startPoint.getRow()] = 1;
        logicalMap[endPoint.getCol()][endPoint.getRow()] = 1;
        
        List<PathPoint> path = new ArrayList<>(Arrays.asList(startPoint));

        PathPoint currentPoint = startPoint;
        boolean pathDone = false;
        while (!pathDone)
        {
            List<PathPoint> possibleNextPoints = getValidAdjacentPoints(currentPoint, endPoint, logicalMap, 1);

            for (Iterator<PathPoint> i = possibleNextPoints.iterator(); i.hasNext();)
            {
                // can this possible point still get to the exit?
                PathPoint pathPoint = i.next();

                if (pathPoint.equals(endPoint))
                {
                    possibleNextPoints.retainAll(Arrays.asList(pathPoint));
                    break;
                }

                List<PathPoint> pathPoints = findPath(pathPoint, endPoint, logicalMap);

                if (pathPoints == null) i.remove();
            }

            if (possibleNextPoints.size() == 0)
                printLogicalMap(logicalMap);

            PathPoint nextPoint = possibleNextPoints.get(random.nextInt(possibleNextPoints.size()));
            logicalMap[nextPoint.getCol()][nextPoint.getRow()] = 1;
            currentPoint = nextPoint;
            
            path.add(currentPoint);

            if (possibleNextPoints.contains(endPoint)) pathDone = true;
        }
        path.add(endPoint);
        World.setMobPath(path);
        
        return logicalMap;
    }

    private static List<PathPoint> findPath(PathPoint start, PathPoint end, int[][] logicalMap)
    {
        start.setG(0);
        start.setH(getDistance(start.getCol(), start.getRow(), end.getCol(), end.getRow()));
        start.setF(start.getG() + start.getH());

        List<PathPoint> open = new ArrayList<>(Arrays.asList(start));
        List<PathPoint> closed = new ArrayList<>();

        while (true)
        {
            PathPoint currentSquare = getPathPointWithLowestFCost(open);
            open.remove(currentSquare);
            closed.add(currentSquare);

            int maximumBorderedRoads = currentSquare.equals(start) || getDistance(currentSquare, end) <= 2 ? 1 : 0;
            List<PathPoint> validAdjacentPoints = getValidAdjacentPoints(currentSquare, end, logicalMap, maximumBorderedRoads);
            for (PathPoint adjacentPoint : validAdjacentPoints)
            {
                if (logicalMap[adjacentPoint.getCol()][adjacentPoint.getRow()] == 1 && !adjacentPoint.equals(end)) continue;

                if (closed.contains(adjacentPoint)) continue;
                if (!open.contains(adjacentPoint))
                {
                    open.add(adjacentPoint);
                    adjacentPoint.setParent(currentSquare);
                    adjacentPoint.setG(currentSquare.getG() + 1);
                    adjacentPoint.setH(getDistance(adjacentPoint.getCol(), adjacentPoint.getRow(), end.getCol(), end.getRow()));
                    adjacentPoint.setF(adjacentPoint.getG() + adjacentPoint.getH());
                }
                else
                    if (adjacentPoint.getG() < currentSquare.getG())
                        open.remove(adjacentPoint);
            }

            if (closed.contains(end)) break;
            if (open.size() == 0)
                return null;
        }
        return closed;
    }

    private static List<PathPoint> getValidAdjacentPoints(PathPoint start, PathPoint end, int[][] logicalMap, int maximumBorderedRoads)
    {
        List<PathPoint> validAdjacentPoints = new ArrayList<>();
        for (PathPoint adjacentPoint : getAllAdjacentPoints(start, end))
        {
            if (!insideMap(adjacentPoint)) continue;

            boolean bordersOneRoadOrLess = roadsBordered(adjacentPoint, logicalMap) <= maximumBorderedRoads;
            if (bordersOneRoadOrLess || getDistance(adjacentPoint, end) < 2)
                validAdjacentPoints.add(adjacentPoint);
        }
        return validAdjacentPoints;
    }

    private static List<PathPoint> getAllAdjacentPoints(PathPoint origin, PathPoint end)
    {
        List<PathPoint> adjacentPoints = new ArrayList<>();
        adjacentPoints.add(new PathPoint(origin.getCol(), origin.getRow() - 1, origin.getG() + 1, getDistance(end.getCol(), end.getRow(), origin.getCol(), origin.getRow() - 1)));
        adjacentPoints.add(new PathPoint(origin.getCol(), origin.getRow() + 1, origin.getG() + 1, getDistance(end.getCol(), end.getRow(), origin.getCol(), origin.getRow() + 1)));
        adjacentPoints.add(new PathPoint(origin.getCol() - 1, origin.getRow(), origin.getG() + 1, getDistance(end.getCol(), end.getRow(), origin.getCol() - 1, origin.getRow())));
        adjacentPoints.add(new PathPoint(origin.getCol() + 1, origin.getRow(), origin.getG() + 1, getDistance(end.getCol(), end.getRow(), origin.getCol() + 1, origin.getRow())));
        return adjacentPoints;
    }

    private static PathPoint getPathPointWithLowestFCost(List<PathPoint> pathPoints)
    {
        int lowestF = Integer.MAX_VALUE;
        PathPoint pointWithLowestF = null;
        for (PathPoint point : pathPoints)
        {
            if (point.getF() < lowestF)
            {
                lowestF = point.getF();
                pointWithLowestF = point;
            }
        }
        return pointWithLowestF;
    }

    private static int getDistance(int col1, int row1, int col2, int row2)
    {
        return Math.abs(col1 - col2) + Math.abs(row1 - row2);
    }

    private static int getDistance(PathPoint point1, PathPoint point2)
    {
        return Math.abs(point1.getCol() - point2.getCol()) + Math.abs(point1.getRow() - point2.getRow());
    }

    private static boolean insideMap(PathPoint point)
    {
        if (point.getCol() < 0 || point.getCol() >= COLUMNS || point.getRow() < 0 || point.getRow() >= ROWS) return false;
        return true;
    }

    private static int roadsBordered(PathPoint point, int[][] terrain)
    {
        int col = point.getCol();
        int row = point.getRow();
        int roadsBordered = 0;
        if (col > 0           && terrain[col - 1][row] == 1) roadsBordered++;
        if (col + 1 < COLUMNS && terrain[col + 1][row] == 1) roadsBordered++;
        if (row > 0           && terrain[col][row - 1] == 1) roadsBordered++;
        if (row + 1 < ROWS    && terrain[col][row + 1] == 1) roadsBordered++;
        return roadsBordered;
    }

    private static void fillSquare(int col, int row, int[] rgb, BufferedImage terrain)
    {
        terrain.setRGB(col * 32, row * 32, 32, 32, rgb, 0, 32);
    }

    private static void printLogicalMap(int[][] logicalMap)
    {
        for (int row = 0; row < ROWS; row++)
        {
            for (int col = 0; col < COLUMNS; col++)
            {
                System.out.print(logicalMap[col][row]);
            }
            System.out.println();
        }
    }
}
