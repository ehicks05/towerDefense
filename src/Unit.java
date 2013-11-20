import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class Unit
{
    private static int m_seq = 0;
    private int m_objectId;
    private int m_team;
    private String m_name;

    private int m_sizeRadius;
    private int m_sightRadius;
    private int m_moveSpeed;
    private BigDecimal m_timeOfLastMove;
    private boolean m_moving;

    private Point m_location;
    private Point m_destination;

    private int m_hp;
    private int m_armor;
    private int m_minDamage;
    private int m_maxDamage;
    private int m_attackRange;

    private BigDecimal m_attackSpeed;
    private BigDecimal m_timeOfLastAttack;

    private Unit m_target;

    private int m_kills;

    public Unit()
    {
        setObjectId(m_seq++);
        setName(NameLogic.generateName(this));
    }

    public String toString()
    {
        return m_name + " (T" + m_team + ",ID:" + m_objectId + ")";
    }

    public boolean isAlive()
    {
        return m_hp > 0;
    }

    public int getAttackDamage()
    {
        Random random = new Random();
        int damageRange = (m_maxDamage - m_minDamage) + 1;
        int randomPortion = random.nextInt(damageRange);
        return randomPortion + m_minDamage;
    }

    public Unit getClosestVisibleEnemy(List<Unit> units)
    {
        Unit closestEnemy = null;
        double smallestDistance = Double.MAX_VALUE;

        for (Unit unit : units)
        {
            if (unit.equals(this) || unit.getTeam() == m_team) continue;

            double distance = m_location.getDistance(unit.getLocation());

            if (distance <= m_sightRadius && distance < smallestDistance)
            {
                closestEnemy = unit;
                smallestDistance = distance;
            }
        }

        return closestEnemy;
    }

    public boolean isTargetInRange()
    {
        double distance = new BigDecimal(m_location.getDistance(m_target.getLocation())).setScale(0, RoundingMode.HALF_UP).doubleValue();
        return distance <= m_attackRange;
    }

    public void moveTowardCoordinate(Point destination, boolean isDestinationAnEnemyTarget)
    {
        if (!m_moving)
        {
            m_timeOfLastMove = GameLogic.getNow();
            m_moving = true;
        }

        BigDecimal timeSinceLastMove = GameLogic.getElapsedTime(m_timeOfLastMove);
        BigDecimal moveSpeed = new BigDecimal(m_moveSpeed);
        BigDecimal potentialDistanceToMove = moveSpeed.multiply(timeSinceLastMove);
        BigDecimal currentDistance = new BigDecimal(m_location.getDistance(destination)).setScale(0, RoundingMode.HALF_UP);

        BigDecimal desiredDistance = BigDecimal.ZERO;
        if (isDestinationAnEnemyTarget)
            desiredDistance = new BigDecimal(this.getAttackRange());

        // if we are within our desired distance, stop.
        if (currentDistance.compareTo(desiredDistance) <= 0)
        {
            m_destination = null;
            return;
        }

        // if our expected movement would take us closer than we need to be, don't move so far.
        BigDecimal expectedNewDistance = currentDistance.subtract(potentialDistanceToMove);
        BigDecimal actualDistanceToMove = potentialDistanceToMove;
        if (expectedNewDistance.compareTo(desiredDistance) == -1)
        {
            BigDecimal excessMovement = desiredDistance.subtract(expectedNewDistance);
            actualDistanceToMove = potentialDistanceToMove.subtract(excessMovement);
        }

        // calculate the weighting for x and y terms
        BigDecimal deltaX = new BigDecimal(destination.getDeltaX(m_location));
        BigDecimal deltaY = new BigDecimal(destination.getDeltaY(m_location));

        BigDecimal factorX = deltaX.divide(currentDistance, 16, RoundingMode.HALF_UP);
        BigDecimal factorY = deltaY.divide(currentDistance, 16, RoundingMode.HALF_UP);

        BigDecimal distanceToMoveX = actualDistanceToMove.multiply(factorX);
        BigDecimal distanceToMoveY = actualDistanceToMove.multiply(factorY);

        double newX = new BigDecimal(getX()).add((distanceToMoveX)).doubleValue();
        double newY = new BigDecimal(getY()).add((distanceToMoveY)).doubleValue();

        setLocation(new Point(newX, newY));
        setTimeOfLastMove(GameLogic.getNow());

//        Log.logInfo(simulationStart, this + " moved " + distanceToMove.setScale(2, RoundingMode.HALF_UP) + " units" + " from " + startLocation + " to " + this.getLocation());
    }

    public double getX()
    {
        return m_location.getX();
    }

    public double getY()
    {
        return m_location.getY();
    }

    public int getObjectId()
    {
        return m_objectId;
    }

    public void setObjectId(int objectId)
    {
        m_objectId = objectId;
    }

    public boolean isReadyToAttack()
    {
        return GameLogic.getElapsedTime(m_timeOfLastAttack).compareTo(m_attackSpeed) > 0;
    }

    public int getSizeRadius()
    {
        return m_sizeRadius;
    }

    public void setSizeRadius(int sizeRadius)
    {
        m_sizeRadius = sizeRadius;
    }

    public int getTeam()
    {
        return m_team;
    }

    public void setTeam(int team)
    {
        m_team = team;
    }

    public String getName()
    {
        return m_name;
    }

    public void setName(String name)
    {
        m_name = name;
    }

    public int getSightRadius()
    {
        return m_sightRadius;
    }

    public void setSightRadius(int sightRadius)
    {
        m_sightRadius = sightRadius;
    }

    public int getMoveSpeed()
    {
        return m_moveSpeed;
    }

    public void setMoveSpeed(int moveSpeed)
    {
        m_moveSpeed = moveSpeed;
    }

    public BigDecimal getTimeOfLastMove()
    {
        return m_timeOfLastMove;
    }

    public void setTimeOfLastMove(BigDecimal timeOfLastMove)
    {
        m_timeOfLastMove = timeOfLastMove;
    }

    public boolean isMoving()
    {
        return m_moving;
    }

    public void setMoving(boolean moving)
    {
        m_moving = moving;
    }

    public Point getLocation()
    {
        return m_location;
    }

    public void setLocation(Point location)
    {
        m_location = location;
    }

    public Point getDestination()
    {
        return m_destination;
    }

    public void setDestination(Point destination)
    {
        m_destination = destination;
    }

    public int getHp()
    {
        return m_hp;
    }

    public void setHp(int hp)
    {
        m_hp = hp;
    }

    public int getArmor()
    {
        return m_armor;
    }

    public void setArmor(int armor)
    {
        m_armor = armor;
    }

    public void setMinDamage(int minDamage)
    {
        m_minDamage = minDamage;
    }

    public void setMaxDamage(int maxDamage)
    {
        m_maxDamage = maxDamage;
    }

    public int getAttackRange()
    {
        return m_attackRange;
    }

    public void setAttackRange(int attackRange)
    {
        m_attackRange = attackRange;
    }

    public BigDecimal getAttackSpeed()
    {
        return m_attackSpeed;
    }

    public void setAttackSpeed(BigDecimal attackSpeed)
    {
        m_attackSpeed = attackSpeed;
    }

    public BigDecimal getTimeOfLastAttack()
    {
        return m_timeOfLastAttack;
    }

    public void setTimeOfLastAttack(BigDecimal timeOfLastAttack)
    {
        m_timeOfLastAttack = timeOfLastAttack;
    }

    public Unit getTarget()
    {
        return m_target;
    }

    public void setTarget(Unit target)
    {
        m_target = target;
    }

    public int getKills()
    {
        return m_kills;
    }

    public void setKills(int kills)
    {
        m_kills = kills;
    }

    public int getMinDamage()
    {
        return m_minDamage;
    }

    public int getMaxDamage()
    {
        return m_maxDamage;
    }
}
