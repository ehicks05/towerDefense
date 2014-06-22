package hicks.td.ui;

import hicks.td.GameCanvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyGamePanel extends JPanel
{
    private final JButton startRoundButton;

    public MyGamePanel()
    {
        super();
        this.setName("gamePanel");
        this.setPreferredSize(new Dimension(DisplayInfo.getWindowWidth(), DisplayInfo.getWindowHeight()));

        final JToggleButton arrowButton = new JToggleButton(new ImageIcon(UnitPainter.GUARD_TOWER), true);
        arrowButton.setVisible(true);
        final JToggleButton glaiveButton = new JToggleButton(new ImageIcon(UnitPainter.SCOUT_TOWER));
        glaiveButton.setVisible(true);
        final JToggleButton cannonButton = new JToggleButton(new ImageIcon(UnitPainter.CANNON_TOWER));
        cannonButton.setVisible(true);
        final JToggleButton iceButton = new JToggleButton(new ImageIcon(UnitPainter.ICE_TOWER));
        iceButton.setVisible(true);
        final JToggleButton pauseButton = new JToggleButton("Pause");
        pauseButton.setVisible(true);

        startRoundButton = new JButton("Start Round");
        startRoundButton.setVisible(true);

        arrowButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if (arrowButton.isSelected())
                {
                    GameCanvas.setTowerToggle("Arrow");
                    glaiveButton.setSelected(false);
                    cannonButton.setSelected(false);
                    iceButton.setSelected(false);
                }
                else
                {
                    GameCanvas.setTowerToggle("Glaive");
                }
            }
        });

        glaiveButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if (glaiveButton.isSelected())
                {
                    GameCanvas.setTowerToggle("Glaive");
                    arrowButton.setSelected(false);
                    cannonButton.setSelected(false);
                    iceButton.setSelected(false);
                }
                else
                {
                    GameCanvas.setTowerToggle("Arrow");
                }
            }
        });

        cannonButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if (cannonButton.isSelected())
                {
                    GameCanvas.setTowerToggle("Cannon");
                    arrowButton.setSelected(false);
                    glaiveButton.setSelected(false);
                    iceButton.setSelected(false);
                }
                else
                {
                    GameCanvas.setTowerToggle("Arrow");
                }
            }
        });

        iceButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if (iceButton.isSelected())
                {
                    GameCanvas.setTowerToggle("Ice");
                    arrowButton.setSelected(false);
                    glaiveButton.setSelected(false);
                    cannonButton.setSelected(false);
                }
                else
                {
                    GameCanvas.setTowerToggle("Arrow");
                }
            }
        });

        pauseButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if (pauseButton.isSelected())
                {
                    GameCanvas.pauseGame();
                    pauseButton.setText("Resume");
                }
                else
                {
                    GameCanvas.resumeGame();
                    pauseButton.setText("Pause");
                }
            }
        });

        startRoundButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                GameCanvas.startNextRound();
                startRoundButton.setVisible(false);
            }
        });

        this.setLayout(new FlowLayout());
        this.add(arrowButton);
        this.add(glaiveButton);
        this.add(cannonButton);
        this.add(iceButton);
        this.add(pauseButton);
        this.add(startRoundButton);
    }

    public void showNextRoundButton()
    {
        startRoundButton.setVisible(true);
    }
}
