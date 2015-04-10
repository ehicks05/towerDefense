package hicks.td.entities;

public class Explosion extends Unit
{
    private int m_frame;

    public Explosion()
    {
        setFrame(0);
        setSizeRadius(96);
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
