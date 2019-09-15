package net.ehicks.td;

import net.ehicks.td.audio.SoundManager;
import net.ehicks.td.entities.*;
import net.ehicks.td.entities.Projectile;
import net.ehicks.td.ui.DisplayInfo;
import net.ehicks.td.util.*;

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

        Util.setImageDir("img" + File.separator);
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
        String imageDir = Util.getImageDir();

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