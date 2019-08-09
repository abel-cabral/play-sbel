package gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;
import java.net.URL;

import javafx.scene.media.MediaPlayer.Status;

import java.util.ResourceBundle;

public class MainController implements Initializable {
    private boolean pauseMusic = false;
    private boolean playingMusic = false;
    private MediaPlayer.Status status;

    MediaPlayer mediaPlayer = null;
    Media media = null;

    @FXML
    private Slider progressBar;

    // Localizacao do Audio
    private final String source = getClass().getResource("angels.wav").toString();

    @FXML
    private Label musicName;

    @FXML
    MediaView mediaView;

    @FXML
    void closeProgram() {
        Platform.exit();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void syncControlPlayPause() {
        // Instancia um mediaPlayer
        if (mediaPlayer == null) {
            initMusicPlayer(source);
        }

        mediaPlayer.setOnReady(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.play();
            }
        });

        if (mediaPlayer.getStatus() == Status.PLAYING) {
            mediaPlayer.pause();
        } else if (mediaPlayer.getStatus() == Status.PAUSED) {
            mediaPlayer.play();
        }
    }


    private void initMusicPlayer(String source) {
        musicName.setText(new File(source).getName().toUpperCase());
        media = new Media(new File(source).getPath());

        // Instantiating MediaPlayer class
        mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);

        mediaPlayer.setVolume(80);
        progressBar.setValue(0);
    }
}
