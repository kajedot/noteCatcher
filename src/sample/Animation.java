package sample;

import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

import java.util.*;

public class Animation {
    ImageView noteImage;
    Pane road;
    Duration fallingDuration;

    public Animation(ImageView noteImage, Pane road, Duration fallingDuration){
        this.noteImage = noteImage;
        this.road = road;
        this.fallingDuration = fallingDuration;

        noteFall();
    }

    public void noteFall(){
        Point2D startPoint = new Point2D(road.getBoundsInLocal().getWidth()/2, -30);
        Point2D endPoint = new Point2D(road.getBoundsInLocal().getWidth()/2, road.getBoundsInLocal().getHeight()-30);

        noteImage.setX(startPoint.getX());
        noteImage.setY(startPoint.getY());

        Path path = new Path();
        path.getElements().add(new MoveTo(startPoint.getX(), startPoint.getY()));
        path.getElements().add(new LineTo(endPoint.getX(), endPoint.getY()));
        path.setVisible(false);

        noteImage.setVisible(true);

        road.getChildren().addAll(path, noteImage);

        PathTransition transition = new PathTransition();
        transition.setDuration(fallingDuration);
        transition.setPath(path);
        transition.setCycleCount(1);
        transition.setInterpolator(Interpolator.LINEAR);

        transition.setNode(noteImage);
        transition.play();

        transition.setOnFinished(finish -> {
            road.getChildren().remove(path);
            road.getChildren().remove(noteImage);

        });
    }

    public void lightButtonWithNote(ImageView noteImage, Pane road){
        Node button = findButtonInPane(road);
        assert button != null;
        if (checkIfNoteIsOverNode(noteImage, button)) {
            button.setDisable(false);
        } else {
            button.setDisable(true);
        }
    }

    private boolean checkIfNoteIsOverNode(ImageView noteView, Node node){

        if(node.getBoundsInParent().intersects(noteView.getBoundsInParent()))
            return true;
        else
            return false;
    }

    private Node findButtonInPane(Pane road){
        for (Node node : road.getChildren()) {
            if (node instanceof Button) {
                return node;
            }
        }
        return null;
    }
}