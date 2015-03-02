package hicks.td.audio;

import java.io.File;

public enum SoundEffect
{
    DEATH ("humanDeath.wav", 0),
    SHOOT_ARROW ("shootArrow.wav", 0),
    SHOOT_GLAIVE ("shootGlaive.wav", -.3f),
    WEAPON_HIT ("bowHit.wav", 0),
    CANNON_FIRE ("cannonFire.wav", 0),
    CANNON_HIT ("cannonHit.wav", 0),
    ICE_HIT ("iceHit.wav", 0);

    private static final String AUDIO_PATH = "ass" + File.separator + "aud"  + File.separator;

    private final String filename;
    private final float volumeOffset;

    SoundEffect(String filename, float volumeOffset)
    {
        this.filename = filename;
        this.volumeOffset = volumeOffset;
    }

    public String getPath()
    {
        return AUDIO_PATH + filename;
    }

    public float getVolumeOffset()
    {
        return volumeOffset;
    }

    public String getFilename()
    {
        return filename;
    }
}