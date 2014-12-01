package hicks.td.entities;

import hicks.td.World;
import hicks.td.logic.HitEffectLogic;
import hicks.td.util.Log;

import java.awt.*;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Projectile extends Unit
{
    private String m_name;

    private Tower m_originator;
    private String m_fireSound;
    private String m_onHitEffect;

    private double m_maximumRange;
    private double m_distanceTravelled;
    private double m_theta;
    private double m_thetaDelta;

    private int m_minDamage;
    private int m_maxDamage;

    private int m_splashRadius;

    private int m_hitsPossible;
    private int m_hitsPerformed;
    private Mob m_lastMobHit = new Mob(0,0,0,"na",0,0,0,0,0,0,null);
    private int m_bounceRange;

    private List<Upgrade> m_upgrades = new ArrayList<>();

    private String          m_imageFile = "";

    public Projectile(String name, int sizeRadius, int moveSpeed, int minDamage, int maxDamage, int maxRange, String fireSound,
                      String onHitEffect, int splashRadius, int hitsPossible, int bounceRange, double thetaDelta, String imageFile)
    {
        m_name = name;
        this.setSizeRadius(sizeRadius);
        this.setMoveSpeed(moveSpeed);
        m_minDamage = minDamage;
        m_maxDamage = maxDamage;
        m_maximumRange = maxRange;
        m_fireSound = fireSound;
        m_onHitEffect = onHitEffect;
        m_splashRadius = splashRadius;
        m_hitsPossible = hitsPossible;
        m_bounceRange = bounceRange;
        m_thetaDelta = thetaDelta;
        m_imageFile = imageFile;
        setTeam(1);
    }

    // Copy Constructor
    public Projectile(Projectile projectile)
    {
        m_name = projectile.getName();
        this.setSizeRadius(projectile.getSizeRadius());
        this.setMoveSpeed(projectile.getMoveSpeed());
        m_minDamage = projectile.getMinDamage();
        m_maxDamage = projectile.getMaxDamage();
        m_maximumRange = projectile.getMaximumRange();
        m_fireSound = projectile.getFireSound();
        m_onHitEffect = projectile.getOnHitEffect();
        m_splashRadius = projectile.getSplashRadius();
        m_hitsPossible = projectile.getHitsPossible();
        m_bounceRange = projectile.getBounceRange();
        m_thetaDelta = projectile.getThetaDelta();
        m_imageFile = projectile.getImageFile();
        setTeam(1);
    }

    public static List<Projectile> getProjectileTypes()
    {
        List<Projectile> projectileTypes = new ArrayList<>();
        try
        {
            List<String> lines = Files.readAllLines(Paths.get("data\\projectiles.csv"), Charset.defaultCharset());
            lines.remove(0);
            for (String line : lines)
            {
                List<String> elements = Arrays.asList(line.split(","));
                String name             = elements.get(0);
                int sizeRadius          = Integer.parseInt(elements.get(1));
                int moveSpeed           = Integer.parseInt(elements.get(2));
                int minDamage           = Integer.parseInt(elements.get(3));
                int maxDamage           = Integer.parseInt(elements.get(4));
                int maxRange            = Integer.parseInt(elements.get(5));
                String fireSound        = elements.get(6);
                String onHitEffect      = elements.get(7);
                int splashRadius        = Integer.parseInt(elements.get(8));
                int hitsPossible        = Integer.parseInt(elements.get(9));
                int bounceRange         = Integer.parseInt(elements.get(10));
                double thetaDelta         = Double.parseDouble(elements.get(11));
                String imageFile        = elements.get(12);

                Projectile projectile = new Projectile(name, sizeRadius, moveSpeed, minDamage, maxDamage, maxRange, fireSound,
                        onHitEffect, splashRadius, hitsPossible, bounceRange, thetaDelta, imageFile);
                projectileTypes.add(projectile);
            }
        }
        catch (IOException e)
        {
            Log.info(e.getMessage(), true);
        }

        return projectileTypes;
    }

    public Image getImage()
    {
        return World.getGameImage(World.getImageDir() + m_imageFile).getImage();
    }

    public int getAttackDamage()
    {
        Random random       = new Random();
        int damageRange     = m_maxDamage - m_minDamage + 1;
        int randomPortion   = random.nextInt(damageRange);

        return m_minDamage + randomPortion;
    }

    // ---------- Behavior

    public void onHit(Mob victim)
    {
        HitEffectLogic.applyHitEffect(this, victim);
    }

    public void applyUpgrades(List<Upgrade> upgrades)
    {
        if (upgrades == null) return;

        for (Upgrade upgrade : upgrades)
            upgrade.applyProjectileEffect(this);
    }

    // ---------- Properties

    public String getName()
    {
        return m_name;
    }

    public void setName(String name)
    {
        m_name = name;
    }

    public Tower getOriginator()
    {
        return m_originator;
    }

    public void setOriginator(Tower originator)
    {
        this.m_originator = originator;
    }

    public String getFireSound()
    {
        return m_fireSound;
    }

    public void setFireSound(String fireSound)
    {
        m_fireSound = fireSound;
    }

    public String getOnHitEffect()
    {
        return m_onHitEffect;
    }

    public void setOnHitEffect(String onHitEffect)
    {
        m_onHitEffect = onHitEffect;
    }

    public double getMaximumRange()
    {
        return m_maximumRange;
    }

    public void setMaximumRange(double maximumRange)
    {
        m_maximumRange = maximumRange;
    }

    public double getDistanceTravelled()
    {
        return m_distanceTravelled;
    }

    public void setDistanceTravelled(double distanceTravelled)
    {
        m_distanceTravelled = distanceTravelled;
    }

    public double getTheta()
    {
        return m_theta;
    }

    public double getThetaDelta()
    {
        return m_thetaDelta;
    }

    public void setThetaDelta(double thetaDelta)
    {
        m_thetaDelta = thetaDelta;
    }

    public void setTheta(double theta)
    {
        m_theta = theta;
    }

    public void setMinDamage(int minDamage)
    {
        m_minDamage = minDamage;
    }

    public void setMaxDamage(int maxDamage)
    {
        m_maxDamage = maxDamage;
    }

    public int getMinDamage()
    {
        return m_minDamage;
    }

    public int getMaxDamage()
    {
        return m_maxDamage;
    }

    public int getSplashRadius()
    {
        return m_splashRadius;
    }

    public void setSplashRadius(int splashRadius)
    {
        m_splashRadius = splashRadius;
    }

    public int getHitsPossible()
    {
        return m_hitsPossible;
    }

    public void setHitsPossible(int hitsPossible)
    {
        m_hitsPossible = hitsPossible;
    }

    public int getHitsPerformed()
    {
        return m_hitsPerformed;
    }

    public void setHitsPerformed(int hitsPerformed)
    {
        m_hitsPerformed = hitsPerformed;
    }

    public Mob getLastMobHit()
    {
        return m_lastMobHit;
    }

    public void setLastMobHit(Mob lastMobHit)
    {
        m_lastMobHit = lastMobHit;
    }

    public int getBounceRange()
    {
        return m_bounceRange;
    }

    public void setBounceRange(int bounceRange)
    {
        m_bounceRange = bounceRange;
    }

    public List<Upgrade> getUpgrades()
    {
        return m_upgrades;
    }

    public void setUpgrades(List<Upgrade> upgrades)
    {
        m_upgrades = upgrades;
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
