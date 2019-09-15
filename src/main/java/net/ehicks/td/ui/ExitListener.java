package net.ehicks.td.ui;

import net.ehicks.td.util.Log;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ExitListener implements ActionListener
{
    public void actionPerformed(ActionEvent e)
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
}
