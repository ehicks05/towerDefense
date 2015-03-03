package hicks.td.audio;

import javafx.scene.media.AudioClip;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This code is originally from <b>Carl Dea's JavaFX 2 GameTutorial Part 5</b>.<br>
 *
 * Responsible for loading sound media to be played using an id or key.
 * Contains all sounds for use later.
 * </pre>
 * <pre> * User: cdea
 */
public class SoundEngine
{
    ExecutorService soundPool = Executors.newFixedThreadPool(2);
    Map<String, AudioClip> soundEffectsMap = new HashMap<>();

    /**
     * Constructor to create a simple thread pool.
     *
     * @param numberOfThreads - number of threads to use media players in the map.
     */
    public SoundEngine(int numberOfThreads)
    {
        soundPool = Executors.newFixedThreadPool(numberOfThreads);
    }

    /**
     * Load a sound into a map to later be played based on the id.
     *
     * @param id  - The identifier for a sound.
     * @param url - The url location of the media or audio resource. Usually in src/main/resources directory.
     */
    public void loadSoundEffects(String id, URL url, float volume, boolean loop)
    {
        AudioClip sound = new AudioClip(url.toExternalForm());
        sound.setVolume(.8 + volume);
        if (loop) sound.setCycleCount(AudioClip.INDEFINITE);
        soundEffectsMap.put(id, sound);
    }

    /**
     * Lookup a name resource to play sound based on the id.
     *
     * @param id identifier for a sound to be played.
     */
    public void playSound(final String id)
    {
        Runnable soundPlay = new Runnable()
        {
            @Override
            public void run()
            {
                soundEffectsMap.get(id).play();
            }
        };
        soundPool.execute(soundPlay);
    }

    /**
     * Stop all threads and media players.
     */
    public void shutdown()
    {
        soundPool.shutdown();
    }

}