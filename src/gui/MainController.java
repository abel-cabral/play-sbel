package gui;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import util.SelectFile;
import util.TimeConvert;
import util.Utils;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class MainController implements Initializable {
    public static List<File> playList = new ArrayList<File>();
    private int current = 0;
    public File playing;
    private boolean progressBarSystem = false;      // Quanto true o usuario moveu a barra de tempo, caso contrário é o sistema atuando

    Double timeMusic;

    MediaPlayer mediaPlayer = null;
    Media media = null;

    @FXML
    public Slider progressBar;
    @FXML
    Label timeScreen;
    @FXML
    Label musicName;
    @FXML
    MediaView mediaView;
    @FXML
    FontAwesomeIcon playPauseIcon;
    @FXML
    ImageView gifDancing;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        progressBar.setValue(0);
        gifDancing.setVisible(false);
    }

    @FXML
    private void openFile() {
        playing = null;
        List<File> aux = SelectFile.selectMusics();

        if (aux == null) {
            return;
        }

        Set<File> auxList = new HashSet<File>(); // Irá evitar repeticoes
        auxList.addAll(aux);

        // Add a playlist
        playList = auxList.stream().collect(Collectors.toList());
        playing = playList.get(current);
        mediaPlayer = mediaPlayerFactory(playing, mediaPlayer);
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
                Platform.runLater(() -> {
                            // Relogio decrementando
                            timeScreen.setText(TimeConvert.convertToMinute(mediaPlayer.getTotalDuration().toMillis() - mediaPlayer.getCurrentTime().toMillis(), "mm:ss"));
                            // Barra de progresso avançando
                            double auxPercent = (mediaPlayer.getCurrentTime().toMillis() / mediaPlayer.getTotalDuration().toMillis()) * 100;
                            progressBarSystem = true;
                            progressBar.setValue(auxPercent);
                            progressBarSystem = false;
                        }
                );

                if (mediaPlayer.getTotalDuration().toMillis() == mediaPlayer.getCurrentTime().toMillis() || mediaPlayer.getStatus() == Status.PAUSED || mediaPlayer.getStatus() == Status.STOPPED) {
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
            mediaPlayer = mediaPlayerFactory(playing, mediaPlayer);
            playMusicButton();
        }
    }

    @FXML
    public void previousMusic() {
        if (current > 0) {
            current -= 1;
            mediaPlayer.stop();
            playing = playList.get(current);
            mediaPlayer = mediaPlayerFactory(playing, mediaPlayer);
            playMusicButton();
        }
    }

    @FXML
    public void playMusicButton() {
        if (mediaPlayer != null) {
            mediaPlayer.setOnReady(new Runnable() {
                @Override
                public void run() {
                    // Dados da Música Atual
                    timeMusic = mediaPlayer.getTotalDuration().toMillis();
                    timeScreen.setText(TimeConvert.convertToMinute(timeMusic, "mm:ss"));
                    musicName.setText(playing.getName().substring(0, playing.getName().lastIndexOf('.')).toUpperCase());
                    mediaPlayer.seek(Duration.millis(1));       // Corrige bug de leitura de mp3

                    // Posiciona a barra de progresso
                    mediaPlayer.play();
                    playPauseIcon.setIcon(FontAwesomeIcons.PAUSE);
                    gifDancing.setVisible(true);

                    // Barra de Progresso e Tempo de Execução
                    timeLabel();
                }
            });

            if (mediaPlayer.getStatus() == Status.PAUSED) {
                mediaPlayer.play();
                playPauseIcon.setIcon(FontAwesomeIcons.PAUSE);
                gifDancing.setVisible(true);
                timeLabel();
            } else if (mediaPlayer.getStatus() == Status.PLAYING) {
                playPauseIcon.setIcon(FontAwesomeIcons.ARROW_CIRCLE_RIGHT);
                mediaPlayer.pause();
                gifDancing.setVisible(false);
            }
            // Ao fim da musica avança para a proxima
            mediaPlayer.setOnEndOfMedia(() -> {
                nextMusic();
            });
        } else {
            openFile();
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
    public void closeProgram() {
        Platform.exit();
        Platform.isImplicitExit();
        System.exit(143);
    }

    @FXML
    public void progressBarManager() {
        if (mediaPlayer == null) {
            return;
        }
        playMusicButton();
    }

    // UTILITARIOS
    public MediaPlayer mediaPlayerFactory(File source, MediaPlayer now) {
        if (now != null) {
            now.stop();
        }
        // Instantiating MediaPlayer class
        Media facMedia = new Media(new File("file:" + source.getPath().toString().replaceAll(" ", "%20")).getPath());               // Remove espaços que causam error ao ler path
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

                if (progressBarSystem) {
                    return;
                }

                mediaPlayer.pause();
                timeScreen.setText(TimeConvert.convertToMinute(timeMusic, "mm:ss"));
                timeMusic = mediaPlayer.getTotalDuration().toMillis() - mediaPlayer.getCurrentTime().toMillis();
                mediaPlayer.seek(mediaPlayer.getMedia().getDuration().multiply(progressBar.getValue() / 100));
            }
        });
        return facMediaPlayer;
    }
}
