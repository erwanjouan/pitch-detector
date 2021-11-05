package stream.reader;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public class ReSampler {

    private final float targetFreq;

    public ReSampler(final float targetFreq) {
        this.targetFreq = targetFreq;
    }

    public AudioInputStream resample(final AudioInputStream audioIn) {
        final AudioFormat srcFormat = audioIn.getFormat();

        final AudioFormat dstFormat = new AudioFormat(srcFormat.getEncoding(),
                this.targetFreq,
                srcFormat.getSampleSizeInBits(),
                srcFormat.getChannels(),
                srcFormat.getFrameSize(),
                srcFormat.getFrameRate() / 2,
                srcFormat.isBigEndian());
        return AudioSystem.getAudioInputStream(dstFormat, audioIn);
    }
}
