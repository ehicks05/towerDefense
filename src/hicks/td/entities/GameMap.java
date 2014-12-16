package hicks.td.entities;

public final class GameMap
{
    private final int m_width;
    private final int m_height;

    private final int m_worldWidthInTiles;
    private final int m_worldHeightInTiles;

    private Tile[][] m_tiles;

    public GameMap(int width, int height)
    {
        m_width = width;
        m_height = height;

        m_worldWidthInTiles = width / 32;
        m_worldHeightInTiles = height / 32;
    }

    public int getWorldWidthInTiles()
    {
        return m_worldWidthInTiles;
    }

    public int getWorldHeightInTiles()
    {
        return m_worldHeightInTiles;
    }

    public int getWidth()
    {
        return m_width;
    }

    public int getHeight()
    {
        return m_height;
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
