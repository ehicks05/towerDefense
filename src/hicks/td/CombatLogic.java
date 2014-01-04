package hicks.td;

import hicks.td.entities.Unit;

import java.math.BigDecimal;

public final class CombatLogic
{
    public static void performAttack(Unit attacker)
    {
        Unit defender           = attacker.getTarget();
        int rawDamage           = attacker.getAttackDamage();
        int unmitigatedDamage   = getUnmitigatedDamage(rawDamage, defender.getArmor());

        defender.setCurrentHp(defender.getCurrentHp() - unmitigatedDamage);
        attacker.setTimeOfLastAttack(Util.now());

        if (!defender.isAlive())
        {
            processDeath(defender);
            GameState.getPlayer().addGold(10);
            attacker.setKills(attacker.getKills() + 1);
        }
    }

    private static void processDeath(Unit defender)
    {
        for (Unit unit : GameState.getUnits())
        {
            if (unit.getTarget() != null && unit.getTarget().equals(defender))
                unit.setTarget(null);
        }

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
