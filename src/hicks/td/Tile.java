package hicks.td;

import java.awt.image.BufferedImage;

public final class Tile
{
    private String m_code;

    private int m_width;
    private int m_height;
    private BufferedImage m_image;

    public Tile(String code, int width, int height, BufferedImage image)
    {
        m_code = code;
        m_width = width;
        m_height = height;
        m_image = image;
    }

    public String getCode()
    {
        return m_code;
    }

    public void setCode(String code)
    {
        m_code = code;
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

    public BufferedImage getImage()
    {
        return m_image;
    }

    public void setImage(BufferedImage image)
    {
        m_image = image;
    }
}
