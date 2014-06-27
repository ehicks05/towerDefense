package hicks.td.ui;

import hicks.td.GameCanvas;

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
            GameCanvas.displayMenu();
        }
    }

    public void keyReleased(KeyEvent e)
    {
        super.keyReleased(e);
    }
}
