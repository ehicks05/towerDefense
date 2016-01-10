package hicks.td;

import hicks.td.audio.SoundManager;
import hicks.td.entities.*;
import hicks.td.entities.Projectile;
import hicks.td.ui.DisplayInfo;
import hicks.td.util.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class Init
{
    public static void init(boolean loadResources)
    {
        World.setStartTime(Util.now());
        Log.deleteLogs();
        Log.info("Initializing...");

        World.setImageDir("ass" + File.separator + "img" + File.separator);
        World.setGameMap(new GameMap(768, 576));
        World.getGameMap().setLogicalMap(MapBuilder.buildRandomMap(18, 24));

        if (loadResources)
            loadResources();

        World.setPlayer(new Player(300, 1, 0));
        World.setWaves(Wave.getWaves(10));
        GameMap.setTerrainImage(MapBuilder.createImageFromLogicalMap(World.getGameMap().getLogicalMap()));
        World.setUnits(new ArrayList<>());
    }

    private static void loadResources()
    {
        DisplayInfo.setDisplayProperties();
        SoundManager.init();
        MobTileLoader.init();

        List<Upgrade> allUpgrades = new ArrayList<>();
        allUpgrades.add(new Upgrade("AR", "Attack Range", "", 50));
        allUpgrades.add(new Upgrade("AD", "Attack Damage", "", 50));
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
}