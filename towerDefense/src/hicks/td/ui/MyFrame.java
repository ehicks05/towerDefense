package hicks.td.ui;

import hicks.td.util.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MyFrame extends JFrame
{
    public MyFrame()
    {
        super("Eric's Tower Defense");
        getContentPane().setPreferredSize(new Dimension(DisplayInfo.getWindowWidth(), DisplayInfo.getWindowHeight()));

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setLocation(32, 32);
        setResizable(false);
        setVisible(true);

        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent windowEvent)
            {
                if (JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to exit?", "Exit?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
                {
                    Log.info("Game was manually terminated...", true);
                    System.exit(0);
                }
            }
        });
    }
}
