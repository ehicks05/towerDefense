package hicks.td.ui;

import java.awt.*;

public class MyButton
{
    private String label;
    private int x;
    private int y;
    private int width;
    private int height;

    public MyButton(String label, int x, int y, int width, int height)
    {
        this.label = label;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(Graphics2D g2d)
    {
        FontMetrics fontMetrics = g2d.getFontMetrics();
        int stringWidth = fontMetrics.stringWidth(label);
        int extraSpace = width - stringWidth;
        int textX = x + extraSpace / 2;

        g2d.setColor(Color.BLACK);
        g2d.drawRoundRect(x, y, width, height, 20, 20);
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRoundRect(x, y, width, height, 20, 20);
        g2d.setColor(Color.BLACK);
        g2d.drawString(label, textX, y + 15);
    }
}
