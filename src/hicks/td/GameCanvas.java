package hicks.td;

import hicks.td.entities.Player;
import hicks.td.logic.BehaviorLogic;
import hicks.td.ui.*;
import hicks.td.net.HighScoreClient;
import hicks.td.util.Log;
import hicks.td.util.Metrics;
import hicks.td.util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

public final class GameCanvas extends Canvas
{
    private static JFrame frame;
    private static JMenuBar menuBar;
    private static MyGamePanel gamePanel;
    private static MainMenuPanel mainMenuPanel;
    private static JPanel cards;
    private static CardLayout cardLayout;

    public static long frameTime;
    public static long accumulator;

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
        Init.init(true);

        frame = new MyFrame();
        menuBar = frame.getJMenuBar();

        gamePanel = new MyGamePanel();

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

//    private static void runGameLoop(BufferStrategy bufferStrategy)
//    {
////        final int DELAY = 16666666; // (16 ms)
//        final int DELAY = 33333333; // (33 ms)
//        long beforeTime = System.nanoTime();
//        long sleep;
//
//        // run game loop
//        while (true)
//        {
//            // loops through every unit on the map and updates their state
//            if (InterfaceLogic.runningSimulation)
//                BehaviorLogic.updateState();
//
//            // Grab the current non visible frame (Memory on the graphics card)
//            // getDrawGraphics actually creates a new off screen buffer; it doesn't get something that already exists.
//            Graphics2D frameBuffer = (Graphics2D) bufferStrategy.getDrawGraphics();
//
//            InterfaceLogic.paintWorld(frameBuffer);
//
//            // Release the off screen buffer
//            frameBuffer.dispose();
//
//            // Flip the off screen buffer back in.
//            bufferStrategy.show();
//
//            long now = System.nanoTime();
//            Metrics.timeDiff = now - beforeTime;
//            sleep = (DELAY - Metrics.timeDiff) / 1000000;
//            beforeTime = System.nanoTime();
//
//            if (sleep < 0)
//                sleep = 1;
//            try
//            {
//                Thread.sleep(sleep);
//            }
//            catch (InterruptedException e)
//            {
//                System.out.println("interrupted");
//            }
//
//            checkGameOverConditions();
//        }
//    }

    // todo alternate game loop logic...
    private static void runGameLoop(BufferStrategy bufferStrategy)
    {
        double t = 0;
//        final int dt = 33333333; // (33 ms)
        final int dt = 16666666; // (16 ms)

        long currentTime = System.nanoTime();
        accumulator = 0;

        // run game loop
        while (true)
        {
            long newTime = System.nanoTime();
            frameTime = newTime - currentTime;
            if (frameTime < dt)
            {
                long waitTime = (dt - frameTime) / 1_000_000;
                try
                {
                    Thread.sleep(waitTime);
                }
                catch (InterruptedException e)
                {
                    Log.info(e.getMessage());
                }
            }
            newTime = System.nanoTime();
            frameTime = newTime - currentTime;
            currentTime = newTime;
            Metrics.timeDiff = frameTime;
            accumulator += frameTime;

            if (InterfaceLogic.runningSimulation)
            {
                while(accumulator >= dt)
                {
                    BigDecimal dtInSeconds = new BigDecimal(String.valueOf(dt)).divide(new BigDecimal(String.valueOf(1_000_000_000)), 5, BigDecimal.ROUND_HALF_UP);
                    BehaviorLogic.updateState(dtInSeconds);
                    accumulator -= dt;
                    t += dt;
                }
            }

            // Grab the current non visible frame (Memory on the graphics card)
            // getDrawGraphics actually creates a new off screen buffer; it doesn't get something that already exists.
            Graphics2D frameBuffer = (Graphics2D) bufferStrategy.getDrawGraphics();

            InterfaceLogic.paintWorld(frameBuffer);

            // Release the off screen buffer
            frameBuffer.dispose();

            // Flip the off screen buffer back in.
            bufferStrategy.show();

            if (!Arrays.asList("RIP!", "YOU WIN!").contains(InterfaceLogic.stopSimulationReason))
                checkGameOverConditions();
        }
    }

    private static void checkGameOverConditions()
    {
        boolean playerLost = World.getPlayer().getLives() <= 0;
        boolean playerWon = World.getPlayer().getWaveNumber() > World.getWaves().size() && Util.getMobs().size() == 0;

        if (playerLost || playerWon)
        {
            InterfaceLogic.setRunningSimulation(false);
            if (playerLost) InterfaceLogic.stopSimulationReason = "RIP!";
            if (playerWon) InterfaceLogic.stopSimulationReason = "YOU WIN!";
            reportScore();
        }
    }

    private static void reportScore()
    {
        String playerName = getPlayerName();
        if (playerName != null)
        {
            Player player = World.getPlayer();
            String score = playerName + "\t" + player.getWaveNumber() + "\t" + player.getLives() + "\t" + player.getGold() + "\t" + LocalDate.now();

            try
            {
                HighScoreClient.sendScore(score);
                showHighScoresDialog();
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void showHighScoresDialog()
    {
        try
        {
            String highScores = HighScoreClient.getHighScores();
            JOptionPane.showMessageDialog(frame, highScores, "High Scores", JOptionPane.INFORMATION_MESSAGE);
        }
        catch (Exception e)
        {
            Log.info(e.getMessage());
        }
    }

    private static String getPlayerName()
    {
        return JOptionPane.showInputDialog(frame, "Enter your name:", "Share Your High Score", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void openMenu()
    {
        InterfaceLogic.pauseGame();
        getMenuBar().setVisible(false);
        cardLayout.show(cards, mainMenuPanel.getName());
    }

    public static void closeMenu()
    {
        cardLayout.show(cards, gamePanel.getName());
        getMenuBar().setVisible(true);
        InterfaceLogic.resumeGame();
    }

    //


    public static JMenuBar getMenuBar()
    {
        return menuBar;
    }

    public static MyGamePanel getGamePanel()
    {
        return gamePanel;
    }

    public static MainMenuPanel getMainMenuPanel()
    {
        return mainMenuPanel;
    }
}
