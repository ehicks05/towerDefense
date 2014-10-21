package hicks.td.ui;

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

        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER, 0, 0);
        flowLayout.setAlignment(FlowLayout.LEFT);
        this.setLayout(flowLayout);

        InterfaceLogic.infoLabel = new JLabel();
        InterfaceLogic.infoLabel.setVisible(true);
        InterfaceUtil.setSizeFields(InterfaceLogic.infoLabel, new Dimension(DisplayInfo.getWindowWidth(), 10));
        this.add(InterfaceLogic.infoLabel);

        final Dimension towerButtonDimension = new Dimension(48, 48);
        final List<JToggleButton> towerButtons = new ArrayList<>();

        JPanel towerButtonPanel = new JPanel();
        for (Tower tower : World.getTowerTypes())
        {
            final JToggleButton towerButton = new JToggleButton(new ImageIcon(tower.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH)), false);
            towerButton.setName(tower.getName());
            towerButton.setVisible(true);
            towerButton.setToolTipText(tower.getName() + ": " + tower.getPrice() + " gold");

            InterfaceUtil.setSizeFields(towerButton, towerButtonDimension);
            towerButtons.add(towerButton);
            towerButtonPanel.add(towerButton);
        }

        this.add(towerButtonPanel);

        towerButtons.get(0).setSelected(true);

        for (final JToggleButton towerButton : towerButtons)
        {
            towerButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    if (towerButton.isSelected())
                    {
                        InterfaceLogic.setTowerToggle(towerButton.getName());

                        for (final JToggleButton innerTowerButton : towerButtons)
                        {
                            if (innerTowerButton.equals(towerButton)) continue;
                            innerTowerButton.setSelected(false);
                        }
                    }
                    else
                    {
                        towerButton.setSelected(true);
                    }
                }
            });
        }

        unitInfo = new JPanel();
//        unitInfo.setVisible(true);
//        InterfaceUtil.setSizeFields(unitInfo, new Dimension(200, 80));

        JLabel unitInfoLabel = new JLabel();
        unitInfoLabel.setVisible(true);
        unitInfo.add(unitInfoLabel);

        startWaveButton = new JButton("Start");
        startWaveButton.setVisible(true);
        startWaveButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                InterfaceLogic.startNextWave();
                startWaveButton.setVisible(false);
            }
        });

        List<Upgrade> upgrades = World.getUpgradeTypes();
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
                        Tower tower = (Tower) InterfaceLogic.getSelectedUnit();
                        tower.addUpgrade(upgrade);
                        World.getPlayer().removeGold(upgrade.getCost());
                        button.setVisible(false);
                    }
                }
            });

            unitInfo.add(button);
        }

        final JButton sellTowerButton = new JButton("Sell");
        sellTowerButton.setName("sell");
        sellTowerButton.setVisible(false);

        sellTowerButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                Tower tower = (Tower) InterfaceLogic.getSelectedUnit();
                int sellPrice = tower.getPrice() / 2;
                World.removeUnit(tower);
                World.getPlayer().addGold(sellPrice);
                sellTowerButton.setVisible(false);
                InterfaceLogic.setSelectedUnit(null);
            }
        });

        unitInfo.add(sellTowerButton);

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
