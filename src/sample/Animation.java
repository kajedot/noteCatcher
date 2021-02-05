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

public class Animation {

    public Animation(){
    }

    public void noteFall(ImageView noteImage, Pane road){
        Point2D startPoint = new Point2D(road.getBoundsInLocal().getWidth()/2, 0);
        Point2D endPoint = new Point2D(road.getBoundsInLocal().getWidth()/2, road.getBoundsInLocal().getHeight());

        Path path = new Path();
        path.getElements().add(new MoveTo(startPoint.getX(), startPoint.getY()));
        path.getElements().add(new LineTo(endPoint.getX(), endPoint.getY()));
        path.setVisible(false);

        noteImage.setVisible(true);

        PathTransition transition = new PathTransition();
        transition.setDuration(Duration.seconds(3));
        transition.setPath(path);
        transition.setCycleCount(5);
        transition.setInterpolator(Interpolator.LINEAR);

        transition.setNode(noteImage);
        transition.play();

        road.getChildren().addAll(path);
    }
}