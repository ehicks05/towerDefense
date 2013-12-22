package hicks.combat;

import hicks.combat.entities.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameCanvas extends Canvas
{
    private static final Image PEASANT = new ImageIcon("wc2h_peasant.gif").getImage();
    private static List<Unit> units = new ArrayList<>();
    private static BufferedImage terrainImage;
    private static int WORLD_WIDTH = Init.WORLD_WIDTH;
    private static int WORLD_HEIGHT = Init.WORLD_HEIGHT;

    // -------- SELECTION LASSO
    private static boolean drawSelectionRect;
    private static int selectionRectStartingX;
    private static int selectionRectStartingY;
    private static int selectionRectX;
    private static int selectionRectY;
    private static int selectionRectW;
    private static int selectionRectH;
    private static java.util.List<Unit> selectedUnits = new ArrayList<>();

    // -------- VIEW PORT
    private static boolean pan = false;
    private static int viewPortX = 0;
    private static int viewPortY = 0;
    private static int viewPortW = 640;
    private static int viewPortH = 480;
    private static Set<String> directionKeysPressed = new HashSet<>();

    public GameCanvas()
    {
        setSize(viewPortW, viewPortH);

        addKeyListener(new KeyAdapter()
        {
            public void keyTyped(KeyEvent e)
            {
                super.keyTyped(e);
            }

            public void keyPressed(KeyEvent e)
            {
                super.keyPressed(e);
                pan = true;
                int keyCode = e.getKeyCode();

                if (keyCode == KeyEvent.VK_DOWN) directionKeysPressed.add("down");
                if (keyCode == KeyEvent.VK_UP) directionKeysPressed.add("up");
                if (keyCode == KeyEvent.VK_LEFT) directionKeysPressed.add("left");
                if (keyCode == KeyEvent.VK_RIGHT) directionKeysPressed.add("right");
            }

            public void keyReleased(KeyEvent e)
            {
                super.keyReleased(e);
                int keyCode = e.getKeyCode();

                if (keyCode == KeyEvent.VK_DOWN) directionKeysPressed.remove("down");
                if (keyCode == KeyEvent.VK_UP) directionKeysPressed.remove("up");
                if (keyCode == KeyEvent.VK_LEFT) directionKeysPressed.remove("left");
                if (keyCode == KeyEvent.VK_RIGHT) directionKeysPressed.remove("right");

                if (directionKeysPressed.size() == 0) pan = false;
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

            int adjustedX = unitX - viewPortX;
            int adjustedY = unitY - viewPortY;

            if (adjustedX >= selectionRectX && adjustedX <= (selectionRectX + selectionRectW) &&
                    adjustedY >= selectionRectY && adjustedY <= (selectionRectY + selectionRectH))
            {
                if (unit.getTeam() == GameState.getTeamChosen()) selectedUnits.add(unit);
            }
        }
    }

    private static void drawUnits(Graphics2D g2d)
    {
        for (Unit unit : units)
        {
            int x = (int) unit.getLocation().getX();
            int y = (int) unit.getLocation().getY();

            drawVisionCircle(g2d, unit);

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
                g2d.drawImage(PEASANT, (x - size/2) - viewPortX, (y - size/2) - viewPortY, size, size, null);
            else
                g2d.fillRect(x - viewPortX, y - viewPortY, size, size);

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
            g2d.drawRect((x - 8 + (i * 2)) - viewPortX, (y - 4) - viewPortY, 2, 2);
    }

    private static void drawVisionCircle(Graphics2D g2d, Unit unit)
    {
        int x = (int) unit.getLocation().getX();
        int y = (int) unit.getLocation().getY();

        g2d.setColor(Color.getHSBColor(.12f, .12f, .12f));

        int size = unit.getSightRadius();

        g2d.drawOval((x - size) - viewPortX, (y - size) - viewPortY, size * 2, size * 2);
    }

    public static void paintWorld(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;

        g2d.drawImage(terrainImage, 0 - viewPortX, 0 - viewPortY, null);

        drawUnits(g2d);

        int x = 10;
        int y = 0;

        BigDecimal fps = Metrics.calculateFPS();

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
        if (pan) performPan();

        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    public static void performPan()
    {
        if (directionKeysPressed.contains("down") && (viewPortY + viewPortH) < WORLD_HEIGHT) viewPortY++;
        if (directionKeysPressed.contains("up") && (viewPortY) > 0) viewPortY--;
        if (directionKeysPressed.contains("right") && (viewPortX + viewPortW) < WORLD_WIDTH) viewPortX++;
        if (directionKeysPressed.contains("left") && (viewPortX) > 0) viewPortX--;
    }

    public static void main(String[] args)
    {
        final JFrame frame = new JFrame("CombatFrame");
        frame.getContentPane().setPreferredSize(new Dimension(viewPortW, viewPortH));

        GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        graphicsDevice.getDisplayMode();

        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.setLocation(32, 32);
        frame.setResizable(false);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter()
        {
            public void windowOpened(WindowEvent e)
            {
                String[] options = {"Team 1", "Team 2"};
                int answer = JOptionPane.showOptionDialog(frame, "Choose your team", "Choose Team",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, "Team 1");

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

        panel.setPreferredSize(new Dimension(viewPortW, viewPortH));
        panel.setLayout(null);

        GameCanvas gameCanvas = new GameCanvas();
        gameCanvas.setBounds(0, 0, viewPortW, viewPortH);
        panel.add(gameCanvas);
        gameCanvas.setIgnoreRepaint(true);
        frame.pack();

        // Let our Canvas know we want to do Double Buffering
        gameCanvas.createBufferStrategy(2);
        BufferStrategy bufferStrategy = gameCanvas.getBufferStrategy();

        //---------------

        Init.init();
        terrainImage = MapBuilder.buildTerrainImage(WORLD_WIDTH, WORLD_HEIGHT);

        final int DELAY = 16666666; // (16 ms)
        long beforeTime = System.nanoTime();
        long sleep;

        // run game loop
        while (GameLogic.teamsLeft(GameState.getUnits()).size() > 1)
        {
            // loops through every unit on the map and updates their state
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
            sleep = (1*DELAY - Metrics.timeDiff) / 1000000;
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

        }
        Log.logInfo("Team " + GameLogic.teamsLeft(GameState.getUnits()).get(0) + " wins!", true);
    }
}
