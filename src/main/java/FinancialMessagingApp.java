import io.aeron.Aeron;
import io.aeron.driver.MediaDriver;
import processor.Processor;
import publisher.Publisher;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import common.MessagingUtil;

 public class FinancialMessagingApp {
     public static void main(String[] args) {
         try {
             // Create a temporary directory for Aeron
             Path tempDir = Files.createTempDirectory("aeron");
             MediaDriver.Context driverContext = new MediaDriver.Context();
             driverContext.aeronDirectoryName(tempDir.toString()); // Set the directory for the Media Driver

             final MediaDriver driver = MediaDriver.launchEmbedded(driverContext); // Launch the Media Driver with the context
             final Aeron.Context ctx = new Aeron.Context().aeronDirectoryName(driver.aeronDirectoryName());

             System.out.println("Aeron Directory: " + ctx.aeronDirectoryName()); // Print the directory name

             // Write the Aeron directory path to a file
             Path pathFile = Paths.get(MessagingUtil.AERON_DIRECTORY);
             Files.write(pathFile, ctx.aeronDirectoryName().getBytes());

             long startTime = System.nanoTime(); // Start time for latency measurement
             try (Aeron aeron = Aeron.connect(ctx)) {
                 long endTime = System.nanoTime(); // End time for latency measurement
                 long latency = endTime - startTime; // Calculate latency in nanoseconds
                 System.out.printf("Aeron connection established | Latency: %d microseconds%n", latency / 1000);

                 // Process #1: 3 Publishers and 1 Processor
                 Thread[] publishers = new Thread[3];
                 for (int i = 0; i < 3; i++) {
                     publishers[i] = new Thread(new Publisher(aeron, i), "Publisher-" + i);
                     publishers[i].start();
                 }
                 Thread processor1 = new Thread(new Processor(aeron, 1), "Processor1");
                 processor1.start();

                 // Process #2: 1 Processor that communicates with Process #1's Publishers
                 Thread processor2 = new Thread(new Processor(aeron, 2), "Processor2");
                 processor2.start();

//                 // Continuous monitoring and logging
//                 while (!Thread.currentThread().isInterrupted()) {
//
//                 }

                 // Wait for all threads to complete
                 for (Thread pub : publishers) {
                     pub.join();
                 }
                 processor1.join();
                 processor2.join();
             } catch (Exception e) {
                 System.err.println("Error in running processes: " + e.getMessage());
                 e.printStackTrace();
             } finally {
                 driver.close();
                 System.out.println("Media Driver closed. Application terminated.");
             }
         } catch (Exception e) {
             System.err.println("Error creating temporary directory: " + e.getMessage());
             e.printStackTrace();
         }
     }
 }

