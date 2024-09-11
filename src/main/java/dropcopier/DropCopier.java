package dropcopier;

import common.MessagingUtil;
import io.aeron.Aeron;
import io.aeron.Subscription;
import io.aeron.logbuffer.FragmentHandler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Represents a DropCopier that subscribes to quote and order response streams and logs the received messages.
 */
public class DropCopier implements Runnable {
    private final Aeron aeron;
    private final Subscription quoteSubscription;
    private final Subscription orderResponseSubscription;
    private volatile boolean running = true; // Control flag for the run loop

    /**
     * Constructor to initialize the DropCopier with Aeron context.
     *
     * @param aeron Aeron instance for communication
     */
    public DropCopier(Aeron aeron) {
        this.aeron = aeron;
        // Subscribe using the network channel for quotes and order responses
        this.quoteSubscription = aeron.addSubscription(MessagingUtil.IPC_CHANNEL, MessagingUtil.QUOTE_STREAM_ID);
        this.orderResponseSubscription = aeron.addSubscription(MessagingUtil.IPC_CHANNEL, MessagingUtil.ORDER_STREAM_ID);
    }

    /**
     * Run method to continuously poll for incoming messages.
     */
    @Override
    public void run() {
        // Handler for processing received messages
        FragmentHandler handler = (buffer, offset, length, header) -> {
            String message = buffer.getStringAscii(offset);

            // Extract the timestamp from the message to calculate latency
            long sentTimestamp = extractSentTimestamp(message);
            if (sentTimestamp != 0) {
                long currentTimestamp = System.nanoTime();  // Current time when message is received
                long latency = TimeUnit.NANOSECONDS.toMicros(currentTimestamp - sentTimestamp);  // Calculate latency in microseconds
                log("Received: " + message + " | Latency: " + latency + " microseconds");
            } else {
                log("Received message without a valid timestamp: " + message);
            }
        };

        while (running) {
            quoteSubscription.poll(handler, 10);  // Poll for quotes
            orderResponseSubscription.poll(handler, 10);  // Poll for order responses
        }
    }

    /**
     * Extracts the sent timestamp from the received message.
     *
     * @param message The message from which to extract the timestamp
     * @return The extracted timestamp or 0 if not found
     */
    private long extractSentTimestamp(String message) {
        String[] parts = message.split(" ");
        if (parts.length < 2) return 0;  // Check if there are enough parts to extract the timestamp

        try {
            // Assume the last part of the message is the timestamp (as it was added by the Publisher/Processor)
            return Long.parseLong(parts[parts.length - 1]);
        } catch (NumberFormatException e) {
            log("Invalid timestamp in message: " + message);
            return 0;  // Return 0 if the timestamp is not a valid number
        }
    }

    /**
     * Stops the DropCopier from running.
     */
    public void stop() {
        running = false;  // Set the flag to false to exit the run loop
    }

    /**
     * Static log method to include timestamp.
     *
     * @param message The message to log
     */
    private static void log(String message) {
        String timestamp = new SimpleDateFormat("HH:mm:ss.SSS").format(new Date());
        System.out.printf("[%s] [DropCopier] %s%n", timestamp, message);
    }
}
