package hicks.td.util;

public class PathPoint
{
    private int col;
    private int row;
    private int g;
    private int h;
    private int f;

    private PathPoint parent;

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
        return col + ", " + row + " f:" + f + " g:" + g + " h:" + h;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof PathPoint)) return false;
        PathPoint that = (PathPoint) obj;
        return this.col == that.col && this.row == that.row;
    }

    public int getCol()
    {
        return col;
    }

    public void setCol(int col)
    {
        this.col = col;
    }

    public int getRow()
    {
        return row;
    }

    public void setRow(int row)
    {
        this.row = row;
    }

    public int getG()
    {
        return g;
    }

    public void setG(int g)
    {
        this.g = g;
    }

    public int getH()
    {
        return h;
    }

    public void setH(int h)
    {
        this.h = h;
    }

    public int getF()
    {
        return f;
    }

    public void setF(int f)
    {
        this.f = f;
    }

    public PathPoint getParent()
    {
        return parent;
    }

    public void setParent(PathPoint parent)
    {
        this.parent = parent;
    }
}
