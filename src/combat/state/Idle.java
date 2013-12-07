package combat.state;

import combat.Log;
import combat.Unit;

public class Idle extends State
{
    public void enter(Unit unit)
    {
    }

    public void execute(Unit unit)
    {
//        Unit closestVisibleEnemy = unit.getClosestVisibleEnemy(map.getExistingUnits());
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
//            if (unit.getTarget() != null) Log.logInfo(simulationStart, unit + " has targeted " + unit.getTarget() + "!");
//        }
    }

    public void exit(Unit unit)
    {
    }
}
