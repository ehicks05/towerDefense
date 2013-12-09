package hicks.combat.state;

import hicks.combat.CombatPanel;
import hicks.combat.GameLogic;
import hicks.combat.entities.Builder;
import hicks.combat.entities.Peasant;
import hicks.combat.entities.Unit;
import hicks.combat.entities.UnitLogic;

public class Idle implements State
{
    public void enter(Unit unit)
    {
    }

    public void execute(Unit unit)
    {
        if (!(unit instanceof Builder))
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
                unit.changeState(new Hostile());
            }
        }
        if (unit instanceof Peasant)
        {
            if (GameLogic.isClearOfBarracks(unit, Unit.getMap()))
            {
                unit.changeState(new Build());
            }
            else
            {
                if (unit.getDestination() == null)
                    unit.setDestination(Unit.getMap().getRandomPointOnMap());

                UnitLogic.moveTowardCoordinate(unit, unit.getDestination(), false);
            }
        }
    }

    public void exit(Unit unit)
    {
        Unit closestVisibleEnemy = UnitLogic.getClosestVisibleEnemy(unit, Unit.getMap().getExistingUnits());

        unit.setDestination(null);
        unit.setTarget(closestVisibleEnemy);
        if (unit.getTarget() != null)
            hicks.combat.Log.logInfo(CombatPanel.getSimulationStart(), unit + " has targeted " + unit.getTarget() + "!");
    }
}
