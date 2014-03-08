package hicks.td.components;

import com.artemis.Component;

public class Position extends Component
{
    private double x;
    private double y;

    public Position(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    //

    public void addX(double x)
    {
        this.x += x;
    }

    public void addY(double y)
    {
        this.y += y;
    }

    //

    public double getX()
    {
        return x;
    }

    public void setX(double x)
    {
        this.x = x;
    }

    public double getY()
    {
        return y;
    }

    public void setY(double y)
    {
        this.y = y;
    }
}
