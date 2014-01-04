package hicks.td;

import hicks.td.entities.*;

import javax.swing.*;
import java.awt.*;

public final class UnitPainter
{
    private static final Image MOB1 = new ImageIcon("ass\\mob1.png").getImage();

    public static void drawUnits(Graphics2D g2d)
    {
        for (Unit unit : GameCanvas.units)
        {
            int x = (int) unit.getLocation().getX();
            int y = (int) unit.getLocation().getY();
            int size = unit.getSizeRadius();

            if (unit.getTeam() == 1)
                g2d.setColor(Color.RED);
            if (unit.getTeam() == 2)
                g2d.setColor(Color.DARK_GRAY);

            g2d.drawImage(MOB1, (x - size / 2), (y - size / 2), size, size, null);

            if (unit.getCurrentHp() != unit.getMaxHp()) drawHealthBar(g2d, unit);

            if (GameCanvas.selectedUnit != null && GameCanvas.selectedUnit.equals(unit))
            {
                drawVisionCircle(g2d, unit);
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
            g2d.drawRect((x - unit.getSizeRadius() + (i * 2)), (y - 12), 2, 2);
    }

    private static void drawVisionCircle(Graphics2D g2d, Unit unit)
    {
        int x = (int) unit.getLocation().getX();
        int y = (int) unit.getLocation().getY();
        int size = unit.getSightRadius();

        g2d.setColor(Color.BLACK);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 2 * .1f));
        g2d.fillOval((x - size), (y - size), size * 2, size * 2);

        g2d.setColor(Color.DARK_GRAY);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
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
