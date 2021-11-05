package stream.model;

public class Detection {
    private final float startTime;
    private final String note;
    private final double frequency;
    private final double power;

    public Detection(final float startTime, final String note, final double frequency, final double power) {
        this.startTime = startTime;
        this.note = note;
        this.frequency = frequency;
        this.power = power;
    }

    public float getStartTime() {
        return this.startTime;
    }

    public String getNote() {
        return this.note;
    }

    public double getPower() {
        return this.power;
    }

    public double getFrequency() {
        return this.frequency;
    }
}
