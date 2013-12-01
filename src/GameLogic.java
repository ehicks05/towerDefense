import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameLogic
{
    public static void updateState(BigDecimal simulationStart, GameMap map)
    {
        // copy list to try to avoid concurrentModificationExceptions
        List<Unit> unitsToProcess = new ArrayList<>(map.getExistingUnits());

        for (Unit unit : unitsToProcess)
        {
            if (!unit.isAlive()) return;

            if (!(unit instanceof Barracks))
            {
                if (unit.getTarget() == null)
                    performIdleBehavior(unit, simulationStart, map);
                if (unit.getTarget() != null)
                    performHostileBehavior(unit, simulationStart, map);
            }
            if (unit instanceof Barracks)
                performBuildBehavior(unit, simulationStart, map);
        }
    }

    private static void performBuildBehavior(Unit unit, BigDecimal simulationStart, GameMap map)
    {
        Barracks barracks = (Barracks) unit;
        if ((barracks.isReadyToBuild()))
        {
            // create and add to map, a new footman... it should be in an available spot next to the barracks
            Random gen = new Random();
            int typeToBuild = gen.nextInt(4);
            Unit barracksCreation;

            switch (typeToBuild)
            {
                case 0: barracksCreation = new Footman(barracks.getTeam());
                    break;
                case 1: barracksCreation = new Knight(barracks.getTeam());
                    break;
                case 2: barracksCreation = new Archer(barracks.getTeam());
                    break;
                case 3: barracksCreation = new Berserker(barracks.getTeam());
                    break;
                default: barracksCreation = new Footman(barracks.getTeam());
            }

            barracksCreation.setLocation(map.getAvailableAdjacentLocation(barracks.getLocation()));
            map.addUnitToExistingUnits(barracksCreation);
            barracks.setTimeOfLastBuild(GameLogic.getNow());

            Log.logInfo(simulationStart, barracks + " has created a footman " + barracksCreation);

            int unitsOnTeam1 = getUnitsOnTeam(map.getExistingUnits(), 1);
            int unitsOnTeam2 = getUnitsOnTeam(map.getExistingUnits(), 2);
            int winningTeam = unitsOnTeam1 > unitsOnTeam2 ? 1 : 2;

            if (barracks.getTeam() == winningTeam)
                barracks.setBuildSpeed(new BigDecimal(10));
            else
                barracks.setBuildSpeed(new BigDecimal(5));
        }
    }

    private static void performHostileBehavior(Unit unit, BigDecimal simulationStart, GameMap map)
    {
        if (unit.isTargetInRange() && unit.isReadyToAttack())
            CombatLogic.performAttack(unit, map, simulationStart);
        else
            unit.moveTowardCoordinate(unit.getTarget().getLocation(), true);
    }

    private static void performIdleBehavior(Unit unit, BigDecimal simulationStart, GameMap map)
    {
        Unit closestVisibleEnemy = unit.getClosestVisibleEnemy(map.getExistingUnits());

        if (closestVisibleEnemy == null)
        {
            if (unit.getDestination() == null)
                unit.setDestination(map.getRandomPointOnMap());

            unit.moveTowardCoordinate(unit.getDestination(), false);
        }
        else
        {
            unit.setDestination(null);
            unit.setTarget(closestVisibleEnemy);
            if (unit.getTarget() != null) Log.logInfo(simulationStart, unit + " has targeted " + unit.getTarget() + "!");
        }
    }

    public static BigDecimal getElapsedTime(BigDecimal startTime)
    {
        return GameLogic.getNow().subtract(startTime).divide(new BigDecimal("1000000000"));
    }

    public static List<Integer> teamsLeft(List<Unit> units)
    {
        List<Integer> teamsAlive = new ArrayList<>();
        for (Unit unit : units)
            if (unit.isAlive() && !teamsAlive.contains(unit.getTeam()))
                teamsAlive.add(unit.getTeam());

        return teamsAlive;
    }

    public static BigDecimal getNow()
    {
        return new BigDecimal(System.nanoTime());
    }

    public static int getUnitsOnTeam(List<Unit> units, int team)
    {
        int result = 0;
        for (Unit unit : units)
            if (unit.getTeam() == team) result++;

        return result;
    }
}
