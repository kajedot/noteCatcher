package sample;

import javafx.animation.Timeline;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class GameLogic {

    private Animation animation;
    private Animation animation2;

    ArrayList<Pane> panes;
    ArrayList<Note> notesColumn = new ArrayList<>(); //? list, queue or what? what if 2 notes at same time?

    public GameLogic(ArrayList<Pane> p){
        panes = p;
        notesColumn.add(new Note(2));
        notesColumn.add(new Note(1));
        notesColumn.add(new Note(2));
        initializeAnimation();
    }

    public void startGame(){
        showNotes();
    }

    private void initializeAnimation(){
        animation = new Animation();
    }

    private void showNotes(){
        for (Note n : notesColumn) {
            animation.showNote(n, panes.get(3));
        }
    }
}