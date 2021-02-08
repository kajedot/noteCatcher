package sample;
//0 172 345
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.util.*;

public class GameLogic {

    private SpectrumListener spectrumListener;
    private MusicService analyzingMusicService;

    private final Timeline spectrumListenerTimeline;
    private final Timeline technicalTimeline;

    Set<Note> landedNotes = new LinkedHashSet<>();

    Queue<Note>[] notesQueues = new ArrayDeque[4];

    double tempoPeriod = 0.5; //here smth with tempo of music
    int bands = 128;
    int musicPlayersDelay;
    float[] magnitudes = new float[bands]; //default 128 bands
    float[] phases = new float[bands]; //same
    float[] magnitudesCopy = new float[bands]; //default 128 bands

    public int getPoints() {
        return points;
    }

    int points = 0;

    ArrayList<Pane> panes = new ArrayList<>();

    public GameLogic(ArrayList<Pane> p, int musicPlayersDelay){
        this.musicPlayersDelay = musicPlayersDelay;
        for (int i=0; i<notesQueues.length; i++){
            notesQueues[i] = new ArrayDeque<Note>();
        }

        panes.addAll(p);

        spectrumListenerTimeline = new Timeline(
                new KeyFrame(Duration.seconds(tempoPeriod), e -> {
                    updateSpectrumListener();
                })
        );
        spectrumListenerTimeline.setCycleCount(Timeline.INDEFINITE);

        technicalTimeline = new Timeline(
                new KeyFrame(Duration.seconds(0.01), e -> {
                    lightButtonWithNotes();
                    clearLandedNotes();
                    checkStatus();
                })
        );
        technicalTimeline.setCycleCount(Timeline.INDEFINITE);
    }

    public void checkStatus(){
        if (!analyzingMusicService.getStatus().equals("PLAYING")){
            spectrumListenerTimeline.stop();
        }
    }

    public void lightButtonWithNotes(){
        for (int i=0; i<notesQueues.length; i++){
            if (!notesQueues[i].isEmpty())
                notesQueues[i].peek().lightButtonWithNote();
            else {
                Objects.requireNonNull(findButtonInPane(panes.get(i))).setDisable(true);
            }
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

        spectrumListenerTimeline.play();
        technicalTimeline.play();
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
//        if (magnitudesCopy[0] > 3){
//            addNote(0, Duration.seconds(musicPlayersDelay), Duration.millis(System.nanoTime()/1000000.));
//        }
        if (magnitudesCopy[2] > 26){
            addNote(1, Duration.seconds(musicPlayersDelay), Duration.millis(System.nanoTime()/1000000.));
        }
//        if (magnitudesCopy[2] > 7){
//            addNote(0, Duration.seconds(musicPlayersDelay), Duration.millis(System.nanoTime()/1000000.));
//        }
//        if (magnitudesCopy[11] > 7){
//            addNote(1, Duration.seconds(musicPlayersDelay), Duration.millis(System.nanoTime()/1000000.));
//        }
    }

    public void addNote(int roadID, Duration fallDuration, Duration bornTime){
        notesQueues[roadID].add(new Note(panes.get(roadID), fallDuration, bornTime));
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