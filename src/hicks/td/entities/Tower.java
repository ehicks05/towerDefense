package hicks.td.entities;

import hicks.td.World;
import hicks.td.logic.ProjectileLogic;
import hicks.td.logic.UnitLogic;
import hicks.td.util.Log;
import hicks.td.util.Util;

import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tower extends Unit
{
    private int             m_price;
    private int             m_attackRange;
    private BigDecimal      m_attackSpeed;
    private BigDecimal      m_timeOfLastAttack = Util.now();
    private List<Mob>       m_targets;
    private int             m_kills;
    private int             m_numberOfTargets;
    private List<Upgrade>   m_upgrades = new ArrayList<>();
    private String          m_name = "";
    private String          m_projectileType = "";
    private String          m_imageFile = "";

    public Tower(int price, int attackRange, BigDecimal attackSpeed, int numberOfTargets, String name, String projectileType, int sizeRadius, String imageFile)
    {
        m_price = price;
        m_attackRange = attackRange;
        m_attackSpeed = attackSpeed;
        m_timeOfLastAttack = Util.now();
        m_targets = new ArrayList<>();
        m_numberOfTargets = numberOfTargets;
        m_upgrades = new ArrayList<>();
        m_name = name;
        m_projectileType = projectileType;
        m_imageFile = imageFile;
        setSizeRadius(sizeRadius);
    }

    // Copy Constructor
    public Tower(Tower tower)
    {
        m_price = tower.getPrice();
        m_attackRange = tower.getAttackRange();
        m_attackSpeed = tower.getAttackSpeed();
        m_timeOfLastAttack = Util.now();
        m_targets = new ArrayList<>();
        m_numberOfTargets = tower.getNumberOfTargets();
        m_upgrades = new ArrayList<>();
        m_name = tower.getName();
        m_projectileType = tower.getProjectileType();
        m_imageFile = tower.getImageFile();
        setSizeRadius(tower.getSizeRadius());
    }

    public Image getImage()
    {
        return World.getGameImage(World.getImageDir() + m_imageFile).getImage();
    }

    public void performTowerBehavior()
    {
        // set targets
        this.setTargets(UnitLogic.getEnemiesClosestToCore(this, m_attackRange, null, this.getNumberOfTargets()));

        if (this.getTargets().size() > 0) this.performHostileBehavior();
    }

    public void addUpgrade(Upgrade upgrade)
    {
        upgrade.applyTowerEffect(this);
        m_upgrades.add(upgrade);
    }

    public boolean isReadyToAttack()
    {
        return Util.getElapsedTime(m_timeOfLastAttack).compareTo(m_attackSpeed) > 0;
    }

    public void performHostileBehavior()
    {
        if (isReadyToAttack())
        {
            for (Mob target : this.getTargets())
            {
                Projectile newProjectile = getProjectileWithUpgrades();

                newProjectile.setOriginator(this);

                ProjectileLogic.shootProjectile(this, newProjectile, target.getLocation());
                this.setTimeOfLastAttack(Util.now());
            }
        }
    }

    public Projectile getProjectileWithUpgrades()
    {
        Projectile newProjectile = World.getProjectileByName(m_projectileType);
        for (Upgrade upgrade : m_upgrades)
            upgrade.applyProjectileEffect(newProjectile);
        return newProjectile;
    }

    public List<Upgrade> getAvailableUpgrades()
    {
        List<Upgrade> availableUpgrades = new ArrayList<>();
        List<Upgrade> upgrades = World.getUpgradeTypes();
        for (Upgrade upgrade : upgrades)
        {
            String preReqCode = upgrade.getPreReq();
            if (!hasUpgrade(upgrade.getCode()) && hasPreReq(preReqCode)) availableUpgrades.add(upgrade);
        }

        return availableUpgrades;
    }

    public boolean hasPreReq(String preReqCode)
    {
        if (preReqCode.length() == 0) return true;
        for (Upgrade upgrade : m_upgrades)
            if (upgrade.getCode().equals(preReqCode)) return true;
        return false;
    }

    public boolean hasUpgrade(String code)
    {
        for (Upgrade upgrade : m_upgrades)
            if (upgrade.getCode().equals(code)) return true;
        return false;
    }

    // ------------ Properties

    public int getPrice()
    {
        return m_price;
    }

    public void setPrice(int price)
    {
        m_price = price;
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

    public List<Mob> getTargets()
    {
        return m_targets;
    }

    public void setTargets(List<Mob> targets)
    {
        m_targets = targets;
    }

    public int getKills()
    {
        return m_kills;
    }

    public void setKills(int kills)
    {
        m_kills = kills;
    }

    public int getNumberOfTargets()
    {
        return m_numberOfTargets;
    }

    public void setNumberOfTargets(int numberOfTargets)
    {
        m_numberOfTargets = numberOfTargets;
    }

    public List<Upgrade> getUpgrades()
    {
        return m_upgrades;
    }

    public void setUpgrades(List<Upgrade> upgrades)
    {
        m_upgrades = upgrades;
    }

    public String getName()
    {
        return m_name;
    }

    public void setName(String name)
    {
        m_name = name;
    }

    public String getProjectileType()
    {
        return m_projectileType;
    }

    public void setProjectileType(String projectileType)
    {
        m_projectileType = projectileType;
    }

    public String getImageFile()
    {
        return m_imageFile;
    }

    public void setImageFile(String imageFile)
    {
        m_imageFile = imageFile;
    }
}
