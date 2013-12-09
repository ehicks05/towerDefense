package hicks.combat.state;

import hicks.combat.CombatPanel;
import hicks.combat.GameLogic;
import hicks.combat.GameMap;
import hicks.combat.Log;
import hicks.combat.entities.*;

import java.math.BigDecimal;
import java.util.Random;

public class Build implements State
{
    public void enter(Unit unit)
    {

    }

    public void execute(Unit unit)
    {
        GameMap map = Unit.getMap();
        BigDecimal simulationStart = CombatPanel.getSimulationStart();

        if (unit instanceof Barracks)
        {
            Barracks barracks = (Barracks) unit;
            if (barracks.isReadyToBuild())
            {
                Random gen = new Random();
                int typeToBuild = gen.nextInt(3);
                Unit barracksCreation;

                switch (typeToBuild)
                {
                    case 0: barracksCreation = new Knight(barracks.getTeam());
                        break;
                    case 1: barracksCreation = new Peasant(barracks.getTeam());
                        break;
                    default: barracksCreation = new Footman(barracks.getTeam());
                }

                barracksCreation.setLocation(map.getAvailableAdjacentLocation(barracks.getLocation()));
                barracksCreation.changeState(new Idle());
                map.addUnitToExistingUnits(barracksCreation);
                barracks.setTimeOfLastBuild(GameLogic.now());

                Log.logInfo(simulationStart, barracks + " has created a footman " + barracksCreation);
            }
        }

        if (unit instanceof Peasant)
        {
            Peasant peasant = (Peasant) unit;

            Unit unitCreated = new Barracks(peasant.getTeam());

            unitCreated.setLocation(map.getAvailableAdjacentLocation(peasant.getLocation()));
            map.addUnitToExistingUnits(unitCreated);
            peasant.setTimeOfLastBuild(GameLogic.now());
            map.getExistingUnits().remove(peasant);

            Log.logInfo(simulationStart, peasant + " has created a hicks.combat.entities.Barracks " + unitCreated);
        }
    }

    public void exit(Unit unit)
    {

    }
}
