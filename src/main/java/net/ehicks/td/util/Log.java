package net.ehicks.td.util;

import net.ehicks.td.World;

import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class Log
{
    private static long lines;
    private static String messageQueue = "";
    private static final DecimalFormat TIME_FORMAT = new DecimalFormat("000.00");
    private static final DecimalFormat LINE_NUMBER_FORMAT = new DecimalFormat("000");
    private static final String LOG_FILE = "log.txt";

    public static void info(String message)
    {
        info(message, false);
    }

    public static void info(String message, boolean forceFlush)
    {
        lines++;
        String elapsedString = "000.00";

        BigDecimal simulationStart = World.getStartTime();
        if (simulationStart != null)
        {
            double elapsedTime = Util.getElapsedTime(simulationStart).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            elapsedString = TIME_FORMAT.format(elapsedTime);
        }

        String timeStamp = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss.SSS").format(new Date());

        message = LINE_NUMBER_FORMAT.format(lines) + "  " + timeStamp + " (" + elapsedString + " sec)" + ":  " + message + "\r\n";

        messageQueue += message;

        if (lines % 1000 == 0 || forceFlush)
        {
            try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(LOG_FILE, true))))
            {
                writer.print(messageQueue);
                messageQueue = "";
            }
            catch (IOException e)
            {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void deleteLogs()
    {
        boolean deleteSuccess = new File(LOG_FILE).delete();
        info("Clearing previous logs..." + (deleteSuccess ? "done." : "none found."));
    }
}
