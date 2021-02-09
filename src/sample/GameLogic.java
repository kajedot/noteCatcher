package sample;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import sample.MusicManagement.MusicService;
import sample.MusicManagement.SpectrumListener;

import java.util.*;

public class GameLogic {

    private SpectrumListener spectrumListener;
    private MusicService analyzingMusicService;

    private final Timeline spectrumListenerTimeline;
    private final Timeline technicalTimeline;

    public float getAllNotesCount() {
        return allNotesCount;
    }

    private float allNotesCount = 0;

    Queue<Note>[] notesQueues = new ArrayDeque[4];

    double tempoPeriod = 1;
    int threshold;
    int bands = 128;
    int musicPlayersDelay;
    float[] magnitudes = new float[bands];
    float[] phases = new float[bands];
    float[] magnitudesCopy = new float[bands];

    public float getPoints() {
        return points;
    }

    private float points = 0;

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
                    checkStatus();
                })
        );
        spectrumListenerTimeline.setCycleCount(Timeline.INDEFINITE);

        technicalTimeline = new Timeline(
                new KeyFrame(Duration.seconds(0.3), e -> {
                    lightButtonWithNotes();
                    clearLandedNotes();
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
            if (!notesQueues[i].isEmpty()) {
                assert notesQueues[i].peek() != null;
                notesQueues[i].peek().lightButtonWithNote();
            }
            else {
                Objects.requireNonNull(findButtonInPane(panes.get(i))).setDisable(true);
            }
        }
    }

    public void clearLandedNotes(){
        for (Queue<Note> notesQueue : notesQueues) {
            if (!notesQueue.isEmpty()) {
                assert notesQueue.peek() != null;
                if (notesQueue.peek().getLandingTime().lessThanOrEqualTo(getSystemTime()))
                    notesQueue.remove();
            }
        }
    }

    public void startGame(String musicPath){
        initializeMusicService(musicPath);
        spectrumListener = new SpectrumListener(bands, threshold);
        initializeSpectrumListener();
        analyzingMusicService.play();
        analyzingMusicService.mute();

        spectrumListenerTimeline.play();
        technicalTimeline.play();
    }

    public void addPoint(){
        points++;
    }

    private void initializeMusicService(String musicPath){
        analyzingMusicService = new MusicService(musicPath);
        threshold = analyzingMusicService.getAudioSpectrumThreshold();
    }

    private void initializeSpectrumListener(){
        analyzingMusicService.setSpectrumListener(spectrumListener);
        analyzingMusicService.audioSpectrumInterval(tempoPeriod);
        analyzingMusicService.setAudioSpectrumNumBands(bands);
    }

    public void updateSpectrumListener(){
        spectrumListener.spectrumDataUpdate(analyzingMusicService.getCurrentTime().toMillis(), tempoPeriod, magnitudes, phases);
        magnitudesCopy = spectrumListener.getMagnitudesCopy();

        System.out.println(Arrays.toString(magnitudesCopy));

        testSpectrumFall();
    }

    private void testSpectrumFall(){
        if (magnitudesCopy[1] > 29){
            addNote(0, Duration.seconds(musicPlayersDelay), getSystemTime());
        }
        if (magnitudesCopy[3] > 26){
            addNote(1, Duration.seconds(musicPlayersDelay), getSystemTime());
        }
        if (magnitudesCopy[20] > 15){
            addNote(2, Duration.seconds(musicPlayersDelay), getSystemTime());
        }
        if (magnitudesCopy[40] > 7){
            addNote(3, Duration.seconds(musicPlayersDelay), getSystemTime());
        }
    }

    public Duration getSystemTime(){
        return Duration.millis(System.nanoTime()/1000000.);
    }

    public void addNote(int roadID, Duration fallDuration, Duration bornTime){
        notesQueues[roadID].add(new Note(panes.get(roadID), fallDuration, bornTime));
        allNotesCount++;
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