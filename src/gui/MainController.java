package gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private boolean pauseMusic = false;
    private boolean playingMusic = false;

    MediaPlayer mediaPlayer = null;
    String source = new File("src/music/angels.wav").getAbsolutePath();
    Media media;
    private final String path = getClass().getResource(
            "angels.wav").toString();

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
    void playMusic() {
        if (!playingMusic) {
            musicName.setText(new File(path).getName().toUpperCase());
            media = new Media(new File(path).getPath());

            //Instantiating MediaPlayer class
            mediaPlayer = new MediaPlayer(media);

            mediaView.setMediaPlayer(mediaPlayer);
            mediaPlayer.setAutoPlay(true);
            mediaPlayer.setVolume(100);
            mediaPlayer.play();

            // Define que há uma musica tocando
            playingMusic = true;
        } else {
            if (pauseMusic) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.play();
            }
            pauseMusic = !pauseMusic;
        }

    }
}
