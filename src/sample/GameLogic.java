package sample;

import javafx.animation.Timeline;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class GameLogic {

    private Animation animation;
    private long start = System.nanoTime();

    public int getPoints() {
        return points;
    }

    int points = 0;

    ArrayList<Pane> panes;
    ArrayList<Note> notes = new ArrayList<>(); //? list, queue or what? what if 2 notes at same time?
    Set<Integer> pressedButtonsIDs = new HashSet<Integer>();

    public GameLogic(ArrayList<Pane> p){
        panes = p;
        initializeAnimation();
    }

    public void startGame(){
        notes.add(new Note(0, Duration.seconds(6), Duration.millis(System.nanoTime()/1000000.)));
        notes.add(new Note(1, Duration.seconds(4), Duration.millis(System.nanoTime()/1000000.)));
        notes.add(new Note(3, Duration.seconds(3), Duration.millis(System.nanoTime()/1000000.)));
        showNotes();
    }

    private void initializeAnimation(){
        animation = new Animation();
    }

    private void showNotes(){
            animation.showNotes(notes, panes);
    }

    public void checkIfScored(KeyCode keyCode, Duration strikeTime){
        int keyId = keyCodeToID(keyCode);
        for (Note n : notes){
            double strikeLandingDiff = Math.abs(strikeTime.subtract(n.getLandingTime()).toMillis());
            if (strikeLandingDiff < 1000 & keyId == n.roadId){
                System.out.println("POINT " + strikeLandingDiff);
                points++;
            }
            else {
                System.out.println("strike time: " + strikeTime + " Note landing time: " + n.getLandingTime());
            }
        }
    }

    private int keyCodeToID(KeyCode keyCode){
        switch (keyCode){
            case A:
                return 0;
            case S:
                return 1;
            case D:
                return 2;
            case F:
                return 3;
        }
        return 4;
    }
}