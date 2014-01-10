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
    private static final Image MOB1 = new ImageIcon("ass\\mob1.png").getImage();
    private static final Image ARCHER_TOWER = new ImageIcon("ass\\arrowTower.png").getImage();
    private static final Image ARROW = new ImageIcon("ass\\arrow.png").getImage();

    public static void drawUnits(Graphics2D g2d)
    {
        for (Unit unit : new ArrayList<>(GameCanvas.units))
        {
            int x = (int) unit.getLocation().getX();
            int y = (int) unit.getLocation().getY();
            int size = unit.getSizeRadius();

            if (unit.getTeam() == 1)
                g2d.setColor(Color.RED);
            if (unit.getTeam() == 2)
                g2d.setColor(Color.DARK_GRAY);

            if (unit instanceof Tower)
                g2d.drawImage(ARCHER_TOWER, (x - size / 2), (y - size / 2), size, size, null);
            if (unit instanceof Projectile)
            {
                // Rotation information
                double rotationRequired = Math.toRadians(65);
                double locationX = ARROW.getWidth(null) / 2;
                double locationY = ARROW.getHeight(null) / 2;
                AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
                AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

                try
                {
                    BufferedImage ARROW_BUFFERED = ImageIO.read(new File("ass\\arrow.png"));
                    // Drawing the rotated image at the required drawing locations
                    g2d.drawImage(op.filter(ARROW_BUFFERED, null), x, y, null);
                }
                catch (IOException e)
                {
                    Log.logInfo("DANGER WILL ROBINSON");
                }

//                g2d.drawImage(ARROW, (x - size / 2), (y - size / 2), size, size, null);
            }
            if (!(unit instanceof Tower) && !(unit instanceof Projectile))
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
