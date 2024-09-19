package app;

import io.aeron.Aeron;
import processor.Processor;

public class ProcessTwoApp {
    public static void main(String[] args) {
        final Aeron.Context ctx = new Aeron.Context();

        try (Aeron aeron = Aeron.connect(ctx)) {
            Thread processor2 = new Thread(new Processor(aeron, 2), "Processor2");
            processor2.start();

            // Wait indefinitely
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            // Handle interruption
        }
    }
}
