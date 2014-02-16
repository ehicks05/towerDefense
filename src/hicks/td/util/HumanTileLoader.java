package hicks.td.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HumanTileLoader
{
    private static Map<String, List<BufferedImage>> body = new HashMap<>();
    private static Map<String, List<BufferedImage>> torso = new HashMap<>();
    private static Map<String, List<BufferedImage>> head = new HashMap<>();
    private static Map<String, List<BufferedImage>> belt = new HashMap<>();
    private static Map<String, List<BufferedImage>> back = new HashMap<>();
    private static Map<String, List<BufferedImage>> feet = new HashMap<>();
    private static Map<String, List<BufferedImage>> hands = new HashMap<>();
    private static Map<String, List<BufferedImage>> legs = new HashMap<>();

    private static final int TILE_SIZE = 64;
    private static Map<Integer, String> directionMap = new HashMap<>();

    private static BufferedImage loadTileFile(String filename)
    {
        try
        {
            return ImageIO.read(new File("ass\\img\\" + filename));
        }
        catch (IOException e)
        {
            Log.info(e.getMessage());
        }

        return null;
    }

    public static void init()
    {
        body.put("body", null);
        torso.put("torso", null);
        head.put("head", null);

        directionMap = new HashMap<>();
        directionMap.put(0, "up");
        directionMap.put(1, "left");
        directionMap.put(2, "down");
        directionMap.put(3, "right");

        List<Map<String, List<BufferedImage>>> tileSets = new ArrayList<>();
        tileSets.add(body);
        tileSets.add(torso);
        tileSets.add(head);

        for (String direction : directionMap.values())
            for (Map<String, List<BufferedImage>> tileSet : tileSets)
                tileSet.put(direction, new ArrayList<BufferedImage>());

        Map<Map<String, List<BufferedImage>>, String> tileSetFilenames = new HashMap<>();
        tileSetFilenames.put(body, "maleBody.png");
        tileSetFilenames.put(torso, "torso.png");
        tileSetFilenames.put(head, "headPlate.png");

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

    public static BufferedImage getTile(int i, String direction, String type)
    {
        Map<String, List<BufferedImage>> tileMap = null;
        if (type.equals("body"))
            tileMap = body;
        if (type.equals("torso"))
            tileMap = torso;
        if (type.equals("head"))
            tileMap = head;

        List<BufferedImage> images = tileMap.get(direction);

        if (i > 8)
            i = 8;

        return images.get(i);
    }
}