package hicks.combat.state;

import hicks.combat.CombatPanel;
import hicks.combat.entities.Unit;
import hicks.combat.entities.UnitLogic;

public class Idle extends State
{
    public void enter(Unit unit)
    {
    }

    public void execute(Unit unit)
    {
        Unit closestVisibleEnemy = UnitLogic.getClosestVisibleEnemy(unit, Unit.getMap().getExistingUnits());

        if (closestVisibleEnemy == null)
        {
            if (unit.getDestination() == null)
                unit.setDestination(Unit.getMap().getRandomPointOnMap());

            UnitLogic.moveTowardCoordinate(unit, unit.getDestination(), false);
        }
        else
        {
            unit.setDestination(null);
            unit.setTarget(closestVisibleEnemy);
            if (unit.getTarget() != null) hicks.combat.Log.logInfo(CombatPanel.getSimulationStart(), unit + " has targeted " + unit.getTarget() + "!");
        }
    }

    public void exit(Unit unit)
    {
    }
}
