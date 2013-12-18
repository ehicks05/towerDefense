package hicks.combat;

import hicks.combat.entities.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

public class GameCanvas extends Canvas
{
    private static final int TILE_SIZE = 1;
    private static final Image PEASANT = new ImageIcon("wc2h_peasant.gif").getImage();
    private static final int FRAMES_TO_AVERAGE = 30;

    private static Queue<BigDecimal> frameTimes = new ArrayBlockingQueue<>(30);
    private static java.util.List<Unit> units = new ArrayList<>();
    private static int[][] terrain;
    private static BufferedImage terrainImage;
    private static long timeDiff;
    private static int width = Init.WIDTH;
    private static int height = Init.HEIGHT;

    private static boolean drawSelectionRect;
    private static int selectionRectStartingX;
    private static int selectionRectStartingY;
    private static int selectionRectX;
    private static int selectionRectY;
    private static int selectionRectW;
    private static int selectionRectH;

    private static java.util.List<Unit> selectedUnits = new ArrayList<>();

    public GameCanvas()
    {
        setSize(width, height);
        setBackground(Color.BLACK);
//        setDoubleBuffered(true);

//        addKeyListener(new MyKeyboardEventListener());
//        addMouseListener(new MyMouseEventListener());
//        addMouseMotionListener(new MyMouseEventListener());

        addMouseListener(new MouseAdapter()
        {
            public void mousePressed(MouseEvent e)
            {
                super.mousePressed(e);

                selectionRectX = e.getX();
                selectionRectY = e.getY();
                selectionRectStartingX = e.getX();
                selectionRectStartingY = e.getY();
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
                if (e.getButton() == 1)
                    selectedUnits = new ArrayList<>();
                if (e.getButton() == 3)
                {
                    Point newDestination = new Point(e.getX(), e.getY());
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

                if (e.getX() < selectionRectStartingX) selectionRectX = e.getX();
                else                                   selectionRectX = selectionRectStartingX;

                if (e.getY() < selectionRectStartingY) selectionRectY = e.getY();
                else                                   selectionRectY = selectionRectStartingY;

                // determine width and height
                selectionRectW = Math.abs(selectionRectStartingX - e.getX());
                selectionRectH = Math.abs(selectionRectStartingY - e.getY());
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
                if (unit.getTeam() == GameState.getTeamChosen()) selectedUnits.add(unit);
            }
        }
    }

    private static BigDecimal calculateFPS()
    {
        BigDecimal fps = BigDecimal.ZERO;

        BigDecimal timeDifference = new BigDecimal(timeDiff).divide(new BigDecimal("1000000"), 2, RoundingMode.HALF_UP);
        frameTimes.add(timeDifference);
        if (frameTimes.size() == FRAMES_TO_AVERAGE)
        {
            BigDecimal sum = BigDecimal.ZERO;
            for (BigDecimal frameTime : frameTimes)
                sum = sum.add(frameTime);

            BigDecimal averageFrameTime = sum.divide(new BigDecimal(FRAMES_TO_AVERAGE), 2, RoundingMode.HALF_UP);
            fps = new BigDecimal("1000").divide(averageFrameTime, 2, RoundingMode.HALF_UP);

            frameTimes.remove();
        }
        return fps;
    }

    private static void drawUnits(Graphics2D g2d)
    {
        for (Unit unit : units)
        {
            int x = (int) unit.getLocation().getX();
            int y = (int) unit.getLocation().getY();
            if (unit.getTeam() == 0)
            {
                g2d.setColor(Color.RED);
                if (unit instanceof Barracks) g2d.setColor(Color.PINK);
                if (unit instanceof Berserker) g2d.setColor(new Color(150, 0, 0));
            }
            if (unit.getTeam() == 1)
            {
                g2d.setColor(Color.GREEN);
                if (unit instanceof Barracks) g2d.setColor(Color.YELLOW);
                if (unit instanceof Berserker) g2d.setColor(new Color(0, 120, 0));
            }

            int size = 3;
            if (unit instanceof Knight || unit instanceof Berserker) size = 5;
            if (unit instanceof Barracks) size = 10;
            if (unit instanceof Peasant) size = 12;

            if (unit instanceof Peasant)
                g2d.drawImage(PEASANT, x - size/2, y - size/2, size, size, null);
            else
                g2d.fillRect(x, y, size, size);

            if (selectedUnits.contains(unit)) drawHealthBar(g2d, unit, x, y);
        }
    }

    private static void drawHealthBar(Graphics2D g2d, Unit unit, int x, int y)
    {
        double currentHpPercent = (unit.getCurrentHp() * 100) / unit.getMaxHp();
        int hpBoxes = (int) (currentHpPercent / 10);

        if (unit.getTeam() == 0)
            g2d.setColor(Color.RED);
        if (unit.getTeam() == 1)
            g2d.setColor(Color.GREEN);
        for (int i = 0; i < hpBoxes; i++)
            g2d.drawRect(x - 8 + (i * 2), y - 4, 2, 2);
    }

    private static void drawVisionCircles(Graphics2D g2d)
    {
        for (Unit unit : units)
        {
            int x = (int) unit.getLocation().getX();
            int y = (int) unit.getLocation().getY();

            g2d.setColor(Color.getHSBColor(.12f, .12f, .12f));

            int size = unit.getSightRadius();

            g2d.drawOval(x - size, y - size, size * 2, size * 2);
        }
    }

    private static int[][] buildTerrain()
    {
        Random gen = new Random();

        int[][] terrain = new int[width][height];
        for (int i = 0; i < width; i += TILE_SIZE)
        {
            for (int j = 0; j < height; j += TILE_SIZE)
            {
                if (gen.nextFloat() > 0.9)
                    terrain[i][j] = 0;
                else
                    terrain[i][j] = 1;
            }
        }
        return terrain;
    }

    private static BufferedImage buildTerrainImage(int[][] terrain)
    {
        BufferedImage bf = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < width; i += TILE_SIZE)
        {
            for (int j = 0; j < height; j += TILE_SIZE)
            {
                if (terrain[i][j] == 0)
                    bf.setRGB(i, j, new Color(40, 60, 16).getRGB());
                else
                    bf.setRGB(i, j, new Color(36, 36, 16).getRGB());
            }
        }
        return bf;
    }

    public static void paintWorld(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;

        g2d.drawImage(terrainImage, 0, 0, null);

        drawVisionCircles(g2d);

        drawUnits(g2d);

        int x = 10;
        int y = 0;

        BigDecimal fps = calculateFPS();

        g2d.setColor(Color.WHITE);
        g2d.drawString("Selected Team: " + GameState.getTeamChosen(), x, y += 15);
        g2d.drawString("Stopwatch: " + GameLogic.getElapsedTime(GameState.getStartTime()).setScale(2, RoundingMode.HALF_UP), x, y += 15);
        g2d.drawString("FPS: " + fps, x, y += 15);
        g2d.drawString("Units: " + units.size(), x, y += 15);
        g2d.setColor(Color.RED);
        g2d.drawString("Team1: " + GameLogic.getUnitsOnTeam(units, 0), x, y += 15);
        g2d.setColor(Color.GREEN);
        g2d.drawString("Team2: " + GameLogic.getUnitsOnTeam(units, 1), x, y += 15);

        if (drawSelectionRect) g2d.drawRect(selectionRectX, selectionRectY, selectionRectW, selectionRectH);

        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }


    public static void main(String[] args)
    {
        final JFrame frame = new JFrame("CombatFrame");
        frame.getContentPane().setPreferredSize(new Dimension(Init.WIDTH, Init.HEIGHT));
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter()
        {
            public void windowOpened(WindowEvent e)
            {
                String[] options = {"Team 1", "Team 2"};
                int answer = JOptionPane.showOptionDialog(frame, "Choose your team", "Choose Team", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, "Team 1");

                GameState.setTeamChosen(answer);
            }

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

        panel.setPreferredSize(new Dimension(Init.WIDTH, Init.HEIGHT));
        panel.setLayout(null);

        GameCanvas gameCanvas = new GameCanvas();
        gameCanvas.setBounds(0, 0, Init.WIDTH , Init.HEIGHT);
        panel.add(gameCanvas);
        gameCanvas.setIgnoreRepaint(true);
        frame.pack();

        // Let our Canvas know we want to do Double Buffering
        gameCanvas.createBufferStrategy(2);
        BufferStrategy bufferStrategy = gameCanvas.getBufferStrategy();

        //---------------

        Init.init();
        terrain = buildTerrain();
        terrainImage = buildTerrainImage(terrain);

        final int DELAY = 16000000; // (16 ms)
        long beforeTime = System.nanoTime();
        long sleep;

        // run game loop
        while (GameLogic.teamsLeft(GameState.getUnits()).size() > 1)
        {
            // loops through every unit on the map and updates their state
            BehaviorLogic.updateState();

            units = new ArrayList<>(GameState.getUnits());

            // Grab the current non visible frame (Memory on the graphics card)
            // getDrawGraphics actually creates a new off screen buffer; it doesn't get something that already exists.
            Graphics2D frameBuffer = (Graphics2D) bufferStrategy.getDrawGraphics();

            paintWorld(frameBuffer);

            // Release the off screen buffer
            frameBuffer.dispose();

            // Flip the off screen buffer back in.
            bufferStrategy.show();

            long now = System.nanoTime();
            timeDiff = now - beforeTime;
//            sleep = (DELAY - timeDiff) / 1000000;
//
//            if (sleep < 0)
//                sleep = 2;
//            try
//            {
//                Thread.sleep(sleep);
//            }
//            catch (InterruptedException e)
//            {
//                System.out.println("interrupted");
//            }
//
            beforeTime = System.nanoTime();
        }
        Log.logInfo("Team " + GameLogic.teamsLeft(GameState.getUnits()).get(0) + " wins!", true);
    }
}
