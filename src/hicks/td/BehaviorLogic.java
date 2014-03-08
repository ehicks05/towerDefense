package hicks.td;

import hicks.td.entities.*;
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
        for (Unit unit : new ArrayList<>(World.getUnits()))
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

            if (unit instanceof Explosion)
            {
                Explosion explosion = (Explosion) unit;
                if (explosion.getFrame() > 73)
                    World.removeUnit(unit);
            }
        }
    }

    private static void performSpawningPhase()
    {
        Spawner spawner = World.getSpawner();
        Player player   = World.getPlayer();
        Round round     = World.getRound(player.getRoundNumber());

        // spawn units
        if (GameCanvas.isActiveRound() && spawner.isReadyToBuild() && player.getRoundNumber() < 7)
        {
            Mob mob = new Footman(2);
            mob.setLocation(new Point(32, 32));
            mob.setPath(mob.createPath());
            mob.setMobBodyPartCollection(round.getMobBodyPartCollection());

            if (player.getRoundNumber() > 1)
            {
                mob.setCurrentHp(mob.getCurrentHp() + 10 * player.getRoundNumber());
                mob.setMaxHp(mob.getMaxHp() + 10 * player.getRoundNumber());

                mob.setArmor(mob.getArmor() + 2 * player.getRoundNumber());

                mob.setBounty(mob.getBounty() + 1);
            }

            World.addUnit(mob);
            spawner.setTimeOfLastBuild(Util.now());
            spawner.incrementUnitsCreated();

            if (spawner.getUnitsCreated() == 20)
            {
                GameCanvas.setActiveRound(false);
                GameCanvas.getGamePanel().showNextRoundButton();
            }
        }
    }
}
