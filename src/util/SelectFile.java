package util;

import application.Main;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;

public class SelectFile {

    public static final List<File> selectMusics() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Localizar Músicas");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("WAV", "*.wav")
                , new FileChooser.ExtensionFilter("MP3", "*.mp3")
        );
        List<File> list = fileChooser.showOpenMultipleDialog(Main.stage);
        return list;
    }

}
