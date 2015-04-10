package hicks.td.net;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class Score implements Comparable
{
    private String name = "";
    private int wave;
    private int lives;
    private int gold;
    private LocalDate createdOn;

    public Score(String line)
    {
        List<String> tokens = Arrays.asList(line.split("\t"));
        name  = tokens.get(0);
        wave  = Integer.parseInt(tokens.get(1));
        lives = Integer.parseInt(tokens.get(2));
        gold  = Integer.parseInt(tokens.get(3));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        createdOn = LocalDate.parse(tokens.get(4), formatter);
    }

    @Override
    public String toString()
    {
        return name + "\t" + wave + "\t" + lives + "\t" + gold + "\t" + createdOn;
    }

    @Override
    public int compareTo(Object o)
    {
        Score that = (Score) o;
//        if (this.wave == that.wave) return 0;
        if (this.wave > that.wave) return -1;
        if (this.wave < that.wave) return 1;

        if (this.lives > that.lives) return -1;
        if (this.lives < that.lives) return 1;

        if (this.gold > that.gold) return -1;
        if (this.gold < that.gold) return 1;

        if (this.createdOn.isBefore(that.createdOn)) return -1;
        if (this.createdOn.isAfter(that.createdOn)) return 1;

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

    public LocalDate getCreatedOn()
    {
        return createdOn;
    }

    public void setCreatedOn(LocalDate createdOn)
    {
        this.createdOn = createdOn;
    }
}
