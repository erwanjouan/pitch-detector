package stream.fft;

import static stream.App.BUFFER_SIZE;

public class FFTCoefficient {

    private final double power;
    private final double frequency;

    public FFTCoefficient(final int index, final Complex fftCoef, final float sampleRate) {
        this.power = fftCoef.abs();
        final double stepFrequency = sampleRate / (double) BUFFER_SIZE;
        this.frequency = index * stepFrequency;
    }

    public double getPower() {
        return this.power;
    }

    public double getFrequency() {
        return this.frequency;
    }

    @Override
    public String toString() {
        return "FFTCoefficient{" +
                ", power=" + this.power +
                ", frequency=" + this.frequency +
                '}';
    }
}
