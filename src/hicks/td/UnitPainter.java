package hicks.td;

import hicks.td.entities.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public final class UnitPainter
{
    private static final Image ARCHER_TOWER = new ImageIcon("ass\\guardTower.png").getImage();
    private static final Image ARROW        = new ImageIcon("ass\\arrow.png").getImage();
    private static final Image MOB1         = new ImageIcon("ass\\mob1.png").getImage();
    private static final Image PEASANT      = new ImageIcon("ass\\peasant.gif").getImage();

    public static void drawUnits(Graphics2D g2d)
    {
        for (Unit unit : new ArrayList<>(GameCanvas.units))
        {
            int size = unit.getSizeRadius();
            int diameter = size * 2;
            int x = (int) unit.getLocation().getX();
            int y = (int) unit.getLocation().getY();
            int drawX = x - size;
            int drawY = y - size;

            if (unit.getTeam() == 1)
                g2d.setColor(Color.RED);
            if (unit.getTeam() == 2)
                g2d.setColor(Color.DARK_GRAY);

            // draw the unit
            if (unit instanceof Tower)
                g2d.drawImage(ARCHER_TOWER, drawX, drawY, diameter, diameter, null);
            if (unit instanceof Projectile)
            {
                double rotationRequired = ((Arrow) unit).getTheta();
                g2d.rotate(rotationRequired, x, y);

                g2d.drawImage(ARROW, drawX, drawY, diameter, diameter, null);

                // reset the transform
                AffineTransform reset = new AffineTransform();
                reset.rotate(0,0,0);
                g2d.setTransform(reset);
            }
            if (!(unit instanceof Tower) && !(unit instanceof Projectile))
                g2d.drawImage(PEASANT, drawX, drawY, diameter, diameter, null);

            // draw additional UI elements connected to the unit
            if (!isFullHealth(unit) || isSelected(unit)) drawHealthBar(g2d, unit);

            if (isSelected(unit))
            {
                drawVisionCircle(g2d, unit);
                drawObjectId(g2d, unit);
            }
        }
    }

    private static boolean isFullHealth(Unit unit)
    {
        return unit.getCurrentHp() == unit.getMaxHp();
    }

    private static boolean isSelected(Unit unit)
    {
        return GameCanvas.selectedUnit != null && GameCanvas.selectedUnit.equals(unit);
    }

    private static void drawHealthBar(Graphics2D g2d, Unit unit)
    {
        int unitX = (int) unit.getLocation().getX();
        int unitY = (int) unit.getLocation().getY();
        int widthOfSlice = 1;

        double currentHpPercent = (unit.getCurrentHp() * 100) / unit.getMaxHp();
        int hpBoxes = (int) (currentHpPercent / 5);

        if (currentHpPercent > 66.6)
            g2d.setColor(Color.GREEN);
        if (currentHpPercent <= 66.6 && currentHpPercent > 33.3)
            g2d.setColor(Color.YELLOW);
        if (currentHpPercent <= 33.3)
            g2d.setColor(Color.RED);

        // the health bar can be considered 20px wide. so start 10 pixels to the left of the vertex...
        for (int i = 0; i < hpBoxes; i++)
            g2d.drawRect((unitX - 10 + (i * widthOfSlice)), (unitY - unit.getSizeRadius()), 1, 2);
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

    private static void drawObjectId(Graphics2D g2d, Unit unit)
    {
        int x = (int) unit.getLocation().getX();
        int y = (int) unit.getLocation().getY();
        int size = unit.getSightRadius();

        g2d.setColor(Color.BLACK);
        g2d.drawString(String.valueOf(unit.getObjectId()), x + size + 5, y);
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
