package hicks.td;

public final class GameMap
{
    private int m_width;
    private int m_height;
    private Tile[][] m_tiles = new Tile[Init.WORLD_WIDTH_IN_TILES][Init.WORLD_HEIGHT_IN_TILES];

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
