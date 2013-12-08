package hicks.combat;

import hicks.combat.entities.Unit;

import java.math.BigDecimal;

public class CombatLogic
{
    public static void performAttack(Unit attacker, GameMap map, BigDecimal simulationStart)
    {
        if (attacker.isMoving())
            attacker.setMoving(false);

        Unit defender = attacker.getTarget();
        int rawDamage = attacker.getAttackDamage();
        int unmitigatedDamage = getUnmitigatedDamage(rawDamage, defender.getArmor());

        defender.setHp(defender.getHp() - unmitigatedDamage);

        attacker.setTimeOfLastAttack(GameLogic.now());

        // update battle log
        Log.logInfo(simulationStart, attacker + " attacks " + attacker.getTarget() + " for " + unmitigatedDamage + " damage.");
        Log.logInfo(simulationStart, defender + " has " + defender.getHp() + " hp left.");

        if (!defender.isAlive())
        {
            processDeath(defender, map, simulationStart);
            attacker.setKills(attacker.getKills() + 1);
        }
    }

    private static void processDeath(Unit defender, GameMap map, BigDecimal simulationStart)
    {
        Log.logInfo(simulationStart, defender + " is dead.");

        for (Unit unit : map.getExistingUnits())
            if (unit.getTarget() != null && unit.getTarget().equals(defender))
            {
                unit.setTarget(null);
                unit.setTimeOfLastMove(GameLogic.now());
            }

        map.removeUnitFromExistingUnits(defender);
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
