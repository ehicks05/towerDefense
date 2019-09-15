package net.ehicks.td.util;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapTileLoader
{
    private static List<String> tileNames = Arrays.asList(
            "CCCC", "DDDD", "GGGG", "WWWW", "XXX1", "XXX2", "XXX3", "XXX4", "GGDD", "GDDD",
            "RRR1", "RRGG", "GGRR", "RRR2", "GRGR", "RGRG", "OIL1", "DDGG", "DGDG", "DDDG",
            "CGCC", "GCCC", "CCCG", "CCGC", "GGCC", "CCGG", "CGCG", "GCGC", "DGDD", "DDGD",
            "WGWG", "GWGW", "WWGG", "GGWW", "WGWW", "GWWW", "WWWG", "WWGW", "GDGD", "NULL");

    public static Map<String, BufferedImage> createTileList()
    {
        Map<String, BufferedImage> tiles = new HashMap<>();

        BufferedImage tileSet = Util.loadBufferedImage("tiles.bmp");

        int x = 0;
        int y = 0;

        for (int i = 0; i < 40; i++)
        {
            if (i > 0 && i % 10 == 0)
            {
                x = 0;
                y += 33;
            }

            BufferedImage image = tileSet.getSubimage(x, y, 32, 32);
            tiles.put(tileNames.get(i), image);
            x += 33;
        }

        return tiles;
    }
}