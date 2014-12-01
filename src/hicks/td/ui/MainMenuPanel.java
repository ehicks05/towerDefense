package hicks.td.ui;

import hicks.td.GameCanvas;
import hicks.td.Init;
import hicks.td.World;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuPanel extends JPanel
{
    private final JButton restartButton;

    public MainMenuPanel()
    {
        this.setPreferredSize(new Dimension(DisplayInfo.getWindowWidth(), DisplayInfo.getWindowHeight()));
        this.setVisible(true);
        this.setBackground(Color.LIGHT_GRAY);
        this.setName("mainMenuPanel");
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        final JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
        InterfaceUtil.setSizeFields(container, new Dimension(800, 600));
        container.setBackground(Color.LIGHT_GRAY);

        container.setVisible(true);
        container.setName("containerPanel");

        final JLabel mainLabel = new JLabel("Eric's Tower Defense");
        Font mainLabelFont = new Font("Verdana", Font.BOLD, 48);
        mainLabel.setFont(mainLabelFont);

        mainLabel.setAlignmentX(CENTER_ALIGNMENT);

        final JButton startButton = new JButton("Start");
        startButton.setVisible(true);
        startButton.setFont(new Font("Verdana", Font.PLAIN, 32));
        startButton.setAlignmentX(CENTER_ALIGNMENT);
        InterfaceUtil.setSizeFields(startButton, new Dimension(240, 80));

        restartButton = new JButton("Restart");
        restartButton.setVisible(false);
        restartButton.setFont(new Font("Verdana", Font.PLAIN, 32));
        restartButton.setAlignmentX(CENTER_ALIGNMENT);
        InterfaceUtil.setSizeFields(restartButton, new Dimension(240, 80));

        final JButton showHighScoresButton = new JButton("High Scores");
        showHighScoresButton.setVisible(true);
        showHighScoresButton.setFont(new Font("Verdana", Font.PLAIN, 32));
        showHighScoresButton.setAlignmentX(CENTER_ALIGNMENT);
        InterfaceUtil.setSizeFields(showHighScoresButton, new Dimension(240, 80));

        final JButton exitButton = new JButton("Exit");
        exitButton.setVisible(true);
        exitButton.setFont(new Font("Verdana", Font.PLAIN, 32));
        exitButton.setAlignmentX(CENTER_ALIGNMENT);
        InterfaceUtil.setSizeFields(exitButton, new Dimension(240, 80));


        startButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                GameCanvas.closeMenu();
                startButton.setText("Resume");
            }
        });

        restartButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                InterfaceLogic.setActiveRound(false);
                InterfaceLogic.setGameStarted(false);
                InterfaceLogic.stopSimulationReason = "";
                Init.init(false);
                GameCanvas.closeMenu();
                GameCanvas.getGamePanel().showNextWaveButton();
                hideRestartGameButton();
            }
        });

        showHighScoresButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                GameCanvas.showHighScoresDialog();
            }
        });

        exitButton.addActionListener(new ExitListener());

        this.add(Box.createRigidArea(new Dimension(0, 50)));
        this.add(container);

        container.add(mainLabel);
        container.add(Box.createRigidArea(new Dimension(0, 50)));

        JLabel picLabel = new JLabel(new ImageIcon(World.getTowerByName("IceTower").getImage()));
        picLabel.setAlignmentX(CENTER_ALIGNMENT);
        container.add(picLabel);

        container.add(Box.createRigidArea(new Dimension(0, 50)));
        container.add(startButton);
        container.add(Box.createRigidArea(new Dimension(0, 10)));
        container.add(restartButton);
        container.add(Box.createRigidArea(new Dimension(0, 10)));
        container.add(showHighScoresButton);
        container.add(Box.createRigidArea(new Dimension(0, 10)));
        container.add(exitButton);
    }

    public void showRestartGameButton()
    {
        restartButton.setVisible(true);
    }

    public void hideRestartGameButton()
    {
        restartButton.setVisible(true);
    }
}
