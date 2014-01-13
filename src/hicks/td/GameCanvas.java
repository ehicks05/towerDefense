package hicks.td;

import hicks.td.entities.ArrowTower;
import hicks.td.entities.Unit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public final class GameCanvas extends Canvas
{
    public static List<Unit> units = new ArrayList<>();
    private static BufferedImage terrainImage;

    // -------- SELECTION LASSO
    public static Unit selectedUnit = new Unit();

    private static boolean runningSimulation = true;
    private static String stopSimulationReason = "";

    public GameCanvas()
    {
        setSize(Init.WORLD_WIDTH, Init.TOTAL_SCREEN_HEIGHT);

        addKeyListener(new KeyAdapter()
        {
            public void keyTyped(KeyEvent e)
            {
                super.keyTyped(e);
            }

            public void keyPressed(KeyEvent e)
            {
                super.keyPressed(e);
            }

            public void keyReleased(KeyEvent e)
            {
                super.keyReleased(e);
            }
        });

        addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent e)
            {
                super.mouseClicked(e);
                int eventX = e.getX();
                int eventY = e.getY();

                selectedUnit = null;
                if (e.getButton() == 1)
                {
                    for (Unit unit : units)
                    {
                        int unitX = (int) unit.getLocation().getX();
                        int unitY = (int) unit.getLocation().getY();
                        int unitSize = unit.getSizeRadius();

                        int minX = unitX - unitSize;
                        int maxX = unitX + unitSize;
                        int minY = unitY - unitSize;
                        int maxY = unitY + unitSize;

                        if (eventX >= minX && eventX <= maxX && eventY >= minY && eventY <= maxY)
                            selectedUnit = unit;
                    }
                }
                if (e.getButton() == 3)
                {
                    if (GameState.getPlayer().getGold() >= 50)
                    {
                        Unit arrowTower = new ArrowTower(1);
                        arrowTower.setLocation(new Point(eventX, eventY));
                        GameState.addUnit(arrowTower);
                        GameState.getPlayer().removeGold(50);
                    }
                }
            }
        });

        addMouseMotionListener(new MouseAdapter()
        {
            public void mouseDragged(MouseEvent e)
            {
                super.mouseDragged(e);
                int eventX = e.getX();
                int eventY = e.getY();
            }
        });
    }

    public static void paintWorld(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;

        g2d.drawImage(terrainImage, 0, 0, null);

        UnitPainter.drawUnits(g2d);

        drawInterface(g2d);

        if (!runningSimulation)
        {
            Font font = new Font("Helvetica", Font.PLAIN, 36);
            g2d.setFont(font);
            g2d.setColor(Color.BLACK);
            g2d.drawString(stopSimulationReason, Init.WORLD_WIDTH / 2, Init.TOTAL_SCREEN_HEIGHT / 2);
        }

        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    private static void drawInterface(Graphics2D g2d)
    {
        int x = 10;
        int y = Init.WORLD_HEIGHT;

        BigDecimal fps = Metrics.calculateFPS();

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, y, Init.WORLD_WIDTH, Init.INTERFACE_HEIGHT);
        g2d.setColor(Color.WHITE);

        // info column 1
        g2d.drawString("Gold: " + GameState.getPlayer().getGold(), x, y += 15);
        g2d.drawString("Round: " + GameState.getPlayer().getRound(), x, y += 15);
        g2d.drawString("Lives: " + GameState.getPlayer().getLives(), x, y += 15);

        // info column 2
        y -= 45;
        x += 90;

        g2d.drawString("Stopwatch: " + Util.getElapsedTime(GameState.getStartTime()).setScale(2, RoundingMode.HALF_UP), x, y += 15);
        g2d.drawString("FPS: " + fps, x, y += 15);
        g2d.drawString("Units: " + units.size(), x, y += 15);
    }

    public static void main(String[] args)
    {
        GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DisplayMode displayMode = graphicsDevice.getDisplayMode();
        Init.WORLD_WIDTH = displayMode.getWidth() - displayMode.getWidth() % 32 - 128;
        Init.WORLD_HEIGHT = displayMode.getHeight() - displayMode.getHeight() % 32 - 64 - 128;
        Init.TOTAL_SCREEN_HEIGHT = Init.WORLD_HEIGHT + 64;

        final JFrame frame = new JFrame("Eric's Tower Defense");
        frame.getContentPane().setPreferredSize(new Dimension(Init.WORLD_WIDTH, Init.TOTAL_SCREEN_HEIGHT));

        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.setLocation(32, 32);
        frame.setResizable(false);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent windowEvent)
            {
                if (JOptionPane.showConfirmDialog(frame,
                        "Are you sure you want to exit?", "Exit?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
                {
                    Log.logInfo("Game was manually terminated...", true);
                    System.exit(0);
                }
            }
        });

        JPanel panel = (JPanel) frame.getContentPane();

        panel.setPreferredSize(new Dimension(Init.WORLD_WIDTH, Init.TOTAL_SCREEN_HEIGHT));
        panel.setLayout(null);

        GameCanvas gameCanvas = new GameCanvas();
        gameCanvas.setBounds(0, 0, Init.WORLD_WIDTH, Init.TOTAL_SCREEN_HEIGHT);
        panel.add(gameCanvas);
        gameCanvas.setIgnoreRepaint(true);
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

            units = GameState.getUnits();

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
            if (GameState.getPlayer().getRound() > 5)
            {
                runningSimulation = false;
                stopSimulationReason = "YOU WIN!";
            }
        }
    }
}
