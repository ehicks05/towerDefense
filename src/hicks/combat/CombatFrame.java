package hicks.combat;

import javax.swing.*;
import java.awt.*;

public class CombatFrame extends JFrame
{
    public CombatFrame()
    {
        getContentPane().setPreferredSize(new Dimension(Init.WIDTH, Init.HEIGHT));
        add(new CombatPanel());
        pack();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("CombatFrame");
        setResizable(false);
        setVisible(true);
    }

    public static void main(String[] args)
    {
        JFrame frame = new CombatFrame();

    }
}