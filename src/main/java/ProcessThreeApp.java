import dropcopier.DropCopier;
import io.aeron.Aeron;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import common.MessagingUtil;

public class ProcessThreeApp {
    /**
     * Main method to start the ProcessThree application.
     * It initializes the Aeron connection and starts the DropCopier.
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        // Read the Aeron directory path from the file
        Path pathFile = Paths.get(MessagingUtil.AERON_DIRECTORY);
        String aeronDirectoryPath;

        try {
            // Read the Aeron directory path from the specified file
            aeronDirectoryPath = new String(Files.readAllBytes(pathFile));
        } catch (Exception e) {
            // Handle any exceptions that occur while reading the directory path
            System.err.println("Error reading Aeron directory path: " + e.getMessage());
            return; // Exit the application if the directory path cannot be read
        }

        // Connect to the existing Media Driver
        final Aeron.Context ctx = new Aeron.Context();
        ctx.aeronDirectoryName(aeronDirectoryPath.trim()); // Use the read directory path

        // Connect to Aeron
        try (Aeron aeron = Aeron.connect(ctx)) {
            long startTime = System.nanoTime(); // Start time for latency measurement
            // Create and start the Drop Copier
            DropCopier dropCopier = new DropCopier(aeron);
            new Thread(dropCopier).start(); // Start the DropCopier in a new thread
            long endTime = System.nanoTime(); // End time for latency measurement
            long latency = endTime - startTime; // Calculate latency in nanoseconds

            // Log the latency for starting DropCopier
            System.out.printf("DropCopier started | Latency: %d microseconds%n", latency / 1000);

            // Optionally, you can add a shutdown hook to clean up resources
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                dropCopier.stop(); // Implement a stop method in DropCopier to handle cleanup
                aeron.close(); // Close the Aeron connection
            }));

            // Keep the application running
            Thread.currentThread().join(); // Prevent main thread from exiting
        } catch (Exception e) {
            // Handle any exceptions that occur during the Aeron connection or DropCopier execution
            System.err.println("Error in running processes: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
