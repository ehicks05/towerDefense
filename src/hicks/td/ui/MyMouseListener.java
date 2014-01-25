package hicks.td.ui;

import hicks.td.GameCanvas;
import hicks.td.GameState;
import hicks.td.entities.ArrowTower;
import hicks.td.entities.Point;
import hicks.td.entities.Unit;

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
            boolean canAffordGoldCost = GameState.getPlayer().getGold() >= 50;
            boolean validLocation = InterfaceUtil.isValidLocation(eventX, eventY, new ArrowTower(2).getSizeRadius());

            if (canAffordGoldCost && validLocation)
            {
                Unit arrowTower = new ArrowTower(1);
                arrowTower.setLocation(new Point(eventX, eventY));
                GameState.addUnit(arrowTower);
                GameState.getPlayer().removeGold(50);
            }
        }
    }
}
