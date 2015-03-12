package hicks.td.audio;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

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
 */
public class SoundEngine
{
    ExecutorService soundPool = Executors.newFixedThreadPool(8);
    Map<String, AudioClip> soundEffectsMap = new HashMap<>();
    Map<String, Media> musicMap = new HashMap<>();

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
     * @param id  - The identifier for a sound.
     * @param url - The url location of the media or audio resource. Usually in src/main/resources directory.
     */
    public void loadSoundEffects(String id, URL url, float volume)
    {
        AudioClip sound = new AudioClip(url.toExternalForm());
        sound.setVolume(.8 + volume);
        soundEffectsMap.put(id, sound);
    }

    public void loadMusic(String id, URL url)
    {
        Media music = new Media(url.toExternalForm());
        musicMap.put(id, music);
    }

    /**
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

    public void playMusic(final String id)
    {
        Runnable musicPlay = new Runnable()
        {
            @Override
            public void run()
            {
                MediaPlayer player = new MediaPlayer(musicMap.get(id));
                player.setCycleCount(MediaPlayer.INDEFINITE);
                player.play();
            }
        };
        soundPool.execute(musicPlay);
    }

    /**
     * Stop all threads and media players.
     */
    public void shutdown()
    {
        soundPool.shutdown();
    }
}