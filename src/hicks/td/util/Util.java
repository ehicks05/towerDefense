package hicks.td.util;

import hicks.td.World;
import hicks.td.entities.Mob;
import hicks.td.entities.Tower;
import hicks.td.entities.Unit;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public final class Util
{
    public static String imageDir;

    // TIME
    public static BigDecimal now()
    {
        return new BigDecimal(System.currentTimeMillis());
    }

    public static BigDecimal getElapsedTime(BigDecimal startTime)
    {
        return getElapsedTime(startTime, Util.now());
    }

    public static BigDecimal getElapsedTime(BigDecimal startTime, BigDecimal endTime)
    {
        return endTime.subtract(startTime).divide(new BigDecimal("1000"));
    }

    public static BigDecimal getElapsedTimeRaw(BigDecimal startTime, BigDecimal endTime)
    {
        return endTime.subtract(startTime);
    }

    public static void adjustStartTime(BigDecimal offset)
    {
        World.setStartTime(World.getStartTime().add(offset));
    }

    // UNIT GETTERS
    public static List<Tower> getTowers()
    {
        List<Tower> towers = new ArrayList<>();

        for (Unit unit : new ArrayList<>(World.getUnits()))
            if (unit instanceof Tower) towers.add((Tower) unit);

        return towers;
    }

    public static List<Mob> getMobs()
    {
        List<Mob> mobs = new ArrayList<>();

        for (Unit unit : new ArrayList<>(World.getUnits()))
            if (unit instanceof Mob) mobs.add((Mob) unit);

        return mobs;
    }

    // FILE SYSTEM
    public static BufferedImage loadBufferedImage(String name)
    {
        try
        {
            return ImageIO.read(new File(getImageDir() + name));
        }
        catch (IOException e)
        {
            Log.info(e.getMessage());
        }

        return null;
    }

    public static List<File> getFiles(String path)
    {
        File rootDirectory = new File(path);

        List<File> files = new ArrayList<>();
        File[] possibleFiles = rootDirectory.listFiles();

        for (File file : possibleFiles)
            if (file.isFile())
                files.add(file);

        return files;
    }

    static List<String> readAllLines(String path, boolean removeHeader)
    {
        List<String> lines = new ArrayList<>();
        try
        {
            lines = Files.readAllLines(Paths.get(path), Charset.defaultCharset());
            if (removeHeader) lines.remove(0);
        }
        catch (IOException e)
        {
            Log.info(e.getMessage(), true);
        }
        return lines;
    }

    public static String getImageDir()
    {
        return imageDir;
    }

    public static void setImageDir(String imageDir)
    {
        Util.imageDir = imageDir;
    }
}
