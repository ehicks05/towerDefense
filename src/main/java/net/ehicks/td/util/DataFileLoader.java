package net.ehicks.td.util;

import net.ehicks.td.entities.*;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataFileLoader
{
    private static final String DATA_PATH = "data" + File.separator;

    public static List<Tower> getTowerTypes()
    {
        List<Tower> towerTypes = new ArrayList<>();

        List<String> lines = Util.readAllLines(DATA_PATH + "towers.csv", true);
        for (String line : lines)
        {
            List<String> elements = Arrays.asList(line.split(","));
            String name             = elements.get(0);
            String projectileType   = elements.get(1);
            int price               = Integer.parseInt(elements.get(2));
            int attackRange         = Integer.parseInt(elements.get(3));
            BigDecimal attackSpeed  = new BigDecimal(elements.get(4));
            int numberOfTargets     = Integer.parseInt(elements.get(5));
            int sizeRadius          = Integer.parseInt(elements.get(6));
            String imageFile        = elements.get(7);

            Tower tower = new Tower(price, attackRange, attackSpeed, numberOfTargets, name, projectileType, sizeRadius, imageFile);
            towerTypes.add(tower);
        }

        return towerTypes;
    }

    public static List<Projectile> getProjectileTypes()
    {
        List<Projectile> projectileTypes = new ArrayList<>();

        List<String> lines = Util.readAllLines(DATA_PATH + "projectiles.csv", true);
        for (String line : lines)
        {
            List<String> elements = Arrays.asList(line.split(","));
            String name         = elements.get(0);
            int sizeRadius      = Integer.parseInt(elements.get(1));
            int moveSpeed       = Integer.parseInt(elements.get(2));
            int minDamage       = Integer.parseInt(elements.get(3));
            int maxDamage       = Integer.parseInt(elements.get(4));
            int maxRange        = Integer.parseInt(elements.get(5));
            String fireSound    = elements.get(6);
            String onHitEffect  = elements.get(7);
            int splashRadius    = Integer.parseInt(elements.get(8));
            int hitsPossible    = Integer.parseInt(elements.get(9));
            int bounceRange     = Integer.parseInt(elements.get(10));
            double thetaDelta   = Double.parseDouble(elements.get(11));
            String imageFile    = elements.get(12);

            Projectile projectile = new Projectile(name, sizeRadius, moveSpeed, minDamage, maxDamage, maxRange, fireSound,
                    onHitEffect, splashRadius, hitsPossible, bounceRange, thetaDelta, imageFile);
            projectileTypes.add(projectile);
        }

        return projectileTypes;
    }

    public static List<Mob> getMobTypes()
    {
        List<Mob> mobTypes = new ArrayList<>();

        List<String> lines = Util.readAllLines(DATA_PATH + "mobs.csv", true);
        for (String line : lines)
        {
            List<String> elements = Arrays.asList(line.split(","));
            String name     = elements.get(0);
            int index       = Integer.parseInt(elements.get(1));
            int size        = Integer.parseInt(elements.get(2));
            int speed       = Integer.parseInt(elements.get(3));
            int powerBudget = Integer.parseInt(elements.get(4));
            int maxHp       = Integer.parseInt(elements.get(5));
            int armor       = Integer.parseInt(elements.get(6));
            int bounty      = Integer.parseInt(elements.get(7));
            int outfit      = Integer.parseInt(elements.get(8));

            Mob mob = new Mob(2, size, speed, name, index, powerBudget, maxHp, armor, bounty, 0, outfit);
            mobTypes.add(mob);
        }

        return mobTypes;
    }

    public static List<Outfit> getOutfitTypes()
    {
        List<Outfit> outfits = new ArrayList<>();
        outfits.add(new Outfit(BodyPart.HUMAN, BodyPart.L_ROBE, BodyPart.T_ROBE));
        outfits.add(new Outfit(BodyPart.HUMAN, BodyPart.B_LEATHER, BodyPart.F_SHOES, BodyPart.L_PANTS, BodyPart.T_SHIRT, BodyPart.T_LEATHER, BodyPart.S_LEATHER, BodyPart.BR_LEATHER));
        outfits.add(new Outfit(BodyPart.HUMAN, BodyPart.B_LEATHER, BodyPart.F_SHOES, BodyPart.G_PLATE, BodyPart.H_CHAIN_HOOD, BodyPart.L_PANTS, BodyPart.T_SHIRT, BodyPart.T_LEATHER, BodyPart.S_LEATHER, BodyPart.BR_LEATHER));
        outfits.add(new Outfit(BodyPart.HUMAN, BodyPart.F_PLATE, BodyPart.G_PLATE, BodyPart.H_CHAIN_HOOD, BodyPart.L_PANTS, BodyPart.T_CHAIN, BodyPart.T_CHAIN_JACKET));
        outfits.add(new Outfit(BodyPart.HUMAN, BodyPart.F_PLATE, BodyPart.G_PLATE, BodyPart.L_PLATE, BodyPart.T_PLATE, BodyPart.S_PLATE));
        outfits.add(new Outfit(BodyPart.SKELE, BodyPart.L_ROBE, BodyPart.T_ROBE));
        outfits.add(new Outfit(BodyPart.SKELE, BodyPart.B_LEATHER, BodyPart.F_SHOES, BodyPart.L_PANTS, BodyPart.T_SHIRT, BodyPart.T_LEATHER, BodyPart.S_LEATHER, BodyPart.BR_LEATHER));
        outfits.add(new Outfit(BodyPart.SKELE, BodyPart.B_LEATHER, BodyPart.F_SHOES, BodyPart.G_PLATE, BodyPart.H_CHAIN_HOOD, BodyPart.L_PANTS, BodyPart.T_SHIRT, BodyPart.T_LEATHER, BodyPart.S_LEATHER, BodyPart.BR_LEATHER));
        outfits.add(new Outfit(BodyPart.SKELE, BodyPart.F_PLATE, BodyPart.G_PLATE, BodyPart.H_CHAIN_HOOD, BodyPart.L_PANTS, BodyPart.T_CHAIN, BodyPart.T_CHAIN_JACKET));
        outfits.add(new Outfit(BodyPart.SKELE, BodyPart.F_PLATE, BodyPart.G_PLATE, BodyPart.L_PLATE, BodyPart.T_PLATE, BodyPart.S_PLATE));
        return outfits;
    }
}
