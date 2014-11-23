package hicks.td.ui;

import hicks.td.GameCanvas;
import hicks.td.World;
import hicks.td.entities.*;
import hicks.td.entities.Point;
import hicks.td.entities.projectile.Projectile;
import hicks.td.util.ExplosionTileLoader;
import hicks.td.util.BodyPartCollection;
import hicks.td.util.MobTileLoader;
import hicks.td.util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
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
                Projectile projectile = (Projectile) unit;

                g2d.rotate(projectile.getTheta(), x, y);

                g2d.drawImage(projectile.getImage(), drawX, drawY, diameter, diameter, null);

                if (InterfaceLogic.isRunningSimulation())
                    projectile.setTheta(projectile.getTheta() + projectile.getThetaDelta());

                // reset the transform
                AffineTransform reset = new AffineTransform();
                reset.rotate(0,0,0);
                g2d.setTransform(reset);
            }
            if (unit instanceof Mob)
            {
                Mob mob = (Mob) unit;
                int frameIndex = mob.getFrame() / 10;

                Point previousPoint = mob.getPreviousPoint();
                Point destination = mob.getPath().peek();
                String direction = "";
                if (destination.getY() > previousPoint.getY()) direction = "down";
                if (destination.getY() < previousPoint.getY()) direction = "up";
                if (destination.getX() > previousPoint.getX()) direction = "right";
                if (destination.getX() < previousPoint.getX()) direction = "left";

                drawMobBodyParts(g2d, frameIndex, direction, drawX, drawY, diameter, mob.getBodyPartCollection());

                if (InterfaceLogic.isRunningSimulation())
                {
                    mob.setFrame(mob.getFrame() + 1);
                    if (mob.getFrame() > 89)
                        mob.setFrame(10); // start on frame index 1 because index 0 is the idle position...
                }
            }
            if (unit instanceof Explosion)
            {
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 7 * .1f));
                Explosion explosion = (Explosion) unit;
                int frameIndex = explosion.getFrame();
                g2d.drawImage(ExplosionTileLoader.getTile(frameIndex), drawX, drawY, diameter, diameter, null);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));

                if (InterfaceLogic.isRunningSimulation())
                    explosion.setFrame(frameIndex + 2);
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

    private static void drawMobBodyParts(Graphics2D g2d, int frameIndex, String direction, int drawX, int drawY, int diameter, BodyPartCollection bodyParts)
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
