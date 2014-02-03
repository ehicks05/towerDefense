package hicks.td.ui;

import hicks.td.GameCanvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuPanel extends JPanel
{
    public MainMenuPanel()
    {
        this.setPreferredSize(new Dimension(DisplayInfo.getWindowWidth(), DisplayInfo.getWindowHeight()));
        this.setVisible(true);
        this.setName("mainMenuPanel");
        this.setLayout(new FlowLayout());

        final JButton startButton = new JButton("Start");
        startButton.setVisible(true);
        final JButton exitButton = new JButton("Exit");
        exitButton.setVisible(true);

        startButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                GameCanvas.closeMenu();
            }
        });

        this.add(startButton);
        this.add(exitButton);
    }
}
