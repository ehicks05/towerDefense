package hicks.td;

import hicks.td.entities.*;

import javax.swing.*;
import java.awt.*;

public class UnitPainter
{
    private static final Image PEASANT = new ImageIcon("ass\\wc2h_peasant.gif").getImage();

    public static void drawUnits(Graphics2D g2d)
    {
        for (Unit unit : GameCanvas.units)
        {
            int x = (int) unit.getLocation().getX();
            int y = (int) unit.getLocation().getY();

            if (unit.getTeam() == 0)
            {
                g2d.setColor(Color.RED);
                if (unit instanceof Berserker) g2d.setColor(new Color(150, 0, 0));
            }
            if (unit.getTeam() == 1)
            {
                g2d.setColor(Color.GREEN);
                if (unit instanceof Berserker) g2d.setColor(new Color(0, 120, 0));
            }

            int size = 3;
            if (unit instanceof Knight || unit instanceof Berserker) size = 5;
            if (unit instanceof Peasant) size = 12;

            if (unit instanceof Peasant)
                g2d.drawImage(PEASANT, (x - size/2), (y - size/2), size, size, null);
            else
                g2d.fillRect(x, y, size, size);

            if (GameCanvas.selectedUnits.contains(unit))
            {
                drawVisionCircle(g2d, unit);
                drawHealthBar(g2d, unit);
                drawCoordinates(g2d, unit);
            }
        }
    }

    private static void drawHealthBar(Graphics2D g2d, Unit unit)
    {
        int x = (int) unit.getLocation().getX();
        int y = (int) unit.getLocation().getY();

        double currentHpPercent = (unit.getCurrentHp() * 100) / unit.getMaxHp();
        int hpBoxes = (int) (currentHpPercent / 10);

        if (unit.getTeam() == 0)
            g2d.setColor(Color.RED);
        if (unit.getTeam() == 1)
            g2d.setColor(Color.GREEN);
        for (int i = 0; i < hpBoxes; i++)
            g2d.drawRect((x - 8 + (i * 2)), (y - 4), 2, 2);
    }

    private static void drawVisionCircle(Graphics2D g2d, Unit unit)
    {
        int x = (int) unit.getLocation().getX();
        int y = (int) unit.getLocation().getY();

        g2d.setColor(Color.GREEN);

        int size = unit.getSightRadius();

        g2d.drawOval((x - size), (y - size), size * 2, size * 2);
    }

    private static void drawCoordinates(Graphics2D g2d, Unit unit)
    {
        int x = (int) unit.getLocation().getX();
        int y = (int) unit.getLocation().getY();

        String coordinate = x + ", " + y;

        g2d.setColor(Color.GREEN);
        g2d.drawString(coordinate, x + 5, y);
    }
}
