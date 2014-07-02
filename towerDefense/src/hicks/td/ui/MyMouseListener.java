package hicks.td.ui;

import hicks.td.GameCanvas;
import hicks.td.World;
import hicks.td.entities.Point;
import hicks.td.entities.Unit;
import hicks.td.entities.tower.*;

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
            for (Unit unit : World.getUnits())
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
            eventX = InterfaceUtil.snapToMiddleOfTile(eventX);
            eventY = InterfaceUtil.snapToMiddleOfTile(eventY);

            String towerToggle = GameCanvas.getTowerToggle();

            Tower tower = World.getTowerByName(towerToggle);
            if (tower == null) tower = World.getTowerByName("ArrowTower");

            int goldCost = tower.getPrice();

            boolean canAffordGoldCost = World.getPlayer().getGold() >= goldCost;
            boolean validLocation = InterfaceUtil.isValidLocation(eventX, eventY, tower.getSizeRadius());

            if (canAffordGoldCost && validLocation)
            {
                tower.setLocation(new Point(eventX, eventY));
                World.addUnit(tower);
                World.getPlayer().removeGold(tower.getPrice());
            }
        }
    }
}
