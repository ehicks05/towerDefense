package hicks.td.ui;

import hicks.td.GameCanvas;
import hicks.td.World;
import hicks.td.entities.Upgrade;
import hicks.td.entities.Tower;

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
        final List<JToggleButton> towerButtons = new ArrayList<>();

        for (Tower tower : World.getTowers())
        {
            final JToggleButton towerButton = new JToggleButton(new ImageIcon(tower.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH)), false);
            towerButton.setName(tower.getName());
            towerButton.setVisible(true);
            towerButton.setToolTipText("Price: " + tower.getPrice() + " gold");

            InterfaceUtil.setSizeFields(towerButton, towerButtonDimension);
            towerButtons.add(towerButton);
            this.add(towerButton);
        }

        towerButtons.get(0).setSelected(true);

        for (final JToggleButton towerButton : towerButtons)
        {
            towerButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    if (towerButton.isSelected())
                    {
                        GameCanvas.setTowerToggle(towerButton.getName());

                        for (final JToggleButton innerTowerButton : towerButtons)
                        {
                            if (innerTowerButton.equals(towerButton)) continue;
                            innerTowerButton.setSelected(false);
                        }
                    }
                    else
                    {
                        GameCanvas.setTowerToggle("");
                    }
                }
            });
        }

        final JToggleButton pauseButton = new JToggleButton("Pause");
        pauseButton.setVisible(true);
        final JButton mainMenuButton = new JButton("Menu");
        mainMenuButton.setVisible(true);

        unitInfo = new JPanel();
        unitInfo.setVisible(true);
        InterfaceUtil.setSizeFields(unitInfo, new Dimension(300, 80));

        JLabel unitInfoLabel = new JLabel();
        unitInfoLabel.setVisible(true);
        unitInfo.add(unitInfoLabel);

        startWaveButton = new JButton("Start Wave");
        startWaveButton.setVisible(true);

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

        this.add(mainMenuButton);
        this.add(pauseButton);
        this.add(startWaveButton);
        this.add(unitInfo);

//        JPanel menuPanel = new JPanel();
//        menuPanel.setVisible(true);
//        this.add(menuPanel);
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
