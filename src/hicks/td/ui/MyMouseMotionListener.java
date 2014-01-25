package hicks.td.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MyMouseMotionListener extends MouseAdapter
{
    public void mouseDragged(MouseEvent e)
    {
        super.mouseDragged(e);
        int eventX = e.getX();
        int eventY = e.getY();
    }
}
