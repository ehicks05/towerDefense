package hicks.combat.state;

import hicks.combat.entities.Unit;

public class Idle extends State
{
    public void enter(Unit unit)
    {
    }

    public void execute(Unit unit)
    {
//        hicks.combat.entities.Unit closestVisibleEnemy = unit.getClosestVisibleEnemy(map.getExistingUnits());
//
//        if (closestVisibleEnemy == null)
//        {
//            if (unit.getDestination() == null)
//                unit.setDestination(map.getRandomPointOnMap());
//
//            unit.moveTowardCoordinate(unit.getDestination(), false);
//        }
//        else
//        {
//            unit.setDestination(null);
//            unit.setTarget(closestVisibleEnemy);
//            if (unit.getTarget() != null) hicks.combat.Log.logInfo(simulationStart, unit + " has targeted " + unit.getTarget() + "!");
//        }
    }

    public void exit(Unit unit)
    {
    }
}
