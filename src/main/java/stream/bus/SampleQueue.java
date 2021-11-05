package stream.bus;

import java.util.Deque;
import java.util.concurrent.LinkedBlockingDeque;

public class SampleQueue<T> {

    private final Deque<T> samples;

    public SampleQueue() {
        this.samples = new LinkedBlockingDeque<>();
    }

    public void push(final T sample) {
        this.samples.addFirst(sample);
    }

    public T get() {
        return this.samples.pollLast();
    }

    public boolean isEmpty() {
        return this.samples.isEmpty();
    }
}
