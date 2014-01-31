package hicks.td;

import hicks.td.entities.GameMap;
import hicks.td.entities.Unit;
import hicks.td.entities.UnitLogic;
import hicks.td.ui.*;
import hicks.td.util.MapBuilder;
import hicks.td.util.Metrics;
import hicks.td.util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.math.RoundingMode;

public final class GameCanvas extends Canvas
{
    private static BufferedImage terrainImage;
    private static Unit selectedUnit = new Unit();

    private static boolean runningSimulation = true;
    private static String stopSimulationReason = "";

    private static JLabel label;
    private static String towerToggle = "Arrow";

    public GameCanvas()
    {
        setSize(GameState.getGameMap().getWidth(), GameState.getGameMap().getHeight());

        addKeyListener(new MyKeyListener());
        addMouseListener(new MyMouseListener());
        addMouseMotionListener(new MyMouseMotionListener());
    }

    public static void paintWorld(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;

        double scalingFactor = DisplayInfo.getScalingFactor();
//        g2d.scale(scalingFactor, scalingFactor);

        g2d.drawImage(terrainImage, 0, 0, null);

        UnitPainter.drawUnits(g2d);
//        InterfacePainter.drawInterface(g2d);
        label.setText(getLabelText());

        if (!runningSimulation)
        {
            Font font = new Font("Helvetica", Font.PLAIN, 36);
            g2d.setFont(font);
            g2d.setColor(Color.BLACK);
            g2d.drawString(stopSimulationReason, GameState.getGameMap().getWidth() / 2, DisplayInfo.getTotalScreenHeight() / 2);
        }

        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    private static String getLabelText()
    {
        String labelText = "<html><table><tr>";
        // info column 1
        labelText += "<td>Gold: " + GameState.getPlayer().getGold()   + "</td>";
        labelText += "<td>Round: " + GameState.getPlayer().getRound() + "</td>";
        labelText += "<td>Lives: " + GameState.getPlayer().getLives() + "</td>";
        labelText += "</tr><tr>";

        BigDecimal elapsed = Util.getElapsedTime(GameState.getStartTime()).setScale(2, RoundingMode.HALF_UP);

        labelText += "<td>Stopwatch: " + elapsed                 + "</td>";
        labelText += "<td>FPS: " + Metrics.calculateFPS()        + "</td>";
        labelText += "<td>Units: " + GameState.getUnits().size() + "</td>";

        labelText += "</tr></table></html>";
        return labelText;
    }

    public static void main(String[] args)
    {
        GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DisplayMode displayMode = graphicsDevice.getDisplayMode();
        DisplayInfo.setDisplayWidth(displayMode.getWidth());
        DisplayInfo.setDisplayHeight(displayMode.getHeight());

        GameState.setGameMap(new GameMap(1024, 768));
        GameState.getGameMap().setWorldWidthInTiles(GameState.getGameMap().getWidth() / 32);
        GameState.getGameMap().setWorldHeightInTiles(GameState.getGameMap().getHeight() / 32);
        DisplayInfo.setTotalScreenHeight(GameState.getGameMap().getHeight());

        final JFrame frame = new MyFrame();
        final JPanel panel = (JPanel) frame.getContentPane();

        panel.setPreferredSize(new Dimension(GameState.getGameMap().getWidth(), GameState.getGameMap().getHeight() + 96));
        panel.setLayout(null);

        GameCanvas gameCanvas = new GameCanvas();
        gameCanvas.setBounds(0, 0, GameState.getGameMap().getWidth(), GameState.getGameMap().getHeight());
        gameCanvas.setIgnoreRepaint(true);
        panel.add(gameCanvas);

        label = new JLabel();
        label.setVisible(true);

        final JToggleButton arrowButton = new JToggleButton("Arrow Tower", new ImageIcon(UnitPainter.GUARD_TOWER), true);
        arrowButton.setVisible(true);
        final JToggleButton glaiveButton = new JToggleButton("Glaive Tower", new ImageIcon(UnitPainter.CANNON_TOWER));
        glaiveButton.setVisible(true);

        panel.setLayout(new FlowLayout());
        panel.add(label);
        panel.add(arrowButton);
        panel.add(glaiveButton);

        arrowButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if (arrowButton.isSelected())
                {
                    towerToggle = "Arrow";
                    glaiveButton.setSelected(false);
                }
                else
                {
                    towerToggle = "Glaive";
                }
            }
        });

        glaiveButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if (glaiveButton.isSelected())
                {
                    towerToggle = "Glaive";
                    arrowButton.setSelected(false);
                }
                else
                {
                    towerToggle = "Arrow";
                }
            }
        });

        frame.pack();

        // Let our Canvas know we want to do Double Buffering
        gameCanvas.createBufferStrategy(2);
        BufferStrategy bufferStrategy = gameCanvas.getBufferStrategy();

        runGameLoop(bufferStrategy);
    }

    private static void runGameLoop(BufferStrategy bufferStrategy)
    {
        Init.init();
        terrainImage = MapBuilder.buildMap();

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
            if (GameState.getPlayer().getRound() > 5 && UnitLogic.getUnitsOnTeam(2) == 0)
            {
                runningSimulation = false;
                stopSimulationReason = "YOU WIN!";
            }
        }
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
}
