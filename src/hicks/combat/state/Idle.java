package hicks.combat.state;

import hicks.combat.GameLogic;
import hicks.combat.GameState;
import hicks.combat.UnitLogic;
import hicks.combat.entities.Barracks;
import hicks.combat.entities.Builder;
import hicks.combat.entities.Peasant;
import hicks.combat.entities.Unit;

public class Idle implements State
{
    public void enter(Unit unit)
    {
    }

    public void execute(Unit unit)
    {
        if (!(unit instanceof Builder))
        {
            Unit closestVisibleEnemy = UnitLogic.getClosestVisibleEnemy(unit, GameState.getUnits());

            if (closestVisibleEnemy == null)
            {
                if (unit.getDestination() == null)
                    unit.setDestination(GameState.getGameMap().getRandomPointOnMap());

                UnitLogic.moveTowardCoordinate(unit, unit.getDestination(), false);
            }
            else
            {
                unit.changeState(new Hostile());
            }
        }
        if (unit instanceof Peasant)
        {
            if (GameLogic.isClearOfBarracks(unit))
            {
                unit.changeState(new Build());
            }
            else
            {
                if (unit.getDestination() == null)
                    unit.setDestination(GameState.getGameMap().getRandomPointOnMap());

                UnitLogic.moveTowardCoordinate(unit, unit.getDestination(), false);
            }
        }
        if (unit instanceof Barracks)
        {
            unit.changeState(new Build());  // todo: this is debugging code, we should never reach this block...
        }
    }

    public void exit(Unit unit)
    {
        Unit closestVisibleEnemy = UnitLogic.getClosestVisibleEnemy(unit, GameState.getUnits());

        unit.setDestination(null);
        unit.setTarget(closestVisibleEnemy);
        if (unit.getTarget() != null)
            hicks.combat.Log.logInfo(unit + " has targeted " + unit.getTarget() + "!");
    }
}
