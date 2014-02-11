package hicks.td.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExplosionTileLoader
{
    private static List<BufferedImage> tiles;

    private static BufferedImage loadTileSet()
    {
        try
        {
            return ImageIO.read(new File("ass\\img\\explosion.png"));
        }
        catch (IOException e)
        {
            Log.info(e.getMessage());
        }

        return null;
    }

    public static void createTileList()
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

    public static BufferedImage getTile(int i)
    {
        if (tiles == null)
            createTileList();

        if (i > 80)
            i = 80;

        return tiles.get(i);
    }
}