package hicks.td.net;

import java.util.Arrays;
import java.util.List;

public class Score implements Comparable
{
    private String name = "";
    private int wave;
    private int lives;
    private int gold;

    public Score(String name, int wave, int lives, int gold)
    {
        this.name = name;
        this.wave = wave;
        this.lives = lives;
        this.gold = gold;
    }

    public Score(String line)
    {
        List<String> tokens = Arrays.asList(line.split("\t"));
        this.name  = tokens.get(0);
        this.wave  = Integer.parseInt(tokens.get(1));
        this.lives = Integer.parseInt(tokens.get(2));
        this.gold  = Integer.parseInt(tokens.get(3));
    }

    @Override
    public String toString()
    {
        return this.name + "\t" + this.wave + "\t" + this.lives + "\t" + this.gold;
    }

    @Override
    public int compareTo(Object o)
    {
        Score that = (Score) o;
        if (this.wave == that.wave) return 0;
        if (this.wave > that.wave) return -1;
        if (this.wave < that.wave) return 1;

        return 0;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getWave()
    {
        return wave;
    }

    public void setWave(int wave)
    {
        this.wave = wave;
    }

    public int getLives()
    {
        return lives;
    }

    public void setLives(int lives)
    {
        this.lives = lives;
    }

    public int getGold()
    {
        return gold;
    }

    public void setGold(int gold)
    {
        this.gold = gold;
    }
}
