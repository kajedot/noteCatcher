package sample;

import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.File;

public class Note {
    String imagePath = "resources/note_icon.png";
    int roadId;
    Duration fallingDuration;
    Duration bornTime;
    ImageView noteView;

    public Note(int roadId, Duration fallDuration, Duration bornTime){
        this.bornTime = bornTime;
        this.roadId = roadId;
        this.fallingDuration = fallDuration;
        openImageToView();
    }

    private void openImageToView(){
        File input = new File(imagePath);
        Image image = new Image(input.toURI().toString());
        noteView = new ImageView(image);
    }

    public ImageView getNoteView() {
        return noteView;
    }

    public Duration getFallingDuration() {
        return fallingDuration;
    }

    public int getRoadId() {
        return roadId;
    }

    public Duration getLandingTime(){
        return fallingDuration.add(bornTime);
    }
}