package hicks.td.ui;

import hicks.td.GameCanvas;
import hicks.td.GameState;
import hicks.td.entities.*;
import hicks.td.entities.mob.Mob;
import hicks.td.entities.projectile.Arrow;
import hicks.td.entities.projectile.Cannonball;
import hicks.td.entities.projectile.Glaive;
import hicks.td.entities.projectile.Projectile;
import hicks.td.entities.tower.ArrowTower;
import hicks.td.entities.tower.CannonTower;
import hicks.td.entities.tower.GlaiveTower;
import hicks.td.entities.tower.Tower;
import hicks.td.util.ExplosionTileLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public final class UnitPainter
{
    public static final Image SCOUT_TOWER   = new ImageIcon("ass\\img\\scoutTower.gif").getImage();
    public static final Image GUARD_TOWER   = new ImageIcon("ass\\img\\guardTower.gif").getImage();
    public static final Image CANNON_TOWER  = new ImageIcon("ass\\img\\cannonTower.gif").getImage();
    private static final Image ARROW        = new ImageIcon("ass\\img\\arrow.png").getImage();
    private static final Image GLAIVE       = new ImageIcon("ass\\img\\glaive.png").getImage();
    private static final Image PEASANT      = new ImageIcon("ass\\img\\peasant.gif").getImage();
    private static final Image CANNON_BALL  = new ImageIcon("ass\\img\\rock.png").getImage();

    public static void drawUnits(Graphics2D g2d)
    {
        for (Unit unit : new ArrayList<>(GameState.getUnits()))
        {
            int size = unit.getSizeRadius();
            int diameter = size * 2;
            int x = (int) unit.getLocation().getX();
            int y = (int) unit.getLocation().getY();
            int drawX = x - size;
            int drawY = y - size;

            // draw the unit
            if (unit instanceof Tower)
            {
                if (unit instanceof ArrowTower) g2d.drawImage(GUARD_TOWER, drawX, drawY, diameter, diameter, null);
                if (unit instanceof GlaiveTower) g2d.drawImage(SCOUT_TOWER, drawX, drawY, diameter, diameter, null);
                if (unit instanceof CannonTower) g2d.drawImage(CANNON_TOWER, drawX, drawY, diameter, diameter, null);
                if (isSelected(unit)) drawVisionCircle(g2d, (Tower) unit);
            }
            if (unit instanceof Projectile)
            {
                double rotationRequired = ((Projectile) unit).getTheta();
                g2d.rotate(rotationRequired, x, y);

                if (unit instanceof Arrow)
                    g2d.drawImage(ARROW, drawX, drawY, diameter, diameter, null);
                if (unit instanceof Glaive)
                {
                    g2d.drawImage(GLAIVE, drawX, drawY, diameter, diameter, null);
                    Glaive glaive = (Glaive) unit;

                    if (GameCanvas.isRunningSimulation())
                        glaive.setTheta(glaive.getTheta() + .1);
                }
                if (unit instanceof Cannonball)
                {
                    g2d.drawImage(CANNON_BALL, drawX, drawY, diameter, diameter, null);
                    Cannonball cannonball = (Cannonball) unit;

                    if (GameCanvas.isRunningSimulation())
                        cannonball.setTheta(cannonball.getTheta() + .05);
                }

                // reset the transform
                AffineTransform reset = new AffineTransform();
                reset.rotate(0,0,0);
                g2d.setTransform(reset);
            }
            if (unit instanceof Mob)
            {
                g2d.drawImage(PEASANT, drawX, drawY, diameter, diameter, null);

                Mob mob = (Mob) unit;
                if (!isFullHealth(mob) || isSelected(unit)) drawHealthBar(g2d, mob);
            }
            if (unit instanceof Explosion)
            {
                Explosion explosion = (Explosion) unit;
                int frameIndex = explosion.getFrame();
                g2d.drawImage(ExplosionTileLoader.getTile(frameIndex), drawX, drawY, diameter, diameter, null);

                if (GameCanvas.isRunningSimulation())
                    explosion.setFrame(frameIndex + 1);
            }

            // draw additional UI elements connected to the unit
            if (isSelected(unit))
                drawObjectId(g2d, unit);
        }
    }

    private static boolean isFullHealth(Mob mob)
    {
        return mob.getCurrentHp() == mob.getMaxHp();
    }

    private static boolean isSelected(Unit unit)
    {
        Unit selectedUnit = GameCanvas.getSelectedUnit();
        return selectedUnit != null && unit.equals(selectedUnit);
    }

    private static void drawHealthBar(Graphics2D g2d, Mob mob)
    {
        int unitX = (int) mob.getLocation().getX();
        int unitY = (int) mob.getLocation().getY();
        int widthOfSlice = 1;

        double currentHpPercent = (mob.getCurrentHp() * 100) / mob.getMaxHp();
        int hpBoxes = (int) (currentHpPercent / 5);

        if (currentHpPercent > 66.6)
            g2d.setColor(Color.GREEN);
        if (currentHpPercent <= 66.6 && currentHpPercent > 33.3)
            g2d.setColor(Color.YELLOW);
        if (currentHpPercent <= 33.3)
            g2d.setColor(Color.RED);

        // the health bar can be considered 20px wide. so start 10 pixels to the left of the vertex...
        for (int i = 0; i < hpBoxes; i++)
            g2d.drawRect((unitX - 10 + (i * widthOfSlice)), (unitY - mob.getSizeRadius()), 1, 2);
    }

    private static void drawVisionCircle(Graphics2D g2d, Tower tower)
    {
        int x = (int) tower.getLocation().getX();
        int y = (int) tower.getLocation().getY();
        int size = tower.getAttackRange();

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
        int size = unit.getSizeRadius();

        g2d.setColor(Color.BLACK);
        g2d.drawString(String.valueOf(unit.getObjectId()), x + size + 5, y);
    }
}
