package hicks.td.ui;

import hicks.td.GameCanvas;
import hicks.td.World;
import hicks.td.entities.Tower;
import hicks.td.entities.Unit;
import hicks.td.entities.Wave;
import hicks.td.util.Metrics;
import hicks.td.util.Util;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.List;

public class InterfaceLogic
{
    public static Unit selectedUnit = new Unit();
    public static JLabel infoLabel;
    public static String stopSimulationReason = "";
    public static BigDecimal timePaused;
    public static boolean gameStarted = false;
    public static boolean runningSimulation = false;
    private static String towerToggle = "ArrowTower";
    private static boolean activeRound = false;

    private static int mouseX;
    private static int mouseY;

    public static void paintWorld(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;

        double scalingFactor = DisplayInfo.getScalingFactor();
        g2d.scale(scalingFactor, scalingFactor);

        g2d.drawImage(World.getTerrainImage(), 0, 0, null);

        drawTileGrid(g2d);

        UnitPainter.drawUnits(g2d);

        if (isRunningSimulation())
            infoLabel.setText(getLabelText());

//        infoLabel.setPreferredSize(new Dimension(300, 20));

        if (!isRunningSimulation())
        {
            Font font = new Font("Helvetica", Font.PLAIN, 36);
            g2d.setFont(font);
            g2d.setColor(Color.BLACK);
            g2d.drawString(stopSimulationReason, DisplayInfo.getWindowWidth() / 2, DisplayInfo.getWindowHeight() / 2);
        }

        // draw dark circle to preview where a tower would be built
        if (selectedUnit == null)
        {
            g2d.setColor(Color.DARK_GRAY);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 2 * .1f));
            g2d.fillOval(InterfaceUtil.snapToMiddleOfTile(getMouseX()) - 16, InterfaceUtil.snapToMiddleOfTile(getMouseY()) - 16, 32, 32);
            g2d.setColor(Color.BLACK);
        }

        Toolkit.getDefaultToolkit().sync();
//        g.dispose();
    }

    private static void drawTileGrid(Graphics2D g2d)
    {
        g2d.setColor(Color.BLACK);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 3 * .1f));
        for (int width = 0; width < 1024; width += 32)
            g2d.drawLine( width, 0, width, 768);
        for (int height = 0; height < 768; height += 32)
            g2d.drawLine( 0, height, 1024, height);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
    }

    private static String getLabelText()
    {
        int gold = World.getPlayer().getGold();
        int wave = World.getPlayer().getWaveNumber();
        int lives = World.getPlayer().getLives();
        String labelText = "Gold:" + gold + " ";
        labelText += "Wave:" + wave + " ";
        labelText += "Lives:" + lives + " ";
        labelText += "FPS:" + Metrics.calculateFPS() + " ";

        return labelText;

//        String labelText = "<html><table><tr>";
//        labelText += "<td>Gold:</td><td>" + World.getPlayer().getGold()   + "</td>";
//        labelText += "<td>Wave:</td><td>" + World.getPlayer().getWaveNumber() + "</td>";
//        labelText += "<td>Lives:</td><td>" + World.getPlayer().getLives() + "</td>";
//        labelText += "<td>FPS:</td><td>" + Metrics.calculateFPS()        + "</td>";
////        labelText += "<td>Mobs:</td><td>" + Util.getMobs().size() + "</td>";
//        labelText += "</tr></table></html>";
//
//        return labelText;
    }

    public static void resumeGame()
    {
        runningSimulation = true;
        BigDecimal timeResumed = Util.now();

        if (timePaused == null) timePaused = Util.now();

        BigDecimal timeDeltaRaw = Util.getElapsedTimeRaw(timePaused, timeResumed).multiply(new BigDecimal("1000000"));
        BigDecimal timeDeltaSeconds = Util.getElapsedTime(timePaused, timeResumed);
        BigDecimal timeDeltaMillis = timeDeltaSeconds.multiply(new BigDecimal("1000"));
        Util.adjustStartTime(timeDeltaMillis);

        GameCanvas.accumulator -= timeDeltaRaw.longValue();
        if (GameCanvas.accumulator < 0) GameCanvas.accumulator = 0;

        Wave wave = World.getWave(World.getPlayer().getWaveNumber());
        if (wave != null)
            wave.setTimeStarted(wave.getTimeStarted().add(timeDeltaMillis));

        for (Tower tower : Util.getTowers())
            tower.setTimeOfLastAttack(tower.getTimeOfLastAttack().add(timeDeltaMillis));
    }

    public static void pauseGame()
    {
        if (!runningSimulation) return;

        runningSimulation = false;
        timePaused = Util.now();
    }

    public static void setSelectedUnit(Unit selectedUnit)
    {
        JLabel textLabel = (JLabel) GameCanvas.getGamePanel().getUnitInfoPanel().getComponent(0);
        textLabel.setText("");

        List<Component> components = Arrays.asList(GameCanvas.getGamePanel().getUnitInfoPanel().getComponents());
        for (Component component : components)
        {
            if (component instanceof JButton)
            {
                JButton button = (JButton) component;
                button.setVisible(false);
            }
        }

        InterfaceLogic.selectedUnit = selectedUnit;
    }

    public static void startNextWave()
    {
        if (isActiveRound()) return;

        setGameStarted(true);
        World.getPlayer().setWaveNumber(World.getPlayer().getWaveNumber() + 1);

        List<Wave> waves = World.getWaves();
        for (Wave wave : waves)
            if (wave.getWaveNumber() == World.getPlayer().getWaveNumber())
                wave.setTimeStarted(Util.now());

        setActiveRound(true);
    }

    public static String getTowerToggle()
    {
        return towerToggle;
    }

    public static void setTowerToggle(String towerToggle)
    {
        InterfaceLogic.towerToggle = towerToggle;
    }

    public static Unit getSelectedUnit()
    {
        return selectedUnit;
    }

    public static boolean isRunningSimulation()
    {
        return runningSimulation;
    }

    public static void setRunningSimulation(boolean runningSimulation)
    {
        InterfaceLogic.runningSimulation = runningSimulation;
    }

    public static boolean isActiveRound()
    {
        return activeRound;
    }

    public static void setActiveRound(boolean activeRound)
    {
        InterfaceLogic.activeRound = activeRound;
    }

    public static boolean isGameStarted()
    {
        return gameStarted;
    }

    public static void setGameStarted(boolean gameStarted)
    {
        InterfaceLogic.gameStarted = gameStarted;
        GameCanvas.getMainMenuPanel().showRestartGameButton();
    }

    public static int getMouseY()
    {
        return mouseY;
    }

    public static void setMouseY(int mouseY)
    {
        InterfaceLogic.mouseY = mouseY;
    }

    public static int getMouseX()
    {
        return mouseX;
    }

    public static void setMouseX(int mouseX)
    {
        InterfaceLogic.mouseX = mouseX;
    }
}
