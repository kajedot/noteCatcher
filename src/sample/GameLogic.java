package sample;
//0 172 345
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.*;

public class GameLogic {

    private SpectrumListener spectrumListener;
    private MusicService analyzingMusicService;

    private final Timeline afterLoadTimeline;

    Set<Note> landedNotes = new LinkedHashSet<>();

    Queue<Note>[] notesQueues = new ArrayDeque[4];

    double tempoPeriod = 0.1; //here smth with tempo of music
    int bands = 128;
    float[] magnitudes = new float[bands]; //default 128 bands
    float[] phases = new float[bands]; //same
    float[] magnitudesCopy = new float[bands]; //default 128 bands

    public int getPoints() {
        return points;
    }

    int points = 0;

    ArrayList<Pane> panes = new ArrayList<>();

    public GameLogic(ArrayList<Pane> p){
        for (int i=0; i<notesQueues.length; i++){
            notesQueues[i] = new ArrayDeque<Note>();
        }

        panes.addAll(p);

        afterLoadTimeline = new Timeline(
                new KeyFrame(Duration.seconds(tempoPeriod), e -> {
                    updateSpectrumListener();
                    lightButtonWithNotes();
                    clearLandedNotes();
                })
        );
        afterLoadTimeline.setCycleCount(Timeline.INDEFINITE);

    }

    public void lightButtonWithNotes(){
        for (int i=0; i<notesQueues.length; i++){
            if (!notesQueues[i].isEmpty())
                notesQueues[i].peek().lightButtonWithNote();
        }
    }

    public void clearLandedNotes(){
        for (int i=0; i<notesQueues.length; i++){
            if (!notesQueues[i].isEmpty())
                if (notesQueues[i].peek().getLandingTime().lessThanOrEqualTo(Duration.millis(System.nanoTime()/1000000.)))
                    notesQueues[i].remove();
        }
    }

    public void startGame(String musicPath){
        initializeMusicService(musicPath);
        spectrumListener = new SpectrumListener();
        initializeSpectrumListener();
        analyzingMusicService.play();
        analyzingMusicService.mute();

        afterLoadTimeline.play();
    }

    public void addPoint(int roadId){
        points++;
    }

    private void initializeMusicService(String musicPath){
        analyzingMusicService = new MusicService(musicPath);
    }

    private void initializeSpectrumListener(){
        analyzingMusicService.setSpectrumListener(spectrumListener);
        analyzingMusicService.audioSpectrumInterval(tempoPeriod);
    }

    public void updateSpectrumListener(){
        spectrumListener.spectrumDataUpdate(analyzingMusicService.getCurrentTime().toMillis(), tempoPeriod, magnitudes, phases);
        magnitudesCopy = spectrumListener.getMagnitudesCopy();

        System.out.println(Arrays.toString(magnitudesCopy));

        testSpectrumFall();
    }

    private void testSpectrumFall(){
        if (magnitudesCopy[0] > 3){
            addNote(0, Duration.seconds(6), Duration.millis(System.nanoTime()/1000000.));
        }
        if (magnitudesCopy[1] > 7){
            addNote(1, Duration.seconds(6), Duration.millis(System.nanoTime()/1000000.));
        }
    }

    public void addNote(int roadID, Duration fallDuration, Duration bornTime){
        notesQueues[roadID].add(new Note(panes.get(roadID), fallDuration, bornTime));
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