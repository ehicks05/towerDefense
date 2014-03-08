package hicks.td.util;

import hicks.td.World;

import java.awt.image.BufferedImage;
import java.util.Map;

public final class MapBuilder
{
    public static BufferedImage buildMap()
    {
        Map<String, BufferedImage> tiles = TileLoader.createTileList();
        int[] grassRGB = tiles.get("GGGG").getRGB(0, 0, 32, 32, null, 0, 32);
        int[] roadRGB  = tiles.get("DDDD").getRGB(0, 0, 32, 32, null, 0, 32);

        BufferedImage terrain = new BufferedImage(World.getGameMap().getWidth(), World.getGameMap().getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int row = 0; row < 24; row++)
        {
            for (int column = 0; column < 32; column++)
            {
                if (row < 2 || row > 21 || column < 2 || column > 29)
                    terrain.setRGB(column * 32, row * 32, 32, 32, roadRGB, 0, 32);
                else
                    terrain.setRGB(column * 32, row * 32, 32, 32, grassRGB, 0, 32);
            }
        }

        return terrain;
    }
}
