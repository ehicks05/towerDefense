package hicks.td.ui;

import hicks.td.GameCanvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyGamePanel extends JPanel
{
    public MyGamePanel()
    {
        this.setName("gamePanel");
        this.setPreferredSize(new Dimension(DisplayInfo.getWindowWidth(), DisplayInfo.getWindowHeight()));

        final JToggleButton arrowButton = new JToggleButton(new ImageIcon(UnitPainter.GUARD_TOWER), true);
        arrowButton.setVisible(true);
        final JToggleButton glaiveButton = new JToggleButton(new ImageIcon(UnitPainter.CANNON_TOWER));
        glaiveButton.setVisible(true);
        final JToggleButton pauseButton = new JToggleButton("Pause");
        pauseButton.setVisible(true);

        arrowButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if (arrowButton.isSelected())
                {
                    GameCanvas.setTowerToggle("Arrow");
                    glaiveButton.setSelected(false);
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

        this.setLayout(new FlowLayout());
        this.add(arrowButton);
        this.add(glaiveButton);
    }
}
