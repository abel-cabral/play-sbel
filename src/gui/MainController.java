package gui;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import util.SelectFile;
import util.TimeConvert;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class MainController implements Initializable {
    public static List<File> playList = new ArrayList<File>();
    private int current = 0;
    public static File playing;

    Double timeMusic;

    MediaPlayer mediaPlayer = null;
    Media media = null;

    @FXML
    public Slider progressBar = new Slider();

    @FXML
    Label timeScreen;
    @FXML
    Label musicName;
    @FXML
    MediaView mediaView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        beforePlayMusic();
        mediaPlayer = mediaPlayerFactory(playing);          // A cada musica nova precisamos instanciar
        mediaView.setMediaPlayer(mediaPlayer);
        progressBar.setValue(0);
        playMusicButton();
    }

    /*
    private void timeBar(double millis) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                progressBar.setValue(progressBar.getValue() + 0.1);
                if (progressBar.getValue() >= 100 || mediaPlayer.getStatus() == Status.PAUSED) {
                    progressBar.setValue(0);
                    timer.cancel();
                }
            }
        }, 0, (int) Math.round(millis) / 1000);
    }
*/
    private void timeLabel() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                Platform.runLater(() ->
                        timeScreen.setText(TimeConvert.convertToMinute(mediaPlayer.getTotalDuration().toMillis() - mediaPlayer.getCurrentTime().toMillis(), "mm:ss"))
                );

                if (mediaPlayer.getTotalDuration().toMillis() == mediaPlayer.getCurrentTime().toMillis() || mediaPlayer.getStatus() == Status.PAUSED) {
                    System.out.println("TimeLabel");
                    timeScreen.setText("");
                    timer.cancel();
                }
            }
        }, 1000, 1000);
    }

    @FXML
    public void nextMusic() {
        if (playList.size() > current + 1) {
            current += 1;
            mediaPlayer.stop();
            playing = playList.get(current);
            mediaPlayer = mediaPlayerFactory(playing);
            playMusicButton();
        }
    }

    @FXML
    public void previousMusic() {
        if (current > 0) {
            current -= 1;
            mediaPlayer.stop();
            playing = playList.get(current);
            mediaPlayer = mediaPlayerFactory(playing);
            playMusicButton();
        }
    }

    @FXML
    public void playMusicButton() {
        mediaPlayer.setOnReady(new Runnable() {
            @Override
            public void run() {
                // Dados da Música Atual
                timeMusic = mediaPlayer.getTotalDuration().toMillis();
                timeScreen.setText(TimeConvert.convertToMinute(timeMusic, "mm:ss"));
                musicName.setText(playing.getName().toUpperCase());
                // timeBar(mediaPlayer.getTotalDuration().toMillis());
                timeLabel();

                // Posiciona a barra de progresso
                mediaPlayer.play();
            }
        });

        if (mediaPlayer.getStatus() == Status.PAUSED) {
            mediaPlayer.play();
        } else if (mediaPlayer.getStatus() == Status.PLAYING) {
            mediaPlayer.pause();
        }
    }

    private void beforePlayMusic() {
        if (playList.isEmpty()) {
            // Evita repeticoes
            Set<File> auxList = new HashSet<File>(); // Irá evitar repeticoes
            auxList.addAll(SelectFile.selectMusics());

            // Add a playlist
            playList = auxList.stream().collect(Collectors.toList());

            if (playing == null) {
                playing = playList.get(current);
            }
        }
    }

    @FXML
    void closeProgram() {
        Platform.exit();
        Platform.isImplicitExit();
        System.exit(143);
    }


    // UTILITARIOS
    public MediaPlayer mediaPlayerFactory(File source) {
        // Instantiating MediaPlayer class
        Media facMedia = new Media(new File("file:" + source).getPath());
        MediaPlayer facMediaPlayer = new MediaPlayer(facMedia);
        // Inicialização de componentes de tela
        facMediaPlayer.setVolume(80);
        facMediaPlayer.setAutoPlay(true);

        // Subscrible para ouvir mudanças na barra de progresso
        progressBar.setValue(0);
        progressBar.setMax(100);

        // Adding Listener to value property.
        progressBar.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                                Number oldValue, Number newValue) {
                double valueProgress = (mediaPlayer.getTotalDuration().toMillis() * (double) newValue) / 100;
                mediaPlayer.pause();
                mediaPlayer.seek(Duration.millis(valueProgress));
                timeMusic = mediaPlayer.getTotalDuration().toMillis() - mediaPlayer.getCurrentTime().toMillis();
                timeScreen.setText(TimeConvert.convertToMinute(timeMusic, "mm:ss"));
            }
        });
        return facMediaPlayer;
    }
}
