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

        World.setUnits(new ArrayList<Unit>());
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
        gameImages.add(new GameImage(imageDir + "arrowTower.gif", new ImageIcon(imageDir + "arrowTower.gif").getImage()));
        gameImages.add(new GameImage(imageDir + "glaiveTower.gif", new ImageIcon(imageDir + "glaiveTower.gif").getImage()));
        gameImages.add(new GameImage(imageDir + "cannonTower.gif", new ImageIcon(imageDir + "cannonTower.gif").getImage()));
        gameImages.add(new GameImage(imageDir + "iceTower.gif", new ImageIcon(imageDir + "iceTower.gif").getImage()));

        gameImages.add(new GameImage(imageDir + "arrow.png", new ImageIcon(imageDir + "arrow.png").getImage()));
        gameImages.add(new GameImage(imageDir + "glaive.png", new ImageIcon(imageDir + "glaive.png").getImage()));
        gameImages.add(new GameImage(imageDir + "rock.png", new ImageIcon(imageDir + "rock.png").getImage()));
        gameImages.add(new GameImage(imageDir + "iceBolt.png", new ImageIcon(imageDir + "iceBolt.png").getImage()));

        return gameImages;
    }

    public static List<Mob> getOneOfEachMobType()
    {
        List<Mob> mobsOfEachType = new ArrayList<>();
        List<BodyPartCollection> bodyPartCollections = getBodyPartCollections();

        mobsOfEachType.add(new Mob(2, 32, 60, "peasant",            1,  1,  30, 0,   1, 0, bodyPartCollections.get(0)));
        mobsOfEachType.add(new Mob(2, 32, 60, "archer",             2,  2,  45, 2,   2, 0, bodyPartCollections.get(1)));
        mobsOfEachType.add(new Mob(2, 32, 60, "footman",            3,  3,  60, 4,   3, 0, bodyPartCollections.get(2)));
        mobsOfEachType.add(new Mob(2, 32, 70, "barbarian",          4,  4,  75, 2,   4, 0, bodyPartCollections.get(3)));
        mobsOfEachType.add(new Mob(2, 32, 60, "knight",             5,  5,  90, 7,   5, 0, bodyPartCollections.get(4)));

        mobsOfEachType.add(new Mob(2, 32, 70, "skeletonPeasant",    6,  7, 135, 2,   8, 0, bodyPartCollections.get(5)));
        mobsOfEachType.add(new Mob(2, 32, 70, "skeletonArcher",     7,  9, 200, 4,  10, 0, bodyPartCollections.get(6)));
        mobsOfEachType.add(new Mob(2, 32, 70, "skeletonFootman",    8, 11, 250, 6,  12, 0, bodyPartCollections.get(7)));
        mobsOfEachType.add(new Mob(2, 32, 80, "skeletonBarbarian",  9, 12, 300, 4,  14, 0, bodyPartCollections.get(8)));
        mobsOfEachType.add(new Mob(2, 32, 70, "skeletonKnight",    10, 15, 400, 9,  16, 0, bodyPartCollections.get(9)));

        return mobsOfEachType;
    }

    public static List<BodyPartCollection> getBodyPartCollections()
    {
        List<BodyPartCollection> bodyPartCollections = new ArrayList<>();
        bodyPartCollections.add(new BodyPartCollection(BodyPart.BODY_HUMAN,    null, null, null, null, null, BodyPart.LEGS_ROBE, BodyPart.TORSO_ROBE, null, null, null));
        bodyPartCollections.add(new BodyPartCollection(BodyPart.BODY_HUMAN,    null, BodyPart.BELT_LEATHER, BodyPart.FEET_SHOES, null, null, BodyPart.LEGS_PANTS, BodyPart.TORSO_LEATHER_SHIRT, BodyPart.TORSO_LEATHER_ARMOR, BodyPart.TORSO_LEATHER_SHOULDERS, BodyPart.TORSO_LEATHER_BRACERS));
        bodyPartCollections.add(new BodyPartCollection(BodyPart.BODY_HUMAN,    null, BodyPart.BELT_LEATHER, BodyPart.FEET_SHOES, BodyPart.HANDS_PLATE, BodyPart.HEAD_CHAIN_HOOD, BodyPart.LEGS_PANTS, BodyPart.TORSO_LEATHER_SHIRT, BodyPart.TORSO_LEATHER_ARMOR, BodyPart.TORSO_LEATHER_SHOULDERS, BodyPart.TORSO_LEATHER_BRACERS));
        bodyPartCollections.add(new BodyPartCollection(BodyPart.BODY_HUMAN,    null, null, BodyPart.FEET_PLATE, BodyPart.HANDS_PLATE, BodyPart.HEAD_CHAIN_HOOD, BodyPart.LEGS_PANTS, BodyPart.TORSO_CHAIN_ARMOR, BodyPart.TORSO_CHAIN_JACKET, null, null));
        bodyPartCollections.add(new BodyPartCollection(BodyPart.BODY_HUMAN,    null, null, BodyPart.FEET_PLATE, BodyPart.HANDS_PLATE, null, BodyPart.LEGS_PLATE, BodyPart.TORSO_PLATE, null, BodyPart.TORSO_PLATE_SHOULDERS, null));
        bodyPartCollections.add(new BodyPartCollection(BodyPart.BODY_SKELETON, null, null, null, null, null, BodyPart.LEGS_ROBE, BodyPart.TORSO_ROBE, null, null, null));
        bodyPartCollections.add(new BodyPartCollection(BodyPart.BODY_SKELETON, null, BodyPart.BELT_LEATHER, BodyPart.FEET_SHOES, null, null, BodyPart.LEGS_PANTS, BodyPart.TORSO_LEATHER_SHIRT, BodyPart.TORSO_LEATHER_ARMOR, BodyPart.TORSO_LEATHER_SHOULDERS, BodyPart.TORSO_LEATHER_BRACERS));
        bodyPartCollections.add(new BodyPartCollection(BodyPart.BODY_SKELETON, null, BodyPart.BELT_LEATHER, BodyPart.FEET_SHOES, BodyPart.HANDS_PLATE, BodyPart.HEAD_CHAIN_HOOD, BodyPart.LEGS_PANTS, BodyPart.TORSO_LEATHER_SHIRT, BodyPart.TORSO_LEATHER_ARMOR, BodyPart.TORSO_LEATHER_SHOULDERS, BodyPart.TORSO_LEATHER_BRACERS));
        bodyPartCollections.add(new BodyPartCollection(BodyPart.BODY_SKELETON, null, null, BodyPart.FEET_PLATE, BodyPart.HANDS_PLATE, BodyPart.HEAD_CHAIN_HOOD, BodyPart.LEGS_PANTS, BodyPart.TORSO_CHAIN_ARMOR, BodyPart.TORSO_CHAIN_JACKET, null, null));
        bodyPartCollections.add(new BodyPartCollection(BodyPart.BODY_SKELETON, null, null, BodyPart.FEET_PLATE, BodyPart.HANDS_PLATE, null, BodyPart.LEGS_PLATE, BodyPart.TORSO_PLATE, null, BodyPart.TORSO_PLATE_SHOULDERS, null));
        return bodyPartCollections;
    }

    private static void deleteLogs()
    {
        boolean deleteSuccess = new File("log.txt").delete();
        Log.info("Clearing logs..." + (deleteSuccess ? "done." : "none found."));
    }
}