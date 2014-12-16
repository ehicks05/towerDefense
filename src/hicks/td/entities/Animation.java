package hicks.td.entities;

public class Animation extends Unit
{
    private final String m_name;
    private final int m_outfit;
    private final int m_totalFrames;
    private int m_frame;

    public Animation(String name, int sizeRadius, Point location)
    {
       this(name, sizeRadius, location, -1);
    }

    public Animation(String name, int sizeRadius, Point location, int outfit)
    {
        setFrame(0);
        setSizeRadius(sizeRadius);
        setLocation(location);

        switch (name)
        {
            case "explosion":
                m_totalFrames = 73;
                break;
            case "death":
                m_totalFrames = 59;
                break;
            default:
                m_totalFrames = 0;
                break;
        }

        m_outfit = outfit;
        m_name = name;
    }

    public String getName()
    {
        return m_name;
    }

    public int getOutfit()
    {
        return m_outfit;
    }

    public int getTotalFrames()
    {
        return m_totalFrames;
    }

    public int getFrame()
    {
        return m_frame;
    }

    public void setFrame(int frame)
    {
        m_frame = frame;
    }
}
