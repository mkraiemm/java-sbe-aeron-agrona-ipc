package app;

import io.aeron.Aeron;
import io.aeron.driver.MediaDriver;
import processor.Processor;
import publisher.Publisher;

public class ProcessOneApp {
    public static void main(String[] args) {
        MediaDriver.Context driverContext = new MediaDriver.Context();
        final MediaDriver driver = MediaDriver.launchEmbedded(driverContext);
        final Aeron.Context ctx = new Aeron.Context();

        try (Aeron aeron = Aeron.connect(ctx)) {
            Thread[] publishers = new Thread[3];
            for (int i = 0; i < 3; i++) {
                publishers[i] = new Thread(new Publisher(aeron, i), "Publisher-" + i);
                publishers[i].start();
            }
            Thread processor1 = new Thread(new Processor(aeron, 1), "Processor1");
            processor1.start();

            // Wait indefinitely
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            // Handle interruption
        } finally {
            driver.close();
        }
    }
}
