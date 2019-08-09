package gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.media.MediaView;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class MainController implements Initializable {
    SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");

    Timer timer = new Timer();
    Double timeMusic;


    MediaPlayer mediaPlayer = null;
    Media media = null;

    // Localizacao do Audio
    private final String source = getClass().getResource("Sorry-Madonna.wav").toString();

    @FXML
    private Slider progressBar;

    @FXML
    private Label timeScreen;

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
                timeMusic = mediaPlayer.getTotalDuration().toMillis();
                timeBar(mediaPlayer.getTotalDuration().toMillis());
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

    private void timeBar(double millis) {
        timer.schedule(new TimerTask() {
            public void run() {
                progressBar.setValue(progressBar.getValue() + 0.1);
                if (progressBar.getValue() >= 100 || mediaPlayer.getStatus().PAUSED == Status.PAUSED) {
                    System.out.println("Fim de timeBar");
                    timer.cancel();
                }
            }
        }, 0, (int) Math.round(millis) / 1000);
    }
}
