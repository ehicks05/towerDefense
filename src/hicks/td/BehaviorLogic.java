package hicks.td;

import hicks.td.entities.Player;
import hicks.td.entities.Point;
import hicks.td.entities.Spawner;
import hicks.td.entities.Unit;
import hicks.td.entities.mob.Footman;
import hicks.td.entities.mob.Mob;
import hicks.td.entities.projectile.Projectile;
import hicks.td.entities.projectile.ProjectileLogic;
import hicks.td.entities.tower.Tower;
import hicks.td.util.Util;

import java.util.ArrayList;

public final class BehaviorLogic
{
    public static void updateState()
    {
        performSpawningPhase();
        performUpdatePhase();

    }

    private static void performUpdatePhase()
    {
        for (Unit unit : new ArrayList<>(GameState.getUnits()))
        {
            if (unit instanceof Mob)
            {
                Mob mob = (Mob) unit;

                if (!mob.isAlive())
                    continue;

                mob.performMobBehavior();
            }

            if (unit instanceof Tower)
            {
                Tower tower = (Tower) unit;
                tower.performTowerBehavior();
            }
            if (unit instanceof Projectile)
            {
                Projectile projectile = (Projectile) unit;
                ProjectileLogic.performProjectileBehavior(projectile);
            }
        }
    }

    private static void performSpawningPhase()
    {
        Spawner spawner = GameState.getSpawner();
        Player player   = GameState.getPlayer();

        // spawn units
        if (spawner.isReadyToBuild() && player.getRound() < 6)
        {
            Mob mob = new Footman(2);
            mob.setLocation(new Point(32, 32));
            mob.setPath(mob.createPath());

            if (player.getRound() > 1)
            {
                mob.setCurrentHp(mob.getCurrentHp() + 10 * player.getRound());
                mob.setMaxHp(mob.getMaxHp() + 10 * player.getRound());
            }

            GameState.addUnit(mob);
            spawner.setTimeOfLastBuild(Util.now());
            spawner.incrementUnitsCreated();

            if (spawner.getUnitsCreated() % 20 == 0)
            {
                player.setRound(player.getRound() + 1);
            }
        }
    }
}
