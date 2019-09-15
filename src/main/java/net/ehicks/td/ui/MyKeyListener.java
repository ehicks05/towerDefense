package net.ehicks.td.ui;

import net.ehicks.td.GameCanvas;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MyKeyListener extends KeyAdapter
{
    public void keyTyped(KeyEvent e)
    {
        super.keyTyped(e);
    }

    public void keyPressed(KeyEvent e)
    {
        super.keyPressed(e);
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
        {
            GameCanvas.openMenu();
        }
    }

    public void keyReleased(KeyEvent e)
    {
        super.keyReleased(e);
    }
}
