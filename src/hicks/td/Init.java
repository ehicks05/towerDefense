package hicks.td;

import hicks.td.audio.SoundManager;
import hicks.td.entities.GameMap;
import hicks.td.entities.Player;
import hicks.td.entities.Wave;
import hicks.td.entities.mob.Mob;
import hicks.td.ui.DisplayInfo;
import hicks.td.util.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public final class Init
{
    public static void init()
    {
        World.setStartTime(Util.now());
        Log.info("Initializing " + new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a").format(World.getStartTime()));

        deleteLogs();

        DisplayInfo.setDisplayProperties();
        SoundManager.init();
        MobTileLoader.init();

        World.setPlayer(new Player(300, 20, 0));
        World.setGameMap(new GameMap(1024, 768));
        World.setTerrainImage(MapBuilder.buildMap());
        World.setWaves(getWaves());
    }

    private static List<Wave> getWaves()
    {
        List<Wave> waves = new ArrayList<>();
        waves.add(new Wave(1, 10));
        waves.add(new Wave(2, 20));
        waves.add(new Wave(3, 30));
        waves.add(new Wave(4, 40));
        waves.add(new Wave(5, 50));
        waves.add(new Wave(6, 60));
        return waves;
    }

    public static List<Mob> getOneOfEachMobType()
    {
        List<Mob> mobsOfEachType = new ArrayList<>();
        List<MobBodyPartCollection> bodyPartCollections = getBodyPartCollections();

        mobsOfEachType.add(new Mob(2, 32, 50, "peasant",   1, 1, 50, 50, 2,  1, 0, Util.now(), bodyPartCollections.get(0)));
        mobsOfEachType.add(new Mob(2, 32, 55, "archer",    2, 2, 50, 50, 4,  2, 0, Util.now(), bodyPartCollections.get(1)));
        mobsOfEachType.add(new Mob(2, 32, 60, "footman",   3, 3, 60, 60, 8,  3, 0, Util.now(), bodyPartCollections.get(2)));
        mobsOfEachType.add(new Mob(2, 32, 65, "barbarian", 4, 4, 60, 60, 8,  4, 0, Util.now(), bodyPartCollections.get(3)));
        mobsOfEachType.add(new Mob(2, 32, 70, "knight",    5, 5, 70, 70, 12, 5, 0, Util.now(), bodyPartCollections.get(4)));

        return mobsOfEachType;
    }

    public static List<MobBodyPartCollection> getBodyPartCollections()
    {
        List<MobBodyPartCollection> bodyPartCollections = new ArrayList<>();
        bodyPartCollections.add(new MobBodyPartCollection(MobBodyPart.BODY_HUMAN, null, null, null, null, null, MobBodyPart.LEGS_ROBE, MobBodyPart.TORSO_ROBE, null, null, null));
        bodyPartCollections.add(new MobBodyPartCollection(MobBodyPart.BODY_HUMAN, null, MobBodyPart.BELT_LEATHER, MobBodyPart.FEET_SHOES, null, MobBodyPart.HEAD_HAIR, MobBodyPart.LEGS_PANTS, MobBodyPart.TORSO_LEATHER_SHIRT, MobBodyPart.TORSO_LEATHER_ARMOR, MobBodyPart.TORSO_LEATHER_SHOULDERS, MobBodyPart.TORSO_LEATHER_BRACERS));
        bodyPartCollections.add(new MobBodyPartCollection(MobBodyPart.BODY_HUMAN, null, MobBodyPart.BELT_LEATHER, MobBodyPart.FEET_SHOES, MobBodyPart.HANDS_PLATE, MobBodyPart.HEAD_CHAIN_HOOD, MobBodyPart.LEGS_PANTS, MobBodyPart.TORSO_LEATHER_SHIRT, MobBodyPart.TORSO_LEATHER_ARMOR, MobBodyPart.TORSO_LEATHER_SHOULDERS, MobBodyPart.TORSO_LEATHER_BRACERS));
        bodyPartCollections.add(new MobBodyPartCollection(MobBodyPart.BODY_HUMAN, null, null, MobBodyPart.FEET_PLATE, MobBodyPart.HANDS_PLATE, MobBodyPart.HEAD_CHAIN_HOOD, MobBodyPart.LEGS_PANTS, MobBodyPart.TORSO_CHAIN_ARMOR, MobBodyPart.TORSO_CHAIN_JACKET, null, null));
        bodyPartCollections.add(new MobBodyPartCollection(MobBodyPart.BODY_HUMAN, null, null, MobBodyPart.FEET_PLATE, MobBodyPart.HANDS_PLATE, MobBodyPart.HEAD_PLATE, MobBodyPart.LEGS_PLATE, MobBodyPart.TORSO_PLATE, null, MobBodyPart.TORSO_PLATE_SHOULDERS, null));
        bodyPartCollections.add(new MobBodyPartCollection(MobBodyPart.BODY_SKELETON, null, null, null, null, null, MobBodyPart.LEGS_ROBE, MobBodyPart.TORSO_ROBE, null, null, null));
        return bodyPartCollections;
    }

    private static void deleteLogs()
    {
        boolean deleteSuccess = new File("log.txt").delete();
        Log.info("Clearing logs..." + (deleteSuccess ? "done." : "log not found."));
    }
}