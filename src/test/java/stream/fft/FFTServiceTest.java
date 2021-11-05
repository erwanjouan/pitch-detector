package stream.fft;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.BlockingDeque;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FFTServiceTest {
    @Test
    void basic() {
        final short[] poisonPill = new short[2];
        final float sampleRate = 44100;
        final List<BlockingDeque<short[]>> deques = List.of();

        final FFTService fftService = new FFTService();
        assertTrue(true);
    }
}