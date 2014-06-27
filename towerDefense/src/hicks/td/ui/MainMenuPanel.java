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
        mainLabel.setFont(new Font("Verdana", Font.BOLD, 64));
        mainLabel.setAlignmentX(CENTER_ALIGNMENT);

        final JButton startButton = new JButton("Start");
        startButton.setVisible(true);
        startButton.setFont(new Font("Verdana", Font.PLAIN, 32));
        startButton.setAlignmentX(CENTER_ALIGNMENT);
        startButton.setMinimumSize(new Dimension(240, 80));
        startButton.setPreferredSize(new Dimension(240, 80));
        startButton.setMaximumSize(new Dimension(240, 80));

        final JButton exitButton = new JButton("Exit");
        exitButton.setVisible(true);
        exitButton.setFont(new Font("Verdana", Font.PLAIN, 32));
        exitButton.setAlignmentX(CENTER_ALIGNMENT);
        exitButton.setMinimumSize(new Dimension(240, 80));
        exitButton.setPreferredSize(new Dimension(240, 80));
        exitButton.setMaximumSize(new Dimension(240, 80));

        startButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                GameCanvas.closeMenu();
                startButton.setText("Resume");
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
        this.add(Box.createRigidArea(new Dimension(0, 50)));

        JLabel picLabel = new JLabel(new ImageIcon(UnitPainter.ICE_TOWER));
        picLabel.setAlignmentX(CENTER_ALIGNMENT);
        add(picLabel);

        this.add(Box.createRigidArea(new Dimension(0, 50)));
        this.add(startButton);
        this.add(Box.createRigidArea(new Dimension(0, 10)));
        this.add(exitButton);
    }
}
