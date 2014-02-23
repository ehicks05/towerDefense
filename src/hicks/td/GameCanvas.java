package hicks.td;

import hicks.td.entities.Unit;
import hicks.td.entities.UnitLogic;
import hicks.td.entities.mob.Mob;
import hicks.td.entities.projectile.Projectile;
import hicks.td.entities.tower.Tower;
import hicks.td.ui.*;
import hicks.td.util.Metrics;
import hicks.td.util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.math.BigDecimal;
import java.math.RoundingMode;

public final class GameCanvas extends Canvas
{
    private static Unit selectedUnit = new Unit();

    private static boolean runningSimulation = false;
    private static boolean displayMenu = true;
    private static String stopSimulationReason = "";

    private static JFrame frame;
    private static JPanel gamePanel;
    private static JPanel mainMenuPanel;
    private static JPanel cards;
    private static CardLayout cardLayout;

    private static BigDecimal timePaused;
    private static BigDecimal timeResumed;

    private static JLabel infoLabel;
    private static String towerToggle = "Arrow";

    public GameCanvas()
    {
        this.setSize(GameState.getGameMap().getWidth(), GameState.getGameMap().getHeight());

        this.addKeyListener(new MyKeyListener());
        this.addMouseListener(new MyMouseListener());
        this.addMouseMotionListener(new MyMouseMotionListener());

        this.setBounds(0, 0, GameState.getGameMap().getWidth(), GameState.getGameMap().getHeight());
        this.setIgnoreRepaint(true);
    }

