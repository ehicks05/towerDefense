package hicks.td.entities;

import java.awt.*;

public final class GameImage
{
    private String m_path;
    private Image m_image;

    public GameImage(String path, Image image)
    {
        m_path = path;
        m_image = image;
    }

    public String getPath()
    {
        return m_path;
    }

    public void setPath(String path)
    {
        m_path = path;
    }

    public Image getImage()
    {
        return m_image;
    }

    public void setImage(Image image)
    {
        m_image = image;
    }
}
