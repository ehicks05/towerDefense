package hicks.td.ui;

public class DisplayInfo
{
    private static int INTERFACE_HEIGHT = 64;
    private static int TOTAL_SCREEN_HEIGHT; //= WORLD_HEIGHT + INTERFACE_HEIGHT;

    public static int getInterfaceHeight()
    {
        return INTERFACE_HEIGHT;
    }

    public static void setInterfaceHeight(int interfaceHeight)
    {
        INTERFACE_HEIGHT = interfaceHeight;
    }

    public static int getTotalScreenHeight()
    {
        return TOTAL_SCREEN_HEIGHT;
    }

    public static void setTotalScreenHeight(int totalScreenHeight)
    {
        DisplayInfo.TOTAL_SCREEN_HEIGHT = totalScreenHeight;
    }
}