    public static void paintWorld(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;

        double scalingFactor = DisplayInfo.getScalingFactor();
        g2d.scale(scalingFactor, scalingFactor);

        g2d.drawImage(GameState.getTerrainImage(), 0, 0, null);

        UnitPainter.drawUnits(g2d);
        if (runningSimulation)
            infoLabel.setText(getLabelText());

        if (!runningSimulation)
        {
            Font font = new Font("Helvetica", Font.PLAIN, 36);
            g2d.setFont(font);
            g2d.setColor(Color.BLACK);
            g2d.drawString(stopSimulationReason, DisplayInfo.getWindowWidth() / 2, DisplayInfo.getWindowHeight() / 2);
        }

        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    private static String getLabelText()
    {
        BigDecimal elapsed = Util.getElapsedTime(GameState.getStartTime()).setScale(2, RoundingMode.HALF_UP);

        String labelText = "<html><table><tr>";

        labelText += "<td>Gold:</td><td>" + GameState.getPlayer().getGold()   + "</td>";
        labelText += "<td>Round:</td><td>" + GameState.getPlayer().getRoundNumber() + "</td>";
        labelText += "<td>Lives:</td><td>" + GameState.getPlayer().getLives() + "</td>";
        labelText += "</tr><tr>";

        labelText += "<td>Stopwatch:</td><td>" + elapsed                 + "</td>";
        labelText += "<td>FPS:</td><td>" + Metrics.calculateFPS()        + "</td>";
        labelText += "<td>Units:</td><td>" + GameState.getUnits().size() + "</td>";
        labelText += "</tr></table></html>";

        return labelText;
    }

    public static void main(String[] args)
    {
        Init.init();

        frame = new MyFrame();
        gamePanel = new MyGamePanel();

        infoLabel = new JLabel();
        infoLabel.setVisible(true);
        gamePanel.add(infoLabel);

        GameCanvas gameCanvas = new GameCanvas();
        gamePanel.add(gameCanvas);

        mainMenuPanel = new MainMenuPanel();

        cards = new JPanel(new CardLayout());
        cards.add(gamePanel);
        cards.add(mainMenuPanel);
        frame.add(cards);

        cardLayout = (CardLayout) cards.getLayout();
        cardLayout.addLayoutComponent(gamePanel, "gamePanel");
        cardLayout.addLayoutComponent(mainMenuPanel, "mainMenuPanel");
        cardLayout.show(cards, mainMenuPanel.getName());

        frame.pack();

        // Let our Canvas know we want to do Double Buffering
        gameCanvas.createBufferStrategy(2);
        BufferStrategy bufferStrategy = gameCanvas.getBufferStrategy();

        runGameLoop(bufferStrategy);
    }

    private static void runGameLoop(BufferStrategy bufferStrategy)
    {
        final int DELAY = 16666666; // (16 ms)
        long beforeTime = System.nanoTime();
        long sleep;

        // run game loop
        while (true)
        {
            // loops through every unit on the map and updates their state
            if (runningSimulation)
                BehaviorLogic.updateState();

            // Grab the current non visible frame (Memory on the graphics card)
            // getDrawGraphics actually creates a new off screen buffer; it doesn't get something that already exists.
            Graphics2D frameBuffer = (Graphics2D) bufferStrategy.getDrawGraphics();

            paintWorld(frameBuffer);

            // Release the off screen buffer
            frameBuffer.dispose();

            // Flip the off screen buffer back in.
            bufferStrategy.show();

            long now = System.nanoTime();
            Metrics.timeDiff = now - beforeTime;
            sleep = (DELAY - Metrics.timeDiff) / 1000000;
            beforeTime = System.nanoTime();

            if (sleep < 0)
                sleep = 1;
            try
            {
                Thread.sleep(sleep);
            }
            catch (InterruptedException e)
            {
                System.out.println("interrupted");
            }

            // check stopping conditions
            if (GameState.getPlayer().getLives() <= 0)
            {
                runningSimulation = false;
                stopSimulationReason = "YOU LOSE!";
            }
            if (GameState.getPlayer().getRoundNumber() > 5 && UnitLogic.getUnitsOnTeam(2) == 0)
            {
                runningSimulation = false;
                stopSimulationReason = "YOU WIN!";
            }
        }
    }

    public static void displayMenu()
    {
        pauseGame();
        cardLayout.show(cards, mainMenuPanel.getName());
    }

    public static void closeMenu()
    {
        cardLayout.show(cards, gamePanel.getName());
        resumeGame();
    }

    public static void pauseGame()
    {
        runningSimulation = false;
        timePaused = Util.now();
    }

    public static void resumeGame()
    {
        runningSimulation = true;
        timeResumed = Util.now();

        if (timePaused == null) timePaused = Util.now();

        BigDecimal timeDeltaSeconds = Util.getElapsedTime(timePaused, timeResumed);
        BigDecimal timeDeltaMillis = timeDeltaSeconds.multiply(new BigDecimal("1000"));
        GameState.adjustStartTime(timeDeltaMillis);

        for (Tower tower : Util.getTowers())
            tower.setTimeOfLastAttack(tower.getTimeOfLastAttack().add(timeDeltaMillis));

        for (Projectile projectile : Util.getProjectiles())
            projectile.setTimeOfLastMove(projectile.getTimeOfLastMove().add(timeDeltaMillis));

        for (Mob mob : Util.getMobs())
            mob.setTimeOfLastMove(mob.getTimeOfLastMove().add(timeDeltaMillis));

        GameState.getSpawner().setTimeOfLastBuild(GameState.getSpawner().getTimeOfLastBuild().add(timeDeltaMillis));
    }

    public static Unit getSelectedUnit()
    {
        return selectedUnit;
    }

    public static void setSelectedUnit(Unit selectedUnit)
    {
        GameCanvas.selectedUnit = selectedUnit;
    }

    public static String getTowerToggle()
    {
        return towerToggle;
    }

    public static void setTowerToggle(String towerToggle)
    {
        GameCanvas.towerToggle = towerToggle;
    }

    public static boolean isRunningSimulation()
    {
        return runningSimulation;
    }

    public static void setRunningSimulation(boolean runningSimulation)
    {
        GameCanvas.runningSimulation = runningSimulation;
    }
}
