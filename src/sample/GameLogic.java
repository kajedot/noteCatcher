package sample;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;

public class GameLogic {

    private Animation animation;
    private SpectrumListener spectrumListener;
    private MusicService analyzingMusicService;


    int bands = 128;
    float[] magnitudes = new float[bands]; //default 128 bands
    float[] phases = new float[bands]; //same
    float[] magnitudesCopy = new float[bands]; //default 128 bands

    public int getPoints() {
        return points;
    }

    int points = 0;

    ArrayList<Pane> panes;
    ArrayList<Note> notes = new ArrayList<>();

    public GameLogic(ArrayList<Pane> p){
        panes = p;
        initializeAnimation();
    }

    public void startGame(String musicPath){
        initializeMusicService(musicPath);
        spectrumListener = new SpectrumListener();
        initializeSpectrumListener();
        analyzingMusicService.play();
        analyzingMusicService.mute();
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

    private void initializeMusicService(String musicPath){
        analyzingMusicService = new MusicService(musicPath);
    }

    private void initializeSpectrumListener(){
        analyzingMusicService.setSpectrumListener(spectrumListener);
    }

    public void updateSpectrumListener(){
        spectrumListener.spectrumDataUpdate(analyzingMusicService.getCurrentTime().toMillis(), 1, magnitudes, phases);
        magnitudesCopy = spectrumListener.getMagnitudesCopy();

        System.out.println(Arrays.toString(magnitudesCopy));

        testSpectrumFall();
    }

    private void testSpectrumFall(){
        if (magnitudesCopy[0] > 5){
            addNoteToAnim(0, Duration.seconds(3), Duration.millis(System.nanoTime()/1000000.));
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