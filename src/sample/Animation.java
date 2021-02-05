package sample;

import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

import java.util.Queue;

public class Animation {

    public Animation(){
    }

    public void noteFall(ImageView noteImage, Pane road){
        Point2D startPoint = new Point2D(road.getBoundsInLocal().getWidth()/2, 50);
        Point2D endPoint = new Point2D(road.getBoundsInLocal().getWidth()/2, road.getBoundsInLocal().getHeight());

        System.out.println(noteImage.getX() + " " + noteImage.getY());

        noteImage.setFitHeight(50);
        noteImage.setFitWidth(50);
        noteImage.setX(startPoint.getX());
        noteImage.setY(startPoint.getY());

        Path path = new Path();
        path.getElements().add(new MoveTo(startPoint.getX(), startPoint.getY()));
        path.getElements().add(new LineTo(endPoint.getX(), endPoint.getY()));
        path.setVisible(false);

        noteImage.setVisible(true);

        road.getChildren().addAll(path, noteImage);

        PathTransition transition = new PathTransition();
        transition.setDuration(Duration.seconds(3));
        transition.setPath(path);
        transition.setCycleCount(1);
        transition.setInterpolator(Interpolator.LINEAR);

        transition.setNode(noteImage);
        transition.play();
    }

    public void showNote(Note note, Pane pane){
        noteFall(note.noteView, pane);
    }
}