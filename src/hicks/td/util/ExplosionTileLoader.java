package hicks.td.util;

import hicks.td.World;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExplosionTileLoader
{
    private static List<BufferedImage> tiles;

    public static BufferedImage getTile(int i)
    {
        if (tiles == null)
            createTileList();

        if (i >= tiles.size())
            i = tiles.size() - 1;

        return tiles.get(i);
    }

    private static BufferedImage loadTileSet()
    {
        try
        {
            return ImageIO.read(new File(World.getImageDir() + "explosion.png"));
        }
        catch (IOException e)
        {
            Log.info(e.getMessage());
        }

        return null;
    }

    private static void createTileList()
    {
        tiles = new ArrayList<>();

        BufferedImage tileSet = loadTileSet();

        for (int i = 0; i < 9; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                BufferedImage image = tileSet.getSubimage(100 * j, 100 * i, 100, 100);
                tiles.add(image);
            }
        }
    }
}