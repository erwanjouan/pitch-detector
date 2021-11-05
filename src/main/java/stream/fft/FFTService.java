package stream.fft;

import stream.model.Detection;
import stream.model.MusicalRange;
import stream.model.SampleChunk;

import java.util.ArrayList;
import java.util.List;

import static stream.App.BUFFER_SIZE;
import static stream.fft.FFT.fft;

public class FFTService {

    public List<Detection> detectWithFFT(final SampleChunk sampleChunk, final double powerThreshold) {
        final Complex[] x = new Complex[BUFFER_SIZE];
        final short[] inputs = sampleChunk.getChannelBytes();
        for (int i = 0; i < inputs.length; i++) {
            final double ratio = (double) inputs[i] / (double) Short.MAX_VALUE;
            x[i] = new Complex(ratio, 0);
        }
        final Complex[] fftCoefs = fft(x);

        return this.getDetections(fftCoefs, sampleChunk, powerThreshold);
    }

    private List<Detection> getDetections(final Complex[] fftCoefs, final SampleChunk sampleChunk, final double powerThreshold) {
        final List<Detection> detections = new ArrayList<>();
        final float sampleRate = sampleChunk.getSampleRate();
        final MusicalRange musicalRange = sampleChunk.getMusicalRange();

        final FFTCoefficient[] fftCoefficients = new FFTCoefficient[BUFFER_SIZE / 2];
        for (int i = 0; i < fftCoefs.length / 2; i++) {
            fftCoefficients[i] = new FFTCoefficient(i, fftCoefs[i], sampleRate);
        }

        final int baseIndex = musicalRange.getBaseIndex();
        //System.out.println(noteScores);
        for (int i = 0; i <= 4; i++) {
            final int multiplier = (int) Math.pow(2, i);
            final int index = baseIndex * multiplier;
            final double power = fftCoefficients[index].getPower();
            if (power > powerThreshold) {
                //this.printDetection(sampleChunk, fftCoefficients[index], i, power);
                detections.add(this.getDetection(sampleChunk, fftCoefficients[index], i, power));
            }
        }
        return detections;
    }

    private Detection getDetection(final SampleChunk sampleChunk, final FFTCoefficient fftCoefficient, final int i, final double power) {
        return new Detection(
                sampleChunk.getStartTime(),
                String.format("%s%d", sampleChunk.getMusicalRange(), i + 1),
                fftCoefficient.getFrequency(),
                fftCoefficient.getPower());
    }

    private void printDetection(final SampleChunk sampleChunk, final FFTCoefficient fftCoefficient, final int i, final double power) {
        final StringBuilder stringBuilder = new StringBuilder();
        final String header = String.format("[%f,%f] ", sampleChunk.getStartTime(), sampleChunk.getEndTime());
        stringBuilder.append(header);
        stringBuilder.append(sampleChunk.getMusicalRange());
        stringBuilder.append(i + 1);
        stringBuilder.append(String.format(" (%f) ", fftCoefficient.getFrequency()));
        stringBuilder.append(power);
        stringBuilder.append(" ");
        System.out.println(stringBuilder.toString());
    }

    public void displayDetection(final Detection d) {
        final StringBuilder stringBuilder = new StringBuilder();
        final String header = String.format("[%f] ", d.getStartTime());
        stringBuilder.append(header);
        stringBuilder.append(d.getNote());
        stringBuilder.append(String.format(" (%f) ", d.getFrequency()));
        stringBuilder.append(d.getPower());
        System.out.println(stringBuilder.toString());
    }
}
