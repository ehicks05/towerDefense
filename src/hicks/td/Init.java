package hicks.td;

import hicks.td.audio.SoundManager;
import hicks.td.entities.*;
import hicks.td.entities.Projectile;
import hicks.td.ui.DisplayInfo;
import hicks.td.util.*;

import javax.swing.*;
import java.awt.*;
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

        World.setImageDir("ass" + File.separator + "img" + File.separator);
        World.setGameMap(new GameMap(768, 576));
        World.setLogicalMap(MapBuilder.buildRandomMap(18, 24));

        if (loadResources)
            loadResources();

        World.setPlayer(new Player(300, 1, 0));
        World.setWaves(Wave.getWaves(10));
        World.setTerrainImage(MapBuilder.createImageFromLogicalMap(World.getLogicalMap()));
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

        World.setTowerTypes(DataFileLoader.getTowerTypes());
        World.setProjectileTypes(DataFileLoader.getProjectileTypes());
        World.setOutfitTypes(DataFileLoader.getOutfitTypes());
        World.setMobTypes(DataFileLoader.getMobTypes());

        World.setGameImages(getGameImages());
    }

    private static List<GameImage> getGameImages()
    {
        String imageDir = World.getImageDir();

        List<GameImage> gameImages = new ArrayList<>();

        for (Tower towerType : World.getTowerTypes())
        {
            String path = imageDir + towerType.getImageFile();
            Image image = new ImageIcon(path).getImage();
            gameImages.add(new GameImage(path, image));
        }

        for (Projectile projectileType : World.getProjectileTypes())
        {
            String path = imageDir + projectileType.getImageFile();
            Image image = new ImageIcon(path).getImage();
            gameImages.add(new GameImage(path, image));
        }

        return gameImages;
    }

    private static void deleteLogs()
    {
        boolean deleteSuccess = new File("log.txt").delete();
        Log.info("Clearing logs..." + (deleteSuccess ? "done." : "none found."));
    }
}