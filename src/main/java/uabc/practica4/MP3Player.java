package uabc.practica4;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MP3Player {
    private String filename;
    private AdvancedPlayer player;
    private boolean isPlaying = false;

    public MP3Player(String filename) {
        this.filename = filename;
    }

    public void play() {
        if (isPlaying)
            return;

        try {
            FileInputStream fileInputStream = new FileInputStream(filename);
            player = new AdvancedPlayer(fileInputStream);
            new Thread(() -> {
                try {
                    isPlaying = true;
                    player.play();
                    isPlaying = false;
                } catch (JavaLayerException e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (JavaLayerException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (!isPlaying)
            return;

        if (player != null) {
            player.close();
            isPlaying = false;
        }
    }

    public boolean isPlaying() {
        return isPlaying;
    }
}
