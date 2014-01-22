package hicks.td.util;

import hicks.td.GameState;
import hicks.td.entities.Tile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class TileLoader
{
    public static int roadOffset;
    static Map<String, BufferedImage> tiles = new HashMap<String, BufferedImage>();
    static List<String> tileNames = Arrays.asList(
            "CCCC", "DDDD", "GGGG", "WWWW", "XXX1", "XXX2", "XXX3", "XXX4", "GGDD", "GDDD",
            "RRR1", "RRGG", "GGRR", "RRR2", "GRGR", "RGRG", "OIL1", "DDGG", "DGDG", "DDDG",
            "CGCC", "GCCC", "CCCG", "CCGC", "GGCC", "CCGG", "CGCG", "GCGC", "DGDD", "DDGD",
            "WGWG", "GWGW", "WWGG", "GGWW", "WGWW", "GWWW", "WWWG", "WWGW", "GDGD", "NULL");

    static BufferedImage loadTileSet()
    {
        try
        {
            return ImageIO.read(new File("ass\\tiles.bmp"));
        }
        catch (IOException e)
        {
            Log.info(e.getMessage());
        }

        return null;
    }

    static void createTileList()
    {
        BufferedImage tileSet = loadTileSet();

        BufferedImage image;
        int x = 0;
        int y = 0;

        for (int i = 0; i < 40; i++)
        {
            if (i > 0 && i % 10 == 0)
            {
                x = 0;
                y += 33;
            }

            image = tileSet.getSubimage(x, y, 32, 32);
            tiles.put(tileNames.get(i), image);
            GameState.addTileToTiles(new Tile(tileNames.get(i), 32, 32, image));
            x += 33;

        }
    }

}