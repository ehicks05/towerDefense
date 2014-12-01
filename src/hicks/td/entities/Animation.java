package hicks.td.entities;

public class Animation extends Unit
{
    private String m_name = "";
    private Outfit m_outfit;
    private int m_frame;
    private int m_totalFrames;

    public Animation(String name, int sizeRadius, Point location)
    {
       this(name, sizeRadius, location, null);
    }

    public Animation(String name, int sizeRadius, Point location, Outfit outfit)
    {
        setFrame(0);
        setSizeRadius(sizeRadius);
        setLocation(location);

        if (name.equals("explosion"))
            m_totalFrames = 73;
        if (name.equals("death"))
            m_totalFrames = 59;

        m_outfit = outfit;
        m_name = name;
    }

    public String getName()
    {
        return m_name;
    }

    public void setName(String name)
    {
        m_name = name;
    }

    public Outfit getOutfit()
    {
        return m_outfit;
    }

    public void setOutfit(Outfit outfit)
    {
        m_outfit = outfit;
    }

    public int getFrame()
    {
        return m_frame;
    }

    public void setFrame(int frame)
    {
        m_frame = frame;
    }

    public int getTotalFrames()
    {
        return m_totalFrames;
    }

    public void setTotalFrames(int totalFrames)
    {
        m_totalFrames = totalFrames;
    }
}
