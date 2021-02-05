package sample;

import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;

public class Note {
    String imagePath="file:note_icon.png";
    int keyId;
    ImageView noteView;
    Transition transition;

    public Note(int keyId){
        openImageToView();
        declareTransition();
    }

    private void openImageToView(){
        File input = new File("resources/note_icon.png");
        Image image = new Image(input.toURI().toString());
        noteView = new ImageView(image);
    }

    private void declareTransition(){

        noteView.setVisible(true);
        PathTransition transition = new PathTransition();
        transition.setDuration(Duration.seconds(3));
        //transition.setPath(path);
        transition.setCycleCount(1);
        transition.setInterpolator(Interpolator.LINEAR);

        transition.setNode(noteView);
    }

    public ImageView getNoteView() {
        return noteView;
    }

    public int getKeyId() {
        return keyId;
    }
}