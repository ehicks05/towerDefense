package hicks.td;

import hicks.td.entities.*;
import hicks.td.entities.Mob;
import hicks.td.entities.projectile.Projectile;
import hicks.td.entities.projectile.ProjectileLogic;
import hicks.td.entities.Tower;
import hicks.td.ui.InterfaceLogic;
import hicks.td.util.Util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
        if (InterfaceLogic.isActiveRound())
        {
            Player player                   = World.getPlayer();
            Wave wave                       = World.getWave(player.getWaveNumber());
            if (wave == null) return;

            BigDecimal timeSinceStartOfWave = Util.getElapsedTime(wave.getTimeStarted(), Util.now());

            List<Integer> mobTypeIndexesToCreate = new ArrayList<>();
            List<WaveSpawn> waveSpawns = wave.getWaveSpawns();
            for (Iterator<WaveSpawn> i = waveSpawns.iterator(); i.hasNext();)
            {
                WaveSpawn waveSpawn = i.next();
                if (waveSpawn.getSpawnTime().compareTo(timeSinceStartOfWave) == -1)
                {
                    mobTypeIndexesToCreate.add(waveSpawn.getMobTypeIndex());
                    i.remove();
                }
            }
            wave.setWaveSpawns(waveSpawns);

            // spawn units
            if (mobTypeIndexesToCreate.size() > 0 && player.getWaveNumber() < World.getWaves().size())
            {
                List<Mob> mobTypes = Init.getOneOfEachMobType();
                for (Integer mobTypeIndexToCreate : mobTypeIndexesToCreate)
                {
                    Mob mob = Mob.duplicateMob(mobTypes.get(mobTypeIndexToCreate));
                    mob.setLocation(new Point(32, 32));
                    mob.setPath(mob.createPath());

                    World.addUnit(mob);
                }
            }

            if (waveSpawns.size() == 0)
            {
                InterfaceLogic.setActiveRound(false);
//                GameCanvas.getGamePanel().showNextWaveButton();
            }
        }
        else
        {
            if (InterfaceLogic.isGameStarted()) InterfaceLogic.startNextWave();
        }
    }
}
