package hicks.combat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CombatFrame extends JFrame
{
    public CombatFrame()
    {
        getContentPane().setPreferredSize(new Dimension(Init.WIDTH, Init.HEIGHT));
        add(new CombatPanel());
        pack();

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("CombatFrame");
        setResizable(false);
        setVisible(true);
    }

    public static void main(String[] args)
    {
        final JFrame frame = new CombatFrame();

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
    }
}