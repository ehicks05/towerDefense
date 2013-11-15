import javax.swing.*;
import java.awt.*;

public class CombatFrame extends JFrame
{
    public CombatFrame()
    {
        getContentPane().setPreferredSize(new Dimension(700, 700));
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