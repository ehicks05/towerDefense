package hicks.deadtv;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class DeadTvPanel extends JPanel implements Runnable
{
    private Thread animator;
    private final int DELAY = DeadTVConfig.delay;
    private final int width  = DeadTVConfig.width;
    private final int height = DeadTVConfig.height;

    public DeadTvPanel()
    {
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
    }

    public void addNotify()
    {
        super.addNotify();
        animator = new Thread(this);
        animator.start();
    }

    Font font = new Font("Sans", Font.PLAIN, 48);
    private long time;
    public void paint(Graphics g)
    {
        super.paint(g);

        Graphics2D g2d = (Graphics2D) g;

        Random r = new Random();

        for (int y = 0; y < height; y+=2)
        {
            for (int x = 0; x < width; x+=2)
            {
                float value = r.nextFloat();
                g2d.setColor(new Color(value, value, value));

                g2d.fillRect(x, y, 2, 2);
            }
        }

        g2d.setColor(Color.GREEN);
        g2d.setFont(font);
        g2d.drawString("CH 04", 50, 50);

        long elapsedTime = (System.currentTimeMillis() - time);
        String fps = String.valueOf(1000 / elapsedTime);
        g2d.drawString(fps, 50, 200);
        time = System.currentTimeMillis();

        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    public void run()
    {
        // run animation loop
        long beforeTime, timeDiff, sleep;
        beforeTime = System.currentTimeMillis();
        while (true)
        {
            repaint();

            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = DELAY - timeDiff;

            if (sleep < 0)
                sleep = 2;
            try
            {
                Thread.sleep(sleep);
            }
            catch (InterruptedException e)
            {
                System.out.println("interrupted");
            }

            beforeTime = System.currentTimeMillis();
        }
    }
}