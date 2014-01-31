package hicks.td.ui;

public class DisplayInfo
{
    private static int interfaceHeight = 64;
    private static int totalScreenHeight; //= WORLD_HEIGHT + interfaceHeight;
    private static int displayWidth;
    private static int displayHeight;

    public static double getScalingFactor()
    {
        double scaleWidth = 1024 / displayWidth;
        double scaleHeight = 800 / displayHeight;

        String scaleBy = scaleWidth < scaleHeight ? "width" : "height";

        if (scaleBy.equals("width"))
            return scaleWidth;
        else
            return scaleHeight;
    }

    public static int getInterfaceHeight()
    {
        return interfaceHeight;
    }

    public static void setInterfaceHeight(int interfaceHeight)
    {
        DisplayInfo.interfaceHeight = interfaceHeight;
    }

    public static int getTotalScreenHeight()
    {
        return totalScreenHeight;
    }

    public static void setTotalScreenHeight(int totalScreenHeight)
    {
        DisplayInfo.totalScreenHeight = totalScreenHeight;
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
}
