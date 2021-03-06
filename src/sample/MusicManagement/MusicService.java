package sample.MusicManagement;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.util.Objects;

public class MusicService{
    private final MediaPlayer player;

    public MusicService(String pathToFile){
        player = new MediaPlayer(new Media(pathToFile));

        player.setOnEndOfMedia(player::stop);
    }

    private String durationToStr(Duration objToConvert) {
        String returnStr;
        returnStr = String.format("%02d", (int) (objToConvert.toMinutes()));
        returnStr += ":";
        returnStr += String.format("%02d", Math.round(objToConvert.toSeconds() % 60));

        return returnStr;
    }

    public void play(){
        if (Objects.nonNull(player)){
            player.play();
        }
    }

    public void volumeUp() {
        if (Objects.nonNull(player)) {
            if (player.getVolume() < 1)
                player.setVolume(player.getVolume() + 0.1);
        }
    }

    public void volumeDown() {
        if (Objects.nonNull(player)) {
            if (player.getVolume() > 0)
                player.setVolume(player.getVolume() - 0.1);


        }
    }

    public void setSpectrumListener(SpectrumListener l){
        player.setAudioSpectrumListener(l);
    }

    public void mute(){
        player.setMute(true);
    }
    public void audioSpectrumInterval(Double interval){
        player.setAudioSpectrumInterval(interval);
    }

    public void setAudioSpectrumNumBands(Integer bands){
        player.setAudioSpectrumNumBands(bands);
    }

    public String getStatus(){
        return player.getStatus().toString();
    }

    public String getCurrentTimeStr(){
        return durationToStr(player.getCurrentTime());
    }

    public String getStopTimeStr(){
        return durationToStr(player.getStopTime());
    }

    public Duration getCurrentTime(){
        return player.getCurrentTime();
    }

    public Duration getStopTime(){
        return player.getStopTime();
    }
    public Integer getAudioSpectrumThreshold(){
        return player.getAudioSpectrumThreshold();
    }

}

