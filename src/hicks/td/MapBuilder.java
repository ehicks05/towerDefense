package hicks.td;

import java.awt.image.BufferedImage;
import java.util.Random;

public final class MapBuilder
{
    public static BufferedImage buildMap()
    {
        TileLoader.createTileList();

        BufferedImage terrain = new BufferedImage(Init.WORLD_WIDTH, Init.WORLD_HEIGHT, BufferedImage.TYPE_INT_RGB);

        int iterations = (Init.WORLD_WIDTH / 32) * (Init.WORLD_HEIGHT / 32);
        int x = 0;
        int y = 0;
        int[] rgbArray = new int[32 * 32];

        // fill map with grass
        for (int i = 0; i < iterations; i++)
        {
            if (x >= Init.WORLD_WIDTH)
            {
                x = 0;
                y += 32;
            }

            BufferedImage tile = TileLoader.tiles.get("GGGG");

            rgbArray = tile.getRGB(0, 0, 32, 32, rgbArray, 0, 32);
            terrain.setRGB(x, y, 32, 32, rgbArray, 0, 32);

            x += 32;
        }

        // add a path
        int tileColumns = Init.WORLD_WIDTH / 32;
        int tileRows = Init.WORLD_HEIGHT / 32;
        Random gen = new Random();

        int randomColumn = gen.nextInt(tileColumns / 2) + tileColumns / 4; // keep road in the middle area of the map...
        TileLoader.roadOffset = randomColumn * 32;

        x = 0;
        y = 0;
        for (int i = 0; i < iterations; i++)
        {
            if (x >= Init.WORLD_WIDTH)
            {
                x = 0;
                y += 32;
                if (y == Init.WORLD_HEIGHT) break;
            }

            if (x == (randomColumn - 1) * 32)
            {
                BufferedImage tile = TileLoader.tiles.get("DDDD");

                rgbArray = tile.getRGB(0, 0, 32, 32, rgbArray, 0, 32);
                terrain.setRGB(x, y, 32, 32, rgbArray, 0, 32);
            }

            x += 32;
        }

        return terrain;
    }
}
