package hicks.td.util;

public class PathPoint
{
    int col;
    int row;
    int g;
    int h;
    int f;

    PathPoint parent;

    public PathPoint(int col, int row, int g, int h)
    {
        this.col = col;
        this.row = row;
        this.g = g;
        this.h = h;
        this.f = g + h;
    }

    @Override
    public String toString()
    {
        return col + ", " + row;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof PathPoint)) return false;
        PathPoint that = (PathPoint) obj;
        return this.col == that.col && this.row == that.row;
    }
}
