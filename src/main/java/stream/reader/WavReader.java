package stream.reader;

import stream.bus.SampleQueue;
import stream.model.MusicalRange;
import stream.model.SampleChunk;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.io.InputStream;

import static java.lang.String.format;
import static stream.App.BUFFER_SIZE;

public class WavReader implements Runnable {

    // Set an arbitrary buffer size of 1024 frames, is equal to sample number
    private final String fileName;
    private AudioFormat format;
    private final SampleQueue sampleQueue;

    public WavReader(final String fileName, final SampleQueue sampleQueue) {
        this.fileName = fileName;
        this.sampleQueue = sampleQueue;
        this.setAudioFormat();
    }

    @Override
    public void run() {
        for (final MusicalRange musicalRange : MusicalRange.values()) {
            try (final InputStream resourceAsStream = WavReader.class.getClassLoader().getResourceAsStream(this.fileName);
                 final AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(resourceAsStream)) {
                this.printInfo(audioInputStream);
                final float sampleRate = musicalRange.getSampleRate(BUFFER_SIZE);
                final ReSampler reSampler = new ReSampler(sampleRate);
                final AudioInputStream resampledAudioInputStream = reSampler.resample(audioInputStream);
                this.readAudioByChunk(resampledAudioInputStream, musicalRange);
            } catch (final IOException | UnsupportedAudioFileException e) {
                e.printStackTrace();
            }
        }
    }

    private void printInfo(final AudioInputStream audioInputStream) {
        final AudioFormat format = audioInputStream.getFormat();
        final int sampleSizeInBits = format.getSampleSizeInBits();
        System.out.println(sampleSizeInBits);
    }

    private void readAudioByChunk(final AudioInputStream audioInputStream, final MusicalRange musicalRange) {
        final AudioFormat format = audioInputStream.getFormat();
        final int bytesPerFrame = format.getFrameSize();
        final int channelNumber = format.getChannels();
        final float sampleRate = format.getSampleRate();
        this.dumpInfo(bytesPerFrame, channelNumber, sampleRate);
        System.out.println(audioInputStream.getFormat());
        final int numBytes = BUFFER_SIZE * bytesPerFrame * channelNumber;
        final byte[] audioBytes = new byte[numBytes];
        float startTime = 0;
        float endTime = this.getChunkDuration(sampleRate);
        try {
            while (audioInputStream.read(audioBytes) != -1) {
                //System.out.println(String.format("start time %f end time %f", startTime, endTime));
                final int numberOfSamples = audioBytes.length / bytesPerFrame / channelNumber;
                for (int channelIndex = 0; channelIndex < channelNumber; channelIndex++) {
                    final short[] channelBytes = new short[BUFFER_SIZE];
                    for (int sampleIndex = 0; sampleIndex < numberOfSamples; sampleIndex++) {
                        final int offset = bytesPerFrame * (sampleIndex * channelNumber + channelIndex);
                        final short value = this.byteArrayToShortLE(audioBytes, offset);
                        channelBytes[sampleIndex] = value;
                        //System.out.println("adding " + value + " to channel " + channelIndex);
                    }
                    this.sampleQueue.push(new SampleChunk(channelIndex, channelBytes, sampleRate, musicalRange, startTime, endTime));
                    startTime += this.getChunkDuration(sampleRate);
                    endTime += this.getChunkDuration(sampleRate);
                }
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }

    private float getChunkDuration(final float sampleRate) {
        return 1 / sampleRate * BUFFER_SIZE;
    }

    private void dumpInfo(final int bytesPerFrame, final int channelNumber, final float sampleRate) {
        final String message = "bytesPerFrame %d, bytesPerFrame %d, sampleRate %f";
        final String format = format(message, bytesPerFrame, channelNumber, sampleRate);
        System.out.println(format);
    }

    public short byteArrayToShortLE(final byte[] b, final int offset) {
        short value = 0;
        for (int i = 0; i < 2; i++) {
            value |= (b[i + offset] & 0x000000FF) << (i * 8);
        }
        return value;
    }

    public int getChannelNumber() {
        return this.format != null ? this.format.getChannels() : 0;
    }

    public float getSampleRate() {
        return this.format != null ? this.format.getSampleRate() : 0;
    }

    private void setAudioFormat() {
        try (final InputStream resourceAsStream = WavReader.class.getClassLoader().getResourceAsStream(this.fileName);
             final AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(resourceAsStream)) {
            this.format = audioInputStream.getFormat();
        } catch (final IOException | UnsupportedAudioFileException e) {
            this.format = null;
        }
    }

    /*public List<BlockingDeque<short[]>> createQueues() {
        final int channelNumber = this.getChannelNumber();
        final List<BlockingDeque<short[]>> channelDeques = new ArrayList<>();
        for (int i = 0; i < channelNumber; i++) {
            channelDeques.add(new LinkedBlockingDeque<>());
        }
        this.deques = channelDeques;
        return channelDeques;
    }

    public void setDeQueues(final List<BlockingDeque<short[]>> deques) {
        this.deques = deques;
    }

    public short[] createPoisonPill() {
        final short[] poisonPill = {Short.MIN_VALUE, Short.MAX_VALUE};
        this.poisonPill = poisonPill;
        return poisonPill;
    }*/
}
