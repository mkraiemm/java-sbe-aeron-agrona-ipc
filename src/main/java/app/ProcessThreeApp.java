package app;

import io.aeron.Aeron;
import dropcopier.DropCopier;

public class ProcessThreeApp {
    public static void main(String[] args) {
        final Aeron.Context ctx = new Aeron.Context();

        try (Aeron aeron = Aeron.connect(ctx)) {
            DropCopier dropCopier = new DropCopier(aeron);
            new Thread(dropCopier).start();

            // Wait indefinitely
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            // Handle interruption
        }
    }
}
