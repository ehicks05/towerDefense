package hicks.td.ui;

import hicks.td.GameCanvas;
import hicks.td.World;
import hicks.td.entities.Explosion;
import hicks.td.entities.Point;
import hicks.td.entities.Unit;
import hicks.td.entities.mob.Mob;
import hicks.td.entities.projectile.*;
import hicks.td.entities.tower.*;
import hicks.td.util.ExplosionTileLoader;
import hicks.td.util.MobBodyPartCollection;
import hicks.td.util.MobTileLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public final class UnitPainter
{
    public static final Image SCOUT_TOWER   = new ImageIcon("ass\\img\\scoutTower.gif").getImage();
    public static final Image GUARD_TOWER   = new ImageIcon("ass\\img\\guardTower.gif").getImage();
    public static final Image CANNON_TOWER  = new ImageIcon("ass\\img\\cannonTower.gif").getImage();
    public static final Image ICE_TOWER     = new ImageIcon("ass\\img\\orcGuardTower.gif").getImage();
    private static final Image ARROW        = new ImageIcon("ass\\img\\arrow.png").getImage();
    private static final Image GLAIVE       = new ImageIcon("ass\\img\\glaive.png").getImage();
    private static final Image CANNON_BALL  = new ImageIcon("ass\\img\\rock.png").getImage();
    private static final Image ICE          = new ImageIcon("ass\\img\\iceBolt.png").getImage();

    public static void drawUnits(Graphics2D g2d)
    {
        for (Unit unit : new ArrayList<>(World.getUnits()))
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
                if (unit instanceof IceTower) g2d.drawImage(ICE_TOWER, drawX, drawY, diameter, diameter, null);
                if (isSelected(unit)) drawVisionCircle(g2d, (Tower) unit);
            }
            if (unit instanceof Projectile)
            {
                double rotationRequired = ((Projectile) unit).getTheta();
                g2d.rotate(rotationRequired, x, y);

                if (unit instanceof Arrow)
                    g2d.drawImage(ARROW, drawX, drawY, diameter, diameter, null);
                if (unit instanceof IceBolt)
                    g2d.drawImage(ICE, drawX, drawY, diameter, diameter, null);
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
                Mob mob = (Mob) unit;
                int frameIndex = mob.getFrame() / 10;

                Point destination = mob.getPath().peek();
                String direction = "";
                if (destination.equals(new Point(32, 32))) direction = "left";
                if (destination.equals(new Point(32, World.getGameMap().getHeight() - 32))) direction = "down";
                if (destination.equals(new Point(World.getGameMap().getWidth() - 32, World.getGameMap().getHeight() - 32))) direction = "right";
                if (destination.equals(new Point(World.getGameMap().getWidth() - 32, 32))) direction = "up";

                drawMobBodyParts(g2d, frameIndex, direction, drawX, drawY, diameter, mob.getMobBodyPartCollection());

                if (GameCanvas.isRunningSimulation())
                {
                    mob.setFrame(mob.getFrame() + 1);
                    if (mob.getFrame() > 89)
                        mob.setFrame(10); // start on frame index 1 because index 0 is the idle position...
                }
            }
            if (unit instanceof Explosion)
            {
                Explosion explosion = (Explosion) unit;
                int frameIndex = explosion.getFrame();
                g2d.drawImage(ExplosionTileLoader.getTile(frameIndex), drawX, drawY, diameter, diameter, null);

                if (GameCanvas.isRunningSimulation())
                    explosion.setFrame(frameIndex + 2);
            }
        }

        for (Unit unit : new ArrayList<>(World.getUnits()))
        {
            if (unit instanceof Mob)
            {
                Mob mob = (Mob) unit;
                if (!isFullHealth(mob) || isSelected(unit)) drawHealthBar(g2d, mob);
            }

            // draw additional UI elements connected to the unit
            if (isSelected(unit))
            {
                JPanel unitInfo = GameCanvas.getGamePanel().getUnitInfoPanel();
                Component component = unitInfo.getComponent(0);
                JLabel label = (JLabel) component;
                label.setText(unit.toString());

            }
        }
    }

    private static void drawMobBodyParts(Graphics2D g2d, int frameIndex, String direction, int drawX, int drawY, int diameter, MobBodyPartCollection bodyParts)
    {
        if (bodyParts.getBody() != null)             g2d.drawImage(MobTileLoader.getTile(frameIndex, direction, bodyParts.getBody()), drawX, drawY, diameter, diameter, null);
        if (bodyParts.getBack() != null)             g2d.drawImage(MobTileLoader.getTile(frameIndex, direction, bodyParts.getBack()), drawX, drawY, diameter, diameter, null);
        if (bodyParts.getBelt() != null)             g2d.drawImage(MobTileLoader.getTile(frameIndex, direction, bodyParts.getBelt()), drawX, drawY, diameter, diameter, null);
        if (bodyParts.getFeet() != null)             g2d.drawImage(MobTileLoader.getTile(frameIndex, direction, bodyParts.getFeet()), drawX, drawY, diameter, diameter, null);
        if (bodyParts.getHands() != null)            g2d.drawImage(MobTileLoader.getTile(frameIndex, direction, bodyParts.getHands()), drawX, drawY, diameter, diameter, null);
        if (bodyParts.getHead() != null)             g2d.drawImage(MobTileLoader.getTile(frameIndex, direction, bodyParts.getHead()), drawX, drawY, diameter, diameter, null);
        if (bodyParts.getLegs() != null)             g2d.drawImage(MobTileLoader.getTile(frameIndex, direction, bodyParts.getLegs()), drawX, drawY, diameter, diameter, null);
        if (bodyParts.getTorsoBottomLayer() != null) g2d.drawImage(MobTileLoader.getTile(frameIndex, direction, bodyParts.getTorsoBottomLayer()), drawX, drawY, diameter, diameter, null);
        if (bodyParts.getTorsoTopLayer() != null)    g2d.drawImage(MobTileLoader.getTile(frameIndex, direction, bodyParts.getTorsoTopLayer()), drawX, drawY, diameter, diameter, null);
        if (bodyParts.getTorsoShoulders() != null)   g2d.drawImage(MobTileLoader.getTile(frameIndex, direction, bodyParts.getTorsoShoulders()), drawX, drawY, diameter, diameter, null);
        if (bodyParts.getTorsoBracers() != null)     g2d.drawImage(MobTileLoader.getTile(frameIndex, direction, bodyParts.getTorsoBracers()), drawX, drawY, diameter, diameter, null);
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
            g2d.drawRect((unitX - 10 + (i * widthOfSlice)), (unitY - mob.getSizeRadius() + 6), 1, 2);
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
}
