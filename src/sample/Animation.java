package sample;

import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

import java.util.ArrayList;

public class Animation {

    public Animation(){
    }

    public void noteFall(Note note, Pane road){

        ImageView noteImage = note.noteView;

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
        transition.setDuration(note.fallingDuration);
        transition.setPath(path);
        transition.setCycleCount(1);
        transition.setInterpolator(Interpolator.LINEAR);

        transition.setNode(noteImage);
        transition.play();
        //parallelTransition.getChildren().add(transition);

        transition.setOnFinished(finish -> {
            road.getChildren().remove(path);
            road.getChildren().remove(noteImage);
        });
    }

    public void showNotes(ArrayList<Note> notes, ArrayList<Pane> panes){
        for (Note n : notes) {
            noteFall(n, panes.get(n.getRoadId()));
        }
    }
}