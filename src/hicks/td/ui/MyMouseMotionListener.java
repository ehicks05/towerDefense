package hicks.td.ui;

import hicks.td.GameCanvas;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MyMouseMotionListener extends MouseAdapter
{
    public void mouseMoved(MouseEvent e)
    {
        super.mouseMoved(e);
        int eventX = e.getX();
        int eventY = e.getY();

        GameCanvas.setMouseX(eventX);
        GameCanvas.setMouseY(eventY);
    }

    public void mouseDragged(MouseEvent e)
    {
        super.mouseDragged(e);
        int eventX = e.getX();
        int eventY = e.getY();
    }
}
