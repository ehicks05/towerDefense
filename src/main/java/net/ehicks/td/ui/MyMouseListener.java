package net.ehicks.td.ui;

import net.ehicks.td.World;
import net.ehicks.td.entities.Animation;
import net.ehicks.td.entities.Point;
import net.ehicks.td.entities.Tower;
import net.ehicks.td.entities.Unit;
import net.ehicks.td.entities.Projectile;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MyMouseListener extends MouseAdapter
{
    public void mouseReleased(MouseEvent e)
    {
        super.mouseReleased(e);
        int eventX = (int) (e.getX() * (1 / DisplayInfo.getScalingFactor()));
        int eventY = (int) (e.getY() * (1 / DisplayInfo.getScalingFactor()));

        InterfaceLogic.setSelectedUnit(null);
        if (e.getButton() == 1) performLeftClick(eventX, eventY);
        if (e.getButton() == 3) performRightClick(eventX, eventY);
    }

    private void performRightClick(int eventX, int eventY)
    {
        // snap to grid
        eventX = InterfaceUtil.snapToMiddleOfTile(eventX);
        eventY = InterfaceUtil.snapToMiddleOfTile(eventY);

        String towerToggle = InterfaceLogic.getTowerToggle();

        Tower tower = World.getTowerByName(towerToggle);
        if (tower == null) tower = World.getTowerByName("ArrowTower");

        boolean isAffordable = World.getPlayer().getGold() >= tower.getPrice();
        boolean isValidLocation = InterfaceUtil.isValidLocation(eventX, eventY, tower.getSizeRadius());

        if (isAffordable && isValidLocation)
        {
            tower.setLocation(new Point(eventX, eventY));
            World.addUnit(tower);
            World.getPlayer().removeGold(tower.getPrice());
        }
    }

    private void performLeftClick(int eventX, int eventY)
    {
        for (Unit unit : World.getUnits())
        {
            if (unit instanceof Projectile || unit instanceof Animation) continue;

            int unitX = (int) unit.getLocation().getX();
            int unitY = (int) unit.getLocation().getY();
            int unitSize = unit.getSizeRadius();

            int minX = unitX - unitSize;
            int maxX = unitX + unitSize;
            int minY = unitY - unitSize;
            int maxY = unitY + unitSize;

            if (eventX >= minX && eventX <= maxX && eventY >= minY && eventY <= maxY)
                InterfaceLogic.setSelectedUnit(unit);
        }
    }
}
