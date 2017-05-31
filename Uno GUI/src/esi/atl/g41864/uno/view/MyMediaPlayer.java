package esi.atl.g41864.uno.view;

import esi.atl.g41864.uno.observers.Observable;
import esi.atl.g41864.uno.observers.Observer;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import static javafx.scene.media.MediaPlayer.INDEFINITE;

/**
 *
 * @author mike
 */
class MyMediaPlayer implements Observable {
    
    private final List<Observer> observers;
    private final MediaPlayer mediaPlayer;
    private double volume;

    MyMediaPlayer() {
        File file = new File(getClass().getResource("/esi/atl/g41864/uno/"
                + "resources/music/unoSong2.mp3").toExternalForm());

        Media music = new Media(file.toString());
        mediaPlayer = new MediaPlayer(music);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setVolume(1);
        mediaPlayer.setCycleCount(INDEFINITE);
        
        volume = mediaPlayer.getVolume();
        observers = new ArrayList<>();
    }

    void muteMusic() {
        mediaPlayer.setMute(!mediaPlayer.isMute());
    }

    void increaseVolume() {
        if (mediaPlayer.getVolume() < 1) {
            mediaPlayer.setVolume(mediaPlayer.getVolume() + 0.1);
            volume = mediaPlayer.getVolume();
        }
        notifyObs();
    }

    void decreaseVolume() {
        if (mediaPlayer.getVolume() > 0.1) {
            mediaPlayer.setVolume(mediaPlayer.getVolume() - 0.1);
            volume = mediaPlayer.getVolume();
        }
        notifyObs();
    }
    
    double getVolume() {
        return volume;
    }

    @Override
    public void addObserver(Observer obs) {
        if (!observers.contains(obs)) {
            observers.add(obs);
        }
    }

    @Override
    public void deleteObserver(Observer obs) {
        if (observers.contains(obs)) {
            observers.remove(obs);
        }
    }

    @Override
    public void notifyObs() {
        observers.forEach((obs) -> {
            obs.updateMusic();
        });
    }
}
