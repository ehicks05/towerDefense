package hicks.td.ui;

import hicks.td.GameCanvas;
import hicks.td.GameState;
import hicks.td.entities.*;
import hicks.td.entities.tower.ArrowTower;
import hicks.td.entities.tower.CannonTower;
import hicks.td.entities.tower.GlaiveTower;
import hicks.td.entities.tower.Tower;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MyMouseListener extends MouseAdapter
{
    public void mouseClicked(MouseEvent e)
    {
        super.mouseClicked(e);
        int eventX = e.getX();
        int eventY = e.getY();

        GameCanvas.setSelectedUnit(null);
        if (e.getButton() == 1)
        {
            for (Unit unit : GameState.getUnits())
            {
                int unitX = (int) unit.getLocation().getX();
                int unitY = (int) unit.getLocation().getY();
                int unitSize = unit.getSizeRadius();

                int minX = unitX - unitSize;
                int maxX = unitX + unitSize;
                int minY = unitY - unitSize;
                int maxY = unitY + unitSize;

                if (eventX >= minX && eventX <= maxX && eventY >= minY && eventY <= maxY)
                    GameCanvas.setSelectedUnit(unit);
            }
        }
        if (e.getButton() == 3)
        {
            // snap to grid
            eventX = InterfaceUtil.snapToGrid(eventX);
            eventY = InterfaceUtil.snapToGrid(eventY);

            String towerToggle = GameCanvas.getTowerToggle();

            Tower tower = null;
            if (towerToggle.equals("Arrow"))
                tower = new ArrowTower(1);
            if (towerToggle.equals("Glaive"))
                tower = new GlaiveTower(1);
            if (towerToggle.equals("Cannon"))
                tower = new CannonTower(1);
            if (tower == null) tower = new ArrowTower(1);

            int goldCost = tower.getPrice();

            boolean canAffordGoldCost = GameState.getPlayer().getGold() >= goldCost;
            boolean validLocation = InterfaceUtil.isValidLocation(eventX, eventY, new ArrowTower(2).getSizeRadius());

            if (canAffordGoldCost && validLocation)
            {
                tower.setLocation(new Point(eventX, eventY));
                GameState.addUnit(tower);
                GameState.getPlayer().removeGold(tower.getPrice());
            }
        }
    }
}
