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
                new FileChooser.ExtensionFilter("Arquivos de Audio", "*.aif", "*.aiff", "*.m3u8", "*.wav", "*.mp3", "*.mp4", "*.m4a", "*.m4v")
        );
        List<File> list = fileChooser.showOpenMultipleDialog(Main.stage);
        return list;
    }

}
