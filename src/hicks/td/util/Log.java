package hicks.td.util;

import hicks.td.World;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class Log
{
    private static long lines;
    private static String messageQueue = "";
    private static final DecimalFormat TIME_FORMAT = new DecimalFormat("000.00");
    private static final DecimalFormat LINE_FORMAT = new DecimalFormat("000");

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

        message = LINE_FORMAT.format(lines) + "  " + timeStamp + " (" + elapsedString + " sec)" + ":  " + message + "\r\n";

        messageQueue += message;

        if (lines % 1000 == 0 || forceFlush)
        {
            // System.out.print(message);
            try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("log.txt", true))))
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
}