package hicks.combat;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

public class Log
{
    private static long m_lines;
    private static String messageQueue;

    public static void logInfo(String message)
    {
        logInfo(null, message, false);
    }
    public static void logInfo(BigDecimal simulationStart, String message)
    {
        logInfo(simulationStart, message, false);
    }
    public static void logInfo(BigDecimal simulationStart, String message, boolean flush)
    {
        m_lines++;
        String elapsed = "";
        if (simulationStart != null)
            elapsed = GameLogic.getElapsedTime(simulationStart).setScale(2, BigDecimal.ROUND_HALF_UP).toString() + ": ";

        message = "Line " + m_lines + " " + elapsed + message;

        messageQueue = messageQueue + message + "\r\n";

        if (m_lines % 1000 == 0 || flush)
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
