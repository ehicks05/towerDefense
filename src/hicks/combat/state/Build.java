package hicks.combat.state;

import hicks.combat.GameLogic;
import hicks.combat.GameState;
import hicks.combat.Log;
import hicks.combat.entities.*;

import java.util.Random;

public class Build implements State
{
    public void enter(Unit unit)
    {

    }

    public void execute(Unit unit)
    {
        if (unit instanceof Barracks)
        {
            Barracks barracks = (Barracks) unit;
            if (barracks.isReadyToBuild())
            {
                Random gen = new Random();
                int typeToBuild = gen.nextInt(3);
                Unit newUnit = null;

                if (typeToBuild == 0) newUnit = new Knight(barracks.getTeam());
                if (typeToBuild == 1) newUnit = new Peasant(barracks.getTeam());
                if (typeToBuild == 2) newUnit = new Footman(barracks.getTeam());

                if (newUnit == null) newUnit = new Footman(barracks.getTeam());

                newUnit.setLocation(GameState.getGameMap().getAvailableAdjacentLocation(barracks.getLocation()));
                newUnit.changeState(new Idle());
                GameState.addUnit(newUnit);
                barracks.setTimeOfLastBuild(GameLogic.now());

                Log.logInfo(barracks + " has created a footman " + newUnit);
            }
        }

        if (unit instanceof Peasant)
        {
            Peasant peasant = (Peasant) unit;

            Unit newUnit = new Barracks(peasant.getTeam());
            newUnit.setLocation(GameState.getGameMap().getAvailableAdjacentLocation(peasant.getLocation()));

            GameState.addUnit(newUnit);
            GameState.removeUnit(peasant);

            Log.logInfo(peasant + " has created a hicks.combat.entities.Barracks " + newUnit);
        }
    }

    public void exit(Unit unit)
    {

    }
}
