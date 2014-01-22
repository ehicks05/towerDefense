package hicks.td.entities;

public interface Projectile
{
    public double getMaximumRange();
    public void setMaximumRange(double maximumRange);

    public double getDistanceTravelled();
    public void setDistanceTravelled(double distanceTravelled);

    public double getTheta();
    public void setTheta(double theta);

    public Point getLocation();
    public void setLocation(Point location);

    public Point getDestination();
    public void setDestination(Point destination);
}
