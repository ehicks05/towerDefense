package hicks.td.util;

import hicks.td.World;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MobTileLoader
{
    private static final int TILE_SIZE = 64;
    private static final Map<Integer, String> directionMap = getDirectionMap();

    private static Map<String, Map<String, List<BufferedImage>>> bodyParts = new HashMap<>();

    public static void init()
    {
        List<File> imageFiles = Util.getFiles(World.getImageDir() + File.separator + "mob");

        for (File file : imageFiles)
        {
            String fileName = file.getName();
            Map<String, List<BufferedImage>> imagesForThisBodyPart = createDirectionalTileMap(fileName);

            String name = fileName.substring(0, fileName.lastIndexOf(".")).toUpperCase();
            bodyParts.put(name, imagesForThisBodyPart);
        }
    }

    public static BufferedImage getTile(BodyPart type, String direction, int frame)
    {
        Map<String, List<BufferedImage>> tileMap = bodyParts.get(type.toString());

        if (direction == null || direction.equals("")) direction = "right";
        List<BufferedImage> images = tileMap.get(direction);

        if (frame == images.size())
            frame = images.size() - 1;

        return images.get(frame);
    }

    private static Map<String, List<BufferedImage>> createDirectionalTileMap(String filename)
    {
        Map<String, List<BufferedImage>> imagesForThisBodyPart = new HashMap<>();

        BufferedImage tileFile = Util.loadBufferedImage("mob\\" + filename);

        for (int row = 0; row < 4; row++)
        {
            for (int col = 0; col < 9; col++)
            {
                BufferedImage image = tileFile.getSubimage(TILE_SIZE * col, TILE_SIZE * row, TILE_SIZE, TILE_SIZE);

                List<BufferedImage> images = imagesForThisBodyPart.get(directionMap.get(row));
                if (images == null)
                    imagesForThisBodyPart.put(directionMap.get(row), new ArrayList<BufferedImage>());

                imagesForThisBodyPart.get(directionMap.get(row)).add(image);
            }
        }

        return imagesForThisBodyPart;
    }

    private static Map<Integer, String> getDirectionMap()
    {
        Map<Integer, String> directionMap = new HashMap<>();
        directionMap.put(0, "up");
        directionMap.put(1, "left");
        directionMap.put(2, "down");
        directionMap.put(3, "right");
        return directionMap;
    }
}