package hicks.combat;

import hicks.combat.entities.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

public class CombatPanel extends JPanel implements Runnable
{
    private final int TILE_SIZE = 5;
    private final Image PEASANT = new ImageIcon("wc2h_peasant.gif").getImage();
    private final int FRAMES_TO_AVERAGE = 30;

    private Queue<BigDecimal> frameTimes = new ArrayBlockingQueue<>(30);
    private List<Unit> units = new ArrayList<>();
    private int[][] terrain;
    private long timeDiff;
    private int width;
    private int height;

    public CombatPanel()
    {
        setBackground(Color.BLACK);
        setDoubleBuffered(true);

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
            public void mouseMoved(MouseEvent e)
            {
                super.mouseMoved(e);
            }

            public void mouseClicked(MouseEvent e)
            {
                super.mouseClicked(e);
            }

            public void mousePressed(MouseEvent e)
            {
                super.mousePressed(e);
            }

            public void mouseReleased(MouseEvent e)
            {
                super.mouseReleased(e);
            }

            public void mouseEntered(MouseEvent e)
            {
                super.mouseEntered(e);
            }

            public void mouseExited(MouseEvent e)
            {
                super.mouseExited(e);
            }

            public void mouseWheelMoved(MouseWheelEvent e)
            {
                super.mouseWheelMoved(e);
            }

            public void mouseDragged(MouseEvent e)
            {
                super.mouseDragged(e);
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

        drawTerrain(g2d);

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

    private void drawTerrain(Graphics2D g2d)
    {
        for (int i = 0; i < width; i += TILE_SIZE)
        {
            for (int j = 0; j < height; j += TILE_SIZE)
            {
                if (terrain[i][j] == 0)
                    g2d.setColor(new Color(40, 60, 16));
                if (terrain[i][j] == 1)
                    g2d.setColor(new Color(36, 36, 16));

                g2d.fillRect(i, j, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    public void run()
    {
        Init.init();
        width = GameState.getGameMap().getWidth();
        height = GameState.getGameMap().getHeight();
        terrain = buildTerrain();

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