package net.ehicks.td.util;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ExplosionTileLoader
{
    private static List<BufferedImage> tiles = new ArrayList<>();

    public static BufferedImage getTile(int i)
    {
        if (tiles.isEmpty())
            createTileList(100, 100);

        if (i >= tiles.size())
            i = tiles.size() - 1;

        return tiles.get(i);
    }

    private static void createTileList(int width, int height)
    {
        BufferedImage tileSet = Util.loadBufferedImage("explosion.png");

        for (int i = 0; i < 9; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                BufferedImage image = tileSet.getSubimage(width * j, height * i, width, height);
                tiles.add(image);
            }
        }
    }
}