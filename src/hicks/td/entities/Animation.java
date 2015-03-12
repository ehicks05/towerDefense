package hicks.td.entities;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Animation extends Unit
{
    private final String m_name;
    private final int m_outfit;
    private final int m_totalFrames;
    private int m_frame;
    private BigDecimal m_secondsPerFrame;
    private BigDecimal m_timeSinceLastFrame = BigDecimal.ZERO;

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
                m_totalFrames = 74;
                m_secondsPerFrame = BigDecimal.ONE.divide(new BigDecimal(120), 3, RoundingMode.HALF_UP);
                break;
            case "death":
                m_totalFrames = 6;
                m_secondsPerFrame = BigDecimal.ONE.divide(new BigDecimal(6), 3, RoundingMode.HALF_UP);
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

    public BigDecimal getSecondsPerFrame()
    {
        return m_secondsPerFrame;
    }

    public void setSecondsPerFrame(BigDecimal secondsPerFrame)
    {
        m_secondsPerFrame = secondsPerFrame;
    }

    public BigDecimal getTimeSinceLastFrame()
    {
        return m_timeSinceLastFrame;
    }

    public void setTimeSinceLastFrame(BigDecimal timeSinceLastFrame)
    {
        m_timeSinceLastFrame = timeSinceLastFrame;
    }
}
