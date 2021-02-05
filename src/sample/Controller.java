package sample;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
    private Slider timeSlider;
    @FXML
    private ImageView noteImage1;
    @FXML
    private Button gameBttn1;
    @FXML
    private Pane road1;
    @FXML
    private Pane road2;
    @FXML
    private Pane road3;
    @FXML
    private Pane road4;

    private Stage primaryStage;
    private File musicFile;

    private MusicService musicService;

    private Timeline afterLoadTimeline;

    private GameLogic gameLogic;

    int bands = 128;
    float[] magnitudes = new float[bands]; //default 128 bands
    float[] phases = new float[bands]; //same

    ArrayList<Pane> panes = new ArrayList<>();

    public Controller() {
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

    @FXML
    private void initialize() throws FileNotFoundException {
//        File input = new File("resources/note_icon.png");
//        Image image = new Image(input.toURI().toString());
//        ImageView noteView = new ImageView(image);
//
//        noteView.setX(10);
//        noteView.setY(10);
//        noteView.setFitHeight(100);
//        noteView.setFitWidth(100);
//
//        Rectangle rec = new Rectangle(10, 10, 100, 100);
//        rec.setFill(Color.BROWN);
//
//        road1.getChildren().addAll(noteView);

        panes.add(road1);
        panes.add(road2);
        panes.add(road3);
        panes.add(road4);

        gameLogic = new GameLogic(panes);
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    private void initializeFile() {
        musicService = new MusicService(musicFile.toURI().toString());
        fileNameLbl.setText(musicFile.getName());
        afterLoadTimeline.play();
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