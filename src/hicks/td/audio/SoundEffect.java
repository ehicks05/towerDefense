package hicks.td.audio;

public enum SoundEffect
{
    DEATH ("humanDeath.wav"),
    SHOOT_ARROW ("shootArrow.wav"),
    SHOOT_GLAIVE ("shootGlaive.wav"),
    WEAPON_HIT ("bowHit.wav");

    private final String filename;
    private final String FOLDER = "ass\\aud\\";

    SoundEffect(String filename)
    {
        this.filename = filename;
    }

    public String getPath()
    {
        return FOLDER + filename;
    }
}