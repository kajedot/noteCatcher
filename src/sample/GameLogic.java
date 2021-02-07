package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class GameLogic {

    private Animation animation;
    private SpectrumListener spectrumListener;
    private MusicService analyzingMusicService;

    private final Timeline afterLoadTimeline;

    Set<Note> landedNotes = new LinkedHashSet<>();

    double tempoPeriod = 0.5; //here smth with tempo of music
    int bands = 128;
    float[] magnitudes = new float[bands]; //default 128 bands
    float[] phases = new float[bands]; //same
    float[] magnitudesCopy = new float[bands]; //default 128 bands

    public int getPoints() {
        return points;
    }

    int points = 0;

    ArrayList<Pane> panes = new ArrayList<>();
    ArrayList<Note> notes = new ArrayList<>();

    public GameLogic(ArrayList<Pane> p){
        panes.addAll(p);
        initializeAnimation();

        afterLoadTimeline = new Timeline(
                new KeyFrame(Duration.seconds(tempoPeriod), e -> {
                    updateSpectrumListener();
                    animation.lightButtonsWithNotes();
                    manageLandedNotes();
                })
        );
        afterLoadTimeline.setCycleCount(Timeline.INDEFINITE);

    }

    public void manageLandedNotes(){
        landedNotes.clear();
        landedNotes.addAll(animation.getRecentlyLandedNotes());
        System.out.println("recently landed: " + landedNotes.toString());
    }

    public void startGame(String musicPath){
        initializeMusicService(musicPath);
        spectrumListener = new SpectrumListener();
        initializeSpectrumListener();
        analyzingMusicService.play();
        analyzingMusicService.mute();

        afterLoadTimeline.play();
    }

    private void initializeAnimation(){
        animation = new Animation(panes);
    }

    public void checkIfScored(KeyCode keyCode, Duration strikeTime){
        int keyId = keyCodeToID(keyCode);
        for (Note n : landedNotes){
            //double strikeLandingDiff = Math.abs(strikeTime.subtract(n.getLandingTime()).toMillis());
            if (keyId == n.roadId){ //strikeLandingDiff < 1000 &
                System.out.println("POINT " + keyId);
                points++;
                landedNotes.remove(n);
            }
            else {
                System.out.println("strike time: " + strikeTime + " Note landing time: " + n.getLandingTime());
            }
        }
    }

    public void addPoint(int roadId){
        points++;
        animation.removeNoteFromRoad(roadId);
    }

    private void initializeMusicService(String musicPath){
        analyzingMusicService = new MusicService(musicPath);
    }

    private void initializeSpectrumListener(){
        analyzingMusicService.setSpectrumListener(spectrumListener);
    }

    public void updateSpectrumListener(){
        spectrumListener.spectrumDataUpdate(analyzingMusicService.getCurrentTime().toMillis(), tempoPeriod, magnitudes, phases);
        magnitudesCopy = spectrumListener.getMagnitudesCopy();

        System.out.println(Arrays.toString(magnitudesCopy));

        testSpectrumFall();
    }

    private void testSpectrumFall(){
        if (magnitudesCopy[0] > 2){
            addNoteToAnim(0, Duration.seconds(6), Duration.millis(System.nanoTime()/1000000.));
        }
        if (magnitudesCopy[1] > 7){
            addNoteToAnim(2, Duration.seconds(6), Duration.millis(System.nanoTime()/1000000.));
        }
    }



    public void addNoteToAnim(int roadID, Duration fallDuration, Duration bornTime){
        //notes.add(new Note(roadID, fallDuration, bornTime));
        animation.noteFall(new Note(roadID, fallDuration, bornTime), panes.get(roadID));
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