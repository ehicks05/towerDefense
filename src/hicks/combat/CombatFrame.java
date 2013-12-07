package hicks.combat;

import javax.swing.*;
import java.awt.*;

public class CombatFrame extends JFrame
{
    public CombatFrame()
    {
        getContentPane().setPreferredSize(new Dimension(800, 600));
        add(new CombatPanel());
        pack();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("hicks.combat.CombatFrame");
        setResizable(false);
        setVisible(true);
    }

    public static void main(String[] args)
    {
        JFrame frame = new CombatFrame();

    }
}