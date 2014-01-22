package hicks.td.entities;

public final class GameMap
{
    private int m_width;
    private int m_height;

    private int m_worldWidthInTiles;
    private int m_worldHeightInTiles;

    private Tile[][] m_tiles;

    public GameMap(int width, int height)
    {
        m_width = width;
        m_height = height;
    }

    public int getWorldWidthInTiles()
    {
        return m_worldWidthInTiles;
    }

    public void setWorldWidthInTiles(int worldWidthInTiles)
    {
        this.m_worldWidthInTiles = worldWidthInTiles;
    }

    public int getWorldHeightInTiles()
    {
        return m_worldHeightInTiles;
    }

    public void setWorldHeightInTiles(int worldHeightInTiles)
    {
        this.m_worldHeightInTiles = worldHeightInTiles;
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

    public Tile[][] getTiles()
    {
        return m_tiles;
    }

    public void setTiles(Tile[][] tiles)
    {
        m_tiles = tiles;
    }
}
