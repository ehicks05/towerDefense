package hicks.td;

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

public class GameCanvas extends Canvas
{
    public static List<Unit> units = new ArrayList<>();
    private static BufferedImage terrainImage;

    // -------- SELECTION LASSO
    private static boolean drawSelectionRect;
    private static int selectionRectStartingX;
    private static int selectionRectStartingY;
    private static int selectionRectX;
    private static int selectionRectY;
    private static int selectionRectW;
    private static int selectionRectH;
    public static List<Unit> selectedUnits = new ArrayList<>();

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
            public void mousePressed(MouseEvent e)
            {
                super.mousePressed(e);
                int eventX = e.getX();
                int eventY = e.getY();

                selectionRectX = eventX;
                selectionRectY = eventY;
                selectionRectStartingX = eventX;
                selectionRectStartingY = eventY;
                drawSelectionRect = true;
            }

            public void mouseReleased(MouseEvent e)
            {
                super.mouseReleased(e);

                if (e.getButton() == 1) highlightSelectedUnits();

                drawSelectionRect = false;
                selectionRectW = 0;
                selectionRectH = 0;
            }

            public void mouseClicked(MouseEvent e)
            {
                super.mouseClicked(e);
                int eventX = e.getX();
                int eventY = e.getY();

                if (e.getButton() == 1)
                    selectedUnits = new ArrayList<>();
                if (e.getButton() == 3)
                {
                    Point newDestination = new Point(eventX, eventY);
                    for (Unit unit : selectedUnits)
                    {
                        unit.setDestination(newDestination);
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

                if (eventX < selectionRectStartingX) selectionRectX = eventX;
                else                                   selectionRectX = selectionRectStartingX;

                if (eventY < selectionRectStartingY) selectionRectY = eventY;
                else                                   selectionRectY = selectionRectStartingY;

                selectionRectW = Math.abs(selectionRectStartingX - eventX);
                selectionRectH = Math.abs(selectionRectStartingY - eventY);
            }
        });
    }

    private static void highlightSelectedUnits()
    {
        selectedUnits = new ArrayList<>();
        for (Unit unit : units)
        {
            int unitX = (int) unit.getLocation().getX();
            int unitY = (int) unit.getLocation().getY();

            if (unitX >= selectionRectX && unitX <= (selectionRectX + selectionRectW) &&
                    unitY >= selectionRectY && unitY <= (selectionRectY + selectionRectH))
            {
//                if (unit.getTeam() == 1)
                    selectedUnits.add(unit);
            }
        }
    }

    public static void paintWorld(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;

        g2d.drawImage(terrainImage, 0, 0, null);

        UnitPainter.drawUnits(g2d);

        drawInterface(g2d);

        if (drawSelectionRect)
        {
            g2d.setColor(Color.GREEN);
            g2d.drawRect(selectionRectX, selectionRectY, selectionRectW, selectionRectH);
        }

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

        g2d.drawString("Stopwatch: " + GameLogic.getElapsedTime(GameState.getStartTime()).setScale(2, RoundingMode.HALF_UP), x, y += 15);
        g2d.drawString("FPS: " + fps, x, y += 15);
        g2d.drawString("Units: " + units.size(), x, y += 15);
    }

    public static void main(String[] args)
    {
        final JFrame frame = new JFrame("Eric's Tower Defense");
        frame.getContentPane().setPreferredSize(new Dimension(Init.WORLD_WIDTH, Init.TOTAL_SCREEN_HEIGHT));

        GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        graphicsDevice.getDisplayMode();

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
            if (GameState.getPlayer().getRound() > 40)
            {
                runningSimulation = false;
                stopSimulationReason = "YOU WIN!";
            }
        }
        // if player wins
        // if player loses
    }
}
