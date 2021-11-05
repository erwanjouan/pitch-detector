package stream;

import stream.bus.SampleQueue;
import stream.fft.FFTService;
import stream.prepare.PrepareWorker;
import stream.reader.WavReader;

public class App {

    public static final int BUFFER_SIZE = 4096;
    //public static final int BUFFER_SIZE = 16384;

    public static void main(final String[] args) {
        //final String fileName = "g_tdd_120_01.wav";
        final String fileName = "B_string.wav";
        //final String fileName = "a.wav";
        //final String fileName = "arpeggios.wav";

        final SampleQueue sampleQueue = new SampleQueue();
        final WavReader wavReader = new WavReader(fileName, sampleQueue);
        wavReader.run();

        final FFTService fftService = new FFTService();
        final PrepareWorker prepareWorker = new PrepareWorker(fftService, sampleQueue);
        final Thread prepareWorkerThread = new Thread(prepareWorker);
        prepareWorkerThread.start();

        /*final FFTService fftService = new FFTService();
        final Thread fftThread = new Thread(fftService);
        fftThread.start();*/
    }
}
