package hicks.td.audio;

public enum SoundEffect
{
    DEATH ("humanDeath.wav", 0),
    SHOOT_ARROW ("shootArrow.wav", 0),
    SHOOT_GLAIVE ("shootGlaive.wav", -9),
    WEAPON_HIT ("bowHit.wav", 0),
    CANNON_FIRE ("cannonFire.wav", 0),
    CANNON_HIT ("cannonHit.wav", 0),
    ICE_HIT ("iceHit.wav", 0);

    private final String filename;
    private final float volumeOffset;

    SoundEffect(String filename, float volumeOffset)
    {
        this.filename = filename;
        this.volumeOffset = volumeOffset;
    }

    public String getPath()
    {
        return "ass\\aud\\" + filename;
    }

    public float getVolumeOffset()
    {
        return volumeOffset;
    }
}