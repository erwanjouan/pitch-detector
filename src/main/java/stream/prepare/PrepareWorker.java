package stream.prepare;

import stream.bus.SampleQueue;
import stream.fft.FFTService;
import stream.model.Detection;
import stream.model.SampleChunk;

import java.util.ArrayList;
import java.util.List;

public class PrepareWorker<T> implements Runnable {

    public static final int POWER_THRESHOLD = 100;
    private final FFTService fftService;
    private final SampleQueue<T> sampleQueue;

    public PrepareWorker(final FFTService fftService, final SampleQueue sampleQueue) {
        this.fftService = fftService;
        this.sampleQueue = sampleQueue;
    }

    @Override
    public void run() {
        final List<Detection> detections = new ArrayList<>();
        while (!this.sampleQueue.isEmpty()) {
            final T t = this.sampleQueue.get();
            if (t instanceof SampleChunk) {
                detections.addAll(this.fftService.detectWithFFT((SampleChunk) t, POWER_THRESHOLD));
            }
        }
        detections.stream()
                .sorted((a, b) -> Float.compare(a.getStartTime(), b.getStartTime()))
                .forEach(d -> this.fftService.displayDetection(d));
    }
}
