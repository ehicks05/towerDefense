package hicks.td.util;

import hicks.td.World;

import java.awt.image.BufferedImage;
import java.util.*;

public final class MapBuilder
{
    private static int ROWS = 18;
    private static int COLUMNS = 24;

    public static BufferedImage buildMap()
    {
        BufferedImage terrain = new BufferedImage(World.getGameMap().getWidth(), World.getGameMap().getHeight(), BufferedImage.TYPE_INT_RGB);
        Map<String, BufferedImage> tiles = TileLoader.createTileList();
        List<int[]> rgbSets = new ArrayList<>();
        int[] grassRGB = tiles.get("GGGG").getRGB(0, 0, 32, 32, null, 0, 32);
        int[] roadRGB  = tiles.get("DDDD").getRGB(0, 0, 32, 32, null, 0, 32);
        rgbSets.add(grassRGB);
        rgbSets.add(roadRGB);

//        buildDonutMap(terrain);
        int[][] logicalMap = buildRandomMap();

        for (int col = 0; col < COLUMNS; col++)
        {
            for (int row = 0; row < ROWS; row++)
            {
                fillSquare(col, row, rgbSets.get(logicalMap[col][row]), terrain);
            }
        }

        return terrain;
    }

    private static int[][] buildRandomMap()
    {
        // determine start and end of road
        Random random = new Random();
        PathPoint startPoint = new PathPoint(0, random.nextInt(ROWS), 0, 0);
        PathPoint endPoint = new PathPoint(COLUMNS - 1, random.nextInt(ROWS), 0, 0);

        int[][] logicalMap = new int[COLUMNS][ROWS];
        logicalMap[startPoint.col][startPoint.row] = 1;
        logicalMap[endPoint.col][endPoint.row] = 1;

        PathPoint currentPoint = startPoint;
        boolean pathDone = false;
        while (!pathDone)
        {
            List<PathPoint> possibleNextPoints = getValidAdjacentPoints(currentPoint, endPoint, logicalMap);

            for (Iterator<PathPoint> i = possibleNextPoints.iterator(); i.hasNext();)
            {
                // can we still get to the exit?
                PathPoint pathPoint = i.next();

//                int originalState = logicalMap[pathPoint.col][pathPoint.row];
//                logicalMap[pathPoint.col][pathPoint.row] = 1;
                List<PathPoint> pathPoints = findPath(pathPoint, endPoint, logicalMap);
//                logicalMap[pathPoint.col][pathPoint.row] = originalState;

                if (pathPoints == null) i.remove();
            }

            if (possibleNextPoints.size() == 0) printLogicalMap(logicalMap);

            PathPoint nextPoint = possibleNextPoints.get(random.nextInt(possibleNextPoints.size()));
            logicalMap[nextPoint.col][nextPoint.row] = 1;
            currentPoint = nextPoint;

            if (possibleNextPoints.contains(endPoint)) pathDone = true;
        }

        return logicalMap;
    }

    private static List<PathPoint> findPath(PathPoint start, PathPoint end, int[][] logicalMap)
    {
        List<PathPoint> open = new ArrayList<>(Arrays.asList(start));
        List<PathPoint> closed = new ArrayList<>();

        while (true)
        {
            PathPoint pointWithLowestF = getPathPointWithLowestFCost(open);
            closed.add(pointWithLowestF);
            open.remove(pointWithLowestF);

            List<PathPoint> validAdjacentPoints = getValidAdjacentPoints(pointWithLowestF, end, logicalMap);
            for (PathPoint adjacentPoint : validAdjacentPoints)
            {
                if (closed.contains(adjacentPoint)) continue;
                if (!open.contains(adjacentPoint)) open.add(adjacentPoint);
                adjacentPoint.parent = pointWithLowestF;
            }

            if (closed.contains(end)) break;
            if (open.size() == 0) return null;
        }
        return closed;
    }

    private static List<PathPoint> getValidAdjacentPoints(PathPoint start, PathPoint end, int[][] logicalMap)
    {
        List<PathPoint> allAdjacentPoints = getAdjacentPoints(start, end);

        List<PathPoint> validAdjacentPoints = new ArrayList<>();
        for (PathPoint adjacentPoint : allAdjacentPoints)
        {
            if (!insideMap(adjacentPoint.col, adjacentPoint.row, logicalMap)) continue;
//            if (logicalMap[adjacentPoint.col][adjacentPoint.row] == 1) continue;
            boolean bordersOneRoadOrLess = roadsBordered(adjacentPoint, logicalMap) <= 1;
            if (bordersOneRoadOrLess)
                validAdjacentPoints.add(adjacentPoint);
        }
        return validAdjacentPoints;
    }

    private static List<PathPoint> getAdjacentPoints(PathPoint origin, PathPoint end)
    {
        List<PathPoint> adjacentPoints = new ArrayList<>();
        adjacentPoints.add(new PathPoint(origin.col, origin.row - 1, origin.g + 1, getDistance(end.col, end.row, origin.col, origin.row - 1)));
        adjacentPoints.add(new PathPoint(origin.col, origin.row + 1, origin.g + 1, getDistance(end.col, end.row, origin.col, origin.row + 1)));
        adjacentPoints.add(new PathPoint(origin.col - 1, origin.row, origin.g + 1, getDistance(end.col, end.row, origin.col - 1, origin.row)));
        adjacentPoints.add(new PathPoint(origin.col + 1, origin.row, origin.g + 1, getDistance(end.col, end.row, origin.col + 1, origin.row)));
        return adjacentPoints;
    }

    private static PathPoint getPathPointWithLowestFCost(List<PathPoint> pathPoints)
    {
        int lowestF = Integer.MAX_VALUE;
        PathPoint pointWithLowestF = null;
        for (PathPoint point : pathPoints)
        {
            if (point.f < lowestF)
            {
                lowestF = point.f;
                pointWithLowestF = point;
            }
        }
        return pointWithLowestF;
    }

    private static int getDistance(int col1, int row1, int col2, int row2)
    {
        return Math.abs(col1 - col2) + Math.abs(row1 - row2);
    }

    private static boolean insideMap(int col, int row, int[][] terrain)
    {
        if (col < 0 || col >= COLUMNS || row < 0 || row >= ROWS) return false;
        return true;
    }

    private static int roadsBordered(PathPoint point, int[][] terrain)
    {
        int col = point.col;
        int row = point.row;
        int roadsBordered = 0;
        if (col > 0           && terrain[col - 1][row] == 1) roadsBordered++;
        if (col + 1 < COLUMNS && terrain[col + 1][row] == 1) roadsBordered++;
        if (row > 0           && terrain[col][row - 1] == 1) roadsBordered++;
        if (row + 1 < ROWS    && terrain[col][row + 1] == 1) roadsBordered++;
        return roadsBordered;
    }

    private static void buildDonutMap(BufferedImage terrain)
    {
        Map<String, BufferedImage> tiles = TileLoader.createTileList();

        int[] grassRGB = tiles.get("GGGG").getRGB(0, 0, 32, 32, null, 0, 32);
        int[] roadRGB  = tiles.get("DDDD").getRGB(0, 0, 32, 32, null, 0, 32);

        for (int row = 0; row < ROWS; row++)
        {
            for (int column = 0; column < COLUMNS; column++)
            {
                boolean isOutsideEdge = row < 2 || row > ROWS-3 || column < 2 || column > COLUMNS-3;
                if (isOutsideEdge)
                    fillSquare(column, row, roadRGB, terrain);
                else
                    fillSquare(column, row, grassRGB, terrain);
            }
        }
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
