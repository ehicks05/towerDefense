package hicks.combat.state;

import hicks.combat.CombatLogic;
import hicks.combat.CombatPanel;
import hicks.combat.entities.Unit;
import hicks.combat.entities.UnitLogic;

public class Hostile implements State
{
    public void enter(Unit unit)
    {
    }

    public void execute(Unit unit)
    {
        if (!unit.getTarget().isAlive())
        {
            unit.changeState(new Idle());
            return;
        }

        if (UnitLogic.isTargetInRange(unit) && unit.isReadyToAttack())
            CombatLogic.performAttack(unit, Unit.getMap(), CombatPanel.getSimulationStart());
        else
            UnitLogic.moveTowardCoordinate(unit, unit.getTarget().getLocation(), true);
    }

    public void exit(Unit unit)
    {

    }
}
