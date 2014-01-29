package hicks.td;

import hicks.td.entities.*;
import hicks.td.entities.mob.Footman;
import hicks.td.entities.mob.Mob;
import hicks.td.entities.projectile.Projectile;
import hicks.td.entities.projectile.ProjectileLogic;
import hicks.td.entities.tower.Tower;
import hicks.td.util.TileLoader;
import hicks.td.util.Util;

import java.util.ArrayList;

public final class BehaviorLogic
{
    public static void updateState()
    {
        performSpawningPhase();

        for (Unit unit : new ArrayList<>(GameState.getUnits()))
        {
            if (unit instanceof Mob)
            {
                Mob mob = (Mob) unit;

                if (!mob.isAlive())
                    continue;

                mob.performMobBehavior();
            }

            if (unit instanceof Tower) chooseTowerBehavior((Tower) unit);
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
            mob.setLocation(new Point(TileLoader.roadOffset - 16, 0));
            mob.setPath(spawner.createPath());

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

    private static void chooseTowerBehavior(Tower tower)
    {
        if (tower.getTarget() == null) tower.lookForTarget();
        if (tower.getTarget() != null) tower.performHostileBehavior();
    }
}
