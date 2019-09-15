package net.ehicks.td.logic;

import net.ehicks.td.World;
import net.ehicks.td.audio.SoundEffect;
import net.ehicks.td.audio.SoundManager;
import net.ehicks.td.entities.Animation;
import net.ehicks.td.entities.Mob;
import net.ehicks.td.entities.Projectile;

import java.math.BigDecimal;

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
            World.getPlayer().addGold(damageRecipient.getBounty());
            damageSource.getOriginator().setKills(damageSource.getOriginator().getKills() + 1);
        }
    }

    private static void processDeath(Mob defender)
    {
        defender.removeFromTargeting();
        SoundManager.playSFX(SoundEffect.DEATH);
        playDeathAnimation(defender);
        World.removeUnit(defender);
    }

    private static int getUnmitigatedDamage(int damage, int armor)
    {
        BigDecimal rawDamage            = new BigDecimal(damage);
        BigDecimal mitigationFactor     = new BigDecimal((.06 * armor) / (1 + (.06 * armor)));
        BigDecimal mitigatedDamage      = rawDamage.multiply(mitigationFactor);
        BigDecimal unmitigatedDamage    = rawDamage.subtract(mitigatedDamage);

        return unmitigatedDamage.intValue();
    }

    private static void playDeathAnimation(Mob mob)
    {
        // add a dying animation
        Animation animation = new Animation("death", mob.getSizeRadius(), mob.getLocation(), mob.getOutfit());
        World.addUnit(animation);
    }
}
