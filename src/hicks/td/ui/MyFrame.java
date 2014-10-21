package hicks.td.ui;

import hicks.td.GameCanvas;
import hicks.td.util.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MyFrame extends JFrame
{
    public JMenuBar menuBar;

    public MyFrame()
    {
        super("Eric's Tower Defense");
        getContentPane().setPreferredSize(new Dimension(DisplayInfo.getWindowWidth(), DisplayInfo.getWindowHeight()));

        setWindowsLookAndFeel();

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setLocation(32, 32);
        setResizable(false);
        setVisible(true);

        menuBar = createMenuBar();
        setJMenuBar(menuBar);
        menuBar.setVisible(false);

        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent windowEvent)
            {
                if (JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to exit?", "Exit?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
                {
                    Log.info("Game was manually terminated...", true);
                    System.exit(0);
                }
            }
        });
    }

    private void setWindowsLookAndFeel()
    {
        try
        {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        }
        catch (Exception e)
        {
            Log.info("Error setting UIManager Look and Feel.");
        }
    }

    private JMenuBar createMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        JMenuItem mainMenuItem = new JMenuItem("Main Menu");
        mainMenuItem.setMnemonic(KeyEvent.VK_M);
        mainMenuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                GameCanvas.openMenu();
            }
        });

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setMnemonic(KeyEvent.VK_X);
        exitItem.addActionListener(new ExitListener());

        fileMenu.add(mainMenuItem);
        fileMenu.add(exitItem);

        JMenu gameMenu = new JMenu("Game");
        gameMenu.setMnemonic(KeyEvent.VK_G);

        final JMenuItem pauseItem = new JCheckBoxMenuItem("Pause", false);
        pauseItem.setMnemonic(KeyEvent.VK_P);
        pauseItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if (pauseItem.isSelected())
                    InterfaceLogic.pauseGame();
                else
                    InterfaceLogic.resumeGame();
            }
        });

        gameMenu.add(pauseItem);

        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H);

        JMenuItem infoItem = new JMenuItem("Info");
        infoItem.setMnemonic(KeyEvent.VK_I);
        infoItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                JOptionPane.showMessageDialog(null, "Build towers on green squares. Enemies will travel along the brown squares. " +
                        "Don't let them make a full loop around the map or you will lose life.", "Help", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.setMnemonic(KeyEvent.VK_A);
        aboutItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                JOptionPane.showMessageDialog(null, "Copyright Eric Hicks, 2014.", "About", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        helpMenu.add(infoItem);
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(gameMenu);
        menuBar.add(helpMenu);

        return menuBar;
    }
}
