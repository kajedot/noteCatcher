package sample;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
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
    private Slider timeSlider;
    @FXML
    private Button gameBttn0;
    @FXML
    private Button gameBttn1;
    @FXML
    private Button gameBttn2;
    @FXML
    private Button gameBttn3;
    @FXML
    private Pane road0;
    @FXML
    private Pane road1;
    @FXML
    private Pane road2;
    @FXML
    private Pane road3;

    public void setRoot(Parent root) {
        this.root = root;
    }

    private Parent root;

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    private Stage primaryStage;
    private File musicFile;

    private MusicService musicService;

    private Timeline afterLoadTimeline;

    private GameLogic gameLogic;

    private final SpectrumListener spectrumListener = new SpectrumListener();

    int bands = 128;
    float[] magnitudes = new float[bands]; //default 128 bands
    float[] phases = new float[bands]; //same
    float[] magnitudesCopy = new float[bands]; //default 128 bands


    ArrayList<Pane> panes = new ArrayList<>();

    public Controller() {
        afterLoadTimeline = new Timeline(
                new KeyFrame(Duration.seconds(1.0), e -> { //przekleta lambda
                    testSpectrumFall();
                    stateLbl.setText(musicService.getStatus());

                    elapsedTimeLbl.setText(musicService.getCurrentTimeStr());
                    generalTimeLbl.setText(musicService.getStopTimeStr());

                    timeSlider.adjustValue(musicService.getCurrentTime().toMillis() * 100 / musicService.getStopTime().toMillis());

                    pointsLbl.setText("Points: " + Integer.toString(gameLogic.getPoints()));
                })
        );
        afterLoadTimeline.setCycleCount(Timeline.INDEFINITE);


    }

    private void testSpectrumFall(){
        spectrumListener.spectrumDataUpdate(musicService.getCurrentTime().toMillis(), 1000, magnitudes, phases);
        magnitudesCopy = spectrumListener.getMagnitudesCopy();

        System.out.println(Arrays.toString(magnitudesCopy));

        if (magnitudesCopy[0] > 5){
            gameLogic.addNoteToAnim();
        }

    }

    @FXML
    private void initialize() throws FileNotFoundException {
        panes.add(road0);
        panes.add(road1);
        panes.add(road2);
        panes.add(road3);

        gameLogic = new GameLogic(panes);



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
        }
    }

    public void setGlobalEventHandler(Parent root) {
        root.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
            gameLogic.checkIfScored(ev.getCode(), Duration.millis(System.nanoTime()/1000000.));
        });
    }

    @FXML
    private void gameBttn0Action() {
        System.out.println("pressed A");
        gameBttn0.requestFocus();
    }

    @FXML
    private void gameBttn1Action() {
        System.out.println("pressed S");
        gameBttn1.requestFocus();
    }

    @FXML
    private void gameBttn2Action() {
        System.out.println("pressed D");
        gameBttn2.requestFocus();
    }

    @FXML
    private void gameBttn3Action() {
        System.out.println("pressed F");
        gameBttn3.requestFocus();
    }

    @FXML
    private void playMusic() {
        musicService.setSpectrumListener(spectrumListener);
        musicService.play();
        afterLoadTimeline.play();
        gameLogic.startGame();
    }

    @FXML
    private void volumeUp() {
        musicService.volumeUp();
    }

    @FXML
    private void volumeDown() {
        musicService.volumeDown();
    }
}