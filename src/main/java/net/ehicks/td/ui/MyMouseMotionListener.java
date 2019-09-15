package net.ehicks.td.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MyMouseMotionListener extends MouseAdapter
{
    public void mouseMoved(MouseEvent e)
    {
        super.mouseMoved(e);
        int eventX = (int) (e.getX() * (1 / DisplayInfo.getScalingFactor()));
        int eventY = (int) (e.getY() * (1 / DisplayInfo.getScalingFactor()));

        InterfaceLogic.setMouseX(eventX);
        InterfaceLogic.setMouseY(eventY);
    }

    public void mouseDragged(MouseEvent e)
    {
        super.mouseDragged(e);
        int eventX = (int) (e.getX() * (1 / DisplayInfo.getScalingFactor()));
        int eventY = (int) (e.getY() * (1 / DisplayInfo.getScalingFactor()));

        InterfaceLogic.setMouseX(eventX);
        InterfaceLogic.setMouseY(eventY);
    }
}
