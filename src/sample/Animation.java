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

import java.util.*;

public class Animation {

    ArrayList<Pane> roads = new ArrayList<>();
    Queue[] notesQueues;

    public Set<Note> getRecentlyLandedNotes() {

        Set<Note> tempNotesSet = new LinkedHashSet<>(recentlyLandedNotes);
        recentlyLandedNotes.clear();

        return tempNotesSet;
    }

    public void removeNotesFromRecentlyLanded(Set<Note> set){
        recentlyLandedNotes.removeAll(set);
    }

    Set<Note> recentlyLandedNotes = new LinkedHashSet<>();

    public Animation(ArrayList<Pane> panes){
        roads.addAll(panes);

        notesQueues = new Queue[4];
        for (int i=0; i<notesQueues.length; i++){
            notesQueues[i] = new ArrayDeque<Note>();
        }
    }

    public void noteFall(Note note, Pane road){

        notesQueues[note.roadId].add(note);

        ImageView noteImage = note.noteView;

        Point2D startPoint = new Point2D(road.getBoundsInLocal().getWidth()/2, 50);
        Point2D endPoint = new Point2D(road.getBoundsInLocal().getWidth()/2, road.getBoundsInLocal().getHeight()-30);

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

        transition.setOnFinished(finish -> {
            road.getChildren().remove(path);
            road.getChildren().remove(noteImage);
            recentlyLandedNotes.add(note);

            notesQueues[note.roadId].remove(note);
        });
    }

    public void showNotes(ArrayList<Note> notes, ArrayList<Pane> panes){
        for (Note n : notes) {
            noteFall(n, panes.get(n.getRoadId()));
        }
    }

    public void lightButtonsWithNotes(){
        for (int i=0; i<notesQueues.length; i++){
            if (!notesQueues[i].isEmpty()) {
                Note note = (Note) notesQueues[i].peek();
                Node button = findButtonInPane(roads.get(i));
                assert button != null;
                if (checkIfNoteIsOverNode(note.noteView, button)) {
                    button.setDisable(false);
                } else {
                    button.setDisable(true);
                }
            }
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

    public void removeNoteFromRoad(int roadID){
        notesQueues[roadID].remove();

    }
}