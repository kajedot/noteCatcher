package sample;

import javafx.scene.media.AudioSpectrumListener;

import java.util.Arrays;

public class SpectrumListener implements AudioSpectrumListener {

   private float[] magnitudesCopy;

    public SpectrumListener(){
        magnitudesCopy = new float[128];
    }

    @Override
    public void spectrumDataUpdate(double timestamp, double duration, float[] magnitudes, float[] phases) {
        for (int i=0; i < magnitudes.length; i++){
            if (magnitudes[i] != 0){
                magnitudes[i] = magnitudes[i] + 60;
            }
        }
        System.out.println(Arrays.toString(magnitudes));
        this.magnitudesCopy = magnitudes;
    }

    public float[] getMagnitudesCopy() {
        return magnitudesCopy;
    }
}