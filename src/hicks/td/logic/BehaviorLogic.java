package hicks.td.logic;

import hicks.td.World;
import hicks.td.audio.SoundManager;
import hicks.td.entities.*;
import hicks.td.ui.InterfaceLogic;
import hicks.td.util.Util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class BehaviorLogic
{
    public static void updateState(BigDecimal dt)
    {
        SoundManager.clearEffectsTriggeredThisTick();
        goldTick();
        performSpawningPhase();
        performUpdatePhase(dt);
    }

    private static void goldTick()
    {
        if (InterfaceLogic.gameStarted && InterfaceLogic.runningSimulation)
        {
            if (Util.getElapsedTime(World.getPlayer().getTimeOfLastGoldTick(), Util.now()).compareTo(new BigDecimal("1")) > 0)
            {
                World.getPlayer().setGold(World.getPlayer().getGold() + 1);
                World.getPlayer().setTimeOfLastGoldTick(Util.now());
            }
        }
    }

    private static void performUpdatePhase(BigDecimal dt)
    {
        for (final Unit unit : new ArrayList<>(World.getUnits()))
        {
            if (unit instanceof Mob)
            {
                final Mob mob = (Mob) unit;

                if (!mob.isAlive())
                    continue;

                mob.performMobBehavior(dt);
            }

            if (unit instanceof Tower)
            {
                final Tower tower = (Tower) unit;
                tower.performTowerBehavior();
            }
            if (unit instanceof Projectile)
            {
                final Projectile projectile = (Projectile) unit;
                ProjectileLogic.performProjectileBehavior(projectile, dt);
            }

            if (unit instanceof Animation)
            {
                final Animation animation = (Animation) unit;
                animation.setTimeSinceLastFrame(animation.getTimeSinceLastFrame().add(dt));
                while (animation.getTimeSinceLastFrame().compareTo(animation.getSecondsPerFrame()) > 0)
                {
                    animation.setFrame(animation.getFrame() + 1);
                    if (animation.getFrame() > animation.getTotalFrames())
                        World.removeUnit(unit);
                    animation.setTimeSinceLastFrame(animation.getTimeSinceLastFrame().subtract(animation.getSecondsPerFrame()));
                }
            }
        }
    }

    private static void performSpawningPhase()
    {
        if (InterfaceLogic.isActiveRound() && InterfaceLogic.runningSimulation)
        {
            final Player player                   = World.getPlayer();
            final Wave wave                       = World.getWave(player.getWaveNumber());
            if (wave == null) return;

            final BigDecimal timeSinceStartOfWave = Util.getElapsedTime(wave.getTimeStarted(), Util.now());

            final List<Integer> mobTypeIndexesToCreate = new ArrayList<>();
            final List<WaveSpawn> waveSpawns = wave.getWaveSpawns();
            for (Iterator<WaveSpawn> i = waveSpawns.iterator(); i.hasNext();)
            {
                final WaveSpawn waveSpawn = i.next();
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
                for (final Integer mobTypeIndexToCreate : mobTypeIndexesToCreate)
                {
                    final Mob mob = Mob.duplicateMob(World.getMobTypes().get(mobTypeIndexToCreate));

                    if (wave.getWaveNumber() > 5)
                    {
                        final double healthMultiplier = 1 + (4 * (wave.getWaveNumber() - 5) / (double) 100);
                        final int newHp = (int) (mob.getCurrentHp() * healthMultiplier);
                        mob.setCurrentHp(newHp);
                        mob.setMaxHp(newHp);
                    }

                    final PathPoint origin = World.getGameMap().getMobPath().get(0);
                    mob.setLocation(new Point(origin.getCol() * 32 + 16, origin.getRow() * 32));

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
