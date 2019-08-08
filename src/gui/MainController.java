package gui;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.media.Media;
import javafx.scene.media.MediaErrorEvent;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private MediaPlayer mediaPlayer;

    @FXML
    public void closeProgram() {
        Platform.exit();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String source = "/music/Angels - Within Temptation.mp3";
        Media media;
        MediaPlayer mediaPlayer;
        MediaView mediaView;
        try {
            media = new Media(source);
            if (media.getError() == null) {
                media.setOnError(new Runnable() {
                    public void run() {
                        // Handle asynchronous error in Media object.
                    }
                });
                try {
                    mediaPlayer = new MediaPlayer(media);
                    if (mediaPlayer.getError() == null) {
                        mediaPlayer.setOnError(new Runnable() {
                            public void run() {
                                // Handle asynchronous error in MediaPlayer object.
                            }
                        });
                        mediaView = new MediaView(mediaPlayer);
                        mediaView.setOnError(new EventHandler<MediaErrorEvent>() {
                            public void handle(MediaErrorEvent t) {
                                // Handle asynchronous error in MediaView.
                            }
                        });
                    } else {
                        // Handle synchronous error creating MediaPlayer.
                    }
                } catch (Exception mediaPlayerException) {
                    // Handle exception in MediaPlayer constructor.
                }
            } else {
                // Handle synchronous error creating Media.
            }
        } catch (Exception mediaException) {
            // Handle exception in Media constructor.
        }
    }
}
