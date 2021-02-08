package sample;

import javafx.scene.media.AudioSpectrumListener;

import java.util.Arrays;

public class SpectrumListener implements AudioSpectrumListener {

   private float[] magnitudesCopy;

    public SpectrumListener(int bands){
        magnitudesCopy = new float[bands];
    }

    @Override
    public void spectrumDataUpdate(double timestamp, double duration, float[] magnitudes, float[] phases) {
        for (int i=0; i < magnitudes.length; i++){
            if (magnitudes[i] != 0){
                magnitudes[i] = magnitudes[i] + 60;
                magnitudesCopy[i] = magnitudes[i];
            }
        }
    }

    public float[] getMagnitudesCopy() {
        return magnitudesCopy;
    }
}