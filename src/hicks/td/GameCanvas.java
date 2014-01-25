package hicks.td;

import hicks.td.entities.GameMap;
import hicks.td.entities.Unit;
import hicks.td.ui.*;
import hicks.td.util.MapBuilder;
import hicks.td.util.Metrics;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public final class GameCanvas extends Canvas
{
    private static BufferedImage terrainImage;
    private static Unit selectedUnit = new Unit();

    private static boolean runningSimulation = true;
    private static String stopSimulationReason = "";

    public GameCanvas()
    {
        setSize(GameState.getGameMap().getWidth(), DisplayInfo.getTotalScreenHeight());

        addKeyListener(new MyKeyListener());
        addMouseListener(new MyMouseListener());
        addMouseMotionListener(new MyMouseMotionListener());
    }

    public static void paintWorld(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;

        g2d.drawImage(terrainImage, 0, 0, null);

        UnitPainter.drawUnits(g2d);

        InterfacePainter.drawInterface(g2d);

        MyButton myButton = new MyButton("test", 500, 500, 100, 25);
        myButton.draw(g2d);

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

    public static void main(String[] args)
    {
        GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DisplayMode displayMode = graphicsDevice.getDisplayMode();

        GameState.setGameMap(new GameMap(displayMode.getWidth() - displayMode.getWidth() % 32 - 128, displayMode.getHeight() - displayMode.getHeight() % 32 - 64 - 128));
        GameState.getGameMap().setWorldWidthInTiles(GameState.getGameMap().getWidth() / 32);
        GameState.getGameMap().setWorldHeightInTiles(GameState.getGameMap().getHeight() / 32);
        DisplayInfo.setTotalScreenHeight(GameState.getGameMap().getHeight() + 64);

        final JFrame frame = new MyFrame();
        final JPanel panel = (JPanel) frame.getContentPane();

        panel.setPreferredSize(new Dimension(GameState.getGameMap().getWidth(), DisplayInfo.getTotalScreenHeight()));
        panel.setLayout(null);

        GameCanvas gameCanvas = new GameCanvas();
        gameCanvas.setBounds(0, 0, GameState.getGameMap().getWidth(), DisplayInfo.getTotalScreenHeight());
        gameCanvas.setIgnoreRepaint(true);
        panel.add(gameCanvas);
        frame.pack();

        // Let our Canvas know we want to do Double Buffering
        gameCanvas.createBufferStrategy(2);
        BufferStrategy bufferStrategy = gameCanvas.getBufferStrategy();

        //---------------

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
}
