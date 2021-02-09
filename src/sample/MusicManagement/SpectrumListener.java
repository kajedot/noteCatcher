package sample.MusicManagement;

import javafx.scene.media.AudioSpectrumListener;

public class SpectrumListener implements AudioSpectrumListener {

   private final float[] magnitudesCopy;

   private final int threshold;

    public SpectrumListener(int bands, int threshold){
        magnitudesCopy = new float[bands];
        this.threshold = threshold;
    }

    @Override
    public void spectrumDataUpdate(double timestamp, double duration, float[] magnitudes, float[] phases) {
        for (int i=0; i < magnitudes.length; i++){
            if (magnitudes[i] != 0){
                magnitudes[i] = magnitudes[i] - threshold;
                magnitudesCopy[i] = magnitudes[i];
            }
        }
    }

    public float[] getMagnitudesCopy() {
        return magnitudesCopy;
    }
}