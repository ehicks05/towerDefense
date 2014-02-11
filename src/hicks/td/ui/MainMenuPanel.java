package hicks.td.ui;

import hicks.td.GameCanvas;
import hicks.td.util.Log;

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
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        final JLabel mainLabel = new JLabel("Eric's Tower Defense");
        mainLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 72));

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

        exitButton.addActionListener(new ActionListener()
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
        });

        this.add(mainLabel);
        this.add(startButton);
        this.add(exitButton);
    }
}
