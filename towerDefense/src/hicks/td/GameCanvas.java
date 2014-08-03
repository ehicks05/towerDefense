package hicks.td;

import hicks.td.entities.UnitLogic;
import hicks.td.ui.*;
import hicks.td.util.Metrics;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public final class GameCanvas extends Canvas
{
    private static MyGamePanel gamePanel;
    private static JPanel mainMenuPanel;
    private static JPanel cards;
    private static CardLayout cardLayout;

    public GameCanvas()
    {
        this.setSize(World.getGameMap().getWidth(), World.getGameMap().getHeight());

        this.addKeyListener(new MyKeyListener());
        this.addMouseListener(new MyMouseListener());
        this.addMouseMotionListener(new MyMouseMotionListener());

        this.setBounds(0, 0, World.getGameMap().getWidth(), World.getGameMap().getHeight());
        this.setIgnoreRepaint(true);
    }

    public static void main(String[] args)
    {
        Init.init();

        JFrame frame = new MyFrame();
        gamePanel = new MyGamePanel();

        InterfaceLogic.infoLabel = new JLabel();
        InterfaceLogic.infoLabel.setVisible(true);
        gamePanel.add(InterfaceLogic.infoLabel);

        GameCanvas gameCanvas = new GameCanvas();
        gamePanel.add(gameCanvas);

        mainMenuPanel = new MainMenuPanel();

        cards = new JPanel(new CardLayout());
        cards.add(gamePanel);
        cards.add(mainMenuPanel);
        frame.add(cards);

        cardLayout = (CardLayout) cards.getLayout();
        cardLayout.addLayoutComponent(gamePanel, "gamePanel");
        cardLayout.addLayoutComponent(mainMenuPanel, "mainMenuPanel");
        cardLayout.show(cards, mainMenuPanel.getName());

        frame.pack();

        // Let our Canvas know we want to do Double Buffering
        gameCanvas.createBufferStrategy(2);
        BufferStrategy bufferStrategy = gameCanvas.getBufferStrategy();

        runGameLoop(bufferStrategy);
    }

    private static void runGameLoop(BufferStrategy bufferStrategy)
    {
        final int DELAY = 16666666; // (16 ms)
        long beforeTime = System.nanoTime();
        long sleep;

        // run game loop
        while (true)
        {
            // loops through every unit on the map and updates their state
            if (InterfaceLogic.runningSimulation)
                BehaviorLogic.updateState();

            // Grab the current non visible frame (Memory on the graphics card)
            // getDrawGraphics actually creates a new off screen buffer; it doesn't get something that already exists.
            Graphics2D frameBuffer = (Graphics2D) bufferStrategy.getDrawGraphics();

            InterfaceLogic.paintWorld(frameBuffer);

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
            if (World.getPlayer().getLives() <= 0)
            {
                InterfaceLogic.setRunningSimulation(false);
                InterfaceLogic.stopSimulationReason = "YOU LOSE!";
            }
            if (World.getPlayer().getWaveNumber() > World.getWaves().size() && UnitLogic.getUnitsOnTeam(2) == 0)
            {
                InterfaceLogic.setRunningSimulation(false);
                InterfaceLogic.stopSimulationReason = "YOU WIN!";
            }
        }
    }

    public static void openMenu()
    {
        InterfaceLogic.pauseGame();
        cardLayout.show(cards, mainMenuPanel.getName());
    }

    public static void closeMenu()
    {
        cardLayout.show(cards, gamePanel.getName());
        InterfaceLogic.resumeGame();
    }

    //
    public static MyGamePanel getGamePanel()
    {
        return gamePanel;
    }

    public static void setGamePanel(MyGamePanel gamePanel)
    {
        GameCanvas.gamePanel = gamePanel;
    }

}
