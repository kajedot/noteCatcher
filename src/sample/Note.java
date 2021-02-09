package sample;

import javafx.animation.Transition;
import javafx.scene.CacheHint;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.File;

public class Note {
    String imagePath = "resources/note_icon.png";
    Pane road;
    Duration fallingDuration;
    Duration bornTime;
    ImageView noteView;
    Animation animation;

    public Note(Pane road, Duration fallDuration, Duration bornTime){
        this.bornTime = bornTime;
        this.road = road;
        this.fallingDuration = fallDuration;
        openImageToView();
        noteView.setFitHeight(50);
        noteView.setFitWidth(50);
        noteView.setCache(true);
        noteView.setCacheHint(CacheHint.SPEED);
        animate();
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

    public Duration getLandingTime(){
        return fallingDuration.add(bornTime);
    }

    public void animate(){
        animation = new Animation(noteView, road, fallingDuration);
    }

    public void lightButtonWithNote(){
        animation.lightButtonWithNote(noteView, road);
    }
}