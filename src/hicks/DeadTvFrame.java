package hicks;

import javax.swing.*;
import java.awt.*;

public class DeadTvFrame extends JFrame
{
    public DeadTvFrame()
    {
        setUndecorated(true);
        getContentPane().setPreferredSize(new Dimension(DeadTVConfig.width, DeadTVConfig.height));
        add(new DeadTvPanel());
        pack();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("DeadTvPanel");
        setResizable(false);
        setVisible(true);
    }

    public static void main(String[] args)
    {
        JFrame frame = new DeadTvFrame();
    }
}