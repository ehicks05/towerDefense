package hicks.td.ui;

import java.awt.*;

public class DisplayInfo
{
    private static int displayWidth;
    private static int displayHeight;

    private static int windowWidth;
    private static int windowHeight;

    public static void setDisplayProperties()
    {
        GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DisplayMode displayMode = graphicsDevice.getDisplayMode();
        DisplayInfo.setDisplayWidth(displayMode.getWidth());
        DisplayInfo.setDisplayHeight(displayMode.getHeight());
        DisplayInfo.setWindowWidth(1024);
        DisplayInfo.setWindowHeight(864);
    }

    public static double getScalingFactor()
    {
        double scaleWidth = (double) windowWidth / 1024;
        double scaleHeight = (double) windowHeight / 800;

        String scaleBy = scaleWidth < scaleHeight ? "width" : "height";

        if (scaleBy.equals("width"))
            return scaleWidth;
        else
            return scaleHeight;
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
}
