package hicks.td.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MobTileLoader
{
    private static Map<String, List<BufferedImage>> backQuiver 		        = new HashMap<>();
    private static Map<String, List<BufferedImage>> beltLeather 	        = new HashMap<>();
    private static Map<String, List<BufferedImage>> beltRope 		        = new HashMap<>();
    private static Map<String, List<BufferedImage>> bodyHuman 		        = new HashMap<>();
    private static Map<String, List<BufferedImage>> bodySkeleton 	        = new HashMap<>();
    private static Map<String, List<BufferedImage>> feetPlate 		        = new HashMap<>();
    private static Map<String, List<BufferedImage>> feetShoes 		        = new HashMap<>();
    private static Map<String, List<BufferedImage>> handsPlate 		        = new HashMap<>();
    private static Map<String, List<BufferedImage>> headChainHelmet         = new HashMap<>();
    private static Map<String, List<BufferedImage>> headChainHood 	        = new HashMap<>();
    private static Map<String, List<BufferedImage>> headHair 		        = new HashMap<>();
    private static Map<String, List<BufferedImage>> headLeatherHat 	        = new HashMap<>();
    private static Map<String, List<BufferedImage>> headPlate 		        = new HashMap<>();
    private static Map<String, List<BufferedImage>> headRobe 		        = new HashMap<>();
    private static Map<String, List<BufferedImage>> legsPants 		        = new HashMap<>();
    private static Map<String, List<BufferedImage>> legsPlate 		        = new HashMap<>();
    private static Map<String, List<BufferedImage>> legsRobe            	= new HashMap<>();
    private static Map<String, List<BufferedImage>> torsoChainJacket        = new HashMap<>();
    private static Map<String, List<BufferedImage>> torsoChainArmor         = new HashMap<>();
    private static Map<String, List<BufferedImage>> torsoLeatherBracers     = new HashMap<>();
    private static Map<String, List<BufferedImage>> torsoLeatherShirt       = new HashMap<>();
    private static Map<String, List<BufferedImage>> torsoLeatherShoulders   = new HashMap<>();
    private static Map<String, List<BufferedImage>> torsoLeatherArmor       = new HashMap<>();
    private static Map<String, List<BufferedImage>> torsoPlateShoulders     = new HashMap<>();
    private static Map<String, List<BufferedImage>> torsoPlate              = new HashMap<>();
    private static Map<String, List<BufferedImage>> torsoRobe               = new HashMap<>();

    private static final int TILE_SIZE = 64;
    private static Map<Integer, String> directionMap = new HashMap<>();

    private static BufferedImage loadTileFile(String filename)
    {
        try
        {
            return ImageIO.read(new File("ass\\img\\mob\\" + filename));
        }
        catch (IOException e)
        {
            Log.info(e.getMessage());
        }

        return null;
    }

    public static void init()
    {
        // put fake entries in the map so the maps aren't treated as being the same object

        backQuiver.put("backQuiver", null);
        beltLeather.put("beltLeather", null);
        beltRope.put("beltRope", null);
        bodyHuman.put("bodyHuman", null);
        bodySkeleton.put("bodySkeleton", null);
        feetPlate.put("feetPlate", null);
        feetShoes.put("feetShoes", null);
        handsPlate.put("handsPlate", null);
        headChainHelmet.put("headChainHelmet", null);
        headChainHood.put("headChainHood", null);
        headHair.put("headHair", null);
        headLeatherHat.put("headLeatherHat", null);
        headPlate.put("headPlate", null);
        headRobe.put("headRobe", null);
        legsPants.put("legsPants", null);
        legsPlate.put("legsPlate", null);
        legsRobe.put("legsRobe", null);
        torsoChainJacket.put("torsoChainJacket", null);
        torsoChainArmor.put("torsoChainArmor", null);
        torsoLeatherBracers.put("torsoLeatherBracers", null);
        torsoLeatherShirt.put("torsoLeatherShirt", null);
        torsoLeatherShoulders.put("torsoLeatherShoulders", null);
        torsoLeatherArmor.put("torsoLeatherArmor", null);
        torsoPlateShoulders.put("torsoPlateShoulders", null);
        torsoPlate.put("torsoPlate", null);
        torsoRobe.put("torsoRobe", null);

        directionMap = new HashMap<>();
        directionMap.put(0, "up");
        directionMap.put(1, "left");
        directionMap.put(2, "down");
        directionMap.put(3, "right");

        List<Map<String, List<BufferedImage>>> tileSets = new ArrayList<>();

        tileSets.add(backQuiver);
        tileSets.add(beltLeather);
        tileSets.add(beltRope);
        tileSets.add(bodyHuman);
        tileSets.add(bodySkeleton);
        tileSets.add(feetPlate);
        tileSets.add(feetShoes);
        tileSets.add(handsPlate);
        tileSets.add(headChainHelmet);
        tileSets.add(headChainHood);
        tileSets.add(headHair);
        tileSets.add(headLeatherHat);
        tileSets.add(headPlate);
        tileSets.add(headRobe);
        tileSets.add(legsPants);
        tileSets.add(legsPlate);
        tileSets.add(legsRobe);
        tileSets.add(torsoChainJacket);
        tileSets.add(torsoChainArmor);
        tileSets.add(torsoLeatherBracers);
        tileSets.add(torsoLeatherShirt);
        tileSets.add(torsoLeatherShoulders);
        tileSets.add(torsoLeatherArmor);
        tileSets.add(torsoPlateShoulders);
        tileSets.add(torsoPlate);
        tileSets.add(torsoRobe);

        for (String direction : directionMap.values())
            for (Map<String, List<BufferedImage>> tileSet : tileSets)
                tileSet.put(direction, new ArrayList<BufferedImage>());

        Map<Map<String, List<BufferedImage>>, String> tileSetFilenames = new HashMap<>();
        tileSetFilenames.put(backQuiver,            "BACK_quiver.png");
        tileSetFilenames.put(beltLeather,           "BELT_leather.png");
        tileSetFilenames.put(beltRope,              "BELT_rope.png");
        tileSetFilenames.put(bodyHuman,             "BODY_human.png");
        tileSetFilenames.put(bodySkeleton,          "BODY_skeleton.png");
        tileSetFilenames.put(feetPlate,             "FEET_plate_armor_shoes.png");
        tileSetFilenames.put(feetShoes,             "FEET_shoes_brown.png");
        tileSetFilenames.put(handsPlate,            "HANDS_plate_armor_gloves.png");
        tileSetFilenames.put(headChainHelmet,       "HEAD_chain_armor_helmet.png");
        tileSetFilenames.put(headChainHood,         "HEAD_chain_armor_hood.png");
        tileSetFilenames.put(headHair,              "HEAD_hair_blonde.png");
        tileSetFilenames.put(headLeatherHat,        "HEAD_leather_armor_hat.png");
        tileSetFilenames.put(headPlate,             "HEAD_plate_armor_helmet.png");
        tileSetFilenames.put(headRobe,              "HEAD_robe_hood.png");
        tileSetFilenames.put(legsPants,             "LEGS_pants_greenish.png");
        tileSetFilenames.put(legsPlate,             "LEGS_plate_armor_pants.png");
        tileSetFilenames.put(legsRobe,              "LEGS_robe_skirt.png");
        tileSetFilenames.put(torsoChainJacket,      "TORSO_chain_armor_jacket_purple.png");
        tileSetFilenames.put(torsoChainArmor,       "TORSO_chain_armor_torso.png");
        tileSetFilenames.put(torsoLeatherBracers,   "TORSO_leather_armor_bracers.png");
        tileSetFilenames.put(torsoLeatherShirt,     "TORSO_leather_armor_shirt_white.png");
        tileSetFilenames.put(torsoLeatherShoulders, "TORSO_leather_armor_shoulders.png");
        tileSetFilenames.put(torsoLeatherArmor,     "TORSO_leather_armor_torso.png");
        tileSetFilenames.put(torsoPlateShoulders,   "TORSO_plate_armor_arms_shoulders.png");
        tileSetFilenames.put(torsoPlate,            "TORSO_plate_armor_torso.png");
        tileSetFilenames.put(torsoRobe,             "TORSO_robe_shirt_brown.png");

        for (Map<String, List<BufferedImage>> tileSet : tileSets)
            createTileMap(tileSet, tileSetFilenames.get(tileSet));
    }

    public static void createTileMap(Map<String, List<BufferedImage>> tileSet, String filename)
    {
        BufferedImage tileFile = loadTileFile(filename);

        for (int row = 0; row < 4; row++)
        {
            for (int col = 0; col < 9; col++)
            {
                BufferedImage image = tileFile.getSubimage(TILE_SIZE * col, TILE_SIZE * row, TILE_SIZE, TILE_SIZE);
                tileSet.get(directionMap.get(row)).add(image);
            }
        }
    }

    public static BufferedImage getTile(int frame, String direction, MobBodyPart type)
    {
        Map<String, List<BufferedImage>> tileMap = null;
        if (type.equals(MobBodyPart.BACK_QUIVER))             tileMap = backQuiver;
        if (type.equals(MobBodyPart.BELT_LEATHER))            tileMap = beltLeather;
        if (type.equals(MobBodyPart.BELT_ROPE))               tileMap = beltRope;
        if (type.equals(MobBodyPart.BODY_HUMAN))              tileMap = bodyHuman;
        if (type.equals(MobBodyPart.BODY_SKELETON))           tileMap = bodySkeleton;
        if (type.equals(MobBodyPart.FEET_PLATE))              tileMap = feetPlate;
        if (type.equals(MobBodyPart.FEET_SHOES))              tileMap = feetShoes;
        if (type.equals(MobBodyPart.HANDS_PLATE))             tileMap = handsPlate;
        if (type.equals(MobBodyPart.HEAD_CHAIN_HELMET))       tileMap = headChainHelmet;
        if (type.equals(MobBodyPart.HEAD_CHAIN_HOOD))         tileMap = headChainHood;
        if (type.equals(MobBodyPart.HEAD_HAIR))               tileMap = headHair;
        if (type.equals(MobBodyPart.HEAD_LEATHER_HAT))        tileMap = headLeatherHat;
        if (type.equals(MobBodyPart.HEAD_PLATE))              tileMap = headPlate;
        if (type.equals(MobBodyPart.HEAD_ROBE))               tileMap = headRobe;
        if (type.equals(MobBodyPart.LEGS_PANTS))              tileMap = legsPants;
        if (type.equals(MobBodyPart.LEGS_PLATE))              tileMap = legsPlate;
        if (type.equals(MobBodyPart.LEGS_ROBE))               tileMap = legsRobe;
        if (type.equals(MobBodyPart.TORSO_CHAIN_JACKET))      tileMap = torsoChainJacket;
        if (type.equals(MobBodyPart.TORSO_CHAIN_ARMOR))       tileMap = torsoChainArmor;
        if (type.equals(MobBodyPart.TORSO_LEATHER_BRACERS))   tileMap = torsoLeatherBracers;
        if (type.equals(MobBodyPart.TORSO_LEATHER_SHIRT))     tileMap = torsoLeatherShirt;
        if (type.equals(MobBodyPart.TORSO_LEATHER_SHOULDERS)) tileMap = torsoLeatherShoulders;
        if (type.equals(MobBodyPart.TORSO_LEATHER_ARMOR))     tileMap = torsoLeatherArmor;
        if (type.equals(MobBodyPart.TORSO_PLATE_SHOULDERS))   tileMap = torsoPlateShoulders;
        if (type.equals(MobBodyPart.TORSO_PLATE))             tileMap = torsoPlate;
        if (type.equals(MobBodyPart.TORSO_ROBE))              tileMap = torsoRobe;

        List<BufferedImage> images = tileMap.get(direction);

        if (frame > 8)
            frame = 8;

        return images.get(frame);
    }
}