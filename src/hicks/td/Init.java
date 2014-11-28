package hicks.td;

import hicks.td.audio.SoundManager;
import hicks.td.entities.*;
import hicks.td.entities.projectile.Projectile;
import hicks.td.ui.DisplayInfo;
import hicks.td.util.*;

import javax.swing.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public final class Init
{
    public static void init(boolean loadResources)
    {
        World.setStartTime(Util.now());
        Log.info("Initializing " + new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a").format(World.getStartTime()));

        deleteLogs();

        World.setImageDir("ass\\img\\");
        World.setPlayer(new Player(300, 1, 0));
        World.setGameMap(new GameMap(768, 576));
        World.setWaves(Wave.getWaves());
        World.setLogicalMap(MapBuilder.buildRandomMap());
        World.setTerrainImage(MapBuilder.createImageFromLogicalMap(World.getLogicalMap()));

        if (loadResources)
            loadResources();
    }

    private static void loadResources()
    {
        DisplayInfo.setDisplayProperties();
        SoundManager.init();
        MobTileLoader.init();

        List<Upgrade> allUpgrades = new ArrayList<>();
        allUpgrades.add(new UpgradeAttackRange());
        allUpgrades.add(new UpgradeDamage());
        World.setUpgradeTypes(allUpgrades);

        World.setTowerTypes(Tower.getTowerTypes());
        World.setProjectileTypes(Projectile.getProjectileTypes());

        World.setGameImages(getGameImages());
    }

    private static List<GameImage> getGameImages()
    {
        String imageDir = World.getImageDir();

        List<GameImage> gameImages = new ArrayList<>();

        for (Tower towerType : World.getTowerTypes())
            gameImages.add(new GameImage(imageDir + towerType.getImageFile(), new ImageIcon(imageDir + towerType.getImageFile()).getImage()));

        for (Projectile projectileType : World.getProjectileTypes())
            gameImages.add(new GameImage(imageDir + projectileType.getImageFile(), new ImageIcon(imageDir + projectileType.getImageFile()).getImage()));

        return gameImages;
    }

    public static List<Mob> getOneOfEachMobType()
    {
        List<Mob> mobsOfEachType = new ArrayList<>();
        List<Outfit> outfits = getBodyPartCollections();

        mobsOfEachType.add(new Mob(2, 32, 60, "peasant",            1,  1,  30, 0,   1, 0, outfits.get(0)));
        mobsOfEachType.add(new Mob(2, 32, 60, "archer",             2,  2,  45, 2,   2, 0, outfits.get(1)));
        mobsOfEachType.add(new Mob(2, 32, 60, "footman",            3,  3,  60, 4,   3, 0, outfits.get(2)));
        mobsOfEachType.add(new Mob(2, 32, 70, "barbarian",          4,  4,  75, 2,   4, 0, outfits.get(3)));
        mobsOfEachType.add(new Mob(2, 32, 60, "knight",             5,  5,  90, 7,   5, 0, outfits.get(4)));

        mobsOfEachType.add(new Mob(2, 32, 70, "skeletonPeasant",    6,  7, 135, 2,   8, 0, outfits.get(5)));
        mobsOfEachType.add(new Mob(2, 32, 70, "skeletonArcher",     7,  9, 200, 4,  10, 0, outfits.get(6)));
        mobsOfEachType.add(new Mob(2, 32, 70, "skeletonFootman",    8, 11, 250, 6,  12, 0, outfits.get(7)));
        mobsOfEachType.add(new Mob(2, 32, 80, "skeletonBarbarian",  9, 12, 300, 4,  14, 0, outfits.get(8)));
        mobsOfEachType.add(new Mob(2, 32, 70, "skeletonKnight",    10, 15, 400, 9,  16, 0, outfits.get(9)));

        return mobsOfEachType;
    }

    public static List<Outfit> getBodyPartCollections()
    {
        List<Outfit> outfits = new ArrayList<>();
        outfits.add(new Outfit(BodyPart.HUMAN, null, null, null, null, BodyPart.L_ROBE, BodyPart.T_ROBE, null, null, null));
        outfits.add(new Outfit(BodyPart.HUMAN, BodyPart.B_LEATHER, BodyPart.F_SHOES, null, null, BodyPart.L_PANTS, BodyPart.T_SHIRT, BodyPart.T_LEATHER, BodyPart.S_LEATHER, BodyPart.BR_LEATHER));
        outfits.add(new Outfit(BodyPart.HUMAN, BodyPart.B_LEATHER, BodyPart.F_SHOES, BodyPart.G_PLATE, BodyPart.H_CHAIN_HOOD, BodyPart.L_PANTS, BodyPart.T_SHIRT, BodyPart.T_LEATHER, BodyPart.S_LEATHER, BodyPart.BR_LEATHER));
        outfits.add(new Outfit(BodyPart.HUMAN, null, BodyPart.F_PLATE, BodyPart.G_PLATE, BodyPart.H_CHAIN_HOOD, BodyPart.L_PANTS, BodyPart.T_CHAIN, BodyPart.T_CHAIN_JACKET, null, null));
        outfits.add(new Outfit(BodyPart.HUMAN, null, BodyPart.F_PLATE, BodyPart.G_PLATE, null, BodyPart.L_PLATE, BodyPart.T_PLATE, null, BodyPart.S_PLATE, null));
        outfits.add(new Outfit(BodyPart.SKELE, null, null, null, null, BodyPart.L_ROBE, BodyPart.T_ROBE, null, null, null));
        outfits.add(new Outfit(BodyPart.SKELE, BodyPart.B_LEATHER, BodyPart.F_SHOES, null, null, BodyPart.L_PANTS, BodyPart.T_SHIRT, BodyPart.T_LEATHER, BodyPart.S_LEATHER, BodyPart.BR_LEATHER));
        outfits.add(new Outfit(BodyPart.SKELE, BodyPart.B_LEATHER, BodyPart.F_SHOES, BodyPart.G_PLATE, BodyPart.H_CHAIN_HOOD, BodyPart.L_PANTS, BodyPart.T_SHIRT, BodyPart.T_LEATHER, BodyPart.S_LEATHER, BodyPart.BR_LEATHER));
        outfits.add(new Outfit(BodyPart.SKELE, null, BodyPart.F_PLATE, BodyPart.G_PLATE, BodyPart.H_CHAIN_HOOD, BodyPart.L_PANTS, BodyPart.T_CHAIN, BodyPart.T_CHAIN_JACKET, null, null));
        outfits.add(new Outfit(BodyPart.SKELE, null, BodyPart.F_PLATE, BodyPart.G_PLATE, null, BodyPart.L_PLATE, BodyPart.T_PLATE, null, BodyPart.S_PLATE, null));
        return outfits;
    }

    private static void deleteLogs()
    {
        boolean deleteSuccess = new File("log.txt").delete();
        Log.info("Clearing logs..." + (deleteSuccess ? "done." : "none found."));
    }
}