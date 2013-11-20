import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.math.BigDecimal;
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
    private Image star;
    private Thread animator;
    private List<Unit> units;
    private int[][] terrain;
    private BigDecimal simulationStart;

    private final int DELAY = 16;

    public CombatPanel()
    {
        setBackground(Color.BLACK);
        setDoubleBuffered(true);

        ImageIcon ii = new ImageIcon("image.png");
        star = ii.getImage();

        units = new ArrayList<>();
        terrain = buildTerrain();
    }

    private int[][] buildTerrain()
    {
        Random gen = new Random();

        int[][] terrain = new int[80][60];
        for (int i = 0; i < 80; i++)
        {
            for (int j = 0; j < 60; j++)
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

            g2d.fillRect(x, y, size, size);
        }

        int x = 10;
        int y = 0;

        g2d.setColor(Color.WHITE);
        g2d.drawString("Stopwatch: " + GameLogic.getElapsedTime(simulationStart), x, y += 15);
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
        for (int i = 0; i < 80; i++)
        {
            for (int j = 0; j < 60; j++)
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
        // create map
        GameMap map = new GameMap();
        Init.init(map);

        // start simulation timer
        simulationStart = GameLogic.getNow();
        Log.logInfo("Simulation starting at " + new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a").format(new Date()));

        // run game loop
        long beforeTime, timeDiff, sleep;
        beforeTime = System.currentTimeMillis();
        while (GameLogic.teamsLeft(map.getExistingUnits()).size() > 1)
        {
            // loops through every unit on the map and updates their state
            GameLogic.updateState(simulationStart, map);

            units = map.getExistingUnits();
            repaint();

            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = DELAY - timeDiff;

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

            beforeTime = System.currentTimeMillis();
        }
        Log.logInfo(simulationStart, "Team " + GameLogic.teamsLeft(map.getExistingUnits()).get(0) + " wins!", true);
    }
}