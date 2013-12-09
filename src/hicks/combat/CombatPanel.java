package hicks.combat;

import hicks.combat.entities.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class CombatPanel extends JPanel implements Runnable
{
    private final int MS_PER_UPDATE = 8;
    private Image peasant;
    private Thread animator;
    private List<Unit> units;
    private int[][] terrain;
    private static BigDecimal simulationStart;
    private long timeDiff;
    private long beforeTime;
    private long sleep;
    private final int DELAY = 16000000;

    public CombatPanel()
    {
        setBackground(Color.BLACK);
        setDoubleBuffered(true);

        ImageIcon peasant = new ImageIcon("wc2h_peasant.gif");
        this.peasant = peasant.getImage();

        units = new ArrayList<>();
        terrain = buildTerrain();
    }

    public static BigDecimal getSimulationStart()
    {
        return simulationStart;
    }

    private int[][] buildTerrain()
    {
        Random gen = new Random();

        int[][] terrain = new int[81][61];
        for (int i = 0; i < 81; i++)
        {
            for (int j = 0; j < 61; j++)
            {
                if (gen.nextFloat() > 0.9)
                    terrain[i][j] = 0;
                else
                    terrain[i][j] = 1;
            }
        }
        return terrain;
    }

    public void addNotify()
    {
        super.addNotify();
        animator = new Thread(this);
        animator.start();
    }

    public void paint(Graphics g)
    {
        super.paint(g);

        Graphics2D g2d = (Graphics2D) g;

        // draw terrain
        drawTerrain(g2d);

        // draw vision circles
        drawVisionCircles(g2d);

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
                g2d.drawImage(peasant, x - size/2, y - size/2, size, size, null);
            else
                g2d.fillRect(x, y, size, size);
        }

        int x = 10;
        int y = 0;

        BigDecimal timeDifference = new BigDecimal(timeDiff).divide(new BigDecimal("1000000"), 2, RoundingMode.HALF_UP);

        g2d.setColor(Color.WHITE);
        g2d.drawString("Stopwatch: " + GameLogic.getElapsedTime(simulationStart).setScale(2, RoundingMode.HALF_UP), x, y += 15);
//        g2d.drawString("FPS: " + new BigDecimal("1000").divide(timeDifference, 2, RoundingMode.HALF_UP), x, y += 15);
        g2d.drawString("Units: " + units.size(), x, y += 15);
        g2d.setColor(Color.RED);
        g2d.drawString("Team1: " + GameLogic.getUnitsOnTeam(units, 1), x, y += 15);
        g2d.setColor(Color.GREEN);
        g2d.drawString("Team2: " + GameLogic.getUnitsOnTeam(units, 2), x, y += 15);

        Toolkit.getDefaultToolkit().sync();
        g.dispose();
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

    private void drawTerrain(Graphics2D g2d)
    {
        for (int i = 0; i < 81; i++)
        {
            for (int j = 0; j < 61; j++)
            {
                if (terrain[i][j] == 0)
                    g2d.setColor(new Color(40, 60, 16));
                if (terrain[i][j] == 1)
                    g2d.setColor(new Color(36, 36, 16));

                g2d.fillRect(i * 10, j * 10, 10, 10);
            }
        }
    }

    public void run()
    {
        // start simulation timer
        simulationStart = GameLogic.now();
        Log.logInfo("Simulation starting at " + new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a").format(new Date()));

        // create map
        GameMap map = new GameMap();
        Init.init(map);

        // run game loop
        beforeTime = System.nanoTime();
        while (GameLogic.teamsLeft(map.getExistingUnits()).size() > 1)
        {
            // loops through every unit on the map and updates their state
            BehaviorLogic.updateState(simulationStart, map);

            units = map.getExistingUnits();
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
        Log.logInfo(simulationStart, "Team " + GameLogic.teamsLeft(map.getExistingUnits()).get(0) + " wins!", true);
    }
}