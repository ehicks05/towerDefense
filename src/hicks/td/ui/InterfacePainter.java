package hicks.td.ui;

import hicks.td.GameState;
import hicks.td.util.Metrics;
import hicks.td.util.Util;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class InterfacePainter
{
    public static void drawInterface(Graphics2D g2d)
    {
        int x = 10;
        int y = GameState.getGameMap().getHeight();

        BigDecimal fps = Metrics.calculateFPS();

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, y, GameState.getGameMap().getWidth(), DisplayInfo.getInterfaceHeight());
        g2d.setColor(Color.WHITE);

        // info column 1
        g2d.drawString("Gold: " + GameState.getPlayer().getGold(), x, y += 15);
        g2d.drawString("Round: " + GameState.getPlayer().getRound(), x, y += 15);
        g2d.drawString("Lives: " + GameState.getPlayer().getLives(), x, y += 15);

        // info column 2
        y -= 45;
        x += 90;

        g2d.drawString("Stopwatch: " + Util.getElapsedTime(GameState.getStartTime()).setScale(2, RoundingMode.HALF_UP), x, y += 15);
        g2d.drawString("FPS: " + fps, x, y += 15);
        g2d.drawString("Units: " + GameState.getUnits().size(), x, y += 15);
    }
}
