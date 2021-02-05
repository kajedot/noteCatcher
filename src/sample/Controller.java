package sample;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
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
    private Slider timeSlider;
    @FXML
    private ImageView noteImage1;
    @FXML
    private Button gameBttn1;
    @FXML
    private Pane road1;

    private Stage primaryStage;
    private File musicFile;

    private MusicService musicService;

    private Timeline afterLoadTimeline;

    private Animation animation;

    private GameLogic gameLogic;

    int bands = 128;
    float[] magnitudes = new float[bands]; //default 128 bands
    float[] phases = new float[bands]; //same

    public Controller() {
        gameLogic = new GameLogic();

        afterLoadTimeline = new Timeline(
                new KeyFrame(Duration.seconds(1.0), e -> { //przekleta lambda
                    stateLbl.setText(musicService.getStatus());

                    elapsedTimeLbl.setText(musicService.getCurrentTimeStr());
                    generalTimeLbl.setText(musicService.getStopTimeStr());

                    timeSlider.adjustValue(musicService.getCurrentTime().toMillis() * 100 / musicService.getStopTime().toMillis());
                })
        );
        afterLoadTimeline.setCycleCount(Timeline.INDEFINITE);
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    private void initializeFile() {
        musicService = new MusicService(musicFile.toURI().toString());
        fileNameLbl.setText(musicFile.getName());
        afterLoadTimeline.play();
        initializeAnimation();
    }

    private void initializeAnimation(){
        animation = new Animation();
        animation.noteFall(noteImage1, road1);
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

    @FXML
    private void playMusic() {
        musicService.play();
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