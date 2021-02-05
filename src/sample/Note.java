package sample;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;

public class Note {
    String imagePath="note_icon.png";
    int keyId;
    ImageView noteView;

    public Note(){
        openImageToView();
    }

    private void openImageToView(){
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(imagePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert inputStream != null;
        Image image = new Image(inputStream);
        noteView = new ImageView(image);
    }

    public ImageView getNoteView() {
        return noteView;
    }
}
