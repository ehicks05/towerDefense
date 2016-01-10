package hicks.td.entities;

import java.awt.image.BufferedImage;
import java.util.List;

public final class GameMap
{
    private static BufferedImage  terrainImage;
    public int[][]         m_logicalMap;
    public List<PathPoint> m_mobPath;

    private final int m_width;
    private final int m_height;

    public GameMap(int width, int height)
    {
        m_width = width;
        m_height = height;
    }

    public static BufferedImage getTerrainImage()
    {
        return terrainImage;
    }

    public static void setTerrainImage(BufferedImage terrainImage)
    {
        GameMap.terrainImage = terrainImage;
    }

    public int[][] getLogicalMap()
    {
        return m_logicalMap;
    }

    public void setLogicalMap(int[][] logicalMap)
    {
        m_logicalMap = logicalMap;
    }

    public List<PathPoint> getMobPath()
    {
        return m_mobPath;
    }

    public void setMobPath(List<PathPoint> mobPath)
    {
        m_mobPath = mobPath;
    }

    public int getWidth()
    {
        return m_width;
    }

    public int getHeight()
    {
        return m_height;
    }
}
