package net.ehicks.td.entities;

import java.awt.*;

public final class GameImage
{
    private final String m_path;
    private final Image m_image;

    public GameImage(String path, Image image)
    {
        m_path = path;
        m_image = image;
    }

    public String getPath()
    {
        return m_path;
    }

    public Image getImage()
    {
        return m_image;
    }
}
