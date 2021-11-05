package stream.model;

public class SampleChunk {

    private final int channelNumber;
    private final short[] channelBytes;
    private final float sampleRate;
    private final MusicalRange musicalRange;
    private final float startTime;
    private final float endTime;

    public SampleChunk(final int channelNumber, final short[] channelBytes, final float sampleRate, final MusicalRange musicalRange,
                       final float startTime, final float endTime) {
        this.channelNumber = channelNumber;
        this.channelBytes = channelBytes;
        this.sampleRate = sampleRate;
        this.musicalRange = musicalRange;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getChannelNumber() {
        return this.channelNumber;
    }

    public short[] getChannelBytes() {
        return this.channelBytes;
    }

    public float getSampleRate() {
        return this.sampleRate;
    }

    public MusicalRange getMusicalRange() {
        return this.musicalRange;
    }

    public float getStartTime() {
        return this.startTime;
    }

    public float getEndTime() {
        return this.endTime;
    }
}
