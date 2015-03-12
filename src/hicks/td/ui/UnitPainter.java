package hicks.td.ui;

import hicks.td.GameCanvas;
import hicks.td.World;
import hicks.td.entities.*;
import hicks.td.entities.Point;
import hicks.td.entities.Projectile;
import hicks.td.util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class UnitPainter
{
    public static void drawUnits(Graphics2D g2d)
    {
        Tower towerThatNeedsVisionCircle = null;
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
                Tower tower = (Tower) unit;

                g2d.drawImage(tower.getImage(), drawX, drawY, diameter, diameter, null);

                if (isSelected(tower)) towerThatNeedsVisionCircle = tower;
            }
            if (unit instanceof Projectile)
            {
                drawProjectile(g2d, (Projectile) unit, diameter, x, y, drawX, drawY);
            }
            if (unit instanceof Mob)
            {
                drawMob(g2d, (Mob) unit, diameter, drawX, drawY);
            }
            if (unit instanceof Animation)
            {
                Animation animation = (Animation) unit;
                int frameIndex = animation.getFrame();

                if (animation.getName().equals("explosion"))
                {
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 7 * .1f));
                    BufferedImage image = ExplosionTileLoader.getTile(frameIndex);
                    g2d.drawImage(image, drawX, drawY, diameter, diameter, null);
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
                }

                if (animation.getName().equals("death"))
                    drawMobBodyParts(g2d, frameIndex, null, drawX, drawY, diameter, animation.getOutfit(), true);
            }
        }

        if (towerThatNeedsVisionCircle != null)
            drawVisionCircle(g2d, towerThatNeedsVisionCircle);

        // if the selected mob has died, stop showing its info...
        if (InterfaceLogic.getSelectedUnit() instanceof Mob && !Util.getMobs().contains(InterfaceLogic.getSelectedUnit()))
        {
            JPanel unitInfo = GameCanvas.getGamePanel().getUnitInfoPanel();
            Component component = unitInfo.getComponent(0);
            JLabel label = (JLabel) component;
            label.setText("");
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

                if (unit instanceof Tower)
                {
                    Tower tower = (Tower) unit;
                    Projectile projectile = tower.getProjectileWithUpgrades();
                    label.setText("  R:" + tower.getAttackRange() + "  D:" + projectile.getMinDamage() + "-" + projectile.getMaxDamage());

                    List<Upgrade> availableUpgrades = tower.getAvailableUpgrades();
                    List<Component> components = Arrays.asList(GameCanvas.getGamePanel().getUnitInfoPanel().getComponents());
                    for (Upgrade upgrade : availableUpgrades)
                    {
                        for (Component component1 : components)
                        {
                            if (component1 instanceof JButton)
                            {
                                JButton button = (JButton) component1;
                                if (button.getName().equals(upgrade.getCode()))
                                    button.setVisible(true);
                            }
                        }
                    }
                    for (Component component1 : components)
                    {
                        if (component1 instanceof JButton)
                        {
                            JButton button = (JButton) component1;
                            if (button.getName().equals("sell"))
                                button.setVisible(true);
                        }
                    }
                }
            }
        }
    }

    private static void drawMob(Graphics2D g2d, Mob mob, int diameter, int drawX, int drawY)
    {
        String direction = getMobDirection(mob);

        drawMobBodyParts(g2d, mob.getFrame(), direction, drawX, drawY, diameter, mob.getOutfit(), false);
    }

    private static String getMobDirection(Mob mob)
    {
        Point previousPoint = mob.getPreviousPoint();
        Point destination = mob.getPath().peek();
        String direction = "";
        if (destination.getY() > previousPoint.getY()) direction = "down";
        if (destination.getY() < previousPoint.getY()) direction = "up";
        if (destination.getX() > previousPoint.getX()) direction = "right";
        if (destination.getX() < previousPoint.getX()) direction = "left";
        return direction;
    }

    private static void drawProjectile(Graphics2D g2d, Projectile projectile, int diameter, int x, int y, int drawX, int drawY)
    {
        g2d.rotate(projectile.getTheta(), x, y);

        g2d.drawImage(projectile.getImage(), drawX, drawY, diameter, diameter, null);

        if (InterfaceLogic.isRunningSimulation())
            projectile.setTheta(projectile.getTheta() + projectile.getThetaDelta());

        // reset the transform
        AffineTransform reset = new AffineTransform();
        reset.rotate(0,0,0);
        g2d.setTransform(reset);
    }

    private static void drawMobBodyParts(Graphics2D g2d, int frameIndex, String direction, int drawX, int drawY, int diameter, int outfitType, boolean dying)
    {
        Outfit outfit = World.getOutfitTypes().get(outfitType);
        for (BodyPart bodyPart : outfit.getAllActiveBodyParts())
        {
            if (dying)
                g2d.drawImage(MobTileLoader.getDyingImage(bodyPart, frameIndex), drawX, drawY, diameter, diameter, null);
            else
                g2d.drawImage(MobTileLoader.getImage(bodyPart, direction, frameIndex), drawX, drawY, diameter, diameter, null);
        }
    }

    private static boolean isFullHealth(Mob mob)
    {
        return mob.getCurrentHp() == mob.getMaxHp();
    }

    private static boolean isSelected(Unit unit)
    {
        Unit selectedUnit = InterfaceLogic.getSelectedUnit();
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
        if (tower == null) return;

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
