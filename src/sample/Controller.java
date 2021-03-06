package sample;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.MusicManagement.MusicService;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class Controller {
    @FXML
    private Label fileNameLbl;
    @FXML
    private Label stateLbl;
    @FXML
    private Label elapsedTimeLbl;
    @FXML
    private Label generalTimeLbl;
    @FXML
    private Label pointsLbl;
    @FXML
    private Label bigInfoLbl;
    @FXML
    private ProgressBar gameProgressBar;
    @FXML
    private Button gameBttn0;
    @FXML
    private Button gameBttn1;
    @FXML
    private Button gameBttn2;
    @FXML
    private Button gameBttn3;
    @FXML
    private Button openFileBttn;
    @FXML
    private Button VolDownBttn;
    @FXML
    private Button VolUpBttn;
    @FXML
    private Button playBttn;
    @FXML
    private Pane road0;
    @FXML
    private Pane road1;
    @FXML
    private Pane road2;
    @FXML
    private Pane road3;

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    private Stage primaryStage;
    private File musicFile;
    private MusicService musicService;
    private final Timeline afterLoadTimeline;
    private GameLogic gameLogic;

    ArrayList<Pane> panes = new ArrayList<>();

    int musicPlayersDelay = 3;
    boolean startedMusic = false;

    public Controller() {
        afterLoadTimeline = new Timeline(
                new KeyFrame(Duration.seconds(1.0), e -> {

                    if (musicPlayersDelay > 0){
                        bigInfoLbl.setVisible(true);
                        bigInfoLbl.setText(Integer.toString(musicPlayersDelay));
                        musicPlayersDelay--;
                    } else {
                        if (! startedMusic){
                            musicService.play();
                            startedMusic = true;
                            bigInfoLbl.setVisible(false);
                        }
                    }
                    guiUpdates();
                })
        );
        afterLoadTimeline.setCycleCount(Timeline.INDEFINITE);
    }

    @FXML
    private void initialize() {
        panes.add(road0);
        panes.add(road1);
        panes.add(road2);
        panes.add(road3);
        bigInfoLbl.setVisible(false);
    }

    private void guiUpdates(){
        stateLbl.setText(musicService.getStatus());

        elapsedTimeLbl.setText(musicService.getCurrentTimeStr());
        generalTimeLbl.setText(musicService.getStopTimeStr());

        gameProgressBar.setProgress(musicService.getCurrentTime().toMillis() / musicService.getStopTime().toMillis());

        pointsLbl.setText("Points: " + (int)gameLogic.getPoints());

        if (musicService.getStatus().equals("STOPPED")){
            afterLoadTimeline.stop();
            pointsLbl.setVisible(false);
            bigInfoLbl.setVisible(true);
            bigInfoLbl.setText("Great game! \n " +
                    "You catched " + (int)gameLogic.getPoints() + " of " + (int)gameLogic.getAllNotesCount() + " notes! " +
                    "(" + (Math.round(gameLogic.getPoints()*100/gameLogic.getAllNotesCount()) + "%)"));

            VolDownBttn.setDisable(true);
            VolUpBttn.setDisable(true);
        }
    }

    private void initializeFile() {
        musicService = new MusicService(musicFile.toURI().toString());
        fileNameLbl.setText(musicFile.getName());
    }

    @FXML
    private void chooseFile() {
        System.out.println("choosing file...");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose music file...");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("WAV", "*.wav")
        );
        musicFile = fileChooser.showOpenDialog(primaryStage);
        if (Objects.nonNull(musicFile)) {
            System.out.println("chosen file: " + musicFile.getAbsolutePath());
            initializeFile();
            openFileBttn.setDisable(true);
        }
    }

    public void setGlobalEventHandler(Parent root) {
        root.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
            switch (ev.getCode()) {
                case A -> gameBttnAction(gameBttn0);
                case S -> gameBttnAction(gameBttn1);
                case D -> gameBttnAction(gameBttn2);
                case F -> gameBttnAction(gameBttn3);
            }
        });
    }

    private void gameBttnAction(Button button) {
        if (! button.isDisabled()) {
            gameLogic.addPoint();
            button.requestFocus();
        }
    }

    @FXML
    private void playMusic() {
        if (musicFile != null) {
            gameLogic = new GameLogic(panes, musicPlayersDelay);
            gameLogic.startGame(musicFile.toURI().toString());

            afterLoadTimeline.play();
            playBttn.setDisable(true);
        }
    }

    @FXML
    private void volumeUp() {
        if (musicFile != null)
            musicService.volumeUp();
    }

    @FXML
    private void volumeDown() {
        if (musicFile != null)
            musicService.volumeDown();
    }
}