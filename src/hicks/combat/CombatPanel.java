package hicks.combat;

import hicks.combat.entities.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

public class CombatPanel extends JPanel implements Runnable
{
    private final int TILE_SIZE = 1;
    private final Image PEASANT = new ImageIcon("wc2h_peasant.gif").getImage();
    private final int FRAMES_TO_AVERAGE = 30;

    private Queue<BigDecimal> frameTimes = new ArrayBlockingQueue<>(30);
    private List<Unit> units = new ArrayList<>();
    private int[][] terrain;
    private BufferedImage terrainImage;
    private long timeDiff;
    private int width = Init.WIDTH;
    private int height = Init.HEIGHT;

    boolean drawSelectionRect;
    private int selectionRectStartingX;
    private int selectionRectStartingY;
    private int selectionRectX;
    private int selectionRectY;
    private int selectionRectW;
    private int selectionRectH;

    public CombatPanel()
    {
        setSize(width, height);
        setBackground(Color.BLACK);
        setDoubleBuffered(true);

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
                drawSelectionRect = false;
                selectionRectW = 0;
                selectionRectH = 0;
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

    public void addNotify()
    {
        super.addNotify();
        Thread animator = new Thread(this);
        animator.start();
    }

    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.drawImage(terrainImage, 0, 0, null);

        drawVisionCircles(g2d);

        drawUnits(g2d);

        int x = 10;
        int y = 0;

        BigDecimal fps = calculateFPS();

        g2d.setColor(Color.WHITE);
        g2d.drawString("Stopwatch: " + GameLogic.getElapsedTime(GameState.getStartTime()).setScale(2, RoundingMode.HALF_UP), x, y += 15);
        g2d.drawString("FPS: " + fps, x, y += 15);
        g2d.drawString("Units: " + units.size(), x, y += 15);
        g2d.setColor(Color.RED);
        g2d.drawString("Team1: " + GameLogic.getUnitsOnTeam(units, 1), x, y += 15);
        g2d.setColor(Color.GREEN);
        g2d.drawString("Team2: " + GameLogic.getUnitsOnTeam(units, 2), x, y += 15);

        if (drawSelectionRect) g2d.drawRect(selectionRectX, selectionRectY, selectionRectW, selectionRectH);

        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    private BigDecimal calculateFPS()
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

    private void drawUnits(Graphics2D g2d)
    {
        for (Unit unit : units)
        {
            int x = (int) unit.getLocation().getX();
            int y = (int) unit.getLocation().getY();
            if (unit.getTeam() == 1)
            {
                g2d.setColor(Color.RED);
                if (unit instanceof Barracks) g2d.setColor(Color.PINK);
                if (unit instanceof Berserker) g2d.setColor(new Color(150, 0, 0));
            }
            if (unit.getTeam() == 2)
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

            drawHealthBar(g2d, unit, x, y);
        }
    }

    private void drawHealthBar(Graphics2D g2d, Unit unit, int x, int y)
    {
        double currentHpPercent = (unit.getCurrentHp() * 100) / unit.getMaxHp();
        int hpBoxes = (int) (currentHpPercent / 10);

        if (unit.getTeam() == 1)
            g2d.setColor(Color.RED);
        if (unit.getTeam() == 2)
            g2d.setColor(Color.GREEN);
        for (int i = 0; i < hpBoxes; i++)
            g2d.drawRect(x - 8 + (i * 2), y - 4, 2, 2);
    }

    private void drawVisionCircles(Graphics2D g2d)
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

    private int[][] buildTerrain()
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

    private BufferedImage buildTerrainImage(int[][] terrain)
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

    public void run()
    {
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
            repaint();

            long now = System.nanoTime();
            timeDiff = now - beforeTime;
            sleep = (DELAY - timeDiff) / 1000000;

            if (sleep < 0)
                sleep = 2;
            try
            {
                Thread.sleep(sleep);
            }
            catch (InterruptedException e)
            {
                System.out.println("interrupted");
            }

            beforeTime = System.nanoTime();
        }
        Log.logInfo("Team " + GameLogic.teamsLeft(GameState.getUnits()).get(0) + " wins!", true);
    }
}