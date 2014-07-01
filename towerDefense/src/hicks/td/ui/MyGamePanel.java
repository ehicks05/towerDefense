package hicks.td.ui;

import hicks.td.GameCanvas;
import hicks.td.World;
import hicks.td.entities.Upgrade;
import hicks.td.entities.tower.Tower;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class MyGamePanel extends JPanel
{
    private final JButton startWaveButton;
    private final JPanel unitInfo;

    public MyGamePanel()
    {
        super();
        this.setName("gamePanel");
        this.setPreferredSize(new Dimension(DisplayInfo.getWindowWidth(), DisplayInfo.getWindowHeight()));

        final Dimension towerButtonDimension = new Dimension(48, 48);
        List<JToggleButton> towerButtons = new ArrayList<>();

        final JToggleButton arrowButton = new JToggleButton(new ImageIcon(UnitPainter.GUARD_TOWER.getScaledInstance(48, 48, Image.SCALE_SMOOTH)), true);
        arrowButton.setVisible(true);
        InterfaceUtil.setSizeFields(arrowButton, towerButtonDimension);
        towerButtons.add(arrowButton);

        final JToggleButton glaiveButton = new JToggleButton(new ImageIcon(UnitPainter.SCOUT_TOWER.getScaledInstance(48, 48, Image.SCALE_SMOOTH)));
        glaiveButton.setVisible(true);
        InterfaceUtil.setSizeFields(glaiveButton, towerButtonDimension);
        towerButtons.add(glaiveButton);

        final JToggleButton cannonButton = new JToggleButton(new ImageIcon(UnitPainter.CANNON_TOWER.getScaledInstance(48, 48, Image.SCALE_SMOOTH)));
        cannonButton.setVisible(true);
        InterfaceUtil.setSizeFields(cannonButton, towerButtonDimension);
        towerButtons.add(cannonButton);

        final JToggleButton iceButton = new JToggleButton(new ImageIcon(UnitPainter.ICE_TOWER.getScaledInstance(48, 48, Image.SCALE_SMOOTH)));
        iceButton.setVisible(true);
        InterfaceUtil.setSizeFields(iceButton, towerButtonDimension);
        towerButtons.add(iceButton);

        final JToggleButton pauseButton = new JToggleButton("Pause");
        pauseButton.setVisible(true);
        final JButton mainMenuButton = new JButton("Menu");
        mainMenuButton.setVisible(true);

        unitInfo = new JPanel();
        unitInfo.setVisible(true);
        InterfaceUtil.setSizeFields(unitInfo, new Dimension(300, 80));

        JLabel unitInfoLabel = new JLabel("test");
        unitInfoLabel.setVisible(true);
        unitInfo.add(unitInfoLabel);

        startWaveButton = new JButton("Start Wave");
        startWaveButton.setVisible(true);

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

        mainMenuButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                GameCanvas.displayMenu();
            }
        });

        startWaveButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                GameCanvas.startNextWave();
                startWaveButton.setVisible(false);
            }
        });

        List<Upgrade> upgrades = World.getUpgrades();
        for (final Upgrade upgrade : upgrades)
        {
            final JButton button = new JButton("Upgrade " + upgrade.getDescription());
            button.setName(upgrade.getCode());
            button.setVisible(false);

            button.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    boolean canAffordGoldCost = World.getPlayer().getGold() >= upgrade.getCost();
                    if (canAffordGoldCost)
                    {
                        Tower tower = (Tower) GameCanvas.getSelectedUnit();
                        tower.addUpgrade(upgrade);
                        World.getPlayer().removeGold(upgrade.getCost());
                        button.setVisible(false);
                    }
                }
            });

            unitInfo.add(button);
        }

        this.setLayout(new FlowLayout());
        this.add(arrowButton);
        this.add(glaiveButton);
        this.add(cannonButton);
        this.add(iceButton);
        this.add(mainMenuButton);
        this.add(pauseButton);
        this.add(startWaveButton);
        this.add(unitInfo);
    }

    public void showNextWaveButton()
    {
        startWaveButton.setVisible(true);
    }

    public JPanel getUnitInfoPanel()
    {
        return unitInfo;
    }
}
