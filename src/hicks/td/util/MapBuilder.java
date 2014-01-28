package hicks.td.util;

import hicks.td.GameState;

import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Random;

public final class MapBuilder
{
    public static BufferedImage buildMap()
    {
        Map<String, BufferedImage> tiles = TileLoader.createTileList();

        BufferedImage terrain = new BufferedImage(GameState.getGameMap().getWidth(), GameState.getGameMap().getHeight(), BufferedImage.TYPE_INT_RGB);

        int iterations = (GameState.getGameMap().getWidth() / 32) * (GameState.getGameMap().getHeight() / 32);
        int x = 0;
        int y = 0;

        int[] grassRGBArray = tiles.get("GGGG").getRGB(0, 0, 32, 32, null, 0, 32);
        int[] roadRGBArray  = tiles.get("DDDD").getRGB(0, 0, 32, 32, null, 0, 32);

        // fill map with grass
        for (int i = 0; i < iterations; i++)
        {
            if (x >= GameState.getGameMap().getWidth())
            {
                x = 0;
                y += 32;
            }

            terrain.setRGB(x, y, 32, 32, grassRGBArray, 0, 32);

            x += 32;
        }

        // add a path
        int tileColumns = GameState.getGameMap().getWidth() / 32;
        int tileRows = GameState.getGameMap().getHeight() / 32;
        Random gen = new Random();

        int randomColumn = gen.nextInt(tileColumns / 2) + tileColumns / 4; // keep road in the middle area of the map...
        TileLoader.roadOffset = randomColumn * 32;

        x = 0;
        y = 0;
        for (int i = 0; i < iterations; i++)
        {
            if (x >= GameState.getGameMap().getWidth())
            {
                x = 0;
                y += 32;
                if (y == GameState.getGameMap().getHeight()) break;
            }

            if (x == (randomColumn - 1) * 32)
                terrain.setRGB(x, y, 32, 32, roadRGBArray, 0, 32);

            x += 32;
        }

        return terrain;
    }
}
