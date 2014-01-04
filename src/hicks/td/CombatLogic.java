package hicks.td;

import hicks.td.entities.Unit;

import java.math.BigDecimal;

public final class CombatLogic
{
    public static void performAttack(Unit attacker)
    {
        if (attacker.isMoving())
            attacker.setMoving(false);

        Unit defender = attacker.getTarget();
        int rawDamage = UnitLogic.getAttackDamage(attacker);
        int unmitigatedDamage = getUnmitigatedDamage(rawDamage, defender.getArmor());

        defender.setCurrentHp(defender.getCurrentHp() - unmitigatedDamage);

        attacker.setTimeOfLastAttack(Util.now());

        // update battle log
        Log.logInfo(attacker + " attacks " + attacker.getTarget() + " for " + unmitigatedDamage + " damage.");
        Log.logInfo(defender + " has " + defender.getCurrentHp() + " hp left.");

        if (!defender.isAlive())
        {
            processDeath(defender);
            attacker.setKills(attacker.getKills() + 1);
        }
    }

    private static void processDeath(Unit defender)
    {
        Log.logInfo(defender + " is dead.");

        for (Unit unit : GameState.getUnits())
            if (unit.getTarget() != null && unit.getTarget().equals(defender))
            {
                unit.setTarget(null);
                unit.setTimeOfLastMove(Util.now());
            }

        GameState.removeUnit(defender);
    }

    private static int getUnmitigatedDamage(int damage, int armor)
    {
        BigDecimal rawDamage = new BigDecimal(damage);
        BigDecimal mitigationFactor = new BigDecimal((.06 * armor) / (1 + (.06 * armor)));
        BigDecimal mitigatedDamage = rawDamage.multiply(mitigationFactor);
        BigDecimal unmitigatedDamage = rawDamage.subtract(mitigatedDamage);
        return unmitigatedDamage.intValue();
    }
}
