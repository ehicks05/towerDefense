package hicks.combat;

import hicks.combat.entities.Barracks;
import hicks.combat.entities.Knight;
import hicks.combat.entities.Unit;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class NameLogic
{
    private static List<String> firstNames;
    private static List<String> lastNames;

    public static void init()
    {
        try
        {
            firstNames = Files.readAllLines(Paths.get("firstNames.txt"), Charset.defaultCharset());
            lastNames = Files.readAllLines(Paths.get("lastNames.txt"), Charset.defaultCharset());
        }
        catch (IOException e)
        {
            Log.logInfo("Error reading name files...");
        }
    }

    public static String generateName(Unit unit)
    {
        Random random = new Random();

        String fullName = firstNames.get(random.nextInt(firstNames.size())) + " " + lastNames.get(random.nextInt(lastNames.size()));
        if (unit instanceof Knight) fullName = "Sir " + fullName;
        if (unit instanceof Barracks) fullName = "Barracks";
        return fullName;
    }
}
