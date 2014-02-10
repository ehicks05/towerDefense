package hicks.td;

import hicks.td.audio.SoundEffect;
import hicks.td.audio.SoundManager;
import hicks.td.entities.mob.Mob;
import hicks.td.entities.projectile.Projectile;
import hicks.td.entities.tower.Tower;
import hicks.td.entities.Unit;

import java.math.BigDecimal;
import java.util.ArrayList;

public final class CombatLogic
{
    public static void performAttack(Projectile damageSource, Mob damageRecipient)
    {
        int rawDamage           = damageSource.getAttackDamage();
        int unmitigatedDamage   = getUnmitigatedDamage(rawDamage, damageRecipient.getArmor());

        damageRecipient.setCurrentHp(damageRecipient.getCurrentHp() - unmitigatedDamage);

        if (!damageRecipient.isAlive())
        {
            processDeath(damageRecipient);
            GameState.getPlayer().addGold(10);
            damageSource.getOriginator().setKills(damageSource.getOriginator().getKills() + 1);
        }
    }

    private static void processDeath(Mob defender)
    {
        for (Unit unit : new ArrayList<>(GameState.getUnits()))
        {
            if (unit instanceof Tower)
            {
                Tower tower = (Tower) unit;
                if (tower.getTargets() != null && tower.getTargets().equals(defender))
                    tower.setTargets(null);
            }
        }

        SoundManager.playSFX(SoundEffect.DEATH);
        GameState.removeUnit(defender);
    }

    private static int getUnmitigatedDamage(int damage, int armor)
    {
        BigDecimal rawDamage            = new BigDecimal(damage);
        BigDecimal mitigationFactor     = new BigDecimal((.06 * armor) / (1 + (.06 * armor)));
        BigDecimal mitigatedDamage      = rawDamage.multiply(mitigationFactor);
        BigDecimal unmitigatedDamage    = rawDamage.subtract(mitigatedDamage);

        return unmitigatedDamage.intValue();
    }
}
