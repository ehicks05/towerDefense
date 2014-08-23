package hicks.td.ui;

import java.awt.*;

public class DisplayInfo
{
    private static int displayWidth;
    private static int displayHeight;

    private static int windowWidth;
    private static int windowHeight;

    private static double scalingFactor;

    public static void setDisplayProperties()
    {
        GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DisplayMode displayMode = graphicsDevice.getDisplayMode();

        int displayWidth = displayMode.getWidth();
        int displayHeight = displayMode.getHeight();
        DisplayInfo.setDisplayWidth(displayWidth);
        DisplayInfo.setDisplayHeight(displayHeight);

        final int desiredWindowWidth = 768;
        final int desiredWindowHeight = 672;

        setScalingFactor(calculateScalingFactor(desiredWindowWidth, desiredWindowHeight));

        DisplayInfo.setWindowWidth((int) (desiredWindowWidth * getScalingFactor()));
        DisplayInfo.setWindowHeight((int) (desiredWindowHeight * getScalingFactor()));
    }

    private static double calculateScalingFactor(int desiredWindowWidth, int desiredWindowHeight)
    {
        final double actualToDesiredWidth = (double) displayWidth / desiredWindowWidth;
        final double actualToDesiredHeight = (double) displayHeight / desiredWindowHeight;
        double scalingFactor = 1;
        if (actualToDesiredWidth <= 1 || actualToDesiredHeight <= 1)
        {
            scalingFactor = actualToDesiredWidth <= actualToDesiredHeight ? actualToDesiredWidth : actualToDesiredHeight;
            scalingFactor *= .8;
        }
//        return scalingFactor;
        return 1;
    }

    public static int getDisplayWidth()
    {
        return displayWidth;
    }

    public static void setDisplayWidth(int displayWidth)
    {
        DisplayInfo.displayWidth = displayWidth;
    }

    public static int getDisplayHeight()
    {
        return displayHeight;
    }

    public static void setDisplayHeight(int displayHeight)
    {
        DisplayInfo.displayHeight = displayHeight;
    }

    public static int getWindowWidth()
    {
        return windowWidth;
    }

    public static void setWindowWidth(int windowWidth)
    {
        DisplayInfo.windowWidth = windowWidth;
    }

    public static int getWindowHeight()
    {
        return windowHeight;
    }

    public static void setWindowHeight(int windowHeight)
    {
        DisplayInfo.windowHeight = windowHeight;
    }

    public static double getScalingFactor()
    {
        return scalingFactor;
    }

    public static void setScalingFactor(double scalingFactor)
    {
        DisplayInfo.scalingFactor = scalingFactor;
    }
}
